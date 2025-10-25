package com.howtokaise.elira.presentation.page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.howtokaise.elira.AppUtil
import com.howtokaise.elira.model.AddressModel
import com.howtokaise.elira.model.UserModel
import com.howtokaise.elira.presentation.navigation.GlobalNavigation

@Composable
fun EditAddress(modifier: Modifier = Modifier) {

    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var alternatePhone by remember { mutableStateOf("") }
    var pincode by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var roadName by remember { mutableStateOf("") }
    var landmark by remember { mutableStateOf("") }


    remember { mutableStateOf(UserModel()) }

    val context = LocalContext.current

    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) Color.Black else Color.White

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val result = it.result.toObject(UserModel::class.java)
                    result?.address?.let {
                        fullName = it.fullName
                        phone = it.phone
                        alternatePhone = it.alternatePhone
                        pincode = it.pincode
                        city = it.city
                        state = it.state
                        roadName = it.roadName
                        landmark = it.landmark
                    }
                }
            }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .imePadding()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black,
                modifier = Modifier
                    .size(30.dp)
                    .clickable { GlobalNavigation.navController.popBackStack() }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Edit Address",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }


        Spacer(Modifier.height(20.dp))

        // Full Name
        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            label = { Text("Full Name (Required) *") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        // Phone Number
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            label = { Text("Phone number (Required) *") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        // pincode
        OutlinedTextField(
            value = pincode,
            onValueChange = { pincode = it },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            label = { Text("Pincode (Required) *") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        // city
        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            label = { Text("City (Required) *") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        // State
        OutlinedTextField(
            value = state,
            onValueChange = { state = it },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            label = { Text("State (Required) *") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        // Road Name
        OutlinedTextField(
            value = roadName,
            onValueChange = { roadName = it },
            label = { Text("Road name, Area, Colony (Required) *") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        // Landmark
        OutlinedTextField(
            value = landmark,
            onValueChange = { landmark = it },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            label = { Text("Nearby Famous Shop/Mall/Landmark") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.weight(1f))

        // Save Button
        Button(
            onClick = {
                val addressModel = AddressModel(
                    fullName = fullName,
                    phone = phone,
                    alternatePhone = alternatePhone,
                    pincode = pincode,
                    city = city,
                    state = state,
                    roadName = roadName,
                    landmark = landmark
                )
                if (fullName.isNotEmpty() && phone.isNotEmpty() && pincode.isNotEmpty()) {
                    Firebase.firestore.collection("users")
                        .document(FirebaseAuth.getInstance().currentUser?.uid!!)
                        .update("address", addressModel)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                AppUtil.showToast(context, "Address updated successfully")
                                GlobalNavigation.navController.popBackStack()
                            }
                        }
                } else {
                    AppUtil.showToast(context, "Address can't be empty")
                }
            },
            modifier = Modifier
                .height(65.dp)
                .fillMaxWidth()
                .padding(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
            shape = RectangleShape
        ) {
            Text("SAVE ADDRESS", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}