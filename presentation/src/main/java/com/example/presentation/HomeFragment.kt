package com.example.presentation

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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.adapter.AdapterTodo
import com.example.presentation.data.TodoItem
import com.example.presentation.databinding.FragmentHomeBinding
import com.example.presentation.view.chip.CustomChipView
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
        setupSingleSelectChipGroup()
        binding.chipAll.isChipSelected = true
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
        setupSwipeToDelete(adapter)

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

    private fun setupSingleSelectChipGroup() {
        val chipGroup = binding.viewChips
        val childCount = chipGroup.childCount

        for (i in 0 until childCount) {
            val chip = chipGroup.getChildAt(i) as? CustomChipView ?: continue

            chip.setOnClickListener {
                for (j in 0 until childCount) {
                    val other = chipGroup.getChildAt(j) as? CustomChipView
                    other?.isChipSelected = false
                }
                chip.isChipSelected = true
            }
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

    private fun setupSwipeToDelete(adapter: AdapterTodo) {
        val swipeCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            override fun onMove(
                recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                showDeleteConfirmDialog(position, adapter)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val context = recyclerView.context

                if (dX < 0) {
                    val cornerRadius = 8.dpToPxF(context)
                    val marginLeft = 16.dpToPx(context)
                    val verticalMargin = 8.dpToPx(context)

                    val top = itemView.top + verticalMargin
                    val bottom = itemView.bottom - verticalMargin

                    //  완전히 왼쪽으로 슬라이드된 경우에만 마진 적용
                    val isFullySlid = dX <= -itemView.width.toFloat()
                    val backgroundLeft =
                        itemView.right.toFloat() + dX + if (isFullySlid) marginLeft.toFloat() else 0f
                    val backgroundRight = itemView.right.toFloat()

                    val path = Path().apply {
                        addRoundRect(
                            backgroundLeft,
                            top.toFloat(),
                            backgroundRight,
                            bottom.toFloat(),
                            floatArrayOf(
                                0f, 0f,
                                cornerRadius, cornerRadius,
                                cornerRadius, cornerRadius,
                                0f, 0f
                            ),
                            Path.Direction.CW
                        )
                    }


                    val backgroundPaint = Paint().apply {
                        color = ContextCompat.getColor(context, R.color.secondary)
                        isAntiAlias = true
                    }

                    c.drawPath(path, backgroundPaint)

                    //  텍스트 중앙 정렬, 배경 경계 안에서 잘리도록 clip
                    val text = "끝까지 밀어서 삭제하기"
                    val textPaint = Paint().apply {
                        color = Color.RED
                        textSize = 12.dpToPxF(context)
                        isAntiAlias = true
                        textAlign = Paint.Align.CENTER
                        typeface = ResourcesCompat.getFont(context, R.font.notosanskrregular)
                    }

                    val textX = (backgroundLeft + backgroundRight) / 2f
                    val textY =
                        itemView.top + (itemView.height / 2f - (textPaint.descent() + textPaint.ascent()) / 2)

                    c.save()
                    c.clipRect(backgroundLeft, top.toFloat(), backgroundRight, bottom.toFloat())
                    c.drawText(text, textX, textY, textPaint)
                    c.restore()
                }

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }
        ItemTouchHelper(swipeCallback).attachToRecyclerView(binding.recyclerView)
    }

    private fun showDeleteConfirmDialog(position: Int, adapter: AdapterTodo) {
        CustomAlertDialog.Builder(requireContext())
            .setTitle("정말 삭제하시겠어요?")
            .setContent("[${adapter.getItem(position).title}] 항목을 삭제할까요?")
            .setPositiveButton("삭제") {
                adapter.removeItem(position)
            }
            .setNegativeButton("취소") {
                adapter.notifyItemChanged(position)
            }
            .setCancelable(false)
            .show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        runnable?.let { handler.removeCallbacks(it) }
        _binding = null
    }
}
