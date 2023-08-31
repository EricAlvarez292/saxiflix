package com.saxipapsi.saxi_movie.presentation.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.saxipapsi.saxi_movie.common.extension.width
import com.saxipapsi.saxi_movie.databinding.DialogVideoBinding
import com.saxipapsi.saxi_movie.presentation.get_content_video.components.ContentVideoViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class VideoDialog(private val contentId : Int, private val contentType : String) : BaseDialog() {

    private lateinit var binding: DialogVideoBinding
    private val contentDetailsModel : ContentVideoViewModel by inject()

    override fun onStart() {
        super.onStart()
        this.width(100)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogVideoBinding.inflate(layoutInflater)
        val view = binding.root
        lifecycle.addObserver(binding.ytPlayer)
        lifecycleScope.launch { observeContentVideo() }
        return view
    }

    override fun onResume() {
        super.onResume()
        contentDetailsModel.getContentVideo(contentId, contentType)
    }

    private suspend fun observeContentVideo(){
        contentDetailsModel.videoState.collectLatest { result ->
            binding.loading.visibility = if (result.isLoading) View.VISIBLE else View.GONE
            binding.tvError.visibility = if (result.error.isNotEmpty()) View.VISIBLE else View.GONE
            binding.tvError.text = result.error
            Log.d("Eric", "observeContentVideo()")
            val trailer = result.data?.results?.find { video -> video.type == "Trailer" }
            trailer?.let { onPlay(it.key) } ?: kotlin.run { /*Toast.makeText(requireActivity(), "No video trailer available", Toast.LENGTH_LONG).show()*/ }
        }
    }

    private fun onPlay(key : String){
        binding.ytPlayer.addYouTubePlayerListener( object : YouTubePlayerListener {
            override fun onApiChange(youTubePlayer: YouTubePlayer) {}
            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {}
            override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {}
            override fun onPlaybackQualityChange(youTubePlayer: YouTubePlayer, playbackQuality: PlayerConstants.PlaybackQuality) {}
            override fun onPlaybackRateChange(youTubePlayer: YouTubePlayer, playbackRate: PlayerConstants.PlaybackRate) {}
            override fun onReady(youTubePlayer: YouTubePlayer) { youTubePlayer.loadVideo(key, 0f)}
            override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {}
            override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {}
            override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {}
            override fun onVideoLoadedFraction(youTubePlayer: YouTubePlayer, loadedFraction: Float) {}
        })
    }

    companion object {
        const val TAG = "VideoDialog"
        fun newInstance(contentId : Int, contentType : String) = VideoDialog(contentId, contentType)
    }
}