package com.example.galletas.api

import android.content.Context
import com.example.galletas.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Objeto singleton que gestiona la configuración y creación de los servicios de Retrofit.
 * Centraliza toda la configuración de red en un solo lugar.
 */
object RetrofitClient {

    // Instancias de Retrofit para los diferentes tipos de endpoints
    private lateinit var authRetrofit: Retrofit // Auth sin token (login, signup)
    private lateinit var authRetrofitAuthenticated: Retrofit // Auth con token (me, perfil)
    private lateinit var storeRetrofit: Retrofit // Tienda con token (productos, pedidos)

    /**
     * Inicializa el cliente Retrofit. Debe llamarse una vez al inicio de la aplicación (en HomeActivity).
     * @param context El contexto de la aplicación.
     */
    fun init(context: Context) {
        // Crea un interceptor para registrar las llamadas y respuestas de la API en el Logcat.
        // Esto es muy útil para depurar. Solo se activa en el modo DEBUG.
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

        // Cliente OkHttp base, usado para llamadas que no requieren autenticación.
        val baseOkHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        // Cliente OkHttp autenticado. Añade el AuthInterceptor para incluir automáticamente
        // el token de autenticación en las cabeceras de las llamadas a la API de la tienda.
        val authenticatedOkHttpClient = baseOkHttpClient.newBuilder()
            .addInterceptor(AuthInterceptor(context.applicationContext))
            .build()

        // Configuración de Retrofit para los endpoints de autenticación (login, signup).
        authRetrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.AUTH_BASE_URL) // URL base para la autenticación.
            .client(baseOkHttpClient) // Usa el cliente sin autenticación.
            .addConverterFactory(GsonConverterFactory.create()) // Convierte JSON a objetos Kotlin.
            .build()

        // Configuración de Retrofit para endpoints de autenticación que REQUIEREN token (me, perfil).
        authRetrofitAuthenticated = Retrofit.Builder()
            .baseUrl(BuildConfig.AUTH_BASE_URL) // Misma URL base de autenticación
            .client(authenticatedOkHttpClient) // Usa el cliente CON autenticación
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Configuración de Retrofit para los endpoints de la tienda (productos, pedidos, etc.).
        storeRetrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.STORE_BASE_URL) // URL base para la tienda.
            .client(authenticatedOkHttpClient) // Usa el cliente que añade el token.
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Creación "lazy" (perezosa) de los servicios de la API.
    // La instancia del servicio solo se crea la primera vez que se accede a ella.

    val authService: AuthService by lazy {
        authRetrofit.create(AuthService::class.java)
    }

    val productService: ProductService by lazy {
        storeRetrofit.create(ProductService::class.java)
    }

    val uploadService: UploadService by lazy {
        storeRetrofit.create(UploadService::class.java)
    }

    val orderService: OrderService by lazy {
        storeRetrofit.create(OrderService::class.java)
    }

    // Servicio para endpoints de usuario en la API de autenticación (/auth/me)
    val authUserService: UserService by lazy {
        authRetrofitAuthenticated.create(UserService::class.java)
    }

    // Servicio para endpoints de usuario en la API de comercio (/user, /user/{id})
    val storeUserService: UserService by lazy {
        storeRetrofit.create(UserService::class.java)
    }

    // Servicio de usuario legacy (deprecated, usar authUserService o storeUserService)
    @Deprecated("Use authUserService for /auth/me or storeUserService for /user endpoints")
    val userService: UserService by lazy {
        authRetrofitAuthenticated.create(UserService::class.java)
    }
}
