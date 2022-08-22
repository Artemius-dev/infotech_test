package com.example.myapplication.ui.presentation.cities_main

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.example.myapplication.R
import com.example.myapplication.data.room.models.CitiesModel
import com.example.myapplication.databinding.MainFragmentBinding
import com.example.myapplication.ui.callbacks.DialogListener
import com.example.myapplication.ui.callbacks.NavigationCallback
import com.example.myapplication.ui.hideKeyboard
import com.example.myapplication.ui.presentation.filter.FilterDialog
import com.example.myapplication.ui.recyclers.cites.CitiesAdapter
import com.example.myapplication.ui.setImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainFragment : Fragment(), NavigationCallback, DialogListener {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainVM by activityViewModels()

    private lateinit var navController: NavController

    private val citiesAdapter = CitiesAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getRetryState().onEach {
            whenResumed {
                submitData(viewModel.items)
            }
        }.launchIn(lifecycleScope)
    }

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
        binding.rvCities.adapter = citiesAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            whenResumed {
                viewModel.items.collectLatest { items ->
                    citiesAdapter.submitData(items)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                citiesAdapter.loadStateFlow.collect {
                    binding.lpiPrependProgress.isVisible = it.source.prepend is LoadState.Loading
                    binding.lpiAppendProgress.isVisible = it.source.append is LoadState.Loading
                }
            }
        }

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.search_menu, menu)
                val searchView = menu.findItem(R.id.menu_search).actionView as SearchView
                searchView.isSubmitButtonEnabled = true
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if (query != null) {
                            searchDatabase(query)
                        } else {
                            viewModel.showAllCities()
                            submitData(items)
                        }
                        hideKeyboard()
                        return true
                    }

                    override fun onQueryTextChange(query: String?): Boolean {
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.menu_filter -> {
                        FilterDialog.newInstance(viewModel, this@MainFragment)
                            .show(childFragmentManager, "Filter Dialog")
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onResume() {
        super.onResume()
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

    fun submitData(items: Flow<PagingData<CitiesModel>>) {
        viewLifecycleOwner.lifecycleScope.launch() {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                items.collectLatest {
                    citiesAdapter.submitData(it)
                }
            }
        }
    }

    override fun onFinishDialog() {
        submitData(viewModel.items)
    }

    override fun setCitieImage(citieId: Int, imageView: ImageView) {
        imageView.setImage(getString(viewModel.setCitieImage(citieId)))
    }

    private fun searchDatabase(query: String) {
        viewModel.searchCities("%$query%")
        submitData(viewModel.items)
    }
}