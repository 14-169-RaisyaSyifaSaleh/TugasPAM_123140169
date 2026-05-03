package com.example.a6_123140169

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.a6_123140169.data.api.NewsApiService
import com.example.a6_123140169.data.local.NewsDatabase
import com.example.a6_123140169.data.repository.NewsRepository
import com.example.a6_123140169.ui.NewsDetailScreen
import com.example.a6_123140169.ui.NewsListScreen
import com.example.a6_123140169.ui.NewsViewModel
import com.example.a6_123140169.ui.theme._6_123140169Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Initialize dependencies
        val database = NewsDatabase.getDatabase(this)
        val apiService = NewsApiService()
        val repository = NewsRepository(apiService, database.newsDao())
        val viewModelFactory = NewsViewModel.Factory(repository)

        setContent {
            _6_123140169Theme {
                NewsApp(viewModelFactory)
            }
        }
    }
}

@Composable
fun NewsApp(viewModelFactory: NewsViewModel.Factory) {
    val navController = rememberNavController()
    // Use the factory to create the ViewModel
    val viewModel: NewsViewModel = viewModel(factory = viewModelFactory)

    NavHost(navController = navController, startDestination = "news_list") {
        composable("news_list") {
            NewsListScreen(
                viewModel = viewModel,
                onArticleClick = { article ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("article", article)
                    navController.navigate("news_detail")
                }
            )
        }
        composable("news_detail") {
            val article = remember {
                navController.previousBackStackEntry?.savedStateHandle?.get<com.example.a6_123140169.data.model.Article>("article")
            }
            article?.let {
                NewsDetailScreen(
                    article = it,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
