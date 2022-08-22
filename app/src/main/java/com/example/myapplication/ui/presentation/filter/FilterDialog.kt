package com.example.myapplication.ui.presentation.filter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenResumed
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.example.myapplication.data.room.models.CitiesModel
import com.example.myapplication.databinding.FilterDialogBinding
import com.example.myapplication.ui.callbacks.DialogListener
import com.example.myapplication.ui.callbacks.NavigationCallback
import com.example.myapplication.ui.presentation.cities_main.MainVM
import com.example.myapplication.ui.recyclers.countries.CountryAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FilterDialog(private val viewModel: MainVM, val listener: DialogListener): DialogFragment(),
    NavigationCallback {
    companion object {

        private const val doYouEnjoyCallbackKey = "callback"

        fun newInstance(
            viewModel: MainVM,
            listener: DialogListener
        ): FilterDialog {
            val args = Bundle()
            val dialog = FilterDialog(viewModel, listener)
            dialog.arguments = args
            return dialog
        }
    }

    private val countryAdapter = CountryAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding =
            FilterDialogBinding.inflate(inflater, container, false)

        dialog?.setOnDismissListener {
            dismiss()
        }

        binding.ivExit.setOnClickListener {
            dismiss()
        }

        countryAdapter.addLoadStateListener { state: CombinedLoadStates ->
            val refreshState = state.refresh
            binding.rvCountryFilter.isVisible = refreshState != LoadState.Loading
            binding.pbFiltersLoading.isVisible = refreshState == LoadState.Loading
        }

        binding.etFilterSearch.addTextChangedListener(
            onTextChanged = { text: CharSequence?, _: Int, _: Int, _: Int ->
                viewModel.getContryByName("%${text.toString()}%")
                viewLifecycleOwner.lifecycleScope.launch {
                    whenResumed {
                        viewModel.filterItems.collectLatest { items ->
                            countryAdapter.submitData(items)
                        }
                    }
                }
            }
        )
        viewModel.getAllCountryNames()
        binding.rvCountryFilter.adapter = countryAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            whenResumed {
                viewModel.filterItems.collectLatest { items ->
                    countryAdapter.submitData(items)
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDialogWidthFillParent()
    }

    private fun setDialogWidthFillParent() {
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun navigateToCitiesDetails(citiesModel: CitiesModel) {
        viewModel.getCitiesByCountry(citiesModel.country)
        listener.onFinishDialog()
        dismiss()
    }

    override fun setCitieImage(citieId: Int, imageView: ImageView) {
    }

}