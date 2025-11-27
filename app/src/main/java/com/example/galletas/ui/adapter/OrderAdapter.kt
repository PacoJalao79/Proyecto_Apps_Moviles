package com.example.galletas.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.galletas.R
import com.example.galletas.data.model.Order
import java.text.SimpleDateFormat
import java.util.Locale

class OrderAdapter(
    private var orders: List<Order>,
    private val isAdmin: Boolean,
    private val onOrderClick: (Order) -> Unit
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderIdText: TextView = itemView.findViewById(R.id.order_id_text)
        val orderDateText: TextView = itemView.findViewById(R.id.order_date_text)
        val orderTotalText: TextView = itemView.findViewById(R.id.order_total_text)
        val orderUserText: TextView = itemView.findViewById(R.id.order_user_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]

        holder.orderIdText.text = "Pedido #${order.id}"
        holder.orderTotalText.text = "$${String.format(Locale.US, "%.2f", order.total)}"

        // Convertir timestamp a fecha legible
        holder.orderDateText.text = formatTimestamp(order.created_at)

        // Si es admin, mostrar información del usuario
        if (isAdmin) {
            holder.orderUserText.visibility = View.VISIBLE

            // Mostrar nombre del usuario si está disponible, sino mostrar ID
            if (order._user != null) {
                holder.orderUserText.text = "Cliente: ${order._user.name} (ID: ${order.user_id})"
            } else {
                holder.orderUserText.text = "Usuario ID: ${order.user_id}"
            }
        } else {
            holder.orderUserText.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            onOrderClick(order)
        }
    }

    private fun formatTimestamp(timestamp: Long): String {
        return try {
            val date = java.util.Date(timestamp)
            val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            outputFormat.format(date)
        } catch (e: Exception) {
            "Fecha inválida"
        }
    }

    override fun getItemCount(): Int = orders.size

    fun updateOrders(newOrders: List<Order>) {
        orders = newOrders
        notifyDataSetChanged()
    }
}

