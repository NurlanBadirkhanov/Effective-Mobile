package com.effective.effectivemobile.ui.favorite


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.effective.courses.model.Course
import com.effective.courses.domain.CoursesRepository
import com.effective.courses.prefs.BookmarksPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface FavoritesUiState {
    data object Loading : FavoritesUiState
    data class Success(val items: List<Course>) : FavoritesUiState
    data object Empty : FavoritesUiState
    data class Error(val message: String) : FavoritesUiState
}

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repo: CoursesRepository,
    private val bookmarks: BookmarksPrefs
) : ViewModel() {

    private val _state = MutableStateFlow<FavoritesUiState>(FavoritesUiState.Loading)
    val state: StateFlow<FavoritesUiState> = _state

    fun load() {
        _state.value = FavoritesUiState.Loading
        viewModelScope.launch {
            runCatching { repo.getCourses(sortDescByPublishDate = true) }
                .onSuccess { list ->
                    val fav = list.filter { bookmarks.isBookmarked(it.id) }
                    _state.value = if (fav.isEmpty()) FavoritesUiState.Empty
                    else FavoritesUiState.Success(fav)
                }
                .onFailure { e ->
                    _state.value = FavoritesUiState.Error(e.message ?: "Ошибка")
                }
        }
    }

    fun refreshAfterToggle() = load()
}
