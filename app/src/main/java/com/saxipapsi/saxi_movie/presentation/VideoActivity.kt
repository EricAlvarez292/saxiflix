package com.saxipapsi.saxi_movie.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.saxipapsi.saxi_movie.databinding.ActivityVideoBinding


class VideoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycle.addObserver(binding.ytPlayer)
        binding.ytPlayer.addYouTubePlayerListener( object : YouTubePlayerListener {
            override fun onApiChange(youTubePlayer: YouTubePlayer) {}
            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {}
            override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {}
            override fun onPlaybackQualityChange(youTubePlayer: YouTubePlayer, playbackQuality: PlayerConstants.PlaybackQuality) {}
            override fun onPlaybackRateChange(youTubePlayer: YouTubePlayer, playbackRate: PlayerConstants.PlaybackRate) {}
            override fun onReady(youTubePlayer: YouTubePlayer) { youTubePlayer.loadVideo("3RhUzevTyb0", 0f)}
            override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {}
            override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {}
            override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {}
            override fun onVideoLoadedFraction(youTubePlayer: YouTubePlayer, loadedFraction: Float) {}
        })
    }


    companion object {
        fun start(context: Context) =
            context.startActivity(Intent(context, VideoActivity::class.java))
    }
}
