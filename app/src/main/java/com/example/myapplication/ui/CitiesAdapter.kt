package com.example.myapplication.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.myapplication.data.room.models.CitiesModel
import com.example.myapplication.databinding.CardCitiesBinding

class CitiesAdapter(private val callback: NavigationCallback) : PagingDataAdapter<CitiesModel, CitiesViewHolder>(CITIES_DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesViewHolder =
        CitiesViewHolder(
            callback,
            CardCitiesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        )

    override fun onBindViewHolder(holder: CitiesViewHolder, position: Int) {
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