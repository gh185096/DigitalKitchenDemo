package com.example.digitalkitchendemo.packagingArea

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.example.digitalkitchendemo.Order
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class PackagingAreaViewModel(
    private val database: FirebaseFirestore,
    application: Application
) : AndroidViewModel(application) {

    var currentSite = 0

    private var siteCollection = database.collection("0")

    private var queuedOrderQuery: Query = siteCollection
        .whereEqualTo("orderStatus","Packaging")
        .orderBy("orderNum")


    private var holdQuery: Query = siteCollection
        .whereEqualTo("orderStatus", "Almost Ready")
        .orderBy("orderNum")

    var packagingQueuedOrderOptions = FirestoreRecyclerOptions.Builder<Order>()
        .setQuery(queuedOrderQuery, Order::class.java)
        .build()

    var packagingHoldOrderOptions = FirestoreRecyclerOptions.Builder<Order>()
        .setQuery(holdQuery, Order::class.java)
        .build()

    var currentOrder: Order? = null

    init {
        queuedOrderQuery = siteCollection
            .whereEqualTo("orderStatus","Packaging")
            .orderBy("orderNum")


        holdQuery = siteCollection
            .whereEqualTo("orderStatus", "Almost Ready")
            .orderBy("orderNum")

        packagingQueuedOrderOptions = FirestoreRecyclerOptions.Builder<Order>()
            .setQuery(queuedOrderQuery, Order::class.java)
            .build()

        packagingHoldOrderOptions = FirestoreRecyclerOptions.Builder<Order>()
            .setQuery(holdQuery, Order::class.java)
            .build()
    }

//    private fun addDummyData() {
//        for (value in 0..9) {
//            val order = Order("Hamburger\n\tNo Ketchup""Packaging" ,"Pickup")
//            database.collection(currentSite.toString()).document(order.orderNum.toString()).set(order)
//        }
//
//    }

    fun deleteOrder(orderNumber: Int) {
        database.collection("0").document(orderNumber.toString()).delete()
    }

    fun updateOrderStatus(orderNumber: String?, orderStatus: String) {
        orderNumber?.let {
            database.collection("0").document(orderNumber.toString()).update("orderStatus", orderStatus)
            /*apiController.post(orderStatus, object : Callback {
                override fun onFailure(request: Request?, e: IOException?) {
                    Log.d("test", e.toString())
                }

                @Throws(IOException::class)
                override fun onResponse(response: Response) {
                    if (response.isSuccessful) {
                        val responseStr = response.body().string()
                        Log.d("test", responseStr)
                    } else {
                        Log.d("test", "not successful")
                    }
                }
            });*/
        } ?: run {
            Toast.makeText(getApplication(), "Error with Order Number", Toast.LENGTH_SHORT).show()
        }
    }

    fun updateCurrentSite(siteId: Int) {
        currentSite = siteId
        siteCollection = database.collection(currentSite.toString())
        queuedOrderQuery = siteCollection
            .whereEqualTo("orderStatus","Packaging")
            .orderBy("orderNum")

        holdQuery = siteCollection
            .whereEqualTo("orderStatus", "Almost Ready")
            .orderBy("orderNum")

        packagingQueuedOrderOptions = FirestoreRecyclerOptions.Builder<Order>()
            .setQuery(queuedOrderQuery, Order::class.java)
            .build()

        packagingHoldOrderOptions = FirestoreRecyclerOptions.Builder<Order>()
            .setQuery(holdQuery, Order::class.java)
            .build()
    }
}