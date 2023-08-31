package com.saxipapsi.saxi_movie.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.saxipapsi.saxi_movie.R
import com.saxipapsi.saxi_movie.common.ContentTrendingType
import com.saxipapsi.saxi_movie.common.ContentType
import com.saxipapsi.saxi_movie.common.extension.load
import com.saxipapsi.saxi_movie.databinding.ActivityMainBinding
import com.saxipapsi.saxi_movie.domain.model.ImageUrlBuilder
import com.saxipapsi.saxi_movie.presentation.dialog.VideoDialog
import com.saxipapsi.saxi_movie.presentation.get_content_details.components.ContentDetailsActivity
import com.saxipapsi.saxi_movie.presentation.get_content_list.components.ContentListAdapter
import com.saxipapsi.saxi_movie.presentation.get_content_list.components.GetContentListViewModel
import com.saxipapsi.saxi_movie.presentation.get_content_list.components.OnContentBehaviorListener
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val getContentListViewModel: GetContentListViewModel by inject()
    private val upComingListAdapter: ContentListAdapter by lazy { ContentListAdapter(object : OnContentBehaviorListener {
        override fun onViewItem(contentId: Int, contentType: String?) { ContentDetailsActivity.start(this@MainActivity, contentId, currentSelectedUpComingContentType) }
        override fun onPlayItem(contentId: Int, contentType: String?) { VideoDialog.newInstance(contentId, contentType ?: ContentType.movie.name).show(supportFragmentManager, VideoDialog.TAG) }
    })}
    private val trendingListAdapter: ContentListAdapter by lazy { ContentListAdapter(object : OnContentBehaviorListener {
        override fun onViewItem(contentId: Int, contentType: String?) { ContentDetailsActivity.start(this@MainActivity, contentId, contentType!!)}
        override fun onPlayItem(contentId: Int, contentType: String?) {}
    }) }
    private val popularListAdapter: ContentListAdapter by lazy { ContentListAdapter(object : OnContentBehaviorListener {
        override fun onViewItem(contentId: Int, contentType: String?) { ContentDetailsActivity.start(this@MainActivity, contentId, currentSelectedPopularContentType) }
        override fun onPlayItem(contentId: Int, contentType: String?) {}
    }) }
    private val topTrendListAdapter: ContentListAdapter by lazy { ContentListAdapter(object : OnContentBehaviorListener {
        override fun onViewItem(contentId: Int, contentType: String?) { ContentDetailsActivity.start(this@MainActivity, contentId, currentSelectedTopRatedContentType) }
        override fun onPlayItem(contentId: Int, contentType: String?) {}
    }) }

    private lateinit var binding: ActivityMainBinding
    private var currentSelectedUpComingContentType : String  = ContentType.movie.name
    private var currentSelectedPopularContentType : String  = ContentType.movie.name
    private var currentSelectedTopRatedContentType : String  = ContentType.movie.name


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.containerUpComing.rvUpComing.adapter = upComingListAdapter
        binding.containerTrending.rvTrending.adapter = trendingListAdapter
        binding.containerPopular.rvPopular.adapter = popularListAdapter
        binding.containerTopRated.rvTopTrend.adapter = topTrendListAdapter

        binding.containerUpComing.rvUpComing.setHasFixedSize(true)
        binding.containerTrending.rvTrending.setHasFixedSize(true)
        binding.containerPopular.rvPopular.setHasFixedSize(true)
        binding.containerTopRated.rvTopTrend.setHasFixedSize(true)
        observeAllData()
    }

    private fun observeAllData(){
        lifecycleScope.launch { observeUpComing() }
        lifecycleScope.launch { observeTrending() }
        lifecycleScope.launch { observePopular() }
        lifecycleScope.launch { observeTopRated() }
    }

    private suspend fun observeUpComing() {
        getContentListViewModel.upComingState.collectLatest { result ->
            binding.containerUpComing.loading.visibility = if (result.isLoading) VISIBLE else GONE
            binding.containerUpComing.tvError.visibility = if (result.error.isNotEmpty()) VISIBLE else GONE
            binding.containerUpComing.tvError.text = result.error
            binding.containerUpComing.tvUpComingHeader.text = result.header?.title
            result.header?.banner?.let { headerBanner -> binding.containerUpComing.ivBackDrop.load(ImageUrlBuilder.getW500PosterUrl(headerBanner), R.drawable.bg_landscape) }
            upComingListAdapter.differ.submitList(result.data)
        }
    }

    private suspend fun observeTrending() {
        getContentListViewModel.trendingState.collectLatest { result ->
            binding.containerTrending.loading.visibility = if (result.isLoading) VISIBLE else GONE
            binding.containerTrending.tvError.visibility = if (result.error.isNotEmpty()) VISIBLE else GONE
            binding.containerTrending.tvError.text = result.error
            binding.containerTrending.tvTrendingHeader.text = result.header?.title
            trendingListAdapter.differ.submitList(result.data)
            binding.containerTrending.buttonGroup.setOnPositionChangedListener { position -> getContentListViewModel.getTrendingList(getContentListViewModel.contentTrendingType[position]) }
        }
    }

    private suspend fun observePopular() {
        getContentListViewModel.popularState.collectLatest { result ->
            binding.containerPopular.loading.visibility = if (result.isLoading) VISIBLE else GONE
            binding.containerPopular.tvError.visibility = if (result.error.isNotEmpty()) VISIBLE else GONE
            binding.containerPopular.tvError.text = result.error
            binding.containerPopular.tvPopularHeader.text = result.header?.title
            popularListAdapter.differ.submitList(result.data)
            binding.containerPopular.buttonGroup.setOnPositionChangedListener { position ->
                val contentType = getContentListViewModel.contentType[position]
                currentSelectedPopularContentType = contentType.name
                getContentListViewModel.getPopularList(contentType)
            }
        }
    }

    private suspend fun observeTopRated() {
        getContentListViewModel.topRatedState.collectLatest { result ->
            binding.containerTopRated.loading.visibility = if (result.isLoading) VISIBLE else GONE
            binding.containerTopRated.tvError.visibility = if (result.error.isNotEmpty()) VISIBLE else GONE
            binding.containerTopRated.tvError.text = result.error
            binding.containerTopRated.tvTopRatedHeader.text = result.header?.title
            topTrendListAdapter.differ.submitList(result.data)
            binding.containerTopRated.buttonGroup.setOnPositionChangedListener { position ->
                val contentType = getContentListViewModel.contentType[position]
                currentSelectedTopRatedContentType = contentType.name
                getContentListViewModel.getTopTrendList(contentType)

            }
        }
    }

    companion object{
       fun start(context : Context){ context.startActivity(Intent(context, MainActivity::class.java))}
    }


}