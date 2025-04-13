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
    
    private var previousIndicatorIndex = -1

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
                layoutParams = getIndicatorParams(isSelected = false)
                setImageResource(R.drawable.bg_dot_normal)
            }
            binding.layoutDotIndicator.addView(dot)
        }
    }

    private fun updateIndicator(position: Int) {
        if (position == previousIndicatorIndex) return

        // 이전 인디케이터 비활성화
        val prevView = binding.layoutDotIndicator.getChildAt(previousIndicatorIndex) as? ImageView
        prevView?.apply {
            layoutParams = getIndicatorParams(false)
            setImageResource(R.drawable.bg_dot_normal)
        }

        // 현재 인디케이터 활성화
        val currentView = binding.layoutDotIndicator.getChildAt(position) as? ImageView
        currentView?.apply {
            layoutParams = getIndicatorParams(true)
            setImageResource(R.drawable.ic_naong_shadow)
        }

        previousIndicatorIndex = position
    }

    // 공통으로 사용하는 Indicator LayoutParams 생성 함수
    private fun getIndicatorParams(isSelected: Boolean): LinearLayout.LayoutParams {
        val sizeInPx = if (isSelected)
            resources.getDimensionPixelSize(R.dimen.indicator_dot_selected_size)
        else
            resources.getDimensionPixelSize(R.dimen.indicator_dot_size)

        val marginInPx = resources.getDimensionPixelSize(R.dimen.indicator_dot_margin)

        return LinearLayout.LayoutParams(sizeInPx, sizeInPx).apply {
            marginEnd = marginInPx
        }
    }

    fun finishOnboarding() {
        SharedPrefUtil.setFirstLaunchCompleted(this)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
