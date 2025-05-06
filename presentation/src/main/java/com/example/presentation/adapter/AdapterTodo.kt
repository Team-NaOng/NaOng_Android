package com.example.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.R
import com.example.presentation.data.TodoItem
import com.example.presentation.databinding.CellTodolistBinding
import com.example.presentation.showCustomToast

class AdapterTodo : RecyclerView.Adapter<AdapterTodo.TodoViewHolder>() {

    private val itemList = mutableListOf<TodoItem>()

    fun submitList(newList: List<TodoItem>) {
        itemList.clear()
        itemList.addAll(newList)
        notifyDataSetChanged()
    }

    inner class TodoViewHolder(private val binding: CellTodolistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TodoItem) {
            val context = binding.root.context

            binding.textTitle.text = item.title
            binding.textTime.visibility = if (item.hasTime) View.VISIBLE else View.GONE

            val iconBgRes = when {
                item.hasRepeat && item.isDone -> R.drawable.bg_cell_icon_primary
                item.hasRepeat && !item.isDone -> R.drawable.bg_cell_icon_secondary
                !item.hasRepeat && item.isDone -> R.drawable.bg_cell_icon_gray
                else -> R.drawable.bg_cell_icon_secondary
            }
            binding.viewIconBackground.setBackgroundResource(iconBgRes)

            binding.buttonRepeat.visibility = if (item.hasRepeat) View.VISIBLE else View.GONE

            if (item.isDone) {
                if (item.hasRepeat) {
                    // 반복 + 완료 → 텍스트는 기본색 유지, 아이콘만 진핑크 tint
                    binding.imageIcon.setColorFilter(context.getColor(R.color.secondary))
                    binding.textTitle.setTextColor(context.getColor(R.color.black))
                    binding.textTime.setTextColor(context.getColor(R.color.gray3))
                } else {
                    // 일반 + 완료 → 회색으로 처리
                    binding.imageIcon.setColorFilter(context.getColor(R.color.gray3))
                    binding.textTitle.setTextColor(context.getColor(R.color.gray2))
                    binding.textTime.setTextColor(context.getColor(R.color.gray2))
                }
            } else {
                // 미완료 상태는 무조건 기본 색
                binding.imageIcon.setColorFilter(context.getColor(R.color.primary))
                binding.textTitle.setTextColor(context.getColor(R.color.black))
                binding.textTime.setTextColor(context.getColor(R.color.gray3))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding =
            CellTodolistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)

        holder.itemView.setOnClickListener {
            item.isDone = !item.isDone

            if (item.isDone) {
                val messages = listOf(
                    "할 일 완료! 오늘도 해냈어옹",
                    "할 일 성공! 오늘도 멋진 하루에옹",
                    "나옹! 오늘도 미션 클리어에옹",
                    "나옹! 오늘도 최고에옹"
                )
                val randomMessage = messages.random()
                showCustomToast(holder.itemView, randomMessage)
            }

            notifyItemChanged(position)
        }
    }

    fun removeItem(position: Int) {
        itemList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = itemList.size
}
