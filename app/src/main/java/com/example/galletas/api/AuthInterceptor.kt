package com.example.galletas.api

import android.content.Context
import com.example.galletas.util.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Un interceptor de OkHttp que añade automáticamente el token de autenticación a las cabeceras
 * de las peticiones de red.
 *
 * @param context El contexto de la aplicación, necesario para inicializar el TokenManager.
 */
class AuthInterceptor(context: Context) : Interceptor {

    // Instancia del gestor de tokens para poder leer el token guardado.
    private val tokenManager = TokenManager(context)

    /**
     * Este método se ejecuta para cada petición de red que use el cliente OkHttp donde esté instalado.
     * Intercepta la petición, le añade la cabecera de autenticación y luego la deja continuar.
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        // Obtiene la petición original.
        val originalRequest = chain.request()
        // Obtiene el token guardado en el dispositivo.
        val token = tokenManager.getToken()

        // Si no hay token (ej: usuario invitado), simplemente deja continuar la petición original sin modificarla.
        if (token == null) {
            return chain.proceed(originalRequest)
        }

        // Si hay un token, crea una nueva petición a partir de la original, añadiendo la cabecera "Authorization".
        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token") // El formato "Bearer {token}" es un estándar.
            .build()

        // Procede con la nueva petición modificada.
        return chain.proceed(newRequest)
    }
}
