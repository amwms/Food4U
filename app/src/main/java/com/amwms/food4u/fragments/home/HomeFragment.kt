package com.amwms.food4u.fragments.home

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.amwms.food4u.R
import com.amwms.food4u.databinding.FragmentHomeBinding
import com.amwms.food4u.model.CoordinateViewModel

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private val sharedViewModel: CoordinateViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fragmentBinding = FragmentHomeBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.homeFragment = this
    }


    fun gotoCountryChoice() {
        findNavController().navigate(R.id.action_homeFragment_to_countryFragment)
    }

    private fun noCountryOrRestaurant() {
        val toast: Toast

        if (sharedViewModel.country.equals("") && sharedViewModel.restaurant.equals(""))
            toast = Toast.makeText(activity, "Set country and restaurant", Toast.LENGTH_SHORT)
        else if (sharedViewModel.country.equals(""))
            toast = Toast.makeText(activity, "Set country", Toast.LENGTH_SHORT)
        else
            toast = Toast.makeText(activity, "Set restaurant", Toast.LENGTH_SHORT)

        toast.setGravity(Gravity.CENTER, 0 , 0)
        toast.show()
    }

}