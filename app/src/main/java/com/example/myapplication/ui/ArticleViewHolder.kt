/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.myapplication.ui

import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.room.models.CitiesModel
import com.example.myapplication.databinding.CardCitiesBinding

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
        }
    }
}
