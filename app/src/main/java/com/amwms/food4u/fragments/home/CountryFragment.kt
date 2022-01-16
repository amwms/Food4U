package com.amwms.food4u.fragments.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amwms.food4u.Food4UApplication
import com.amwms.food4u.R
import com.amwms.food4u.adapters.CountryAdapter
import com.amwms.food4u.database.entities.Country
import com.amwms.food4u.databinding.FragmentCountryBinding
import com.amwms.food4u.viewmodels.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class CountryFragment : Fragment() {

//    private var binding: FragmentCountryBinding? = null
//    private val sharedViewModel: CoordinateViewModel by activityViewModels()
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//        val fragmentBinding = FragmentCountryBinding.inflate(inflater, container, false)
//        binding = fragmentBinding
//        return fragmentBinding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding?.apply {
//            lifecycleOwner = viewLifecycleOwner
//            viewModel = sharedViewModel
//            countryFragment = this@CountryFragment
//        }
//    }
    companion object {
        var LABEL_TEXT = "Chosen country:  "
    }

    private var _binding: FragmentCountryBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private val sharedViewModel: CoordinateViewModel by activityViewModels()

    private val countryRestaurantViewModel: CountryRestaurantViewModel by activityViewModels {
        CountryRestaurantViewModelFactory(
            (activity?.application as Food4UApplication).database.food4uDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCountryBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.submitCountryButton.setOnClickListener{ submitCountryChoice() }
        binding.cancelCountryButton.setOnClickListener{ cancelCountryChoice() }

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val countryAdapter = CountryAdapter { chooseCountry(it) }
        recyclerView.adapter = countryAdapter

        lifecycle.coroutineScope.launch {
            countryRestaurantViewModel.allCountries().collect() {
                countryAdapter.submitList(it)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getLabelText(countryName: String): String {
        return LABEL_TEXT + countryName
    }

    private fun chooseCountry(country: Country) {
        countryRestaurantViewModel.setTempCountry(country)
        binding.chooseCountryHeader.text =
            getLabelText(countryRestaurantViewModel.tempCountryName.value.toString())
    }

    private fun submitCountryChoice() {
        sharedViewModel.setCountry(countryRestaurantViewModel.tempCountryId.value,
                countryRestaurantViewModel.tempCountryName.value.toString())

        Log.d("set country: ", sharedViewModel.countryId.value.toString())
        findNavController().navigate(R.id.action_countryFragment_to_restaurantFragment)

        Toast.makeText(activity, "Changed country", Toast.LENGTH_SHORT).show()
    }

    private fun cancelCountryChoice() {
        findNavController().navigate(R.id.action_countryFragment_to_homeFragment)
    }
}