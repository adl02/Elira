package com.howtokaise.elira

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

object WishlistUtil {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun addToWishlist(productId: String) {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users").document(uid)
            .update("wishlist", FieldValue.arrayUnion(productId))
    }

    fun removeFromWishlist(productId: String) {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users").document(uid)
            .update("wishlist", FieldValue.arrayRemove(productId))
    }
}
