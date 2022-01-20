package com.amwms.food4u.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.amwms.food4u.R
import com.amwms.food4u.databinding.FragmentHomeBinding
import com.amwms.food4u.viewmodels.CoordinateViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: CoordinateViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.changeRestaurantButton.setOnClickListener{ gotoCountryChoice() }

        binding.chosenCountryTextView.text =
            getCountryLabelText(sharedViewModel.countryName.value.toString())

        binding.chosenRestaurantTextView.text =
            getRestaurantLabelText(sharedViewModel.restaurantName.value.toString())
    }


    private fun gotoCountryChoice() {
        findNavController().navigate(R.id.action_homeFragment_to_countryFragment)
    }

    private fun getCountryLabelText(countryName: String): String {
        return CountryFragment.LABEL_TEXT + countryName
    }

    private fun getRestaurantLabelText(restaurantName: String): String {
        return RestaurantFragment.LABEL_TEXT + restaurantName
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}