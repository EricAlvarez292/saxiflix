package com.saxipapsi.saxi_movie.presentation.get_content_list.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.saxipapsi.saxi_movie.R
import com.saxipapsi.saxi_movie.common.extension.load
import com.saxipapsi.saxi_movie.common.extension.toDateFormat
import com.saxipapsi.saxi_movie.domain.model.ContentHeaderModel
import com.saxipapsi.saxi_movie.domain.model.ContentModel
import com.saxipapsi.saxi_movie.domain.model.ImageUrlBuilder

class ContentListAdapter(private val onContentBehaviorListener : OnContentBehaviorListener? = null) : PagingDataAdapter<ContentModel, ContentListViewHolder>(DiffUtilCallBack){

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)!!

        return item.viewContentType.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView: View = when (viewType) {
            ViewContentType.ROW_DEFAULT.ordinal,
            ViewContentType.ROW_TRENDING.ordinal,
            ViewContentType.ROW_POPULAR.ordinal,
            ViewContentType.ROW_TOPRATED.ordinal -> layoutInflater.inflate(R.layout.adapter_content_list_item, parent, false)
            ViewContentType.ROW_UPCOMING.ordinal -> layoutInflater.inflate(R.layout.adapter_content_list_item_landscape, parent, false)
            else -> layoutInflater.inflate(R.layout.adapter_content_list_item, parent, false)
        }
        return ContentListViewHolder(inflatedView, onContentBehaviorListener)
    }

    override fun onBindViewHolder(holder: ContentListViewHolder, position: Int) {
        val item = getItem(position)!!
        when (item.viewContentType) {
            ViewContentType.ROW_DEFAULT,
            ViewContentType.ROW_TRENDING,
            ViewContentType.ROW_POPULAR,
            ViewContentType.ROW_TOPRATED -> holder.onInflateBody(item, R.id.ivBanner, R.id.tvBanner, R.id.tvDate, R.id.pbVotes, R.id.tvVotes)
            ViewContentType.ROW_UPCOMING -> holder.onUpComingInflateBody(item, R.id.ivBanner, R.id.ivPlay, R.id.tvBanner, R.id.tvDate)
        }
    }
    object DiffUtilCallBack : DiffUtil.ItemCallback<ContentModel>() {
        override fun areItemsTheSame(oldItem: ContentModel, newItem: ContentModel): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: ContentModel, newItem: ContentModel): Boolean = oldItem == newItem
    }

//    private val diffUtil = object : DiffUtil.ItemCallback<ContentModel>() {
//        override fun areItemsTheSame(oldItem: ContentModel, newItem: ContentModel): Boolean = oldItem == newItem
//        override fun areContentsTheSame(oldItem: ContentModel, newItem: ContentModel): Boolean = oldItem == newItem
//    }
//    val differ = AsyncListDiffer(this, diffUtil)
}


class ContentListViewHolder(itemView: View, private val onContentBehaviorListener : OnContentBehaviorListener? = null) : RecyclerView.ViewHolder(itemView),
    OnDefaultInflater,
    OnUpComingInflater,
    OnTrendingInflater,
    OnPopularInflater,
    OnTopRatedInflater {

    private val viewMap: MutableMap<Int, View> = HashMap()

    init {
        findViewItems(itemView)
    }

    private fun findViewItems(itemView: View) {
        addToMap(itemView)
        if (itemView is ViewGroup) {
            val childCount = itemView.childCount
            (0 until childCount)
                .map { itemView.getChildAt(it) }
                .forEach { findViewItems(it) }
        }
    }

    private fun addToMap(itemView: View) {
        viewMap[itemView.id] = itemView
    }

    override fun onInflateHeader(data: Any?, @IdRes tvHeader: Int) {
        val contentHeaderModel = data as ContentHeaderModel
        val _tvHeader = (viewMap[tvHeader] ?: throw IllegalArgumentException("View for $tvHeader not found")) as TextView
        _tvHeader.text = contentHeaderModel.title
    }
    override fun onInflateBody(
        data: Any?,
        @IdRes ivBanner: Int,
        @IdRes tvBanner: Int,
        @IdRes tvDate: Int,
        @IdRes pbVotes: Int,
        @IdRes tvVotes: Int,
    ) {
        val _ivBanner = (viewMap[ivBanner] ?: throw IllegalArgumentException("View for $ivBanner not found")) as ImageView
        val _pbVotes = (viewMap[pbVotes] ?: throw IllegalArgumentException("View for $pbVotes not found")) as ProgressBar
        val _tvVotes = (viewMap[tvVotes] ?: throw IllegalArgumentException("View for $tvVotes not found")) as TextView
        val _tvBanner = (viewMap[tvBanner] ?: throw IllegalArgumentException("View for $tvBanner not found")) as TextView
        val _tvDate = (viewMap[tvDate] ?: throw IllegalArgumentException("View for $tvDate not found")) as TextView
        val contentModel = data as ContentModel
        contentModel.poster?.let { _ivBanner.load(ImageUrlBuilder.getOriginalPosterUrl(it)) }
        _pbVotes.progress = contentModel.voteAverage
        _tvVotes.text = "${contentModel.voteAverage}%"
        _tvBanner.text = contentModel.title
        itemView.setOnClickListener { onContentBehaviorListener?.onViewItem(contentModel.id, contentModel.media_type) }
        _tvDate.text = contentModel.date?.toDateFormat()
    }
    override fun onUpComingInflateHeader() {

    }
    override fun onUpComingInflateBody(
        data: Any?,
        @IdRes ivBanner: Int,
        @IdRes ivPlay: Int,
        @IdRes tvBanner: Int,
        @IdRes tvDate: Int
    ) {
        val _ivBanner = (viewMap[ivBanner] ?: throw IllegalArgumentException("View for $ivBanner not found")) as ImageView
        val _ivPlay = (viewMap[ivPlay] ?: throw IllegalArgumentException("View for $ivBanner not found")) as ImageView
        val _tvBanner = (viewMap[tvBanner] ?: throw IllegalArgumentException("View for $tvBanner not found")) as TextView
        val _tvDate = (viewMap[tvDate] ?: throw IllegalArgumentException("View for $tvDate not found")) as TextView
        val contentModel = data as ContentModel
        contentModel.banner?.let { _ivBanner.load(ImageUrlBuilder.getOriginalPosterUrl(it), R.drawable.bg_landscape) }
        _tvBanner.text = contentModel.title
        itemView.setOnClickListener { onContentBehaviorListener?.onPlayItem(contentModel.id, contentModel.media_type) }
        _tvDate.text = contentModel.date?.toDateFormat()
    }
    override fun onTrendingInflateHeader() {}
    override fun onTrendingInflateBody() {}
    override fun onPopularInflateHeader() {}
    override fun onPopularInflateBody() {}
    override fun onTopRatedInflateHeader() {}
    override fun onTopRatedInflateBody() {}

}

enum class ViewContentType { ROW_DEFAULT, ROW_UPCOMING, ROW_TRENDING, ROW_POPULAR, ROW_TOPRATED }

/*ViewContentType Behavior Listeners*/
interface OnContentBehaviorListener {
    fun onViewItem(contentId : Int, contentType : String? = null)
    fun onPlayItem(contentId : Int, contentType : String? = null)
}

/*Default Inflaters*/
interface OnDefaultInflater {
    fun onInflateHeader(data: Any?, @IdRes tvHeader: Int)
    fun onInflateBody(data: Any?, @IdRes ivBanner: Int, @IdRes tvBanner: Int, @IdRes tvDate: Int, @IdRes pbVotes: Int, @IdRes tvVotes: Int)
}

/*ViewContentType Inflaters*/
interface OnUpComingInflater {
    fun onUpComingInflateHeader()
    fun onUpComingInflateBody(data: Any?, @IdRes ivBanner: Int, @IdRes ivPlay: Int, @IdRes tvBanner: Int, @IdRes tvDate: Int)
}

interface OnTrendingInflater {
    fun onTrendingInflateHeader()
    fun onTrendingInflateBody()
}

interface OnPopularInflater {
    fun onPopularInflateHeader()
    fun onPopularInflateBody()
}

interface OnTopRatedInflater {
    fun onTopRatedInflateHeader()
    fun onTopRatedInflateBody()
}
