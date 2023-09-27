package com.example.mymovies.ui.screens.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mymovies.R
import com.example.mymovies.data.model.db.Genres
import com.example.mymovies.data.model.db.MovieDetail
import com.example.mymovies.data.model.db.ProductionCompany
import com.example.mymovies.ui.state.ErrorItem
import com.example.mymovies.ui.state.LoadingView
import com.example.mymovies.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    movieId: Int,
    navigateUp: () -> Unit,
    viewModel: MovieDetailViewModel = hiltViewModel(),
) {
    val uiState = remember {
        viewModel.fetchMovieDetail(movieId = movieId)
        viewModel.uiState
    }.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.movie_detail),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable {
                                navigateUp()
                            }
                    )
                }
            )
        }, content = { paddingValues ->
            Surface(modifier = Modifier.padding(paddingValues = paddingValues)) {
                when (uiState.value) {
                    is MovieDetailViewModel.MovieDetailUIState.Loading -> {
                        LoadingView(modifier = Modifier.fillMaxSize())
                    }

                    is MovieDetailViewModel.MovieDetailUIState.Success -> {
                        val result =
                            uiState.value as MovieDetailViewModel.MovieDetailUIState.Success
                        Movie(movieDetail = result.data)
                    }

                    is MovieDetailViewModel.MovieDetailUIState.Error -> {
                        val error = uiState.value as MovieDetailViewModel.MovieDetailUIState.Error
                        ErrorItem(
                            modifier = Modifier.fillMaxSize(),
                            errorState = error.errorState,
                            onClickRetry = {
                                viewModel.fetchMovieDetail(movieId = movieId)
                            }
                        )
                    }
                }
            }

        }
    )
}

@Composable
fun Movie(movieDetail: MovieDetail) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(Constants.BIG_IMAGE_URL_PATH + movieDetail.backdropPath)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.ic_photo),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 15.dp, end = 15.dp)
        ) {
            Text(
                text = movieDetail.originalTitle ?: "",
                modifier = Modifier.padding(top = 10.dp),
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 10.dp, top = 10.dp)
            ) {
                Column(
                    Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.language),
                        fontWeight = FontWeight.Bold

                    )
                    Text(
                        text = movieDetail.originalLanguage ?: "",
                    )
                }
                Column(
                    Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.rating),
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = movieDetail.voteAverage.toString(),
                    )
                }
                Column(
                    Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.release_date),
                        fontWeight = FontWeight.Bold

                    )
                    Text(
                        text = movieDetail.releaseDate ?: "",
                    )
                }
            }
            Text(
                text = stringResource(R.string.description),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 6.dp)
            )
            Text(
                text = movieDetail.overview ?: "",
                modifier = Modifier.padding(bottom = 12.dp)
            )
            movieDetail.genres?.let { Genres(genres = it) }
            movieDetail.productionCompanies?.let { ProductionCompanies(productionCompanies = it) }
            movieDetail.homePage?.let { MoreInfo(it) }
        }
    }
}

@Composable
fun Genres(genres: List<Genres>) {
    Column(modifier = Modifier.padding(bottom = 12.dp, top = 24.dp)) {
        Text(
            text = stringResource(R.string.genres),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        LazyRow {
            items(genres.count(), itemContent = { index ->
                val item = genres[index]
                OutlinedButton(
                    onClick = { },
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.padding(end = 6.dp)
                ) {
                    Text(text = item.name)
                }
            })
        }
    }
}

@Composable
fun ProductionCompanies(productionCompanies: List<ProductionCompany>) {
    Column(modifier = Modifier.padding(bottom = 24.dp)) {
        Text(
            text = stringResource(R.string.companies),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
        )
        LazyRow {
            items(productionCompanies.count(), itemContent = { index ->
                val item = productionCompanies[index]
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(Constants.IMAGE_URL_PATH + item.logoPath)
                        .build(),
                    placeholder = painterResource(R.drawable.ic_photo),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .height(70.dp)
                        .padding(horizontal = 8.dp)
                )
            })
        }
    }
}

@Composable
fun MoreInfo(link: String) {
    val context = LocalContext.current
    val intent = remember { Intent(Intent.ACTION_VIEW, Uri.parse(link)) }

    Button(
        modifier = Modifier.padding(bottom = 16.dp),
        onClick = { context.startActivity(intent) }) {
        Text(text = stringResource(id = R.string.more_info))
    }
}
