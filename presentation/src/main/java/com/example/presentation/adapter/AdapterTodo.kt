package com.example.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.todo.TodoItem
import com.example.presentation.R
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
            binding.textTime.visibility = if (item.category == "시간") View.VISIBLE else View.GONE
            binding.textTime.text = item.time

            val iconRes = if (item.category == "시간") R.drawable.ic_time else R.drawable.ic_location
            binding.imageIcon.setImageResource(iconRes)

            val iconBgRes = when {
                item.hasRepeat && item.isDone -> R.drawable.bg_cell_icon_primary    // 반복 + 완료 → 진한 핑크
                item.hasRepeat && !item.isDone -> R.drawable.bg_cell_icon_secondary // 반복 + 미완료 → 연한 핑크
                !item.hasRepeat && item.isDone -> R.drawable.bg_cell_icon_gray      // 미반복 + 완료 → 회색
                else -> R.drawable.bg_cell_icon_secondary                           // 미반복 + 미완료 → 연한 핑크
            }
            binding.viewIconBackground.setBackgroundResource(iconBgRes)

            val iconColor = when {
                item.hasRepeat && item.isDone -> R.color.secondary
                item.isDone -> R.color.gray3
                else -> R.color.primary
            }
            binding.imageIcon.setColorFilter(context.getColor(iconColor))

            binding.buttonRepeat.visibility = if (item.hasRepeat) View.VISIBLE else View.GONE

            if (item.isDone) {
                binding.textTitle.setTextColor(context.getColor(R.color.gray2))
                binding.textTime.setTextColor(context.getColor(R.color.gray2))
            } else {
                binding.textTitle.setTextColor(context.getColor(R.color.black))
                binding.textTime.setTextColor(context.getColor(R.color.gray3))
            }

            itemView.setOnClickListener {
                item.isDone = !item.isDone

                if (item.isDone) {
                    val context = itemView.context
                    val messages = listOf(
                        context.getString(R.string.todo_complete_1),
                        context.getString(R.string.todo_complete_2),
                        context.getString(R.string.todo_complete_3),
                        context.getString(R.string.todo_complete_4)
                    )
                    showCustomToast(itemView, messages.random())
                }
                notifyItemChanged(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding =
            CellTodolistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    fun removeItem(position: Int) {
        itemList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getItem(position: Int): TodoItem = itemList[position]

    override fun getItemCount(): Int = itemList.size
}
