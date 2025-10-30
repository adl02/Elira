package com.howtokaise.elira.presentation.page

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.howtokaise.elira.presentation.components.ProductItemView
import com.howtokaise.elira.presentation.viewmodel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPage(modifier: Modifier = Modifier, viewModel: SearchViewModel = viewModel()) {

    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) Color.Black else Color.White

    var isSearchActive by rememberSaveable { mutableStateOf(false) }

    var searchQuery = viewModel.searchQuery
    var searchResults = viewModel.searchResults
    var isLoading = viewModel.isLoading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        SearchBar(
            query = searchQuery,
            onQueryChange = { viewModel.searchQuery = it },
            onSearch = {
                viewModel.searchProducts(viewModel.searchQuery)
                isSearchActive = false
            },
            active = isSearchActive,
            onActiveChange = { isSearchActive = it },
            placeholder = { Text("Enter category") },
            modifier = Modifier.fillMaxWidth()
        ) {}
        Spacer(modifier = Modifier.width(8.dp))

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                searchResults.isEmpty() && searchQuery.isNotBlank() -> {
                    Text(
                        "No results found",
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp)
                    ) {
                        items(searchResults.chunked(2)) { rowItems ->
                            Row(modifier = Modifier.fillMaxWidth()) {
                                rowItems.forEach {
                                    ProductItemView(
                                        product = it,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                if (rowItems.size == 1) Spacer(modifier = Modifier.weight(1f))
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }
        }
    }
}