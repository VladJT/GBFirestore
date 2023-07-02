package jt.projects.gbfirestore.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jt.projects.gbfirestore.model.Note
import jt.projects.gbfirestore.viewholders.NoteTitleViewHolder
import jt.projects.gbfirestore.viewholders.NoteViewHolder


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
            holder.bind(data[position], onEditNoteClicked)
        }
        if (holder is NoteTitleViewHolder) {
            holder.bind(data[position])
        }
    }

    override fun getItemCount(): Int = data.size

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
//        data.removeAt(fromPosition).apply {
//            data.add(
//                if (toPosition > fromPosition) toPosition - 1 else toPosition,
//                this
//            )
//        }
//        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        if (data[position].isHeader) {
            // заголовок нельзя удалять - восстанавливаем после swipe-а обратно
            notifyItemChanged(position)
        } else {
            // удаляем данные swipe-ом
            onDeleteNoteClicked?.let { it.invoke(data[position]) }
            data.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}