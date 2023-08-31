package com.saxipapsi.saxi_movie.presentation.get_content_details.components

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.saxipapsi.saxi_movie.common.extension.load
import com.saxipapsi.saxi_movie.common.extension.toDateFormat
import com.saxipapsi.saxi_movie.databinding.ActivityContentDetailsBinding
import com.saxipapsi.saxi_movie.domain.model.ImageUrlBuilder
import com.saxipapsi.saxi_movie.presentation.dialog.VideoDialog
import com.saxipapsi.saxi_movie.presentation.get_content_credits.components.ContentCreditsAdapter
import com.saxipapsi.saxi_movie.presentation.get_content_credits.components.ContentCreditsViewModel
import com.saxipapsi.saxi_movie.presentation.get_content_credits.components.CreditsViewType
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class ContentDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContentDetailsBinding

    private val getContentDetailsViewModel: GetContentDetailsViewModel by inject()
    private val contentCreditsViewModel: ContentCreditsViewModel by inject()

    private val creditsAdapter : ContentCreditsAdapter by lazy { ContentCreditsAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAdapter()
        initObserver()
        val id = intent.getIntExtra(CONTENT_ID_TAG, 0)
        val contentType = intent.getStringExtra(CONTENT_TYPE_TAG)
        contentType?.let {
            getContentDetailsViewModel.getContentDetails(id, it)
            contentCreditsViewModel.getCredits(contentType, id)
        } ?: kotlin.run {
            Toast.makeText(this, "Content Type undefined.", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun initAdapter() {
        val glm = GridLayoutManager(this, 3)
        glm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (creditsAdapter.getItemViewType(position) == CreditsViewType.HEADER.ordinal) 3 else 1
            }
        }
        binding.rvCredits.layoutManager = glm
        binding.rvCredits.adapter = creditsAdapter
    }

    private fun initObserver() {
        lifecycleScope.launch { observeContentDetails() }
        lifecycleScope.launch { observeContentCredits() }
    }

    private suspend fun observeContentDetails() {
        getContentDetailsViewModel.state.collectLatest { state ->
            binding.loading.visibility = if (state.isLoading) View.VISIBLE else View.GONE
            binding.tvError.visibility = if (state.error.isNotEmpty()) View.VISIBLE else View.GONE
            binding.tvError.text = state.error
            state.data?.let { data ->
                data.banner?.let { banner -> binding.ivBanner.load(ImageUrlBuilder.getW500PosterUrl(banner))}
                data.foster?.let { foster -> binding.ivFoster.load(ImageUrlBuilder.getOriginalPosterUrl(foster))}
                binding.tvBanner.text = data.title
                binding.tvGenre.text = data.genres
                binding.pbVotes.progress = data.voteAverage
                binding.tvVotes.text = "${data.voteAverage}%"
                binding.tvDate.text = data.date?.toDateFormat()
                binding.tvTagLine.text = data.tagline
                binding.tvOverView.text = data.overView
                Log.d("Eric", "")
                binding.tvPlayTrailer.setOnClickListener { VideoDialog.newInstance(data.id, data.contentType.name).show(supportFragmentManager, VideoDialog.TAG) }
            }
        }
    }

    private suspend fun observeContentCredits(){
        contentCreditsViewModel.creditState.collectLatest { state ->
            binding.loading.visibility = if (state.isLoading) View.VISIBLE else View.GONE
            binding.tvError.visibility = if (state.error.isNotEmpty()) View.VISIBLE else View.GONE
            binding.tvError.text = state.error
            state.data?.let { result -> creditsAdapter.differ.submitList(result) }
        }
    }

    companion object {
        private const val CONTENT_ID_TAG = "content-id"
        private const val CONTENT_TYPE_TAG = "content-type"
        fun start(context: Context, contentId: Int, contentType: String) {
            val intent = Intent(context, ContentDetailsActivity::class.java)
            intent.putExtra(CONTENT_ID_TAG, contentId)
            intent.putExtra(CONTENT_TYPE_TAG, contentType)
            context.startActivity(intent)
        }
    }
}