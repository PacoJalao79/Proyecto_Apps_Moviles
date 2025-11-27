package com.example.galletas.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.galletas.api.RetrofitClient
import com.example.galletas.data.CartManager
import com.example.galletas.data.model.Product
import com.example.galletas.databinding.FragmentProductsBinding
import com.example.galletas.ui.adapter.ProductAdapter
import com.example.galletas.util.UserManager
import kotlinx.coroutines.launch

/**
 * Fragmento que muestra la lista de todos los productos disponibles en la tienda.
 * Permite a los usuarios ver los productos, añadirlos al carrito y eliminarlos (si son administradores).
 */
class ProductsFragment : Fragment() {

    // View Binding para acceder a las vistas del layout de forma segura.
    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!

    // Adapter para el RecyclerView que mostrará los productos.
    private lateinit var productAdapter: ProductAdapter
    // Instancia del servicio de productos para interactuar con la API.
    private val productService by lazy { RetrofitClient.productService }
    // Gestor de usuario para verificar el rol.
    private lateinit var userManager: UserManager

    /**
     * Se llama para crear la vista del fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        userManager = UserManager(requireContext())
        return binding.root
    }

    /**
     * Se llama después de que la vista del fragmento ha sido creada.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        fetchProducts()
    }

    /**
     * Configura el RecyclerView, incluyendo su LayoutManager y su Adapter.
     */
    private fun setupRecyclerView() {
        val isAdmin = userManager.isAdmin()

        productAdapter = ProductAdapter(
            products = emptyList(),
            isAdmin = isAdmin,
            // Define la acción a ejecutar cuando se pulsa "Añadir al carrito".
            onAddToCartClicked = { product ->
                CartManager.addProduct(product)
                Toast.makeText(requireContext(), "${product.name} añadido al carrito", Toast.LENGTH_SHORT).show()
            },
            // Define la acción a ejecutar cuando se pulsa el botón de borrar.
            onDeleteClicked = { product ->
                showDeleteConfirmationDialog(product)
            },
            // Define la acción a ejecutar cuando se hace clic en el producto.
            onProductClicked = { product ->
                showProductDetailsDialog(product)
            }
        )

        binding.productsRecyclerView.apply {
            adapter = productAdapter
            // Usa un GridLayoutManager para mostrar los productos en una cuadrícula de 2 columnas.
            layoutManager = GridLayoutManager(context, 2)
        }
    }

    /**
     * Obtiene la lista de productos desde la API y la muestra en el RecyclerView.
     */
    private fun fetchProducts() {
        showLoading(true)
        lifecycleScope.launch {
            try {
                // Llama a la API para obtener los productos.
                val products = productService.getProducts()
                // Actualiza el adapter con la nueva lista de productos.
                productAdapter.updateProducts(products)
            } catch (e: Exception) {
                // Si hay un error de red, muestra un mensaje.
                Toast.makeText(requireContext(), "Error al cargar productos: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                // Oculta el indicador de carga, tanto si la operación tuvo éxito como si falló.
                showLoading(false)
            }
        }
    }

    /**
     * Muestra un diálogo de confirmación antes de eliminar un producto.
     * @param product El producto que se va a eliminar.
     */
    private fun showDeleteConfirmationDialog(product: Product) {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirmar Eliminación")
            .setMessage("¿Estás seguro de que quieres eliminar el producto '${product.name}'?")
            .setPositiveButton("Eliminar") { _, _ ->
                // Si el usuario pulsa "Eliminar", procede con la eliminación.
                deleteProduct(product)
            }
            .setNegativeButton("Cancelar", null) // Si pulsa "Cancelar", no hace nada.
            .show()
    }

    /**
     * Llama a la API para eliminar un producto y actualiza la lista.
     * @param product El producto a eliminar.
     */
    private fun deleteProduct(product: Product) {
        lifecycleScope.launch {
            try {
                // Llama a la API para borrar el producto usando su ID.
                productService.deleteProduct(product.id)
                Toast.makeText(requireContext(), "Producto eliminado con éxito", Toast.LENGTH_SHORT).show()
                // Vuelve a cargar la lista de productos para que el producto eliminado desaparezca.
                fetchProducts()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al eliminar el producto: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Controla la visibilidad de la barra de progreso.
     * @param isLoading `true` para mostrar la carga, `false` para ocultarla.
     */
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    /**
     * Muestra un diálogo con los detalles completos del producto y un carrusel de imágenes.
     * @param product El producto del que se mostrarán los detalles.
     */
    private fun showProductDetailsDialog(product: Product) {
        val dialogView = layoutInflater.inflate(com.example.galletas.R.layout.dialog_product_detail, null)

        // ViewPager2 para el carrusel de imágenes
        val viewPager = dialogView.findViewById<androidx.viewpager2.widget.ViewPager2>(com.example.galletas.R.id.images_viewpager)
        val tabLayout = dialogView.findViewById<com.google.android.material.tabs.TabLayout>(com.example.galletas.R.id.image_indicator)

        // Configurar carrusel de imágenes
        val images = product.image ?: emptyList()
        if (images.isNotEmpty()) {
            val imageAdapter = com.example.galletas.ui.adapter.ImageSliderAdapter(images)
            viewPager.adapter = imageAdapter

            // Conectar TabLayout con ViewPager2 para los indicadores
            com.google.android.material.tabs.TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()
        } else {
            tabLayout.visibility = View.GONE
        }

        // Configurar textos
        val nameTextView = dialogView.findViewById<android.widget.TextView>(com.example.galletas.R.id.detail_product_name)
        val priceTextView = dialogView.findViewById<android.widget.TextView>(com.example.galletas.R.id.detail_product_price)
        val descriptionLabel = dialogView.findViewById<android.widget.TextView>(com.example.galletas.R.id.detail_product_description_label)
        val descriptionTextView = dialogView.findViewById<android.widget.TextView>(com.example.galletas.R.id.detail_product_description)
        val addToCartButton = dialogView.findViewById<com.google.android.material.button.MaterialButton>(com.example.galletas.R.id.detail_add_to_cart_button)

        nameTextView.text = product.name
        priceTextView.text = "$${product.price}"

        // Como Product no tiene campo description, ocultamos la sección de descripción
        descriptionLabel.visibility = View.GONE
        descriptionTextView.visibility = View.GONE

        // Ocultar botón "Agregar al carrito" si es admin
        if (userManager.isAdmin()) {
            addToCartButton.visibility = View.GONE
        }

        // Crear y mostrar el diálogo
        val dialog = com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .create()

        // Configurar botón de agregar al carrito
        addToCartButton.setOnClickListener {
            CartManager.addProduct(product)
            Toast.makeText(requireContext(), "${product.name} añadido al carrito", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
    }

    /**
     * Se llama cuando la vista del fragmento va a ser destruida.
     * Limpia la referencia al binding para evitar fugas de memoria.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
