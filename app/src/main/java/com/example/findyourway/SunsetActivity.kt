package com.example.findyourway

import androidx.fragment.app.Fragment

class SunsetActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment {
        return SunsetFragment.newInstance()
    }
}