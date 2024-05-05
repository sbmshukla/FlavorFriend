package com.sbmshukla.flavorfriend.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.sbmshukla.flavorfriend.FlavorFriendApplication
import com.sbmshukla.flavorfriend.R
import com.sbmshukla.flavorfriend.databinding.FragmentHomeBinding
import com.sbmshukla.flavorfriend.pojo.CategoryMeals
import com.sbmshukla.flavorfriend.pojo.Meal
import com.sbmshukla.flavorfriend.reposetories.Repository
import com.sbmshukla.flavorfriend.ui.adapters.MostPopularItemAdapter
import com.sbmshukla.flavorfriend.utils.Resource
import com.sbmshukla.flavorfriend.viewmodel.HomeViewModel
import com.sbmshukla.flavorfriend.viewmodel.HomeViewModelFactory
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var repository: Repository
    private lateinit var popularItemAdapter: MostPopularItemAdapter

    private var currentRandomMeal: Meal? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        popularItemAdapter = MostPopularItemAdapter()
        repository = (requireActivity().application as FlavorFriendApplication).repository
        homeViewModel = ViewModelProvider(
            this, HomeViewModelFactory(requireActivity().application, repository)
        )[HomeViewModel::class.java]

        binding.lifecycleOwner = this;

        lifecycleScope.launch {
            homeViewModel.getRandomMeal()
        }


        setPopularRecycler()
        setRandomMealObserver()
        setPopularMealObserver()

        binding.randomMealCard.setOnClickListener {
            if (currentRandomMeal != null) {
                currentRandomMeal?.apply {
                    navigateToMealDetailFragment(
                        mealId = idMeal!!, mealName = strMeal!!, mealThumb = strMealThumb!!
                    )
                }
            }
        }

        popularItemAdapter.setOnItemClickListener { currentCategory ->
            currentCategory.apply {
                navigateToMealDetailFragment(
                    mealId = idMeal, mealName = strMeal, mealThumb = strMealThumb
                )
            }
        }

    }

    private fun navigateToMealDetailFragment(mealId: String, mealName: String, mealThumb: String) {
        val action = HomeFragmentDirections.actionHomeFragmentToMealFragment(
            mealId = mealId, mealName = mealName, mealThumb = mealThumb
        )
        findNavController().navigate(action)
    }

    private fun setPopularRecycler() {
        binding.rvPopularMeals.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemAdapter
        }
    }

    private fun setPopularMealObserver() {
        homeViewModel.popularMealLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    Log.d("RandomMeal", "Loading...")
                }

                is Resource.Success -> {
                    Log.d("RandomMeal", "Successfully Loaded Random Meal...")
                    val result = it.data
                    if (result != null) {
                        popularItemAdapter.setMeals(result.meals as ArrayList<CategoryMeals>)
                    }
                }

                is Resource.Error -> {
                    Log.d(
                        "API_Error",
                        "Getting error while trying to load random Meal : ${it.message.toString()}"
                    )
                }
            }
        }
    }

    private fun setRandomMealObserver() {
        homeViewModel.randomMealLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    Log.d("RandomMeal", "Loading...")
                }

                is Resource.Success -> {
                    Log.d("RandomMeal", "Successfully Loaded Random Meal...")
                    val result = it.data
                    if (result != null) {
                        currentRandomMeal = result.meals[0]
                        binding.randomMealData = currentRandomMeal
                    }
                }

                is Resource.Error -> {
                    Log.d(
                        "API_Error",
                        "Getting error while trying to load random Meal : ${it.message.toString()}"
                    )
                }
            }
        }
    }
}