package jt.projects.gbfirestore.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.android.AndroidInjection
import jt.projects.gbfirestore.databinding.ActivityMainBinding
import jt.projects.gbfirestore.model.Note
import jt.projects.gbfirestore.ui.adapters.ItemTouchHelperCallback
import jt.projects.gbfirestore.ui.adapters.MainAdapter
import jt.projects.gbfirestore.utils.ADD_NOTE_DIALOG_TAG
import jt.projects.gbfirestore.utils.showSnackbar
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var viewModel: MainViewModel


    lateinit var itemTouchHelper: ItemTouchHelper//  для свайпов и т.п.
    private val mainAdapter by lazy {
        MainAdapter(
            onEditNoteClicked = viewModel::onEditNoteClicked,
            onDeleteNoteClicked = viewModel::onDeleteNoteClicked
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AndroidInjection.inject(this)

        //showGreetings()

        viewModel.loadData()
        initUi()
        observeViewModelData()
        observeLoadingVisible()
        observeErrors()
        observeEditNote()
    }

    private fun showGreetings() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Важно!")
            .setMessage(
                "Данные хранятся в Cloud Firestore\n\n" +
                        "Для добавления данных нажмите FloatingActionButton\n\n" +
                        "Для редактирования данных нажмите на заметку\n\n" +
                        "Для удаления данных смахните элемент"
            )
            .setPositiveButton("ОК") { _, _ ->
            }
            .show()
    }

    private fun initUi() {
        with(binding.rvNotes) {
            layoutManager = LinearLayoutManager(context)
            adapter = mainAdapter
        }

        itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(mainAdapter))
        itemTouchHelper.attachToRecyclerView(binding.rvNotes)

        binding.floatingActionButton.setOnClickListener {
            showAddNoteDialog(null)
        }
    }

    private fun observeViewModelData() {
        viewModel.liveDataForViewToObserve.observe(this) {
            mainAdapter.setData(it)
        }
//        this@MainActivity.lifecycleScope.launch {
//            this@MainActivity.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.resultRecycler.collect {
//                    mainAdapter.setData(it)
//                }
//            }
//        }
    }

    private fun observeLoadingVisible() {
        this@MainActivity.lifecycleScope.launch {
            this@MainActivity.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collect {
                    binding.loadingFrameLayout.root.isVisible = it
                    binding.progressCircular.isVisible = it
                }
            }
        }
    }

    private fun observeErrors() {
        viewModel.someErrorLiveData.observe(this) {
            showSnackbar(it)
        }
    }

    private fun observeEditNote() {
        this@MainActivity.lifecycleScope.launch {
            this@MainActivity.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.noteIdFlow
                    .collect {
                        showAddNoteDialog(it)
                    }
            }
        }
    }

    private fun showAddNoteDialog(note: Note?) {
        NoteDialogFragment.newInstance(note)
            .show(
                supportFragmentManager,
                ADD_NOTE_DIALOG_TAG
            )
    }
}