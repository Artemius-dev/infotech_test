package com.example.myapplication.ui.recyclers.countries

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.myapplication.data.room.models.CitiesModel
import com.example.myapplication.databinding.FilterCardBinding
import com.example.myapplication.ui.callbacks.NavigationCallback

class CountryAdapter(private val callback: NavigationCallback) : PagingDataAdapter<CitiesModel, CountryViewHolder>(
    CITIES_DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder =
        CountryViewHolder(
            callback,
            FilterCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        )

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val tile = getItem(position)
        if (tile != null) {
            holder.bind(tile)
        }
    }

    companion object {
        private val CITIES_DIFF_CALLBACK = object : DiffUtil.ItemCallback<CitiesModel>() {
            override fun areItemsTheSame(oldItem: CitiesModel, newItem: CitiesModel): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: CitiesModel, newItem: CitiesModel): Boolean =
                oldItem == newItem
        }
    }
}