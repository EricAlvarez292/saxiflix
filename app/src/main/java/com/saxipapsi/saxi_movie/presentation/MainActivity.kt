package com.saxipapsi.saxi_movie.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
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
import com.saxipapsi.saxi_movie.presentation.get_content_list.components.UiAction
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getStateViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var getContentListViewModel: GetContentListViewModel

    private val upComingListAdapter: ContentListAdapter by lazy {
        ContentListAdapter(object : OnContentBehaviorListener {
            override fun onViewItem(contentId: Int, contentType: String?) {}
            override fun onPlayItem(contentId: Int, contentType: String?) { VideoDialog.newInstance(contentId, contentType ?: ContentType.movie.name).show(supportFragmentManager, VideoDialog.TAG) }
        })
    }
    private val trendingListAdapter: ContentListAdapter by lazy {
        ContentListAdapter(object : OnContentBehaviorListener {
            override fun onViewItem(contentId: Int, contentType: String?) { ContentDetailsActivity.start(this@MainActivity, contentId, contentType!!) }
            override fun onPlayItem(contentId: Int, contentType: String?) {}
        })
    }
    private val popularListAdapter: ContentListAdapter by lazy {
        ContentListAdapter(object : OnContentBehaviorListener {
            override fun onViewItem(contentId: Int, contentType: String?) { ContentDetailsActivity.start(this@MainActivity, contentId, currentSelectedPopularFilter) }
            override fun onPlayItem(contentId: Int, contentType: String?) {}
        })
    }
    private val topTrendListAdapter: ContentListAdapter by lazy {
        ContentListAdapter(object : OnContentBehaviorListener {
            override fun onViewItem(contentId: Int, contentType: String?) { ContentDetailsActivity.start(this@MainActivity, contentId, currentSelectedTopRatedFilter) }
            override fun onPlayItem(contentId: Int, contentType: String?) {}
        })
    }

    private lateinit var binding: ActivityMainBinding
    private var currentSelectedTrendingFilter: String = ContentTrendingType.day.name
    private var currentSelectedPopularFilter: String = ContentType.movie.name
    private var currentSelectedTopRatedFilter: String = ContentType.movie.name


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getContentListViewModel = getStateViewModel()

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

    private fun observeAllData() {
        lifecycleScope.launch { observeUpComingLoadState() }
        lifecycleScope.launch { observeTrendingLoadState() }
        lifecycleScope.launch { observePopularLoadState() }
        lifecycleScope.launch { observeTopRatedLoadState() }

        lifecycleScope.launch { observeUpComing() }
        lifecycleScope.launch { observeTrending() }
        lifecycleScope.launch { observePopular() }
        lifecycleScope.launch { observeTopRated() }
    }

    private suspend fun observeUpComing() {
        getContentListViewModel.upComingDataFlow.collectLatest { data ->
            upComingListAdapter.loadStateFlow.distinctUntilChangedBy { it.source.refresh }.map { it.refresh }
            upComingListAdapter.submitData(data)
        }
        binding.containerUpComing.btnRetry.setOnClickListener { upComingListAdapter.retry() }
    }


    private suspend fun observeUpComingLoadState() {
        upComingListAdapter.loadStateFlow.collectLatest { loadState ->
            upComingListAdapter.snapshot().items.asSequence().shuffled().find { true }?.banner?.let { headerBanner -> binding.containerUpComing.ivBackDrop.load(ImageUrlBuilder.getW500PosterUrl(headerBanner), R.drawable.bg_landscape) }
            binding.containerUpComing.btnRetry.isVisible =
                loadState.refresh is LoadState.NotLoading && upComingListAdapter.itemCount == 0
            binding.containerUpComing.rvUpComing.isVisible =
                loadState.source.refresh is LoadState.NotLoading || loadState.mediator?.refresh is LoadState.NotLoading
            binding.containerUpComing.loading.isVisible =
                loadState.mediator?.refresh is LoadState.Loading
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            binding.containerUpComing.tvError.isVisible = errorState?.let {
                binding.containerUpComing.tvError.text = it.error.localizedMessage
                true
            } ?: false
        }
    }


    private suspend fun observeTrendingLoadState() {
        trendingListAdapter.loadStateFlow.collectLatest { loadState ->
            binding.containerTrending.btnRetry.isVisible =
                loadState.refresh is LoadState.NotLoading && trendingListAdapter.itemCount == 0
            binding.containerTrending.rvTrending.isVisible =
                loadState.source.refresh is LoadState.NotLoading || loadState.mediator?.refresh is LoadState.NotLoading
            binding.containerTrending.loading.isVisible =
                loadState.mediator?.refresh is LoadState.Loading
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            binding.containerTrending.tvError.isVisible = errorState?.let {
                binding.containerTrending.tvError.text = it.error.localizedMessage
                true
            } ?: false
        }
    }


    private suspend fun observePopularLoadState() {
        popularListAdapter.loadStateFlow.collectLatest { loadState ->
            binding.containerPopular.btnRetry.isVisible =
                loadState.refresh is LoadState.NotLoading && popularListAdapter.itemCount == 0
            binding.containerPopular.rvPopular.isVisible =
                loadState.source.refresh is LoadState.NotLoading || loadState.mediator?.refresh is LoadState.NotLoading
            binding.containerPopular.loading.isVisible =
                loadState.mediator?.refresh is LoadState.Loading
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            binding.containerPopular.tvError.isVisible = errorState?.let {
                binding.containerPopular.tvError.text = it.error.localizedMessage
                true
            } ?: false
        }
    }

    private suspend fun observeTopRatedLoadState() {
        topTrendListAdapter.loadStateFlow.collectLatest { loadState ->
            binding.containerTopRated.btnRetry.isVisible =
                loadState.refresh is LoadState.NotLoading && topTrendListAdapter.itemCount == 0
            binding.containerTopRated.rvTopTrend.isVisible =
                loadState.source.refresh is LoadState.NotLoading || loadState.mediator?.refresh is LoadState.NotLoading
            binding.containerTopRated.loading.isVisible =
                loadState.mediator?.refresh is LoadState.Loading
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            binding.containerTopRated.tvError.isVisible = errorState?.let {
                binding.containerTopRated.tvError.text = it.error.localizedMessage
                true
            } ?: false
        }
    }


    private suspend fun observeTrending() {
        binding.containerTrending.buttonGroup.setOnPositionChangedListener { position ->
            val contentType = getContentListViewModel.contentTrendingType[position]
            currentSelectedTrendingFilter = contentType.name
            binding.containerTrending.rvTrending.scrollToPosition(0)
            getContentListViewModel.trendingSFunction.invoke(UiAction.Search(query = currentSelectedTrendingFilter))
        }
        getContentListViewModel.trendingDataFlow.collectLatest { data ->
            trendingListAdapter.loadStateFlow.distinctUntilChangedBy { it.source.refresh }.map { it.refresh }
            trendingListAdapter.submitData(data)
        }
        binding.containerTrending.btnRetry.setOnClickListener { trendingListAdapter.retry() }
    }

    private suspend fun observePopular() {
        binding.containerPopular.buttonGroup.setOnPositionChangedListener { position ->
            val contentType = getContentListViewModel.contentType[position]
            currentSelectedPopularFilter = contentType.name
            binding.containerPopular.rvPopular.scrollToPosition(0)
            getContentListViewModel.popularFunction.invoke(UiAction.Search(query = currentSelectedPopularFilter))
        }
        getContentListViewModel.popularDataFlow.collectLatest { data ->
            popularListAdapter.loadStateFlow.distinctUntilChangedBy { it.source.refresh }.map { it.refresh }
            popularListAdapter.submitData(data)
        }
        binding.containerPopular.btnRetry.setOnClickListener { popularListAdapter.retry() }
    }

    private suspend fun observeTopRated() {
        binding.containerTopRated.buttonGroup.setOnPositionChangedListener { position ->
            val contentType = getContentListViewModel.contentType[position]
            currentSelectedTopRatedFilter = contentType.name
            binding.containerTopRated.rvTopTrend.scrollToPosition(0)
            getContentListViewModel.topRatedSFunction.invoke(UiAction.Search(query = currentSelectedTopRatedFilter))
        }
        getContentListViewModel.topRatedSDataFlow.collectLatest { data ->
            topTrendListAdapter.loadStateFlow.distinctUntilChangedBy { it.source.refresh }.map { it.refresh }
            topTrendListAdapter.submitData(data)
        }
        binding.containerTopRated.btnRetry.setOnClickListener { topTrendListAdapter.retry() }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }


}