package com.example.viewpager

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SeekBar
import androidx.fragment.app.Fragment

class Fragment3 : Fragment() {

    lateinit var runnable: Runnable
    private var handler = Handler()
    private var mediaplayer: MediaPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //MediaPlayer object
        val mediaplayer = MediaPlayer.create(requireContext(), R.raw.peaches)
        val playbutton = view.findViewById<ImageButton>(R.id.playBtn)
        val seekbar = view.findViewById<SeekBar>(R.id.seekbar)

        seekbar.progress = 0
        seekbar.max = mediaplayer.duration

        playbutton.setOnClickListener {
            if (!mediaplayer.isPlaying) {
                mediaplayer.start()
                playbutton.setImageResource(R.drawable.ic_baseline_pause_24)
            }else {
                mediaplayer.pause()
                playbutton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            }
        }


        //seekbar - change the position
        seekbar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, pos: Int, changed: Boolean) {
                if (changed) {
                    mediaplayer.seekTo(pos)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        runnable = Runnable {
            seekbar.progress = mediaplayer.currentPosition
            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
        mediaplayer.setOnCompletionListener {
            playbutton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            seekbar.progress = 0
        }

    }

    override fun onStop() {
        super.onStop()
        mediaplayer?.release()
        mediaplayer = null

    }
}