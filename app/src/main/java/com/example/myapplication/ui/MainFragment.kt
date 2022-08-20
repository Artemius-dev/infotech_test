package com.example.myapplication.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.paging.LoadState
import com.example.myapplication.R
import com.example.myapplication.data.room.models.CitiesModel
import com.example.myapplication.databinding.MainFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import java.io.IOException

@AndroidEntryPoint
class MainFragment : Fragment(), NavigationCallback {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainVM by activityViewModels()

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val items = viewModel.items
        val citiesAdapter = CitiesAdapter(this)

        binding.rvCities.adapter = citiesAdapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                citiesAdapter.loadStateFlow.collect {
                    binding.lpiPrependProgress.isVisible = it.source.prepend is LoadState.Loading
                    binding.lpiAppendProgress.isVisible = it.source.append is LoadState.Loading
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch() {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                items.collectLatest {
                    citiesAdapter.submitData(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun navigateToCitiesDetails(citiesModel: CitiesModel) {
        navController.navigate(
            MainFragmentDirections.actionMainFragmentToCitiesDetailFragment(
                citiesModel.id,
                citiesModel.name,
                citiesModel.coord.lat.toString(),
                citiesModel.coord.lon.toString()
            )
        )
    }

}