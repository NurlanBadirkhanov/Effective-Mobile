package com.effective.effectivemobile.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.effective.courses.domain.CoursesRepository
import com.effective.courses.model.Course
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val items: List<Course>) : HomeUiState
    data object Empty : HomeUiState
    data class Error(val message: String) : HomeUiState
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: CoursesRepository
) : ViewModel() {

    private val _state = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val state: StateFlow<HomeUiState> = _state

    private val _sortDesc = MutableStateFlow(true)
    val sortDesc: StateFlow<Boolean> = _sortDesc

    private var query: String? = null

    fun setQuery(q: String?) {
        query = q?.takeIf { it.isNotBlank() }
    }

    fun toggleSort() {
        _sortDesc.value = !_sortDesc.value
        load()
    }

    fun load(q: String? = query) {
        query = q?.takeIf { it.isNotBlank() }
        _state.value = HomeUiState.Loading
        viewModelScope.launch {
            runCatching {
                repo.getCourses(sortDescByPublishDate = _sortDesc.value)
            }.onSuccess { list ->
                val filtered = query?.let { q ->
                    val qLower = q.lowercase()
                    list.filter { it.title.lowercase().contains(qLower) }
                } ?: list

                _state.value = if (filtered.isEmpty()) {
                    HomeUiState.Empty
                } else {
                    HomeUiState.Success(filtered)
                }
            }.onFailure { e ->
                _state.value = HomeUiState.Error(e.message ?: "Ошибка")
            }
        }
    }
}

