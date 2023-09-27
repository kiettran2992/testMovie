package com.example.mymovies.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mymovies.ui.navigation.AppDestinations.MOVIES_ROUTE
import com.example.mymovies.ui.navigation.AppDestinations.MOVIE_DETAIL_ID_KEY
import com.example.mymovies.ui.navigation.AppDestinations.MOVIE_DETAIL_ROUTE
import com.example.mymovies.ui.screens.detail.MovieDetailScreen
import com.example.mymovies.ui.screens.list.MoviesScreen


private object AppDestinations {
    const val MOVIES_ROUTE = "movies"
    const val MOVIE_DETAIL_ROUTE = "movie"
    const val MOVIE_DETAIL_ID_KEY = "movieID"
}

@Composable
fun AppNavigation(
    startDestination: String = MOVIES_ROUTE,
) {
    val navController = rememberNavController()
    val actions = remember(navController) { AppActions(navController) }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(MOVIES_ROUTE) {
            MoviesScreen(navigateToPhotoDetail = actions.selectedMovie)
        }

        composable(
            "${MOVIE_DETAIL_ROUTE}/{$MOVIE_DETAIL_ID_KEY}",
            arguments = listOf(
                navArgument(MOVIE_DETAIL_ID_KEY) {
                    type = NavType.IntType
                }
            )
        ) {backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            MovieDetailScreen(
                movieId = arguments.getInt(MOVIE_DETAIL_ID_KEY),
                navigateUp = actions.navigateUp
            )
        }
    }
}

private class AppActions(
    navController: NavHostController
) {
    val selectedMovie: (Int) -> Unit = { movieId: Int ->
        navController.navigate("$MOVIE_DETAIL_ROUTE/$movieId")
    }
    val navigateUp: () -> Unit = {
        navController.navigateUp()
    }
}


