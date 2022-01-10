package com.amwms.food4u.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
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

    private val menuViewModel: MenuViewModel by activityViewModels {
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
            handleEnterKeyEvent(view, keyCode)
        }

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val restaurantMenuAdapter = RestaurantMenuAdapter({})
        recyclerView.adapter = restaurantMenuAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getDishNameEditTextInput() {
        if (noCountryOrRestaurant()) {
            toastNoCountryOrRestaurant()
            return
        }

        val stringInEditTextField = binding.dishNameFieldEditText.text.toString()
        val restaurantMenuAdapter = RestaurantMenuAdapter({
            val action = RestaurantMenuFragmentDirections
                .actionRestaurantMenuFragmentToCaloriesFragment(
                      dishId = it.id.toString()
                        )
            view?.findNavController()?.navigate(action)
        })
        recyclerView.adapter = restaurantMenuAdapter

        lifecycle.coroutineScope.launch {
            menuViewModel.allDishesContainingString(stringInEditTextField,
                1, 2).collect() {
                restaurantMenuAdapter.submitList(it)
            }
        }
    }

    private fun handleEnterKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide the keyboard
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }

    private fun noCountryOrRestaurant() : Boolean {
        return sharedViewModel.country.value.equals("") /*|| sharedViewModel.restaurant.equals("")*/
    }

    private fun toastNoCountryOrRestaurant() {
        val toast: Toast

        if (sharedViewModel.country.value.equals("") && sharedViewModel.restaurant.value.equals(""))
            toast = Toast.makeText(activity, "Set country and restaurant", Toast.LENGTH_SHORT)
        else if (sharedViewModel.country.value.equals(""))
            toast = Toast.makeText(activity, "Set country", Toast.LENGTH_SHORT)
        else
            toast = Toast.makeText(activity, "Set restaurant", Toast.LENGTH_SHORT)

        toast.setGravity(Gravity.CENTER, 0 , 0)
        toast.show()
    }
}