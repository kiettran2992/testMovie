package com.example.mymovies.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.mymovies.domain.IGetMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val getMovieUseCase: IGetMovieUseCase
) : ViewModel() {
    private val _search = MutableStateFlow("")

    private val search = _search.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = "",
        )

    fun setSearch(query: String) {
        _search.value = query
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val movies = search.debounce(300.milliseconds).flatMapLatest { query ->
        getMovieUseCase.execute(query)
    }.cachedIn(viewModelScope)

}

