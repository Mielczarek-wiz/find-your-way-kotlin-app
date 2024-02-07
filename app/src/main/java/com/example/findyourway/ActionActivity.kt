package com.example.findyourway

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ShareActionProvider
import androidx.core.view.MenuItemCompat

class ActionActivity : AppCompatActivity() {
    private lateinit var shareActionProvider: ShareActionProvider
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_action)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val menuItem = menu?.findItem(R.id.action_share)
        shareActionProvider = MenuItemCompat.getActionProvider(menuItem) as ShareActionProvider
        menu?.findItem(R.id.action_action)?.isVisible = false

        return super.onCreateOptionsMenu(menu)
    }
}