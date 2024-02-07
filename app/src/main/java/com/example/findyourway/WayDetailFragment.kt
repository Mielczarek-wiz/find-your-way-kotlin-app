package com.example.findyourway

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction


class WayDetailFragment : Fragment() {
    private var roadId : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState != null){
            this.roadId = savedInstanceState.getLong("roadId")
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_way_detail, container, false)
    }

    override fun onStart() {
        super.onStart()
        val view = view as View
        val context = context as Context
        if (view != null && context  != null) {
            val db = DataBase(context)
            val road = view.findViewById<TextView>(R.id.road)
            road.text = db.getRoad(roadId)

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("roadId", roadId)
    }

    fun setRoad(Id : Long){
        roadId = Id
    }
}