package com.example.presentation

import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.adapter.AdapterTodo
import com.example.presentation.data.TodoItem
import com.example.presentation.databinding.FragmentHomeBinding
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null
    private var isShrink = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupFabAnimation()
        applyCustomFabBackground()
        binding.fabAddTodo.bringToFront()
    }
    // 셀 없을 때 테스트 코드
    /*  private fun setupRecyclerView() {
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
            val adapter = AdapterTodo()
            binding.recyclerView.adapter = adapter

            val list = listOf(
                TodoItem("반복 완료", hasRepeat = true, isDone = true),
                TodoItem("반복 미완료", hasRepeat = true, isDone = false),
                TodoItem("일반 완료", hasRepeat = false, isDone = true),
                TodoItem("일반 미완료", hasRepeat = false, isDone = false)
            )

            adapter.submitList(list)

            val list1 = emptyList<TodoItem>()
            adapter.submitList(list1)
            toggleEmptyView(list1.isEmpty())
        }*/

    // 셀 있을 때 테스트 코드
    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = AdapterTodo()
        binding.recyclerView.adapter = adapter

        val list = listOf(
            // 반복 + 완료 + 시간 있음
            TodoItem("반복 완료 (시간 있음)", hasRepeat = true, isDone = true, hasTime = true),

            // 반복 + 완료 + 시간 없음
            TodoItem("반복 완료 (시간 없음)", hasRepeat = true, isDone = true, hasTime = false),

            // 반복 + 미완료 + 시간 있음
            TodoItem("반복 미완료 (시간 있음)", hasRepeat = true, isDone = false, hasTime = true),

            // 반복 + 미완료 + 시간 없음
            TodoItem("반복 미완료 (시간 없음)", hasRepeat = true, isDone = false, hasTime = false),

            // 일반 + 완료 + 시간 있음
            TodoItem("일반 완료 (시간 있음)", hasRepeat = false, isDone = true, hasTime = true),

            // 일반 + 완료 + 시간 없음
            TodoItem("일반 완료 (시간 없음)", hasRepeat = false, isDone = true, hasTime = false),

            // 일반 + 미완료 + 시간 있음
            TodoItem("일반 미완료 (시간 있음)", hasRepeat = false, isDone = false, hasTime = true),

            // 일반 + 미완료 + 시간 없음
            TodoItem("일반 미완료 (시간 없음)", hasRepeat = false, isDone = false, hasTime = false)
        )

        adapter.submitList(list)
        toggleEmptyView(list.isEmpty())
    }


    private fun applyCustomFabBackground() {
        val fab = binding.fabAddTodo

        val radiusPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 100f, resources.displayMetrics
        )

        val shapeAppearanceModel = ShapeAppearanceModel()
            .withCornerSize(radiusPx)

        val shapeDrawable = MaterialShapeDrawable(shapeAppearanceModel).apply {
            fillColor = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), android.R.color.white)
            )
        }

        fab.background = shapeDrawable
    }

    private fun toggleEmptyView(isEmpty: Boolean) {
        binding.recyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
        binding.layoutEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun setupFabAnimation() {
        val fab = binding.fabAddTodo
        val originalText = fab.text

        fab.post {
            val expandedWidth = fab.width
            val shrinkWidth = resources.getDimensionPixelSize(R.dimen.fab_shrink_width)

            binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                    if (!isShrink && (dy > 10 || dy < -10)) {
                        animateFab(fab, expandedWidth, shrinkWidth, "")
                        isShrink = true
                    }

                    runnable?.let { handler.removeCallbacks(it) }
                    runnable = Runnable {
                        if (isShrink) {
                            animateFab(fab, shrinkWidth, expandedWidth, originalText.toString())
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

        if (targetText.isEmpty()) {
            fab.text = ""
        }

        anim.addUpdateListener {
            val newWidth = it.animatedValue as Int
            fab.layoutParams.width = newWidth
            fab.requestLayout()
        }

        anim.doOnEnd {
            if (targetText.isNotEmpty()) {
                fab.text = targetText
            }
        }

        anim.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        runnable?.let { handler.removeCallbacks(it) }
        _binding = null
    }
}
