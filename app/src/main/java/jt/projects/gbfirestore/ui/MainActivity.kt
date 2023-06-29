package jt.projects.gbfirestore.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.AndroidInjection
import jt.projects.gbfirestore.databinding.ActivityMainBinding
import jt.projects.gbfirestore.utils.ADD_NOTE_DIALOG_TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var viewModel: MainViewModel

    private val mainAdapter by lazy { MainAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AndroidInjection.inject(this)

        initUi()
        observeViewModelData()
        observeLoadingVisible()
    }

    private fun initUi() {
        with(binding.rvNotes) {
            layoutManager = LinearLayoutManager(context)
            adapter = mainAdapter
        }

        binding.floatingActionButton.setOnClickListener {
            showAddNoteDialog()
        }
    }

    private fun observeViewModelData() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.resultRecycler.collect {
                mainAdapter.setData(it)
            }
        }
//        lifecycleScope.launch {
//            this@MainActivity.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel
//                    .resultRecycler
//                    .collect {
//                        mainAdapter.setData(it)
//                    }
//            }
//        }
    }

    private fun observeLoadingVisible() {
        this.lifecycleScope.launch {
            this@MainActivity.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collect {
                    binding.loadingFrameLayout.root.isVisible = it
                    binding.progressCircular.isVisible = it
                }
            }
        }
    }

    private fun showAddNoteDialog() {
        NoteDialogFragment.newInstance(null).show(
            supportFragmentManager,
            ADD_NOTE_DIALOG_TAG
        )
    }
}