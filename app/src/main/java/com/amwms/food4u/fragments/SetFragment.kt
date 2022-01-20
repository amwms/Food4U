package com.amwms.food4u.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amwms.food4u.Food4UApplication
import com.amwms.food4u.adapters.RestaurantMenuAdapter
import com.amwms.food4u.databinding.FragmentSetBinding
import com.amwms.food4u.viewmodels.CoordinateViewModel
import com.amwms.food4u.viewmodels.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SetFragment : Fragment() {

    private var _binding: FragmentSetBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView

    private lateinit var setId : String
    private lateinit var setName : String

    private val favoritesViewModel: FavoritesViewModel by activityViewModels {
        FavoritesViewModelFactory(
            (activity?.application as Food4UApplication).database.food4uDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            setId = it.getString("setId").toString()
            setName = it.getString("setName").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSetBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setHeader.text = setName

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val dishesInSetAdapter = RestaurantMenuAdapter({
            val action = SetFragmentDirections
                .actionSetFragmentToCaloriesFragment(
                    dishId = it.id.toString(),
                    dishName = it.name
                )
            view.findNavController().navigate(action)
        })

        recyclerView.adapter = dishesInSetAdapter

        lifecycle.coroutineScope.launch {
            favoritesViewModel.allDishesInSet(setId.toInt()).collect {
                dishesInSetAdapter.submitList(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}