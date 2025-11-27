package com.example.galletas.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.galletas.R
import com.example.galletas.data.model.Product
import com.example.galletas.databinding.ItemProductBinding

/**
 * Adapter para el RecyclerView que muestra la lista de productos en la tienda.
 * Se encarga de tomar la lista de productos y "dibujar" cada uno en la pantalla.
 *
 * @param products La lista inicial de productos a mostrar.
 * @param isAdmin Indica si el usuario actual es administrador.
 * @param onAddToCartClicked Una función que se ejecutará cuando el usuario pulse el botón "Añadir al carrito".
 * @param onDeleteClicked Una función que se ejecutará cuando el usuario pulse el botón de eliminar.
 * @param onProductClicked Una función que se ejecutará cuando el usuario haga clic en el producto para ver detalles.
 */
class ProductAdapter(
    private var products: List<Product>,
    private val isAdmin: Boolean = false,
    private val onAddToCartClicked: (Product) -> Unit,
    private val onDeleteClicked: (Product) -> Unit,
    private val onProductClicked: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    /**
     * ViewHolder que representa una única vista de producto en la lista.
     * Mantiene una referencia al binding del layout para acceder a sus vistas (nombre, precio, etc.).
     */
    inner class ProductViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)

    /**
     * Se llama cuando el RecyclerView necesita crear un nuevo ViewHolder (una nueva "fila" o "tarjeta").
     * Infla el layout del ítem y crea una instancia del ViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    /**
     * Se llama para mostrar los datos en una posición específica. Vincula los datos del producto
     * (nombre, precio, imagen) con las vistas del ViewHolder.
     */
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        // Obtiene el producto correspondiente a esta posición.
        val product = products[position]
        // Usa `with(holder.binding)` para acceder fácilmente a las vistas.
        with(holder.binding) {
            productName.text = product.name
            productPrice.text = "$${product.price}" // Formatea el precio con un símbolo de dólar.

            // Obtiene la URL de la primera imagen del producto.
            val imageUrl = product.image?.firstOrNull()?.url
            // Carga la imagen usando la librería Coil.
            productImage.load(imageUrl) {
                placeholder(R.drawable.ic_launcher_background) // Muestra una imagen de carga.
                error(R.drawable.ic_launcher_foreground)       // Muestra una imagen si hay un error.
                crossfade(true) // Aplica una transición suave.
            }

            // Click en la tarjeta completa para ver detalles
            root.setOnClickListener {
                onProductClicked(product)
            }

            // Muestra/oculta botones según el rol del usuario.
            if (isAdmin) {
                // Admin: solo puede eliminar productos, no añadir al carrito.
                addToCartButton.visibility = View.GONE
                deleteProductButton.visibility = View.VISIBLE
                deleteProductButton.setOnClickListener {
                    onDeleteClicked(product)
                }
            } else {
                // Usuario normal: solo puede añadir al carrito, no eliminar.
                addToCartButton.visibility = View.VISIBLE
                deleteProductButton.visibility = View.GONE
                addToCartButton.setOnClickListener {
                    onAddToCartClicked(product)
                }
            }
        }
    }

    /**
     * Devuelve el número total de ítems en la lista.
     */
    override fun getItemCount() = products.size

    /**
     * Actualiza la lista de productos en el adapter y notifica al RecyclerView
     * que los datos han cambiado para que se vuelva a dibujar.
     */
    fun updateProducts(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
}
