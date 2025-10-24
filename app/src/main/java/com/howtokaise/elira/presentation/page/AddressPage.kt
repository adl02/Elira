package com.howtokaise.elira.presentation.page

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.howtokaise.elira.AppUtil
import com.howtokaise.elira.model.UserModel

@Composable
fun AddressPage(modifier: Modifier = Modifier) {

    val userModel = remember { mutableStateOf(UserModel()) }
    var addressInput by remember { mutableStateOf(userModel.value.address) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val result = it.result.toObject(UserModel::class.java)
                    if (result != null) {
                        userModel.value = result
                        addressInput = userModel.value.address
                    }
                }
            }
    }


    TextField(
        value = addressInput,
        onValueChange = { addressInput = it },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            if (addressInput.isNotEmpty()) {
                Firebase.firestore.collection("users")
                    .document(FirebaseAuth.getInstance().currentUser?.uid!!)
                    .update("address", addressInput)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            AppUtil.showToast(context, "Address updated successfully")
                        }
                    }
            } else {
                AppUtil.showToast(context, "Address can't be empty")
            }
        })
    )
}