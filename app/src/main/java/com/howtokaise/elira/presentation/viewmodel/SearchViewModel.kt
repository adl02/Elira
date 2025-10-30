package com.howtokaise.elira.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.howtokaise.elira.model.ProductModel

class SearchViewModel : ViewModel() {

    var searchQuery by mutableStateOf("")
    var searchResults by mutableStateOf<List<ProductModel>>(emptyList())
    var isLoading by mutableStateOf(false)
        private set

    fun searchProducts(category: String) {
        if (category.isBlank()) return
        isLoading = true

        Firebase.firestore.collection("data")
            .document("stock")
            .collection("products")
            .whereEqualTo("category", category.trim())
            .get()
            .addOnSuccessListener { result ->
                val products = result.documents.mapNotNull { it.toObject(ProductModel::class.java) }
                searchResults = products
                isLoading = false
            }
            .addOnFailureListener {
                isLoading = false
            }
    }
}
