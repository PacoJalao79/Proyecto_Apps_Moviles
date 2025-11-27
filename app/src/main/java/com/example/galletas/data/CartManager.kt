package com.example.galletas.data

import com.example.galletas.data.model.Product

/**
 * Objeto singleton para gestionar el estado del carrito de compras en toda la aplicación.
 * Al ser un `object`, solo existe una instancia de CartManager, asegurando que el carrito
 * es el mismo y está sincronizado en todas las pantallas.
 */
object CartManager {

    // Map que almacena productos y sus cantidades
    private val cartItems = mutableMapOf<Product, Int>()

    /**
     * Añade un producto a la lista del carrito.
     * Si ya existe, incrementa su cantidad.
     * @param product El producto a añadir.
     */
    fun addProduct(product: Product) {
        val currentQuantity = cartItems[product] ?: 0
        cartItems[product] = currentQuantity + 1
    }

    /**
     * Elimina un producto específico de la lista del carrito completamente.
     * @param product El producto a eliminar.
     */
    fun removeProduct(product: Product) {
        cartItems.remove(product)
    }

    /**
     * Incrementa la cantidad de un producto en el carrito.
     * @param product El producto cuya cantidad se incrementará.
     */
    fun increaseQuantity(product: Product) {
        val currentQuantity = cartItems[product] ?: 0
        cartItems[product] = currentQuantity + 1
    }

    /**
     * Decrementa la cantidad de un producto en el carrito.
     * Si la cantidad llega a 0, elimina el producto del carrito.
     * @param product El producto cuya cantidad se decrementará.
     */
    fun decreaseQuantity(product: Product) {
        val currentQuantity = cartItems[product] ?: 0
        if (currentQuantity > 1) {
            cartItems[product] = currentQuantity - 1
        } else {
            cartItems.remove(product)
        }
    }

    /**
     * Obtiene la cantidad de un producto específico en el carrito.
     * @param product El producto a consultar.
     * @return La cantidad del producto, o 0 si no está en el carrito.
     */
    fun getQuantity(product: Product): Int {
        return cartItems[product] ?: 0
    }

    /**
     * Devuelve una lista de productos en el carrito (sin repetir).
     * @return Una lista de solo lectura de los productos.
     */
    fun getCartItems(): List<Product> {
        return cartItems.keys.toList()
    }

    /**
     * Calcula y devuelve el precio total de todos los items en el carrito,
     * considerando las cantidades de cada producto.
     * @return El precio total como un Double.
     */
    fun getCartTotal(): Double {
        return cartItems.entries.sumOf { (product, quantity) ->
            (product.price?.toDouble() ?: 0.0) * quantity
        }
    }

    /**
     * Vacía el carrito por completo, eliminando todos los productos y cantidades.
     */
    fun clearCart() {
        cartItems.clear()
    }

    /**
     * Devuelve una lista de los IDs de los productos en el carrito,
     * repitiendo cada ID según su cantidad.
     * Esto es útil para el cuerpo de la petición al crear una nueva orden.
     * @return Una lista de enteros con los IDs de los productos.
     */
    fun getProductIds(): List<Int> {
        return cartItems.flatMap { (product, quantity) ->
            List(quantity) { product.id }
        }
    }
}
