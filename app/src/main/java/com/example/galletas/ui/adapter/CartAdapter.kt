package com.example.galletas.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.galletas.R
import com.example.galletas.data.CartManager
import com.example.galletas.data.model.Product
import com.example.galletas.databinding.ItemCartProductBinding

/**
 * Adapter para el RecyclerView que muestra los productos en el carrito de compras.
 * Es similar al ProductAdapter, pero usa un layout más compacto.
 *
 * @param products La lista inicial de productos en el carrito.
 * @param onRemoveClicked Una función que se ejecutará cuando el usuario pulse el botón de eliminar un ítem.
 * @param onQuantityChanged Una función que se ejecutará cuando cambie la cantidad de un producto.
 */
class CartAdapter(
    private var products: List<Product>,
    private val onRemoveClicked: (Product) -> Unit,
    private val onQuantityChanged: () -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    /**
     * ViewHolder que representa una única vista de producto en la lista del carrito.
     */
    inner class CartViewHolder(val binding: ItemCartProductBinding) : RecyclerView.ViewHolder(binding.root)

    /**
     * Crea un nuevo ViewHolder inflando el layout del ítem del carrito.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    /**
     * Vincula los datos de un producto (nombre, precio, imagen, cantidad) con las vistas del ViewHolder.
     */
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = products[position]
        val quantity = CartManager.getQuantity(product)

        with(holder.binding) {
            productNameCart.text = product.name
            productPriceCart.text = "$${product.price}"
            quantityText.text = quantity.toString()

            val imageUrl = product.image?.firstOrNull()?.url
            productImageCart.load(imageUrl) {
                placeholder(R.drawable.ic_launcher_background)
                error(R.drawable.ic_launcher_foreground)
                crossfade(true)
            }

            // Botón para incrementar cantidad
            increaseQuantityButton.setOnClickListener {
                CartManager.increaseQuantity(product)
                quantityText.text = CartManager.getQuantity(product).toString()
                onQuantityChanged()
            }

            // Botón para decrementar cantidad
            decreaseQuantityButton.setOnClickListener {
                CartManager.decreaseQuantity(product)
                onQuantityChanged()
            }

            // Asigna la función onRemoveClicked al botón de eliminar.
            removeItemButton.setOnClickListener {
                onRemoveClicked(product)
            }
        }
    }

    /**
     * Devuelve el número total de ítems en el carrito.
     */
    override fun getItemCount() = products.size

    /**
     * Actualiza la lista de productos en el adapter y notifica al RecyclerView.
     */
    fun updateItems(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
}
