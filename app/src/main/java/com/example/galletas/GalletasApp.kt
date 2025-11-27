package com.example.galletas

import android.app.Application
import com.example.galletas.api.RetrofitClient

/**
 * Clase Application personalizada para la inicialización de componentes globales.
 */
class GalletasApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Inicializar el cliente Retrofit una sola vez para toda la aplicación.
        RetrofitClient.init(this)
    }
}
