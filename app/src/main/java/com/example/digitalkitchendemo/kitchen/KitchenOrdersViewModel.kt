package com.example.digitalkitchendemo.kitchen


import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.example.digitalkitchendemo.Order
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects


class KitchenOrdersViewModel(
    private val database: FirebaseFirestore,
    application: Application
) : AndroidViewModel(application) {

    var currentSite = 0
    /*val currentSite: LiveData<Int>
        get() = _currentSite*/

    private var siteCollection = database.collection("0")

    private var queuedOrderQuery: Query


    private var holdQuery: Query

    var queuedOrderOptions: FirestoreRecyclerOptions<Order>

    var heldOrderOptions: FirestoreRecyclerOptions<Order>

    var currentOrder: Order? = null

    private var orders = mutableListOf<Order>()


    init {

        queuedOrderQuery = siteCollection.whereIn("orderStatus", mutableListOf("Queued", "Prepping Your Order"))
            //.orderBy("orderNum")

        holdQuery = siteCollection.whereEqualTo("orderStatus", "Cooking")
            //.orderBy("orderNum")

        queuedOrderOptions = FirestoreRecyclerOptions.Builder<Order>()
            .setQuery(queuedOrderQuery, Order::class.java)
            .build()

        heldOrderOptions = FirestoreRecyclerOptions.Builder<Order>()
            .setQuery(holdQuery, Order::class.java)
            .build()

        addDummyData()
        //currentOrder = queuedOrderOptions.snapshots[0]
    }

    private fun addDummyData() {
        for (value in 0..9) {
            val order = Order("Hamburger", value,"Queued", "Breakfast")
            database.collection(currentSite.toString()).document(order.orderNum.toString()).set(order)
        }
    }

    fun deleteOrder(orderNumber: Int) {
        database.collection("0").document(orderNumber.toString()).delete()
    }

    fun updateOrderStatus(orderNumber: Int?, orderStatus: String) {
        orderNumber?.let {
            database.collection("0").document(it.toString()).update("orderStatus", orderStatus)
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
                        Log.d("test", "not successful ${response.body().toString()}")
                    }
                }
            })*/
        } ?: run {
            Toast.makeText(getApplication(), "Error with Order Number", Toast.LENGTH_SHORT).show()
        }
    }

    fun updateCurrentSite(siteId: Int) {
        currentSite = siteId
        siteCollection = database.collection(currentSite.toString())
        queuedOrderQuery = siteCollection
            .whereIn("orderStatus", mutableListOf("Queued", "Prepping Your Order"))
            //.orderBy("orderNum")

        holdQuery = siteCollection
            .whereEqualTo("orderStatus", "Cooking")
            //.orderBy("orderNum")

        queuedOrderOptions = FirestoreRecyclerOptions.Builder<Order>()
            .setQuery(queuedOrderQuery, Order::class.java)
            .build()

        heldOrderOptions = FirestoreRecyclerOptions.Builder<Order>()
            .setQuery(holdQuery, Order::class.java)
            .build()

    }

}
