package jt.projects.gbfirestore.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import jt.projects.gbfirestore.model.Note
import jt.projects.gbfirestore.viewholders.NoteTitleViewHolder
import jt.projects.gbfirestore.viewholders.NoteViewHolder

//Наш адаптер имплементирует ItemTouchHelperAdapter для управления элементами через
//вспомогательные класс . onItemMove будет вызываться, когда элемент списка будет
//перетянут на достаточное расстояние, чтобы запустить анимацию перемещения. onItemDismiss
//будет вызываться во время свайпа по элементу.
//Также имплементируем интерфейс ItemTouchHelperViewHolder в нашем ViewHolder, чтобы
//управлять элементами через тот же вспомогательный класс: onItemSelected будет вызываться в
//процессе смахивания или перетаскивания элемента, onItemClear — когда этот процесс закончится.
interface ItemTouchHelperAdapter {
    fun onItemMove(fromPosition: Int, toPosition: Int)
    fun onItemDismiss(position: Int)
}

interface ItemTouchHelperViewHolder {
    fun onItemSelected()
    fun onItemClear()
}

class MainAdapter(
    private var onEditNoteClicked: ((Note) -> Unit)?,
    private var onDeleteNoteClicked: ((Note) -> Unit)?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ItemTouchHelperAdapter {

    companion object {
        const val NOTE = 1
        const val TITLE = 2
    }

    private var data = mutableListOf<Note>()

    fun setData(data: List<Note>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position].isHeader) {
            true -> TITLE
            false -> NOTE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            TITLE -> NoteTitleViewHolder(parent)
            NOTE -> NoteViewHolder(parent)
            else -> throw IllegalStateException()
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NoteViewHolder) {
            holder.bind(data[position], onEditNoteClicked, onDeleteNoteClicked)
        }
        if (holder is NoteTitleViewHolder) {
            holder.bind(data[position])
        }
    }

    override fun getItemCount(): Int = data.size

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        data.removeAt(fromPosition).apply {
            data.add(
                if (toPosition > fromPosition) toPosition - 1 else toPosition,
                this
            )
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }
}

class NewsDiffCallback : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }

}