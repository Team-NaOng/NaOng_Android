package com.example.presentation.home

import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.CustomAlertDialog
import com.example.presentation.R
import com.example.presentation.adapter.AdapterTodo
import com.example.presentation.databinding.FragmentHomeBinding
import com.example.presentation.dpToPx
import com.example.presentation.dpToPxF
import com.example.presentation.helper.SwipeToDeleteCallback
import com.example.presentation.showCustomToast
import com.example.presentation.view.chip.CustomChipView
import com.example.presentation.view.tab.SlidingTabToggleView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: AdapterTodo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTodayDate()
        setupRecyclerView()
        setupTopTabToggle()
        setupSingleSelectChipGroup()
        setupFabAnimation()
        applyCustomFabBackground()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            binding.textViewToday.setPadding(0, statusBarHeight, 0, 0)
            insets
        }

        viewModel.todoList.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            toggleEmptyView(list.isEmpty())
        }

        viewModel.filterTodoList()
        binding.chipAll.isChipSelected = true
        binding.fabAddTodo.bringToFront()
    }

    private fun setupRecyclerView() {
        adapter = AdapterTodo()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        setupSwipeToDelete()
    }

    private fun setupTopTabToggle() {
        binding.viewSlidingTabToggle.onTabSelected = { tab ->
            viewModel.selectedMainCategory = when (tab) {
                SlidingTabToggleView.Tab.LOCATION -> "위치"
                SlidingTabToggleView.Tab.TIME -> "시간"
            }
            resetChipSelectionToAll()
            viewModel.filterTodoList()
        }
    }

    private fun resetChipSelectionToAll() {
        val chipGroup = binding.viewChips
        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as? CustomChipView ?: continue
            chip.isChipSelected = chip.id == R.id.chipAll
        }
        viewModel.selectedSubCategory = "전체"
    }

    private fun setupSingleSelectChipGroup() {
        val chipGroup = binding.viewChips
        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as? CustomChipView ?: continue
            chip.setOnClickListener {
                for (j in 0 until chipGroup.childCount) {
                    val other = chipGroup.getChildAt(j) as? CustomChipView
                    other?.isChipSelected = false
                }
                chip.isChipSelected = true
                viewModel.selectedSubCategory = chip.text.toString()
                viewModel.filterTodoList()
            }
        }
    }

    private fun setupFabAnimation() {
        val fab = binding.fabAddTodo
        fab.post {
            val expandedWidth = fab.width
            val shrinkWidth = resources.getDimensionPixelSize(R.dimen.fab_shrink_width)
            binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                private val handler = Handler(Looper.getMainLooper())
                private var runnable: Runnable? = null
                private var isShrink = false

                override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                    if (!isShrink && (dy > 10 || dy < -10)) {
                        animateFab(fab, expandedWidth, shrinkWidth, "")
                        isShrink = true
                    }
                    runnable?.let { handler.removeCallbacks(it) }
                    runnable = Runnable {
                        if (isShrink) {
                            animateFab(fab, shrinkWidth, expandedWidth, getString(R.string.fab_add_todo))
                            isShrink = false
                        }
                    }.also { handler.postDelayed(it, 1000L) }
                }
            })
        }
    }

    private fun animateFab(
        fab: ExtendedFloatingActionButton,
        fromWidth: Int,
        toWidth: Int,
        targetText: String
    ) {
        val anim = ValueAnimator.ofInt(fromWidth, toWidth).setDuration(200)
        if (targetText.isEmpty()) fab.text = ""
        anim.addUpdateListener {
            val newWidth = it.animatedValue as Int
            fab.layoutParams.width = newWidth
            fab.requestLayout()
        }
        anim.doOnEnd { if (targetText.isNotEmpty()) fab.text = targetText }
        anim.start()
    }

    private fun toggleEmptyView(isEmpty: Boolean) {
        binding.recyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
        binding.layoutEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun setupSwipeToDelete() {
        val callback = SwipeToDeleteCallback(requireContext()) { position ->
            showDeleteConfirmDialog(position)
        }
        ItemTouchHelper(callback).attachToRecyclerView(binding.recyclerView)
    }

    private fun showDeleteConfirmDialog(position: Int) {
        CustomAlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.delete_confirm_title))
            .setContent(getString(R.string.delete_confirm_content, adapter.getItem(position).title))
            .setPositiveButton(getString(R.string.delete_confirm_positive)) {
                adapter.removeItem(position)
                if (adapter.itemCount == 0) {
                    binding.recyclerView.visibility = View.GONE
                    binding.layoutEmpty.visibility = View.VISIBLE
                }
                showCustomToast(binding.root, getString(R.string.delete_success))
            }
            .setNegativeButton(getString(R.string.delete_confirm_negative)) {
                adapter.notifyItemChanged(position) // 스와이프 복구
            }
            .setCancelable(false)
            .show()
    }

    private fun applyCustomFabBackground() {
        val fab = binding.fabAddTodo
        val radiusPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 100f, resources.displayMetrics
        )

        val shapeAppearanceModel = ShapeAppearanceModel().withCornerSize(radiusPx)
        val shapeDrawable = MaterialShapeDrawable(shapeAppearanceModel).apply {
            fillColor = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), android.R.color.white)
            )
            elevation = fab.elevation
        }
        fab.background = shapeDrawable
    }

    private fun setupTodayDate() {
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일", Locale.KOREAN)
        binding.textViewToday.text = today.format(formatter)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
