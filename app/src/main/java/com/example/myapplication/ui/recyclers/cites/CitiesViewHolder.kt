package com.example.myapplication.ui.recyclers.cites

import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.room.models.CitiesModel
import com.example.myapplication.databinding.CardCitiesBinding
import com.example.myapplication.ui.callbacks.NavigationCallback

class CitiesViewHolder(
    private val callback: NavigationCallback,
    private val binding: CardCitiesBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(citie: CitiesModel) {
        binding.apply {
            binding.tvCitiesName.text = citie.name
            binding.root.setOnClickListener {
                callback.navigateToCitiesDetails(citie)
            }
            callback.setCitieImage(citie.id,  binding.ivCities)
        }
    }
}
