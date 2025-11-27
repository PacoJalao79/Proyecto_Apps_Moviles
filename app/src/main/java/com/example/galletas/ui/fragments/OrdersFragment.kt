package com.example.galletas.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.galletas.api.RetrofitClient
import com.example.galletas.data.model.Order
import com.example.galletas.databinding.FragmentOrdersBinding
import com.example.galletas.ui.adapter.OrderAdapter
import com.example.galletas.util.UserManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class OrdersFragment : Fragment() {

    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!

    private lateinit var orderAdapter: OrderAdapter
    private val orderService by lazy { RetrofitClient.orderService }
    private val userManager by lazy { UserManager(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isAdmin = userManager.getUserRole() == "admin"

        // Actualizar título según el rol
        binding.titleText.text = if (isAdmin) "Todos los Pedidos" else "Mis Pedidos"

        setupRecyclerView(isAdmin)
        loadOrders()
    }

    private fun setupRecyclerView(isAdmin: Boolean) {
        orderAdapter = OrderAdapter(emptyList(), isAdmin) { order ->
            showOrderDetails(order)
        }

        binding.ordersRecyclerView.apply {
            adapter = orderAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun loadOrders() {
        binding.loadingProgress.visibility = View.VISIBLE
        binding.ordersRecyclerView.visibility = View.GONE
        binding.emptyText.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val allOrders = orderService.getOrders()
                val isAdmin = userManager.getUserRole() == "admin"
                val currentUserId = userManager.getUserId()

                // Filtrar pedidos según el rol
                val filteredOrders = if (isAdmin) {
                    // Admin ve todos los pedidos
                    allOrders
                } else {
                    // Usuario normal solo ve sus propios pedidos
                    allOrders.filter { it.user_id == currentUserId }
                }

                if (filteredOrders.isEmpty()) {
                    binding.emptyText.text = if (isAdmin) {
                        "No hay pedidos registrados"
                    } else {
                        "No tienes pedidos aún"
                    }
                    binding.emptyText.visibility = View.VISIBLE
                } else {
                    // Ordenar por fecha más reciente primero
                    val sortedOrders = filteredOrders.sortedByDescending { it.id }
                    orderAdapter.updateOrders(sortedOrders)
                    binding.ordersRecyclerView.visibility = View.VISIBLE
                }

            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Error al cargar pedidos: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
                binding.emptyText.text = "Error al cargar pedidos"
                binding.emptyText.visibility = View.VISIBLE
            } finally {
                binding.loadingProgress.visibility = View.GONE
            }
        }
    }

    private fun showOrderDetails(order: Order) {
        // Formatear fecha desde timestamp
        val date = java.util.Date(order.created_at)
        val dateFormat = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
        val formattedDate = dateFormat.format(date)

        val message = buildString {
            append("Pedido #${order.id}\n\n")
            append("Total: $${String.format(java.util.Locale.US, "%.2f", order.total)}\n")
            append("Fecha: $formattedDate\n")
            append("\n--- Información del Cliente ---\n")
            append("User ID: ${order.user_id}\n")

            if (order._user != null) {
                append("Nombre: ${order._user.name}\n")
                append("Email: ${order._user.email}\n")

                if (!order._user.phone.isNullOrBlank()) {
                    append("Teléfono: ${order._user.phone}\n")
                }

                if (!order._user.shippingAddress.isNullOrBlank()) {
                    append("Dirección: ${order._user.shippingAddress}")
                }
            } else {
                append("\n(Detalles del usuario no disponibles)")
            }
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Detalles del Pedido")
            .setMessage(message)
            .setPositiveButton("Cerrar", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

