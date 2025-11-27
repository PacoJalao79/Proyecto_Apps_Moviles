package com.example.galletas.data.model

/**
 * Modelo de datos para deserializar las respuestas de error personalizadas de la API de Xano.
 * Esto nos permite entender *por qué* una petición ha fallado (ej: email ya existe, contraseña débil, etc.).
 * `{"message":"Invalid input.","payload":{"param":"password"}}`
 *
 * @param message Un mensaje de error legible para humanos.
 * @param payload Un objeto anidado que puede contener detalles adicionales, como el parámetro que falló.
 */
data class XanoErrorResponse(
    val message: String?,
    val payload: ErrorPayload?
) {
    /**
     * Clase anidada para el objeto `payload`.
     * @param param El nombre del parámetro de entrada que causó el error.
     */
    data class ErrorPayload(
        val param: String?
    )
}
