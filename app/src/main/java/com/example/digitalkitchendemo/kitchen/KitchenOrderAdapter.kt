package com.example.digitalkitchendemo.kitchen


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.digitalkitchendemo.Order
import com.example.digitalkitchendemo.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class KitchenOrderAdapter(optionsIn: FirestoreRecyclerOptions<Order>) :
    FirestoreRecyclerAdapter<Order, OrderViewHolder>(optionsIn) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.order_layout, parent, false)
        return OrderViewHolder(v)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int, model: Order) {

        holder.run {
            orderNum?.text = model.orderNum.toString()
            orderType?.text = model.orderType
            orderDescription?.text = model.orderDescription
        }
        when (model.orderStatus) {
            "Cooking" -> {
                holder.run {
                    v.setBackgroundResource(R.drawable.hold_order_style)
                    orderNum?.setTextColor(Color.BLACK)
                    orderType?.setTextColor(Color.BLACK)
                    orderDescription?.setTextColor(Color.BLACK)
                }
            }
            else -> R.drawable.queued_order_style
        }

    }

}


class OrderViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
    val v: View = view
    val orderNum: TextView? = view.findViewById(R.id.order_number_tv)
    val orderType: TextView? = view.findViewById(R.id.order_type)
    val orderDescription: TextView? = view.findViewById(R.id.order_description_tv)
}