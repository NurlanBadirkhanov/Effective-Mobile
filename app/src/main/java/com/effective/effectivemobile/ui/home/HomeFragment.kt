package com.effective.effectivemobile.ui.home

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.compose.material3.Snackbar
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.effective.courses.adapters.HomeAdapters
import com.effective.courses.bookmarks.BookmarkChecker
import com.effective.courses.bookmarks.BookmarkToggler
import com.effective.courses.prefs.BookmarksPrefs
import com.effective.effectivemobile.Extensions.toast
import com.effective.effectivemobile.R
import com.effective.effectivemobile.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    @Inject
    lateinit var bookmarksPrefs: BookmarksPrefs

    private var _b: FragmentHomeBinding? = null
    private val b get() = _b!!
    private val vm: HomeViewModel by viewModels()
    private lateinit var adapter: HomeAdapters
    private var searchJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _b = FragmentHomeBinding.bind(view)
        setupRecycler()
        setupUi()
        observe()
        if (savedInstanceState == null) vm.load()
    }

    private fun setupRecycler() {
        adapter = HomeAdapters(
            isBookmarked = BookmarkChecker { id -> bookmarksPrefs.isBookmarked(id) },
            toggleBookmark = BookmarkToggler { id -> bookmarksPrefs.toggle(id) },
            onClick = {
                requireContext().toast("нет нужды перехода")
            }
        )
        b.recycler.layoutManager = LinearLayoutManager(requireContext())
        b.recycler.adapter = adapter
    }

    private fun setupUi() = with(b) {
        swipe.setOnRefreshListener { vm.load(searchInput.text?.toString()) }
        sortLabel.setOnClickListener { vm.toggleSort() }
        b.filterBtn.setOnClickListener { requireContext().toast("в тз нет инфо о нем") }

        searchInput.setOnEditorActionListener { v, id, _ ->
            if (id == EditorInfo.IME_ACTION_SEARCH) {
                vm.load(v.text?.toString()); hideKeyboard(); true
            } else false
        }
        searchInput.addTextChangedListener { text ->
            searchJob?.cancel()
            searchJob = viewLifecycleOwner.lifecycleScope.launch {
                delay(300); vm.load(text?.toString())
            }
        }
    }

    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.state.collect(::render)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.sortDesc.collect(::setSortLabel)
            }
        }
    }

    private fun render(s: HomeUiState) = with(b) {
        swipe.isRefreshing = s is HomeUiState.Loading && adapter.itemCount > 0
        swipe.isEnabled =
            s is HomeUiState.Success || (s is HomeUiState.Loading && adapter.itemCount > 0)
        recycler.isVisible =
            s is HomeUiState.Success || (s is HomeUiState.Loading && adapter.itemCount > 0)

        val showInitialLoading = s is HomeUiState.Loading && adapter.itemCount == 0
        stateStub.root.isVisible =
            s is HomeUiState.Empty || s is HomeUiState.Error || showInitialLoading
        stateStub.progress.isVisible = showInitialLoading
        stateStub.errorGroup.isVisible = s is HomeUiState.Error
        stateStub.emptyText.isVisible = s is HomeUiState.Empty

        when (s) {
            is HomeUiState.Success -> adapter.submitList(s.items)
            is HomeUiState.Empty -> adapter.submitList(emptyList())
            is HomeUiState.Error -> {
                stateStub.errorText.text = s.message
                stateStub.retryBtn.setOnClickListener { vm.load(searchInput.text?.toString()) }
                adapter.submitList(emptyList())
            }

            HomeUiState.Loading -> Unit
        }
    }

    private fun setSortLabel(desc: Boolean) {
        b.sortLabel.text = if (desc) "По дате добавления  ↓" else "По дате добавления  ↑"
    }

    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(b.searchInput.windowToken, 0)
    }

    override fun onDestroyView() {
        searchJob?.cancel()
        _b = null
        super.onDestroyView()
    }
}
