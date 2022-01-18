package com.amwms.food4u.fragments

import android.content.Context
import android.content.DialogInterface.OnMultiChoiceClickListener
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amwms.food4u.Food4UApplication
import com.amwms.food4u.databinding.FragmentSearchMenuBinding
import com.amwms.food4u.adapters.RestaurantMenuAdapter
import com.amwms.food4u.viewmodels.CoordinateViewModel
import com.amwms.food4u.viewmodels.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.StringBuilder

class SearchMenuFragment : Fragment() {

    private var maximumCaloriesBound: Int = Int.MAX_VALUE
    private var minimumCaloriesBound: Int = 0
    private var numberOfAllergens: Int = 0

    private lateinit var selectedAllergens: BooleanArray
    private var chosenAllergenIdList: ArrayList<Int> = ArrayList()
    private var chosenAllergenList: ArrayList<String> = ArrayList()
    private lateinit var allergenArray: Array<String>

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

        lifecycleScope.launch(Dispatchers.IO) {
            val _numberOfAllergens = menuViewModel.getNumberOfAllergens()
            Log.d("searchMenuFragment", _numberOfAllergens.toString())
            val _allergenArray = menuViewModel.allAllergensNames()

            this@SearchMenuFragment.activity?.runOnUiThread() {
                numberOfAllergens = _numberOfAllergens
                allergenArray = _allergenArray.toTypedArray()
                selectedAllergens = BooleanArray(numberOfAllergens) { false }
            }
        }

        binding.selectAllergensTextView.setOnClickListener(View.OnClickListener {
            createAllergenChoiceAlertDialog(view)
        })

        binding.searchMenuSubmitButton.setOnClickListener {
            handleEnterKeyEvent(view, KeyEvent.KEYCODE_ENTER)
            getConstraintDishes()
        }

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

    private fun createAllergenChoiceAlertDialog(view: View?) {
        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Select allergens you are allergic to")
            .setCancelable(false)
            .setMultiChoiceItems(allergenArray, selectedAllergens,
                OnMultiChoiceClickListener { _, i: Int, b: Boolean ->
                    if (b) { // is checkbox selected
                        chosenAllergenList.add(allergenArray[i])
                        chosenAllergenIdList.add(i)
                        chosenAllergenIdList.sort()
                    } else {
                        chosenAllergenIdList.remove(i)
                    }
                } as OnMultiChoiceClickListener)
            .setPositiveButton("OK") { _, _ ->
                // create string to be displayed in the selectAllergensTextView
                val stringBuilder = StringBuilder()
                for (i in 0 until chosenAllergenIdList.size) {
                    stringBuilder.append(allergenArray.get(chosenAllergenIdList.get(i)))

                    if (i != chosenAllergenIdList.size - 1) { // adding comma between allergens
                        stringBuilder.append(", ")
                    }
                }

                binding.selectAllergensTextView.text = stringBuilder.toString()
            }
            .setNegativeButton("Cancel") { dialogInterface, _ -> dialogInterface.dismiss() }
            .setNeutralButton("Clear All") { _, _ ->
                // clear all lists
                for (i in 0 until numberOfAllergens) {
                    selectedAllergens[i] = false
                    chosenAllergenIdList.clear()
                    binding.selectAllergensTextView.text = ""
                }
            }
            .show()
    }

    private fun getMaximumCaloriesEditTextInput() {
        if (binding.maximumCaloriesEditText.text.toString() != "")
            maximumCaloriesBound = binding.maximumCaloriesEditText.text.toString().toInt()
        else
            maximumCaloriesBound = Int.MAX_VALUE
    }

    private fun getMinimumCaloriesEditTextInput() {
        if (binding.minimumCaloriesEditText.text.toString() != "")
            minimumCaloriesBound = binding.minimumCaloriesEditText.text.toString().toInt()
        else
            minimumCaloriesBound = 0
    }

    private fun getConstraintDishes() {
        if (noCountryOrRestaurant()) {
            toastNoCountryOrRestaurant()
            return
        }

        getMaximumCaloriesEditTextInput()
        getMinimumCaloriesEditTextInput()

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
            menuViewModel.allDishesWithConstraints(chosenAllergenList.toList(),
                maximumCaloriesBound,
                minimumCaloriesBound,
                sharedViewModel.countryId.value!!,
                sharedViewModel.restaurantId.value!!).collect() {
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
        return sharedViewModel.countryNotSet() || sharedViewModel.restaurantNotSet()
    }

    private fun toastNoCountryOrRestaurant() {
        val toast: Toast

        if (sharedViewModel.countryNotSet() && sharedViewModel.restaurantNotSet())
            toast = Toast.makeText(activity, "Set country and restaurant", Toast.LENGTH_SHORT)
        else if (sharedViewModel.countryNotSet())
            toast = Toast.makeText(activity, "Set country", Toast.LENGTH_SHORT)
        else
            toast = Toast.makeText(activity, "Set restaurant", Toast.LENGTH_SHORT)

        toast.setGravity(Gravity.CENTER, 0 , 0)
        toast.show()
    }

}