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
import com.amwms.food4u.adapters.RestaurantAdapter
import com.amwms.food4u.database.entities.Restaurant
import com.amwms.food4u.databinding.FragmentRestaurantBinding
import com.amwms.food4u.viewmodels.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class RestaurantFragment : Fragment() {
    companion object {
        var LABEL_TEXT = "Chosen restaurant:  "
    }

    private var _binding: FragmentRestaurantBinding? = null
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
        _binding = FragmentRestaurantBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.submitRestaurantButton.setOnClickListener{ submitRestaurantChoice() }
        binding.cancelCountryButton.setOnClickListener{ cancelRestaurantChoice() }

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val restaurantAdapter = RestaurantAdapter { chooseRestaurant(it) }
        recyclerView.adapter = restaurantAdapter

        lifecycle.coroutineScope.launch {
            countryRestaurantViewModel.allRestaurants().collect() {
                restaurantAdapter.submitList(it)
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

    private fun chooseRestaurant(restaurant: Restaurant) {
        countryRestaurantViewModel.setTempRestaurant(restaurant)
        binding.chooseRestaurantHeader.text =
            getLabelText(countryRestaurantViewModel.tempRestaurantName.value.toString())
    }

    private fun submitRestaurantChoice() {
        sharedViewModel.setRestaurant(countryRestaurantViewModel.tempRestaurantId.value,
                countryRestaurantViewModel.tempRestaurantName.value.toString())

        Log.d("set restaurant: ", sharedViewModel.restaurantId.value.toString())
        findNavController().navigate(R.id.action_restaurantFragment_to_homeFragment)

        Toast.makeText(activity, "Changed restaurant", Toast.LENGTH_SHORT).show()
    }

    private fun cancelRestaurantChoice() {
        findNavController().navigate(R.id.action_restaurantFragment_to_homeFragment)
    }
}