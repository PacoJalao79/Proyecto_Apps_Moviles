package com.example.galletas.ui.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.galletas.api.RetrofitClient
import com.example.galletas.data.model.CreateProductRequest
import com.example.galletas.databinding.FragmentAddProductBinding
import com.example.galletas.ui.adapter.ImagePreviewAdapter
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * Fragmento para añadir un nuevo producto a la tienda.
 * Contiene un formulario para el nombre, descripción, precio y permite seleccionar imágenes.
 */
class AddProductFragment : Fragment() {

    // View Binding para acceder a las vistas del layout de forma segura.
    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!

    // Lista mutable para almacenar las URIs (direcciones locales) de las imágenes seleccionadas.
    private val selectedImageUris = mutableListOf<Uri>()
    // Adapter para el RecyclerView que mostrará la previsualización de las imágenes.
    private lateinit var imagePreviewAdapter: ImagePreviewAdapter

    // Instancias de los servicios de la API para productos y subida de archivos.
    private val productService by lazy { RetrofitClient.productService }
    private val uploadService by lazy { RetrofitClient.uploadService }

    // Contrato de resultado de actividad para seleccionar múltiples imágenes de la galería.
    // Cuando el usuario selecciona las imágenes, esta lambda se ejecuta.
    private val pickImages = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            selectedImageUris.clear() // Limpia la selección anterior.
            selectedImageUris.addAll(uris) // Añade las nuevas imágenes seleccionadas.
            imagePreviewAdapter.notifyDataSetChanged() // Notifica al adapter que los datos han cambiado.
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupImagePreview()

        // Configura el listener para el botón de seleccionar imágenes.
        binding.selectImagesButton.setOnClickListener {
            pickImages.launch("image/*") // Lanza el selector de archivos del sistema para imágenes.
        }

        // Configura el listener para el botón de guardar.
        binding.saveButton.setOnClickListener {
            saveProduct()
        }
    }

    /**
     * Configura el RecyclerView para la previsualización de imágenes.
     */
    private fun setupImagePreview() {
        imagePreviewAdapter = ImagePreviewAdapter(selectedImageUris)
        binding.imagePreviewRecyclerView.adapter = imagePreviewAdapter
    }

    /**
     * Guarda el nuevo producto. Implica validar los datos, subir las imágenes y luego crear el producto.
     */
    private fun saveProduct() {
        // Obtiene los datos del formulario.
        val name = binding.nameEditText.text.toString().trim()
        val description = binding.descriptionEditText.text.toString().trim()
        val priceString = binding.priceEditText.text.toString().trim()

        // Valida que todos los campos estén completos.
        if (name.isEmpty() || description.isEmpty() || priceString.isEmpty() || selectedImageUris.isEmpty()) {
            Toast.makeText(requireContext(), "Por favor, complete todos los campos y seleccione al menos una imagen", Toast.LENGTH_LONG).show()
            return
        }

        // Valida que el precio sea un número válido.
        val price = priceString.toDoubleOrNull()
        if (price == null) {
            Toast.makeText(requireContext(), "El precio no es válido", Toast.LENGTH_SHORT).show()
            return
        }

        showLoading(true)

        // Lanza una coroutina para las operaciones de red.
        lifecycleScope.launch {
            try {
                // 1. Subir TODAS las imágenes seleccionadas a la API
                val uploadedImages = mutableListOf<com.example.galletas.data.model.ProductImage>()

                for (imageUri in selectedImageUris) {
                    val imagePart = uriToMultipartBody(imageUri)
                    val uploadedImage = uploadService.uploadImage(imagePart)
                    uploadedImages.add(uploadedImage)
                }

                // 2. Crear el objeto de la petición para crear el producto con TODAS las imágenes.
                val createProductRequest = CreateProductRequest(
                    name = name,
                    description = description,
                    price = price,
                    stock = 1, // Valor de ejemplo
                    brand = "Marca Ejemplo", // Valor de ejemplo
                    category = "Categoría Ejemplo", // Valor de ejemplo
                    image = uploadedImages // Usa TODAS las imágenes subidas
                )

                // Llama a la API para crear el producto.
                productService.createProduct(createProductRequest)

                showLoading(false)
                Toast.makeText(requireContext(), "Producto creado con éxito con ${uploadedImages.size} imagen(es)", Toast.LENGTH_SHORT).show()
                clearForm() // Limpia el formulario para el siguiente producto.

            } catch (e: Exception) {
                showLoading(false)
                Toast.makeText(requireContext(), "Error al crear el producto: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Limpia todos los campos del formulario después de una creación exitosa.
     */
    private fun clearForm() {
        binding.nameEditText.text?.clear()
        binding.descriptionEditText.text?.clear()
        binding.priceEditText.text?.clear()
        selectedImageUris.clear()
        imagePreviewAdapter.notifyDataSetChanged()
        binding.nameEditText.requestFocus() // Devuelve el foco al primer campo.
    }

    /**
     * Convierte la URI de una imagen local en una parte de un cuerpo de petición multipart (MultipartBody.Part).
     * Esto es necesario para subir archivos a través de Retrofit.
     * @param uri La URI del archivo de imagen.
     * @return Un objeto MultipartBody.Part listo para ser enviado.
     */
    private fun uriToMultipartBody(uri: Uri): MultipartBody.Part {
        val contentResolver = requireContext().contentResolver
        val inputStream = contentResolver.openInputStream(uri)!!
        val requestBody = inputStream.readBytes().toRequestBody(
            contentResolver.getType(uri)?.toMediaTypeOrNull()
        )
        // El nombre "content" es el que espera la API de Xano por defecto para el archivo.
        return MultipartBody.Part.createFormData("content", "image.jpg", requestBody)
    }

    /**
     * Controla la visibilidad de la barra de progreso.
     */
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.saveButton.isEnabled = !isLoading
    }

    /**
     * Limpia la referencia al binding para evitar fugas de memoria.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
