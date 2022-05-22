package e.vatsal.newsfeed.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import e.vatsal.newsfeed.R
import e.vatsal.newsfeed.data.model.Article
import e.vatsal.newsfeed.databinding.NewsFeedFragmentBinding
import timber.log.Timber

@AndroidEntryPoint
class NewsFeedFragment : Fragment() {

    private lateinit var binding: NewsFeedFragmentBinding

    private lateinit var article: Article

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NewsFeedFragmentBinding.inflate(layoutInflater, container, false)
        fetchIntent()
        setUpUI()

        return binding.root
    }

    private fun fetchIntent() {
        setFragmentResultListener("requestKey") { requestKey, bundle ->
            article = bundle.getParcelable("article")!!
            showData()
        }
    }

    private fun setUpUI() {
        binding.btBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.btShare.setOnClickListener {
            val ii = Intent(Intent.ACTION_SEND)
            ii.type = "text/plain"
            ii.putExtra(
                Intent.EXTRA_TEXT,
                "${article.title.toString()}..... to read complete news Download the NewsFeed App from Playstore   ${article.url}"
            )
            if (ii.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(ii)
            }
        }

        binding.tvTitle.setOnClickListener {
            val webpage: Uri = Uri.parse(article.url)
            val i = Intent(Intent.ACTION_VIEW, webpage)
            if (requireActivity().intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(i)
            }
        }
    }

    private fun showData() {
        binding.apply {
            image.load(article.urlToImage){
                placeholder(R.drawable.news_placeholder)
                error(R.drawable.news_placeholder)
            }
            tvTitle.text = article.title
            tvAuthor.text = " Author ${article.author}"
            tvContent.text = article.content
            tvPublished.text = "Published ${article.publishedAt}"
        }
    }
}