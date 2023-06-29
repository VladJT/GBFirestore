package jt.projects.gbfirestore.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import dagger.android.support.AndroidSupportInjection
import jt.projects.gbfirestore.databinding.AddNoteDialogBinding
import jt.projects.gbfirestore.model.Note
import jt.projects.gbfirestore.utils.ADD_NOTE_DIALOG_DATA_KEY
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

class NoteDialogFragment : DialogFragment() {
    companion object {
        fun newInstance(note: Note?): NoteDialogFragment = NoteDialogFragment().apply {
            val bundle = Bundle()
            bundle.putParcelable(ADD_NOTE_DIALOG_DATA_KEY, note)
            arguments = bundle
        }
    }

    private var _binding: AddNoteDialogBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModel: MainViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = AddNoteDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        binding.btnOk.setOnClickListener {
            with(binding) {
                viewModel.addNote(
                    LocalDate.now(),
                    LocalTime.of(tvTime.hour, tvTime.minute),
                    sliderPressure1.value.toInt(),
                    sliderPressure2.value.toInt(),
                    sliderPulse.value.toInt()
                )
            }
            this.dismiss()
        }

        binding.btnCancel.setOnClickListener {
            this.dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}