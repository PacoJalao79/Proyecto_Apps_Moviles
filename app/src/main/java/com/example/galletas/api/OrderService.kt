package com.example.galletas.api

import com.example.galletas.data.model.Order
import com.example.galletas.data.model.OrderRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Interfaz de Retrofit para los endpoints relacionados con los pedidos (órdenes).
 */
interface OrderService {

    /**
     * Crea una nueva orden de compra en el servidor.
     * Esta llamada requiere autenticación (gestionada por el AuthInterceptor).
     *
     * @param request El cuerpo de la petición, que contiene una lista de los IDs de los productos a comprar.
     * @return Una respuesta vacía (Response<Unit>) si la creación es exitosa.
     */
    @POST("order")
    suspend fun createOrder(@Body request: OrderRequest): Response<Unit>

    /**
     * Obtiene la lista de pedidos.
     * - Para Admin: retorna TODOS los pedidos
     * - Para Cliente: retorna solo los pedidos del usuario autenticado
     */
    @GET("order")
    suspend fun getOrders(): List<Order>
}
