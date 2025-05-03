package com.example.presentation.view.tab

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.presentation.R
import com.example.presentation.databinding.SlidingTabToggleViewBinding

class SlidingTabToggleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val binding: SlidingTabToggleViewBinding
    private var selectedTab: Tab = Tab.LOCATION

    var onTabSelected: ((Tab) -> Unit)? = null

    enum class Tab {
        LOCATION, TIME
    }

    init {
        val inflater = LayoutInflater.from(context)
        binding = SlidingTabToggleViewBinding.inflate(inflater, this, true)

        binding.tabLocation.setOnClickListener { selectTab(Tab.LOCATION) }
        binding.tabTime.setOnClickListener { selectTab(Tab.TIME) }

        // 뷰 레이아웃 완료 후 초기 위치 설정
        post {
            val tabWidth = binding.tabContainer.width / 2

            binding.viewSelectedTabOverlay.layoutParams =
                binding.viewSelectedTabOverlay.layoutParams.apply {
                    width = tabWidth
                }
            binding.viewSelectedTabOverlay.requestLayout()
            selectTab(selectedTab, forceUpdate = true)
        }

    }

    private fun selectTab(tab: Tab, forceUpdate: Boolean = false) {
        if (!forceUpdate && selectedTab == tab) return
        selectedTab = tab

        when (tab) {
            Tab.LOCATION -> {
                updateTabUI(
                    selectedIcon = binding.imageViewLocation,
                    selectedText = binding.textViewLocation,
                    unselectedIcon = binding.imageViewTime,
                    unselectedText = binding.textViewTime
                )
            }

            Tab.TIME -> {
                updateTabUI(
                    selectedIcon = binding.imageViewTime,
                    selectedText = binding.textViewTime,
                    unselectedIcon = binding.imageViewLocation,
                    unselectedText = binding.textViewLocation
                )
            }
        }

        onTabSelected?.invoke(tab)
        moveSelectedOverlay(tab, animate = !forceUpdate)
    }


    private fun moveSelectedOverlay(tab: Tab, animate: Boolean) {
        val targetX = when (tab) {
            Tab.LOCATION -> 0f
            Tab.TIME -> binding.tabContainer.width / 2f
        }

        if (animate) {
            binding.viewSelectedTabOverlay.animate()
                .translationX(targetX)
                .setDuration(220)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .start()
        } else {
            binding.viewSelectedTabOverlay.translationX = targetX
        }
    }

    private fun animateTextAndIconColor(
        icon: ImageView,
        text: TextView,
        fromIconColor: Int,
        toIconColor: Int,
        fromTextColor: Int,
        toTextColor: Int
    ) {
        val duration = 220L

        ValueAnimator.ofArgb(fromIconColor, toIconColor).apply {
            setDuration(duration)
            addUpdateListener { animator ->
                icon.setColorFilter(animator.animatedValue as Int)
            }
            start()
        }

        ValueAnimator.ofArgb(fromTextColor, toTextColor).apply {
            setDuration(duration)
            addUpdateListener { animator ->
                text.setTextColor(animator.animatedValue as Int)
            }
            start()
        }
    }


    private fun updateTabUI(
        selectedIcon: ImageView,
        selectedText: TextView,
        unselectedIcon: ImageView,
        unselectedText: TextView
    ) {
        val colorPrimary = ContextCompat.getColor(context, R.color.primary)
        val colorBlack = ContextCompat.getColor(context, R.color.black)
        val colorGray2 = ContextCompat.getColor(context, R.color.gray2)
        val colorGray3 = ContextCompat.getColor(context, R.color.gray3)

        // 선택된 쪽 부드럽게 강조
        animateTextAndIconColor(
            icon = selectedIcon,
            text = selectedText,
            fromIconColor = colorGray2,
            toIconColor = colorPrimary,
            fromTextColor = colorGray3,
            toTextColor = colorBlack
        )

        // 선택 해제된 쪽 부드럽게 축소
        animateTextAndIconColor(
            icon = unselectedIcon,
            text = unselectedText,
            fromIconColor = colorPrimary,
            toIconColor = colorGray2,
            fromTextColor = colorBlack,
            toTextColor = colorGray3
        )
    }


    fun setSelectedTab(tab: Tab) {
        selectTab(tab)
    }

    fun getSelectedTab(): Tab = selectedTab

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        binding.viewSelectedTabOverlay.animate().cancel()
    }

}
