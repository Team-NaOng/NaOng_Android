package com.example.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.presentation.databinding.PageOnboradingDoneBinding

class OnboardingFragment : Fragment() {

    companion object {
        private const val ARG_LAYOUT_RES_ID = "layoutResId"
        private const val ARG_IS_LAST = "isLastPage"

        fun newInstance(layoutResId: Int, isLastPage: Boolean): OnboardingFragment {
            return OnboardingFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_LAYOUT_RES_ID, layoutResId)
                    putBoolean(ARG_IS_LAST, isLastPage)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val layoutId = requireArguments().getInt(ARG_LAYOUT_RES_ID)
        val isLast = requireArguments().getBoolean(ARG_IS_LAST)

        return if (isLast && layoutId == R.layout.page_onboarding_done) {
            val binding = PageOnboradingDoneBinding.inflate(inflater, container, false)
            binding.buttonOnboardingFinish.setOnClickListener {
                (activity as? OnboardingActivity)?.finishOnboarding()
            }
            binding.root
        } else {
            inflater.inflate(layoutId, container, false)
        }
    }
}
