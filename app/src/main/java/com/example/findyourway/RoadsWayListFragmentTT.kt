
package com.example.findyourway

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.ListFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class RoadsWayListFragmentTT() : Fragment() {

    interface ListenerWW {
        fun itemClicked(id: Long)
    }

    private var listener: ListenerWW? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val roadRecycler = inflater.inflate(R.layout.roads, container, false) as RecyclerView
        val context = context as Context
        if (context != null) {
            val db = DataBase(context)
            val idks = db.getId("TERENOWA")
            val names = db.getNames("TERENOWA")
            val recadapter = CaptionedImagesAdapter(names, idks)
            roadRecycler.adapter = recadapter
            val layoutManager = GridLayoutManager(activity, 2)
            roadRecycler.layoutManager = layoutManager
            recadapter.setListener(object : CaptionedImagesAdapter.ListenerCIA {
                override fun onClick(position: Int) {
                    if (listener != null) {
                        listener!!.itemClicked(idks[position] - 1)
                    }
                }
            })

        }
        return roadRecycler
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as ListenerWW
    }
}
