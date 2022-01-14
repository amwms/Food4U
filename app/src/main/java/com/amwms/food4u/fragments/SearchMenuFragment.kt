package com.amwms.food4u.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.amwms.food4u.databinding.FragmentSearchMenuBinding
import com.amwms.food4u.adapters.RestaurantMenuAdapter
import com.amwms.food4u.viewmodels.CoordinateViewModel
import com.amwms.food4u.viewmodels.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SearchMenuFragment : Fragment() {

    private var maximumCaloriesBound: Int = Int.MAX_VALUE
    private var minimumCaloriesBound: Int = 0

    private var _binding: FragmentSearchMenuBinding? = null
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
        _binding = FragmentSearchMenuBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchMenuSubmitButton.setOnClickListener { getConstraintDishes() }

        binding.maximumCaloriesEditText.setOnKeyListener { view, keyCode, _ ->
            handleEnterKeyEvent(view, keyCode)
        }
        binding.minimumCaloriesEditText.setOnKeyListener { view, keyCode, _ ->
            handleEnterKeyEvent(view, keyCode)
        }

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val searchMenuAdapter = RestaurantMenuAdapter({})
        recyclerView.adapter = searchMenuAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getMaximumCaloriesEditTextInput() {
        if (binding.maximumCaloriesEditText.text.toString() != "")
            maximumCaloriesBound = binding.maximumCaloriesEditText.text.toString().toInt()
    }

    private fun getMinimumumCaloriesEditTextInput() {
        if (binding.minimumCaloriesEditText.text.toString() != "")
            minimumCaloriesBound = binding.minimumCaloriesEditText.text.toString().toInt()
    }

    private fun getConstraintDishes() {
        if (noCountryOrRestaurant()) {
            toastNoCountryOrRestaurant()
            return
        }

        getMaximumCaloriesEditTextInput()
        getMinimumumCaloriesEditTextInput()

        val searchMenuAdapter = RestaurantMenuAdapter({
            val action = SearchMenuFragmentDirections
                .actionSearchMenuFragmentToCaloriesFragment(
                    dishId = it.id.toString(),
                    dishName = it.name.toString()
                )
            view?.findNavController()?.navigate(action)
        })
        recyclerView.adapter = searchMenuAdapter

        lifecycle.coroutineScope.launch {
            menuViewModel.allDishesWithConstraints(maximumCaloriesBound, minimumCaloriesBound,
                1, 2).collect() {
                searchMenuAdapter.submitList(it)
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