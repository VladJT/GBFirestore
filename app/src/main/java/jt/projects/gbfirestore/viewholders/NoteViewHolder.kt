package jt.projects.gbfirestore.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jt.projects.gbfirestore.databinding.ItemNoteBinding
import jt.projects.gbfirestore.model.Note
import jt.projects.gbfirestore.ui.adapters.ItemTouchHelperViewHolder
import jt.projects.gbfirestore.utils.toHourMinString

class NoteViewHolder private constructor(
    private val binding: ItemNoteBinding

) : RecyclerView.ViewHolder(binding.root), ItemTouchHelperViewHolder {

    constructor(parent: ViewGroup) : this(
        ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    fun bind(
        data: Note,
        onEditNoteClicked: ((Note) -> Unit)?
    ) {
        if (layoutPosition != RecyclerView.NO_POSITION) {
            with(binding) {
                tvId.text = data.id
                tvPressure1.text = data.pressure1.toString()
                tvPressure2.text = data.pressure2.toString()
                tvPulse.text = data.pulse.toString()
                tvTime.text = data.dateTime.toHourMinString()
            }

//            binding.root.setOnClickListener {
//                onDeleteNoteClicked?.invoke(data)
//            }
        }
    }

    override fun onItemSelected() {

    }

    override fun onItemClear() {

    }
}