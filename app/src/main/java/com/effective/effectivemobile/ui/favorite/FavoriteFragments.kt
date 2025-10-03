package com.effective.effectivemobile.ui.favorites

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
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
import com.effective.effectivemobile.databinding.FragmentFavoriteBinding
import com.effective.effectivemobile.ui.favorite.FavoritesUiState
import com.effective.effectivemobile.ui.favorite.FavoritesViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragments : Fragment(R.layout.fragment_favorite) {

    @Inject lateinit var bookmarksPrefs: BookmarksPrefs

    private var _b: FragmentFavoriteBinding? = null
    private val b get() = _b!!
    private val vm: FavoritesViewModel by viewModels()
    private lateinit var adapter: HomeAdapters

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _b = FragmentFavoriteBinding.bind(view)

        adapter = HomeAdapters(
            isBookmarked = BookmarkChecker { id -> bookmarksPrefs.isBookmarked(id) },
            toggleBookmark = BookmarkToggler { id ->
                val now = bookmarksPrefs.toggle(id)
                if (!now) vm.refreshAfterToggle()
                now
            },
            onClick = {requireContext().toast("Нет нужды") }
        )
        b.recycler.layoutManager = LinearLayoutManager(requireContext())
        b.recycler.adapter = adapter

        b.swipe.setOnRefreshListener { vm.load() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.state.collect { render(it) }
            }
        }

        if (savedInstanceState == null) vm.load()
    }

    private fun render(s: FavoritesUiState) = with(b) {
        swipe.isRefreshing = s is FavoritesUiState.Loading && adapter.itemCount > 0
        recycler.isVisible = s is FavoritesUiState.Success || (s is FavoritesUiState.Loading && adapter.itemCount > 0)
        stateStub.root.isVisible = s is FavoritesUiState.Empty || s is FavoritesUiState.Error || (s is FavoritesUiState.Loading && adapter.itemCount == 0)

        stateStub.progress.isVisible = s is FavoritesUiState.Loading && adapter.itemCount == 0
        stateStub.errorGroup.isVisible = s is FavoritesUiState.Error
        stateStub.emptyText.isVisible = s is FavoritesUiState.Empty

        when (s) {
            is FavoritesUiState.Success -> adapter.submitList(s.items)
            is FavoritesUiState.Empty -> adapter.submitList(emptyList())
            is FavoritesUiState.Error -> {
                stateStub.errorText.text = s.message
                stateStub.retryBtn.setOnClickListener { vm.load() }
                adapter.submitList(emptyList())
            }
            FavoritesUiState.Loading -> Unit
        }
    }

    override fun onDestroyView() {
        _b = null
        super.onDestroyView()
    }
}
