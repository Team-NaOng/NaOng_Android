package com.example.presentation

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.presentation.databinding.FragmentAddTodoBottomSheetBinding
import com.example.presentation.util.getTodayDateText
import com.google.android.material.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddTodoBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAddTodoBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var selectedAlarmType: String = "위치"

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme).apply {
            setOnShowListener { dialog ->
                val bottomSheetDialog = dialog as BottomSheetDialog
                val bottomSheet =
                    bottomSheetDialog.findViewById<View>(
                        R.id.design_bottom_sheet
                    )

                bottomSheet?.let {
                    val behavior = BottomSheetBehavior.from(it)
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    behavior.skipCollapsed = true
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTodoBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupChipSelection()
        setupTodayDate()
        binding.imageViewClose.setOnClickListener {
            dismiss()
        }



        binding.confirmButton.setOnClickListener {
            // todo 저장 로직 추후 추가 예정입니다
            dismiss()
        }
    }

    private fun setupChipSelection() {
        binding.chipAll.isChipSelected = true
        binding.chipTime.isChipSelected = false

        val chips = listOf(binding.chipAll, binding.chipTime)

        chips.forEach { chip ->
            chip.setOnClickListener {
                chips.forEach { it.isChipSelected = false }
                chip.isChipSelected = true
                selectedAlarmType = chip.text.toString()
            }
        }
    }

    private fun setupTodayDate() {
        binding.textViewTitle.text = getTodayDateText()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}