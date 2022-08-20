package com.example.myapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenResumed
import androidx.navigation.fragment.navArgs
import com.example.myapplication.R
import com.example.myapplication.databinding.CitiesDetailFragmentBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CitiesDetailFragment: Fragment(), OnMapReadyCallback {

    private var _binding: CitiesDetailFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CitiesDetailVM by viewModels()

    private val args: CitiesDetailFragmentArgs by navArgs()

    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = CitiesDetailFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.requestOpenWeatherApi(args.id)

        viewModel.getWeatherResponse().onEach { data ->
            whenResumed {
                binding.tvDescription.text = getString(R.string.description, data?.weather?.getOrNull(0)?.description)
                binding.tvAirTemperature.text = getString(R.string.air_temperature, data?.main?.temp.toString())
                binding.tvMinTemperature.text = getString(R.string.min_air_temperature, data?.main?.temp_min.toString())
                binding.tvMaxTemperature.text = getString(R.string.max_air_temperature, data?.main?.temp_max.toString())
                binding.tvHumidity.text = getString(R.string.humidity, data?.main?.humidity.toString())
                binding.tvWindSpeed.text = getString(R.string.wind_speed, data?.wind?.speed.toString())
            }
        }.launchIn(lifecycleScope)

        val mapFragment = binding.mvCitie.getFragment<SupportMapFragment>()
        mapFragment.getMapAsync { googleMap ->
            // Ensure all places are visible in the map.
            googleMap.setOnMapLoadedCallback {
                val currentCitie = LatLng(args.lat.toDouble(), args.lon.toDouble())
                val zoomLevel = 14.0f
                googleMap.addMarker(
                    MarkerOptions()
                        .position(currentCitie)
                        .title("Citie ${args.name}")
                )
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentCitie, zoomLevel))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val currentCitie = LatLng(args.lat.toDouble(), args.lon.toDouble())
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentCitie))
    }
}