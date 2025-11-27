package com.example.galletas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.galletas.api.RetrofitClient
import com.example.galletas.databinding.ActivityHomeBinding
import com.example.galletas.util.UserManager

/**
 * Actividad que alberga los fragmentos principales de la aplicación después del inicio de sesión.
 * Configura la barra de navegación inferior (Bottom Navigation) para moverse entre las diferentes
 * secciones de la tienda: Productos, Añadir, Carrito y Perfil.
 *
 * La navegación y el menú se adaptan automáticamente según el rol del usuario:
 * - Administrador: [Productos, Añadir, Usuarios, Perfil]
 * - Usuario Normal: [Productos, Carrito, Perfil]
 */
class HomeActivity : AppCompatActivity() {

    // Referencia al View Binding para acceder a las vistas del layout.
    private lateinit var binding: ActivityHomeBinding
    // Gestor para verificar el rol del usuario.
    private lateinit var userManager: UserManager

    /**
     * Método que se llama cuando la actividad es creada.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializa el cliente de Retrofit con el contexto de la aplicación.
        // Esto es crucial para que las llamadas a la API funcionen correctamente.
        RetrofitClient.init(applicationContext)

        // Inicializa el gestor de usuario para verificar el rol.
        userManager = UserManager(this)

        // Infla el layout y lo asigna a la actividad.
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura la navegación según el rol del usuario.
        setupNavigationBasedOnRole()
    }

    /**
     * Configura el NavController y el menú de navegación según el rol del usuario.
     */
    private fun setupNavigationBasedOnRole() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Log para debugging
        android.util.Log.d("HomeActivity", "=== CONFIGURANDO NAVEGACIÓN ===")
        android.util.Log.d("HomeActivity", "Rol del usuario: ${userManager.getUserRole()}")
        android.util.Log.d("HomeActivity", "¿Es admin?: ${userManager.isAdmin()}")
        android.util.Log.d("HomeActivity", "Email del usuario: ${userManager.getUserEmail()}")

        // Determina qué grafo de navegación y menú usar según el rol.
        if (userManager.isAdmin()) {
            // Usuario es administrador: carga el grafo y menú de admin.
            android.util.Log.d("HomeActivity", "✅ Cargando menú de ADMINISTRADOR")
            navController.setGraph(R.navigation.nav_graph_admin)
            binding.bottomNavigation.menu.clear()
            binding.bottomNavigation.inflateMenu(R.menu.bottom_nav_menu_admin)
        } else {
            // Usuario normal o invitado: carga el grafo y menú de usuario.
            android.util.Log.d("HomeActivity", "✅ Cargando menú de USUARIO NORMAL")
            navController.setGraph(R.navigation.nav_graph_user)
            binding.bottomNavigation.menu.clear()
            binding.bottomNavigation.inflateMenu(R.menu.bottom_nav_menu_user)
        }

        // Conecta el BottomNavigationView con el NavController.
        binding.bottomNavigation.setupWithNavController(navController)
    }
}
