package com.amwms.food4u.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amwms.food4u.Food4UApplication
import com.amwms.food4u.adapters.FavoriteSetAdapter
import com.amwms.food4u.databinding.FragmentFavoritesBinding
import com.amwms.food4u.viewmodels.CoordinateViewModel
import com.amwms.food4u.viewmodels.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.StringBuilder

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private val sharedViewModel: CoordinateViewModel by activityViewModels()
    private lateinit var userId: String

    private val favoritesViewModel: FavoritesViewModel by activityViewModels {
        FavoritesViewModelFactory(
            (activity?.application as Food4UApplication).database.food4uDao()
        )
    }

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = Settings.Secure.getString(getContext()?.getContentResolver(), Settings.Secure.ANDROID_ID)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val setAdapter = FavoriteSetAdapter({
            val action = FavoritesFragmentDirections
                .actionFavoritesFragmentToSetFragment(
                    setId = it.id.toString(),
                    setName = it.setName
                )
            view.findNavController().navigate(action)
        })

        recyclerView.adapter = setAdapter

        lifecycle.coroutineScope.launch {
            favoritesViewModel.allFavoriteSets(userId).collect {
                setAdapter.submitList(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}