package jt.projects.gbfirestore.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jt.projects.gbfirestore.databinding.ItemNoteTitleBinding
import jt.projects.gbfirestore.model.Note
import jt.projects.gbfirestore.ui.adapters.ItemTouchHelperViewHolder

class NoteTitleViewHolder private constructor(
    private val binding: ItemNoteTitleBinding
) : RecyclerView.ViewHolder(binding.root), ItemTouchHelperViewHolder {

    constructor(parent: ViewGroup) : this(
        ItemNoteTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    fun bind(data: Note) {
        if (layoutPosition != RecyclerView.NO_POSITION) {
            binding.tvDate.text =
                "${data.dateTime.dayOfMonth} ${data.dateTime.month} ${data.dateTime.year}"
        }
    }

    override fun onItemSelected() {

    }

    override fun onItemClear() {

    }
}