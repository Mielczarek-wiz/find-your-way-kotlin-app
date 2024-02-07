package com.example.findyourway

import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ShareActionProvider
import androidx.core.view.MenuItemCompat

class StatisticActivity : AppCompatActivity() {
    private var roadId : Int = 0
    private lateinit var shareActionProvider: ShareActionProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistic)
        roadId=intent.getIntExtra("roadId", 0)
        setSupportActionBar(findViewById(R.id.toolbar))
        val stoper = supportFragmentManager.findFragmentById(R.id.stoper) as StoperFragment
        stoper.setRoad(roadId.toLong())
        val db = DataBase(this)
        val best = findViewById<TextView>(R.id.best)
        val last = findViewById<TextView>(R.id.last)
        best.text= "BEST\n".plus(db.getShortestTime(roadId.toLong()))
        last.text= "LAST\n".plus(db.getLastTime(roadId.toLong()))

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val menuItem = menu?.findItem(R.id.action_share)
        menu?.findItem(R.id.action_action)?.isVisible = false
        shareActionProvider = MenuItemCompat.getActionProvider(menuItem)
                as ShareActionProvider

        return super.onCreateOptionsMenu(menu)
    }
}
