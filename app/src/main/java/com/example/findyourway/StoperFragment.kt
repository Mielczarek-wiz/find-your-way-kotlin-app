package com.example.findyourway

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class StoperFragment : Fragment(), View.OnClickListener {
    private var roadId : Long = 0
    private var seconds: Int = 0
    private var running: Boolean = false
    private var wasRunning: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState != null){
            roadId = savedInstanceState.getLong("roadId")
            this.seconds = savedInstanceState.getInt("seconds")
            running = savedInstanceState.getBoolean("running")
            wasRunning = savedInstanceState.getBoolean("wasRunning")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val layout = inflater.inflate(R.layout.fragment_stoper, container, false)
        runStoper(layout)
        val startButton = layout.findViewById<Button>(R.id.start_button)
        startButton.setOnClickListener(this)
        val stopButton = layout.findViewById<Button>(R.id.stop_button)
        stopButton.setOnClickListener(this)
        val resetButton = layout.findViewById<Button>(R.id.reset_button)
        resetButton.setOnClickListener(this)
        val saveButton = layout.findViewById<Button>(R.id.save_button)
        saveButton.setOnClickListener(this)
        return layout
    }
    override fun onPause() {
        super.onPause()
        wasRunning = running
        running = false
    }
    override fun onResume() {
        super.onResume()
        if(wasRunning){
            running = true
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("roadId", roadId)
        outState.putInt("seconds", seconds)
        outState.putBoolean("running", running)
        outState.putBoolean("wasRunning", wasRunning)
    }
    private fun onClickStart(){
        running = true
    }

    private fun onClickStop(){
        running = false
    }

    private fun onClickReset(){
        running = false
        seconds = 0
    }
    private fun onClickSave(){
        val context = context as Context
        val db = DataBase(context)
        db.addTime(seconds,roadId)
        running = false
        seconds = 0
    }
    private fun runStoper(view: View) {
        val timeView = view.findViewById(R.id.time_view) as TextView
        val handler = Handler()
        handler.post(object : Runnable {
            override fun run() {
                val hours = seconds / 3600
                val minutes = (seconds % 3600) / 60
                val secs = seconds % 60
                val time = String.format("%d:%02d:%02d", hours, minutes, secs)
                timeView.text = time
                if (running) {
                    seconds++
                }
                handler.postDelayed(this, 1000)
            }
        })
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.start_button -> {
                onClickStart()
            }
            R.id.stop_button -> {
                onClickStop()
            }
            R.id.reset_button -> {
                onClickReset()
            }
            R.id.save_button -> {
                onClickSave()
            }
        }
    }
    fun setRoad(Id : Long){
        roadId = Id
    }
}
