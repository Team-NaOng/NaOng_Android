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

            with(binding) {
                textTitle.text = item.title
                textTime.visibility = if (item.category == "시간") View.VISIBLE else View.GONE
                textTime.text = item.time

                val iconRes =
                    if (item.category == "시간") R.drawable.ic_time else R.drawable.ic_location
                imageIcon.setImageResource(iconRes)

                val iconBgRes = when {
                    item.hasRepeat && item.isDone -> R.drawable.bg_cell_icon_primary    // 반복 + 완료 → 진한 핑크
                    item.hasRepeat && !item.isDone -> R.drawable.bg_cell_icon_secondary // 반복 + 미완료 → 연한 핑크
                    !item.hasRepeat && item.isDone -> R.drawable.bg_cell_icon_gray      // 미반복 + 완료 → 회색
                    else -> R.drawable.bg_cell_icon_secondary                           // 미반복 + 미완료 → 연한 핑크
                }
                viewIconBackground.setBackgroundResource(iconBgRes)

                val iconColor = when {
                    item.hasRepeat && item.isDone -> R.color.secondary
                    item.isDone -> R.color.gray3
                    else -> R.color.primary
                }
                imageIcon.setColorFilter(context.getColor(iconColor))

                buttonRepeat.visibility = if (item.hasRepeat) View.VISIBLE else View.GONE

                if (item.isDone && !item.hasRepeat) {
                    textTitle.setTextColor(context.getColor(R.color.gray2))
                    textTime.setTextColor(context.getColor(R.color.gray2))
                } else {
                    textTitle.setTextColor(context.getColor(R.color.black))
                    textTime.setTextColor(context.getColor(R.color.gray3))
                }

                // TODO SeungHyun isDone 상태 변경 로직은 ViewModel로 이동하여 단방향 데이터 흐름으로 리팩토링할 것
                root.setOnClickListener {
                    item.isDone = !item.isDone

                    if (item.isDone) {
                        val messages = listOf(
                            context.getString(R.string.todo_complete_1),
                            context.getString(R.string.todo_complete_2),
                            context.getString(R.string.todo_complete_3),
                            context.getString(R.string.todo_complete_4)
                        )
                        showCustomToast(root, messages.random())
                    }
                    notifyItemChanged(adapterPosition)
                }
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
