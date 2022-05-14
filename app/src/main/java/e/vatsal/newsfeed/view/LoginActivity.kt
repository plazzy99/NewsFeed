package e.vatsal.newsfeed.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import e.vatsal.newsfeed.R
import e.vatsal.newsfeed.databinding.ActivityLoginBinding
import e.vatsal.newsfeed.utils.extensions.showToast
import e.vatsal.newsfeed.utils.extensions.startActivity
import timber.log.Timber

class LoginActivity : AppCompatActivity() {

    companion object {
        const val RC_SIGN_IN = 34252
    }

    private lateinit var binding: ActivityLoginBinding

    private var mAuth = Firebase.auth
    private var mGoogleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        setUpClick()
    }

    private fun setUpClick() {
        binding.btButton.setOnClickListener {
            signIn()
        }
    }

    override fun onStart() {
        super.onStart()
        if (mAuth.currentUser != null) {
            finishAffinity()
            startActivity<MainActivity>()
            binding.btButton.visibility = View.GONE
            binding.tvText.visibility = View.GONE
        } else {
            binding.btButton.visibility = View.VISIBLE
            binding.tvText.visibility = View.VISIBLE
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
                Timber.d("firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Timber.w("Google sign in failed", e)
                // ...
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    startActivity<MainActivity>()
                    finishAffinity()
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.w("signInWithCredential:failure", task.exception)
                    showToast("sign in failed")
                }
            }
    }

    private fun init() {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
//        sharedPreferences = getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
        finish()
    }
}