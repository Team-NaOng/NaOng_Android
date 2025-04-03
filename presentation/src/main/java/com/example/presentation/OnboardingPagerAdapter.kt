package com.example.presentation

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnboardingPagerAdapter(activity: OnboardingActivity) : FragmentStateAdapter(activity) {
    private val layouts = listOf(
        R.layout.page_welcome,
        R.layout.page_location,
        R.layout.page_time,
        R.layout.page_today,
        R.layout.page_calendar,
        R.layout.page_onborading_done,
    )

    override fun getItemCount(): Int = layouts.size

    override fun createFragment(position: Int): Fragment {
        val isLast = position == layouts.size - 1
        return OnboardingFragment.newInstance(layouts[position], isLast)
    }
}
