package com.example.findyourway

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment


class SunsetFragment : Fragment() {
    private lateinit var mSceneView:View
    private lateinit var mSunView:View
    private lateinit var mSkyView:View
    private var mBlueSkyColor: Int =0
    private var mSunsetSkyColor: Int =0
    private lateinit var  animatorSet: AnimatorSet
    companion object {

        fun newInstance(): SunsetFragment {

            return SunsetFragment()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sunset, container, false)
        mSceneView = view
        mSunView = view.findViewById(R.id.sun)
        mSkyView = view.findViewById(R.id.sky)
        mBlueSkyColor = resources.getColor(R.color.black)
        mSunsetSkyColor = resources.getColor(R.color.sunset_sky)

        mSceneView.setOnClickListener { startAnimation()
        mSceneView.setOnClickListener{}}
        return view
    }
    private fun startAnimation(){
        val sunYStart:Float = mSkyView.bottom.toFloat() - mSunView.height
        val sunYEnd:Float = mSkyView.height/2.toFloat() - mSunView.height/2

        val heightAnimator: ObjectAnimator = ObjectAnimator
            .ofFloat(mSunView, "y", sunYStart,sunYEnd)
            .setDuration(1000)
        val sunsetSkyAnimator: ObjectAnimator = ObjectAnimator
            .ofInt(mSkyView, "backgroundColor", mSunsetSkyColor, mBlueSkyColor)
            .setDuration(3000)
        heightAnimator.interpolator = AccelerateInterpolator()
        sunsetSkyAnimator.setEvaluator(ArgbEvaluator())
        animatorSet = AnimatorSet()
        animatorSet.play(heightAnimator).with(sunsetSkyAnimator)
        animatorSet.start()
        animatorSet.doOnEnd {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

    }
}