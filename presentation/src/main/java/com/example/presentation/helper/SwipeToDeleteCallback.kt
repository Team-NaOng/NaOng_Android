package com.example.presentation.helper

import android.content.Context
import android.graphics.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.R
import com.example.presentation.dpToPx
import com.example.presentation.dpToPxF

class SwipeToDeleteCallback(
    private val context: Context,
    private val onSwipedAction: (position: Int) -> Unit
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        onSwipedAction(position)
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

        if (dX < 0) {
            val cornerRadius = 8.dpToPxF(context)
            val marginLeft = 16.dpToPx(context)
            val verticalMargin = 8.dpToPx(context)

            val top = itemView.top + verticalMargin
            val bottom = itemView.bottom - verticalMargin

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

            val text = context.getString(R.string.swipe_delete_hint)
            val textPaint = Paint().apply {
                color = Color.RED
                textSize = 12.dpToPxF(context)
                isAntiAlias = true
                textAlign = Paint.Align.CENTER
                typeface = ResourcesCompat.getFont(context, R.font.notosanskrregular)
            }

            val textX = (backgroundLeft + backgroundRight) / 2f
            val textY = itemView.top + (itemView.height / 2f -
                    (textPaint.descent() + textPaint.ascent()) / 2f)

            c.save()
            c.clipRect(backgroundLeft, top.toFloat(), backgroundRight, bottom.toFloat())
            c.drawText(text, textX, textY, textPaint)
            c.restore()
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}
