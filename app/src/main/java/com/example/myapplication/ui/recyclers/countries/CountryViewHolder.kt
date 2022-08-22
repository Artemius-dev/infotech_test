package com.example.myapplication.ui.recyclers.countries

import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.room.models.CitiesModel
import com.example.myapplication.databinding.FilterCardBinding
import com.example.myapplication.ui.callbacks.NavigationCallback

class CountryViewHolder(
    private val callback: NavigationCallback,
    private val binding: FilterCardBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(citie: CitiesModel) {
        binding.apply {
            binding.tvCountryName.text = citie.country
            binding.root.setOnClickListener {
                callback.navigateToCitiesDetails(citie)
            }
        }
    }
}
