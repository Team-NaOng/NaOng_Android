package com.example.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.presentation.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var adapter: OnboardingPagerAdapter

    private val indicatorCount
        get() = adapter.itemCount - 1 // 마지막 페이지 제외한 인디케이터 수

    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)

            if (position >= indicatorCount) {
                binding.layoutDotIndicator.visibility = View.GONE
            } else {
                binding.layoutDotIndicator.visibility = View.VISIBLE
                updateIndicator(position)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.mainob) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        adapter = OnboardingPagerAdapter(this)
        binding.viewPager.adapter = adapter

        createIndicators(indicatorCount)
        updateIndicator(0)

        binding.viewPager.registerOnPageChangeCallback(pageChangeCallback)
    }

    override fun onDestroy() {
        binding.viewPager.unregisterOnPageChangeCallback(pageChangeCallback)
        super.onDestroy()
    }

    private fun createIndicators(count: Int) {
        binding.layoutDotIndicator.removeAllViews()

        repeat(count) {
            val dot = ImageView(this).apply {
                val sizeInDp = 8
                val sizeInPx = (sizeInDp * resources.displayMetrics.density).toInt()
                val marginInPx = (8 * resources.displayMetrics.density).toInt()

                layoutParams = LinearLayout.LayoutParams(sizeInPx, sizeInPx).apply {
                    marginEnd = marginInPx
                }
                setImageResource(R.drawable.bg_dot_normal)
            }
            binding.layoutDotIndicator.addView(dot)
        }
    }

    private fun updateIndicator(position: Int) {
        for (i in 0 until binding.layoutDotIndicator.childCount) {
            val imageView = binding.layoutDotIndicator.getChildAt(i) as ImageView
            val isSelected = i == position

            val sizeInDp = if (isSelected) 24 else 8
            val sizeInPx = (sizeInDp * resources.displayMetrics.density).toInt()
            val marginInPx = (8 * resources.displayMetrics.density).toInt()

            imageView.layoutParams = LinearLayout.LayoutParams(sizeInPx, sizeInPx).apply {
                marginEnd = marginInPx
            }

            imageView.setImageResource(
                if (isSelected) R.drawable.ic_naong_shadow
                else R.drawable.bg_dot_normal
            )
        }
    }

    fun finishOnboarding() {
        SharedPrefUtil.setFirstLaunchCompleted(this)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
