package com.crayosa.surveil.fragments

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.crayosa.surveil.R
import com.crayosa.surveil.databinding.FragmentPlayerBinding
import com.crayosa.surveil.utils.YouTubeUtils
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener


class PlayerFragment : Fragment() {
    private var youTubePlayer : YouTubePlayer? = null
    val args : PlayerFragmentArgs by navArgs()
    private lateinit var binding: FragmentPlayerBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_player, container, false
        )
        (requireActivity() as AppCompatActivity).supportActionBar!!.hide()
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        val listener = object: AbstractYouTubePlayerListener(){
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                this@PlayerFragment.youTubePlayer = youTubePlayer
                youTubePlayer.loadVideo(
                    YouTubeUtils.getId(args.lecture.url),
                    0.0f
                )
                binding.youTubePlayerView.enterFullScreen()
            }
        }

        binding.youTubePlayerView.initialize(listener)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.youTubePlayerView.release()
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        (requireActivity() as AppCompatActivity).supportActionBar!!.show()
    }
}