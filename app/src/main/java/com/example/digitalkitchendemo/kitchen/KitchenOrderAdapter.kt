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
            timer?.text = model.timer[0]
            name?.text = model.name[0]
            orderType?.text = model.orderType
            orderDescription?.text = model.orderDescription
            items?.text = model.items[0]
            items1?.text = model.items1[0]
            items2?.text = model.items2[0]
            items3?.text = model.items3[0]

        }
        when (model.orderStatus) {
            "Cooking" -> {
                holder.run {
                   // v.setBackgroundResource(R.drawable.hold_order_style)
                    orderNum?.setTextColor(Color.BLACK)
                    timer?.setTextColor(Color.BLACK)
                    name?.setTextColor(Color.BLACK)
                    orderType?.setTextColor(Color.BLACK)
                    orderDescription?.setTextColor(Color.BLACK)
                    items?.setTextColor(Color.BLACK)
                    items1?.setTextColor(Color.BLACK)
                    items2?.setTextColor(Color.BLACK)
                    items3?.setTextColor(Color.BLACK)

                }
            }
            else -> R.drawable.queued_order_style
        }

    }

}


class OrderViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
    val v: View = view
    val orderNum: TextView? = view.findViewById(R.id.order_number_tv)
    val timer: TextView? = view.findViewById(R.id.timer_tv)
    val name: TextView? = view.findViewById(R.id.name_tv)
    val orderType: TextView? = view.findViewById(R.id.order_type)
    val orderDescription: TextView? = view.findViewById(R.id.order_description_tv)
    val items: TextView? = view.findViewById(R.id.order_description1_tv)
    val items1: TextView? = view.findViewById(R.id.order_description2_tv)
    val items2: TextView? = view.findViewById(R.id.order_description3_tv)
    val items3: TextView? = view.findViewById(R.id.order_description4_tv)
}