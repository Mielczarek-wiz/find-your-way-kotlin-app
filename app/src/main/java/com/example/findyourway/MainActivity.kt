package com.example.findyourway

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ShareActionProvider
import androidx.core.view.MenuItemCompat
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity(),
    RoadsWayListFragment.Listener, RoadsWayListFragmentTT.ListenerWW {
    private lateinit var shareActionProvider: ShareActionProvider
    private var fragmentContainer: View? =null
    private var nsv: NestedScrollView? = null
    private var fab : FloatingActionButton? = null
    private lateinit var pager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        fragmentContainer = findViewById(R.id.detail_frag_container)
        val pagerAdapter= SectionsPagerAdapter(supportFragmentManager, lifecycle, this)
        pager = findViewById(R.id.pager)
        pager.adapter = pagerAdapter
        tabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabLayout, pager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = resources.getString(R.string.home)
                }
                1 -> {
                    tab.text = resources.getString(R.string.miejskie)
                }
                else -> tab.text = resources.getString(R.string.terenowe)
            }
            }.attach()

        if(fragmentContainer != null){
            nsv = findViewById(R.id.nsv)
            nsv?.isVisible = false
            fab = findViewById(R.id.fab)
        }

        pager?.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if(fragmentContainer != null){
                    nsv = findViewById(R.id.nsv)
                    nsv?.isVisible = false
                    fab?.setOnClickListener(){

                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val menuItem = menu?.findItem(R.id.action_share)
        shareActionProvider = MenuItemCompat.getActionProvider(menuItem) as ShareActionProvider
        setShareActionIntent("Blablablablablla")

        return super.onCreateOptionsMenu(menu)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_action -> {
                val intent = Intent(this, ActionActivity::class.java)
                startActivity(intent)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
    override fun itemClicked(id: Long) {
        if (fragmentContainer != null) {
            fab?.setOnClickListener(){
                onClickDone(id.toInt())
            }
            nsv?.isVisible = true
            val details = WayDetailFragment()
            val ft = supportFragmentManager.beginTransaction()
            details.setRoad(id)
            ft.replace(R.id.detail_frag_container, details)
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            ft.addToBackStack(null)
            ft.commit()
        }
        else{
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
    }

    private fun setShareActionIntent(text: String)
    {
        val intent =Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, text)
        shareActionProvider.setShareIntent(intent)
    }
    private fun onClickDone(roadId: Int){
        val intent = Intent(this, StatisticActivity::class.java)
        intent.putExtra("roadId", roadId)
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (fragmentContainer != null) {
            nsv?.isVisible = false
            fab?.setOnClickListener(){}
        }
    }

}
private class SectionsPagerAdapter (fragmentManager: FragmentManager,
                                    lifecycle: Lifecycle,  val context: Context) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                TopFragment()
            }
            1 -> {
                RoadsWayListFragment()
            }
            else -> RoadsWayListFragmentTT()
        }
    }
}
