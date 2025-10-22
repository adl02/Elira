package com.howtokaise.elira

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.howtokaise.elira.model.OrderModel
import com.howtokaise.elira.presentation.navigation.GlobalNavigation
import com.razorpay.Checkout
import org.json.JSONObject
import java.util.UUID

object AppUtil {
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun addToCart(context: Context, productId: String) {
        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)

        userDoc.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val currentCart = it.result.get("cartItems") as? Map<String, Long> ?: emptyMap()
                val currentQuantity = currentCart[productId] ?: 0
                val updateQuantity = currentQuantity + 1

                val updateCart = mapOf("cartItems.$productId" to updateQuantity)

                userDoc.update(updateCart)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            showToast(context, "Item added to the cart")
                        } else {
                            showToast(context, "Failed adding item to the cart")
                        }
                    }
            }
        }
    }

    fun removeToCart(context: Context, productId: String, removeAll : Boolean = false) {
        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)

        userDoc.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val currentCart = it.result.get("cartItems") as? Map<String, Long> ?: emptyMap()
                val currentQuantity = currentCart[productId] ?: 0
                val updateQuantity = currentQuantity - 1

                val updateCart =
                    if (updateQuantity <= 0 || removeAll)
                        mapOf("cartItems.$productId" to FieldValue.delete())
                    else
                        mapOf("cartItems.$productId" to updateQuantity)

                userDoc.update(updateCart)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            showToast(context, "Item removed from cart")
                        } else {
                            showToast(context, "Failed removing item from cart")
                        }
                    }
            }
        }
    }

    fun clearCartAndAddToOrders(){
        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)

        userDoc.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val currentCart = it.result.get("cartItems") as? Map<String, Long> ?: emptyMap()
                val order = OrderModel(
                    id ="ORD_"+ UUID.randomUUID().toString().replace("_","").take(10).uppercase(),
                    userId = FirebaseAuth.getInstance().currentUser?.uid!!,
                    date = Timestamp.now(),
                    items = currentCart,
                    status = "ORDERED",
                    address = it.result.get("address") as String
                )
                Firebase.firestore.collection("orders")
                    .document(order.id).set(order)
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            userDoc.update("cartItems",FieldValue.delete())
                        }
                    }
            }
        }
    }

    fun getDiscountPercentage() : Float{
        return 10.0f
    }

    fun getTaxPercentage() : Float{
        return 13.0f
    }

    fun razorpayApiKey(): String{
        return "rzp_test_RVuWXxPgKJBpSB"
    }

    fun startPayment(amount : Float){
        val checkout = Checkout()
        checkout.setKeyID(razorpayApiKey())

        val amountInPaise = (amount * 100).toInt()

        val options = JSONObject()
        options.put("name","Elira")
        options.put("description","")
        options.put("amount",amountInPaise)
        options.put("currency","INR")

        checkout.open(GlobalNavigation.navController.context as Activity,options)
    }
}