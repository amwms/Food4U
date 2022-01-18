package com.amwms.food4u.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amwms.food4u.Food4UApplication
import com.amwms.food4u.adapters.RestaurantMenuAdapter
import com.amwms.food4u.database.entities.Dish
import com.amwms.food4u.databinding.FragmentSaveSetBinding
import com.amwms.food4u.viewmodels.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SaveSetFragment : Fragment() {

    private var _binding: FragmentSaveSetBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var dishInfo: List<Dish>
    private lateinit var calorieCount: String


    private val sharedViewModel: CoordinateViewModel by activityViewModels()

    private val createSetViewModel: CreateSetViewModel by activityViewModels()

    private val menuViewModel: MenuViewModel by activityViewModels {
        MenuViewModelFactory(
            (activity?.application as Food4UApplication).database.food4uDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dishInfo = createSetViewModel.dishSetList.value!!
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

        binding.saveSetButton.setOnClickListener { saveSet() }

        binding.setHeader.text = "Set - $calorieCount kcal"

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val dishSetAdapter = RestaurantMenuAdapter({})
        recyclerView.adapter = dishSetAdapter

        GlobalScope.launch(Dispatchers.IO) {
            dishSetAdapter.submitList(dishInfo)
        }
    }

    private fun saveSet() {
        lifecycleScope.launch(Dispatchers.IO) {


            this@SaveSetFragment.activity?.runOnUiThread() {

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}