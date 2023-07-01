package jt.projects.gbfirestore.ui

import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.android.support.AndroidSupportInjection
import jt.projects.gbfirestore.databinding.AddNoteDialogBinding
import jt.projects.gbfirestore.model.Note
import jt.projects.gbfirestore.utils.ADD_NOTE_DIALOG_DATA_KEY
import jt.projects.gbfirestore.utils.DATE_PICKER_TAG
import jt.projects.gbfirestore.utils.toStdFormatString
import jt.projects.gbfirestore.utils.toStdLocalDate
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

class NoteDialogFragment : DialogFragment() {
    companion object {
        fun newInstance(
            note: Note?,
        ): NoteDialogFragment =
            NoteDialogFragment().apply {
                val bundle = Bundle()
                bundle.putParcelable(ADD_NOTE_DIALOG_DATA_KEY, note)
                arguments = bundle
            }
    }

    @Inject
    lateinit var viewModel: MainViewModel

    private var _binding: AddNoteDialogBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = AddNoteDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSliders()
        initDialogButtons()
        initButtonChooseDate()
    }

    private fun initSliders() {
        binding.sliderPressure1.addOnChangeListener { _, value, _ ->
            binding.tvLabelPressureValue1.text = value.toInt().toString()
        }

        binding.sliderPressure2.addOnChangeListener { _, value, _ ->
            binding.tvLabelPressureValue2.text = value.toInt().toString()
        }

        binding.sliderPulse.addOnChangeListener { _, value, _ ->
            binding.tvLabelPulseValue.text = value.toInt().toString()
        }
    }

    private fun initDialogButtons() {
        binding.btnOk.setOnClickListener {
            with(binding) {
                val newNote = Note(
                    dateTime = LocalDateTime.of(
                        btnChooseDate.text.toString().toStdLocalDate(),
                        LocalTime.of(tvTime.hour, tvTime.minute)
                    ),
                    pressure1 = sliderPressure1.value.toInt(),
                    pressure2 = sliderPressure2.value.toInt(),
                    pulse = sliderPulse.value.toInt()
                )
                viewModel.addNote(newNote)
            }
            this.dismiss()
        }

        binding.btnCancel.setOnClickListener {
            this.dismiss()
        }
    }

    private fun initButtonChooseDate() {
        binding.btnChooseDate.text = LocalDate.now().toStdFormatString()

        binding.btnChooseDate.setOnClickListener {
            val localDate = binding.btnChooseDate.text.toString().toStdLocalDate()
            val calendar = Calendar.getInstance()
            calendar.set(localDate.year, localDate.monthValue - 1, localDate.dayOfMonth)

            MaterialDatePicker.Builder
                .datePicker()
                .setTitleText("Выберите дату")
                .setSelection(calendar.timeInMillis)
                .build()
                .apply {
                    addOnPositiveButtonClickListener {
                        val date =
                            Instant.ofEpochMilli(it)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                                .toStdFormatString()
                        binding.btnChooseDate.text = date
                    }
                }
                .show(requireActivity().supportFragmentManager, DATE_PICKER_TAG)
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}