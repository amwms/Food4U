package com.amwms.food4u.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amwms.food4u.Food4UApplication
import com.amwms.food4u.adapters.RestaurantMenuAdapter
import com.amwms.food4u.databinding.FragmentRestaurantMenuBinding
import com.amwms.food4u.viewmodels.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RestaurantMenuFragment : Fragment() {

    private var _binding: FragmentRestaurantMenuBinding? = null

    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    private val sharedViewModel: CoordinateViewModel by activityViewModels()

    private val viewModel: MenuViewModel by activityViewModels {
        MenuViewModelFactory(
            (activity?.application as Food4UApplication).database.food4uDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRestaurantMenuBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dishNameSubmitButton.setOnClickListener { getDishNameEditTextInput() }

        binding.dishNameFieldEditText.setOnKeyListener { view, keyCode, _ ->
            handleKeyEvent(
                view,
                keyCode
            )
        }

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val restaurantMenuAdapter = RestaurantMenuAdapter({})

        recyclerView.adapter = restaurantMenuAdapter
//        lifecycle.coroutineScope.launch {
//            viewModel.allDishes().collect() {
//                restaurantMenuAdapter.submitList(it)
//            }
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getDishNameEditTextInput() {
        val stringInEditTextField = binding.dishNameFieldEditText.text.toString();

        Log.d("DishNameEditTextInput", stringInEditTextField)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val restaurantMenuAdapter = RestaurantMenuAdapter({})

        recyclerView.adapter = restaurantMenuAdapter
        lifecycle.coroutineScope.launch {
            viewModel.allDishesContainingString(stringInEditTextField).collect() {
                restaurantMenuAdapter.submitList(it)
            }
        }
    }

    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide the keyboard
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }
}