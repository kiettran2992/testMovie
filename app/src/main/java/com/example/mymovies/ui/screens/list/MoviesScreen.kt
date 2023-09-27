package com.example.mymovies.ui.screens.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mymovies.R
import com.example.mymovies.data.model.ErrorState
import com.example.mymovies.data.model.db.Movie
import com.example.mymovies.ui.state.ErrorItem
import com.example.mymovies.ui.state.LoadingItem
import com.example.mymovies.ui.state.LoadingView
import com.example.mymovies.utils.Constants.Companion.IMAGE_URL_PATH
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesScreen(
    navigateToPhotoDetail: (Int) -> Unit,
    viewModel: MoviesViewModel = hiltViewModel(),
) {
    var text by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            SearchBar(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 24.dp),
                query = text,
                onQueryChange = {
                    text = it
                    viewModel.setSearch(it)
                },
                onSearch = {},
                active = false,
                onActiveChange = {},
                placeholder = {
                    Text(text = "Enter your query")
                },
                trailingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                }) {}
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        )
        {
            MovieList(movies = viewModel.movies, navigateToPhotoDetail)
        }
    }
}

@Composable
fun MovieList(movies: Flow<PagingData<Movie>>, navigateToPhotoDetail: (Int) -> Unit) {
    val lazyMovieItems = movies.collectAsLazyPagingItems()

    LazyColumn {

        items(lazyMovieItems.itemCount) { index ->
            lazyMovieItems[index]?.let { MovieItem(movie = it, navigateToPhotoDetail)}
        }

        lazyMovieItems.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item { LoadingView(modifier = Modifier.fillParentMaxSize()) }
                }
                loadState.append is LoadState.Loading -> {
                    item { LoadingItem() }
                }
                loadState.refresh is LoadState.Error -> {
                    val e = lazyMovieItems.loadState.refresh as LoadState.Error
                    item {
                        ErrorItem(
                            errorState = e.error as ErrorState,
                            modifier = Modifier.fillParentMaxSize(),
                            onClickRetry = { retry() }
                        )
                    }
                }
                loadState.append is LoadState.Error -> {
                    val e = lazyMovieItems.loadState.append as LoadState.Error
                    item {
                        ErrorItem(
                            errorState = e.error as ErrorState,
                            modifier = Modifier.fillParentMaxWidth(),
                            onClickRetry = { retry() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie, navigateToPhotoDetail: (Int) -> Unit, ) {
    Row(
        modifier = Modifier
            .padding(start = 24.dp, top = 24.dp, end = 24.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp,
            ),
            modifier = Modifier.clickable {
               navigateToPhotoDetail(movie.id)
            },
        ) {
            Column {
                MovieImage(
                    IMAGE_URL_PATH + movie.posterPath,
                    modifier = Modifier
                        .height(250.dp)
                        .clip(shape = RoundedCornerShape(size = 12.dp))
                )
                MovieTitle(
                    movie.title,
                    modifier = Modifier.padding(start = 12.dp, top = 12.dp)
                )
                MovieDescription(year = movie.releaseDate, vote = movie.voteAverage, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 16.dp))
            }
        }
    }
}


@Composable
fun MovieImage(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        placeholder = painterResource(R.drawable.ic_photo),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
}

@Composable
fun MovieTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = title,
        maxLines = 2,
        style = MaterialTheme.typography.titleMedium,
        overflow = TextOverflow.Ellipsis,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun MovieDescription(
    year: String,
    vote: String,
    modifier: Modifier
) {
    Row {
        Text(
            modifier = modifier.weight(1f),
            text = stringResource(id = R.string.rating) + " $vote",
            maxLines = 1,
            style = MaterialTheme.typography.titleSmall,
            overflow = TextOverflow.Ellipsis,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )

        Text(
            modifier = modifier.weight(1f),
            text = year,
            maxLines = 1,
            style = MaterialTheme.typography.titleSmall,
            overflow = TextOverflow.Ellipsis,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.End
        )
    }
}


