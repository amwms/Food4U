package com.amwms.food4u.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amwms.food4u.Food4UApplication
import com.amwms.food4u.adapters.RestaurantMenuAdapter
import com.amwms.food4u.database.entities.Dish
import com.amwms.food4u.database.entities.DishSet
import com.amwms.food4u.database.entities.FavoriteSet
import com.amwms.food4u.databinding.FragmentSaveSetBinding
import com.amwms.food4u.viewmodels.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SaveSetFragment : Fragment() {

    private var _binding: FragmentSaveSetBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var dishList: List<Dish>
    private lateinit var calorieCount: String


    private val sharedViewModel: CoordinateViewModel by activityViewModels()

    private val createSetViewModel: CreateSetViewModel by activityViewModels {
        CreateSetViewModelFactory(
            (activity?.application as Food4UApplication).database.food4uDao()
        )
    }

    private val menuViewModel: MenuViewModel by activityViewModels {
        MenuViewModelFactory(
            (activity?.application as Food4UApplication).database.food4uDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dishList = createSetViewModel.dishSetList.value!!
        calorieCount = createSetViewModel.caloriesInSet.value.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSaveSetBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setHeader.text = "Set - $calorieCount kcal"

        binding.saveSetButton.setOnClickListener {
            handleEnterKeyEvent(view, KeyEvent.KEYCODE_ENTER)
            saveSet()
            navigateToFavorites()
        }

        binding.setNameEditText.setOnKeyListener { view, keyCode, _ ->
            handleEnterKeyEvent(view, keyCode)
        }

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val dishSetAdapter = RestaurantMenuAdapter({})
        recyclerView.adapter = dishSetAdapter

        GlobalScope.launch(Dispatchers.IO) {
            dishSetAdapter.submitList(dishList)
        }
    }

    @SuppressLint("HardwareIds")
    private fun saveSet() {
        if (noSetNameGiven()) {
            toastNoSetNameGiven()
            return
        }

        val userId = Settings.Secure.getString(getContext()?.getContentResolver(), Settings.Secure.ANDROID_ID)
        val setName = binding.setNameEditText.text.toString()

        lifecycleScope.launch(Dispatchers.IO) {
            val newSetId = createSetViewModel.numberOfSets() + 1
            Log.d("saveSet", newSetId.toString())
            val newSet = FavoriteSet(newSetId, userId, setName, calorieCount.toInt())

            createSetViewModel.addNewSet(newSet)

            for (i in 0 until dishList.size) {
                addEntry(newSetId, dishList[i].id)
//                createSetViewModel.addNewDishToSet(DishSet(newSetId, dishList[i].id))
            }

            this@SaveSetFragment.activity?.runOnUiThread() {
               toastSetAdded()
            }
        }
    }

    private fun addEntry(newSetId: Int, dishId: Int) {
        if (createSetViewModel.dishExists(dishId) > 0)
            createSetViewModel.addNewDishToSet(DishSet(newSetId, dishId))
    }

    private fun noSetNameGiven(): Boolean {
        return binding.setNameEditText.text.toString() == ""
    }

    private fun toastNoSetNameGiven() {
        val toast = Toast.makeText(activity, "Name your set", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0 , 0)
        toast.show()
    }

    private fun toastSetAdded() {
        val toast = Toast.makeText(activity, "Set added", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0 , 0)
        toast.show()
    }

    private fun navigateToFavorites() {
        val action = SaveSetFragmentDirections.actionSaveSetFragmentToFavoritesFragment()
        view?.findNavController()?.navigate(action)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}