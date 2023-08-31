package com.saxipapsi.saxi_movie.presentation.get_content_credits.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.saxipapsi.saxi_movie.R
import com.saxipapsi.saxi_movie.common.CommonRecyclerViewHolder
import com.saxipapsi.saxi_movie.common.extension.load
import com.saxipapsi.saxi_movie.domain.model.CreditsModel
import com.saxipapsi.saxi_movie.domain.model.ImageUrlBuilder
import com.saxipapsi.saxi_movie.domain.model.RecyclerViewContainer

class ContentCreditsAdapter : RecyclerView.Adapter<ContentCreditsViewHolder>() {

    override fun getItemViewType(position: Int): Int =
        if (differ.currentList[position].isHeader) CreditsViewType.HEADER.ordinal else CreditsViewType.BODY.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentCreditsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = when (viewType) {
            CreditsViewType.HEADER.ordinal -> layoutInflater.inflate(R.layout.adapter_credits_header_item, parent, false)
            else -> layoutInflater.inflate(R.layout.adapter_credits_body_item, parent, false)
        }
        return ContentCreditsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContentCreditsViewHolder, position: Int) {
        val item = differ.currentList[position]
        if (item.isHeader) {
            holder.onInflateHeader(item.title, R.id.tvCreditsHeader)
        } else holder.onInflateBody(item.data, R.id.ivBanner, R.id.tvOriginalName, R.id.tvCharacterOrJob)
    }

    override fun getItemCount(): Int = differ.currentList.size

    private val diffUtil = object : DiffUtil.ItemCallback<RecyclerViewContainer>() {
        override fun areItemsTheSame(
            oldItem: RecyclerViewContainer,
            newItem: RecyclerViewContainer
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: RecyclerViewContainer,
            newItem: RecyclerViewContainer
        ): Boolean = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, diffUtil)

}

class ContentCreditsViewHolder(itemView: View) : CommonRecyclerViewHolder(itemView),
    OnDefaultInflater {
    override fun onInflateHeader(data: Any?, tvHeader: Int) {
        val _tvHeader = (viewMap[tvHeader]
            ?: throw IllegalArgumentException("View for $tvHeader not found")) as TextView
        val header = data as String
        _tvHeader.text = header
    }

    override fun onInflateBody(
        data: Any?,
        @IdRes ivBanner: Int,
        @IdRes tvOriginalName: Int,
        @IdRes tvCharacterOrJob: Int,
    ) {
        val _ivBanner = (viewMap[ivBanner] ?: throw IllegalArgumentException("View for $ivBanner not found")) as ImageView
        val _tvOriginalName = (viewMap[tvOriginalName] ?: throw IllegalArgumentException("View for $tvOriginalName not found")) as TextView
        val _tvCharacterOrJob = (viewMap[tvCharacterOrJob] ?: throw IllegalArgumentException("View for $tvCharacterOrJob not found")) as TextView
        val creditsModel = data as CreditsModel
        creditsModel.profilePath?.let { _ivBanner.load(ImageUrlBuilder.getW500PosterUrl(it)) }
        _tvOriginalName.text = creditsModel.fullName
        _tvCharacterOrJob.text = creditsModel.character ?: creditsModel.job
    }
}


/*Default Inflaters*/
interface OnDefaultInflater {
    fun onInflateHeader(data: Any?, @IdRes tvHeader: Int)
    fun onInflateBody(
        data: Any?,
        @IdRes ivBanner: Int,
        @IdRes tvOriginalName: Int,
        @IdRes tvCharacterOrJob: Int,
    )
}

enum class CreditsViewType { HEADER, BODY }