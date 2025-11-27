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
import com.example.galletas.data.CartManager
import com.example.galletas.data.model.OrderRequest
import com.example.galletas.databinding.FragmentCartBinding
import com.example.galletas.ui.adapter.CartAdapter
import kotlinx.coroutines.launch

/**
 * Fragmento que muestra el contenido del carrito de compras.
 * Permite al usuario ver los productos añadidos, el total, eliminar productos
 * y proceder a la compra (checkout).
 */
class CartFragment : Fragment() {

    // View Binding para acceder a las vistas de forma segura.
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    // Adapter para el RecyclerView que mostrará los ítems del carrito.
    private lateinit var cartAdapter: CartAdapter
    // Instancia del servicio de pedidos para crear una nueva orden en la API.
    private val orderService by lazy { RetrofitClient.orderService }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        updateCartView()

        // Configura el listener para el botón de "Realizar Compra".
        binding.checkoutButton.setOnClickListener {
            if (CartManager.getCartItems().isNotEmpty()) {
                performCheckout()
            }
        }

        // Configura el listener para el botón de "Limpiar Carrito".
        binding.clearCartButton.setOnClickListener {
            CartManager.clearCart()
            updateCartView()
        }
    }

    /**
     * Se llama cada vez que el fragmento se vuelve visible para el usuario.
     * Es un buen lugar para asegurar que la vista del carrito está siempre actualizada.
     */
    override fun onResume() {
        super.onResume()
        updateCartView()
    }

    /**
     * Configura el RecyclerView, su adapter y el LayoutManager.
     */
    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            emptyList(),
            onRemoveClicked = { product ->
                // Define la acción a realizar al pulsar el botón de eliminar un ítem.
                CartManager.removeProduct(product)
                updateCartView() // Actualiza la vista para reflejar el cambio.
                Toast.makeText(requireContext(), "${product.name} eliminado", Toast.LENGTH_SHORT).show()
            },
            onQuantityChanged = {
                // Actualiza la vista cuando cambie la cantidad
                updateCartView()
            }
        )
        binding.cartRecyclerView.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    /**
     * Actualiza toda la vista del carrito: la lista de productos, el precio total
     * y el estado de los botones.
     */
    private fun updateCartView() {
        // Obtiene los ítems actuales del gestor del carrito.
        val cartItems = CartManager.getCartItems()
        cartAdapter.updateItems(cartItems)

        // Calcula y formatea el precio total.
        val total = CartManager.getCartTotal()
        binding.totalPrice.text = "$${String.format("%.2f", total)}"

        // Habilita o deshabilita los botones si el carrito está vacío o no.
        val hasItems = cartItems.isNotEmpty()
        binding.checkoutButton.isEnabled = hasItems
        binding.clearCartButton.isEnabled = hasItems
    }

    /**
     * Realiza la llamada a la API para crear la orden de compra.
     */
    private fun performCheckout() {
        lifecycleScope.launch {
            try {
                // Obtiene el ID del usuario actual
                val userManager = com.example.galletas.util.UserManager(requireContext())
                val currentUser = userManager.getUser()

                if (currentUser == null) {
                    Toast.makeText(requireContext(), "Error: Usuario no encontrado", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                // Obtiene los IDs de los productos en el carrito.
                val productIds = CartManager.getProductIds()
                // Calcula el total del carrito
                val total = CartManager.getCartTotal()
                // Crea el cuerpo de la petición para la API con el total y user_id.
                val orderRequest = OrderRequest(
                    products = productIds,
                    total = total,
                    userId = currentUser.id
                )
                // Llama al endpoint de la API para crear la orden.
                orderService.createOrder(orderRequest)

                Toast.makeText(requireContext(), "¡Compra realizada con éxito!", Toast.LENGTH_LONG).show()
                // Limpia el carrito después de una compra exitosa.
                CartManager.clearCart()
                // Actualiza la vista para que el carrito aparezca vacío.
                updateCartView()

            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al realizar la compra: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Limpia la referencia al binding para evitar fugas de memoria.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
