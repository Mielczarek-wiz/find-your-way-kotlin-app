package com.example.findyourway

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import androidx.appcompat.widget.ShareActionProvider
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DetailActivity : AppCompatActivity() {
    private lateinit var shareActionProvider: ShareActionProvider
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragment = supportFragmentManager.findFragmentById(R.id.detail_fragment)
                as WayDetailFragment
        var roadId = intent.getLongExtra("id", 0)
        fragment.setRoad(roadId)
        val db = DataBase(this)
        val imageView = findViewById<ImageView>(R.id.road_image)
        
        
        if (imageView != null) {
            imageView.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    resources.getIdentifier(
                        "p".plus((roadId + 1).toString()),
                        "drawable",
                        packageName
                    )
                )
            )
            imageView.contentDescription = db.getName(roadId)
        }
        
         
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener(){
            onClickDone(roadId.toInt())
        }

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val menuItem = menu?.findItem(R.id.action_share)
        menu?.findItem(R.id.action_action)?.isVisible = false
        shareActionProvider = MenuItemCompat.getActionProvider(menuItem) as ShareActionProvider


        return super.onCreateOptionsMenu(menu)
    }
    private fun onClickDone(roadId: Int){
        val intent = Intent(this, StatisticActivity::class.java)
        intent.putExtra("roadId", roadId)
        startActivity(intent)
    }
}