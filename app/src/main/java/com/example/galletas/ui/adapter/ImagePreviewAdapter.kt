package com.example.galletas.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.galletas.databinding.ItemImagePreviewBinding

/**
 * Adapter para el RecyclerView que muestra una previsualización horizontal de las imágenes
 * que el usuario ha seleccionado para un nuevo producto.
 *
 * @param imageUris La lista de URIs de las imágenes a mostrar.
 */
class ImagePreviewAdapter(private val imageUris: List<Uri>) : RecyclerView.Adapter<ImagePreviewAdapter.PreviewViewHolder>() {

    /**
     * ViewHolder que representa una única vista de previsualización de imagen.
     */
    inner class PreviewViewHolder(val binding: ItemImagePreviewBinding) : RecyclerView.ViewHolder(binding.root)

    /**
     * Crea un nuevo ViewHolder inflando el layout de previsualización.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewViewHolder {
        val binding = ItemImagePreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PreviewViewHolder(binding)
    }

    /**
     * Vincula la URI de una imagen con el ImageView del ViewHolder, cargándola con Coil.
     */
    override fun onBindViewHolder(holder: PreviewViewHolder, position: Int) {
        holder.binding.imagePreview.load(imageUris[position])
    }

    /**
     * Devuelve el número total de imágenes a previsualizar.
     */
    override fun getItemCount() = imageUris.size
}
