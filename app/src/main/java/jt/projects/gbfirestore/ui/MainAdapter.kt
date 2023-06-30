package jt.projects.gbfirestore.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import jt.projects.gbfirestore.model.Note
import jt.projects.gbfirestore.viewholders.NoteTitleViewHolder
import jt.projects.gbfirestore.viewholders.NoteViewHolder


class MainAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val NOTE = 1
        const val TITLE = 2
    }

    private var data: List<Note> = arrayListOf()

    fun setData(data: List<Note>) {
        this.data = data
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
            holder.bind(data[position])
        }
        if (holder is NoteTitleViewHolder) {
            holder.bind(data[position])
        }
    }

    override fun getItemCount(): Int = data.size
}

class NewsDiffCallback : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }

}