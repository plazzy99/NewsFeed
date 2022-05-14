package e.vatsal.newsfeed.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import e.vatsal.newsfeed.data.model.Article
import e.vatsal.newsfeed.databinding.ActivityCategoryViewAllBinding

@AndroidEntryPoint
class CategoryViewAllActivity : AppCompatActivity() {

    companion object {
        private const val INTENT_ARTICLE = "intent_article"
        fun startActivity(context: Context, resultModel: Article) {
            val intent = Intent(context, CategoryViewAllActivity::class.java)
            intent.putExtra(INTENT_ARTICLE, resultModel)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityCategoryViewAllBinding

    private lateinit var article: Article

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryViewAllBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fetchIntent()
        setUpUI()
        showData()
    }

    private fun fetchIntent() {
        article = intent.getParcelableExtra<Article>(INTENT_ARTICLE) as Article
    }

    private fun setUpUI() {
        binding.btBack.setOnClickListener {
            onBackPressed()
        }

        binding.btShare.setOnClickListener {
            val ii = Intent(Intent.ACTION_SEND)
            ii.type = "text/plain"
            ii.putExtra(
                Intent.EXTRA_TEXT,
                    "${article.title.toString()}..... to read complete news Download the NewsFeed App from Playstore   ${article.url}"
            )
            if (ii.resolveActivity(packageManager) != null) {
                startActivity(ii)
            }
        }

        binding.tvTitle.setOnClickListener {
            val webpage: Uri = Uri.parse(article.url)
            val i = Intent(Intent.ACTION_VIEW, webpage)
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(i)
            }
        }
    }

    private fun showData() {
        binding.apply {
            image.load(article.urlToImage)
            tvTitle.text = article.title
            tvAuthor.text = " Author ${article.author}"
            tvContent.text = article.content
            tvPublished.text = "Published ${article.publishedAt}"
        }
    }
}