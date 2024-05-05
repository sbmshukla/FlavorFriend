package com.sbmshukla.flavorfriend.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import com.sbmshukla.flavorfriend.FlavorFriendApplication
import com.sbmshukla.flavorfriend.R
import com.sbmshukla.flavorfriend.databinding.FragmentMealBinding
import com.sbmshukla.flavorfriend.pojo.Meal
import com.sbmshukla.flavorfriend.reposetories.Repository
import com.sbmshukla.flavorfriend.utils.Resource
import com.sbmshukla.flavorfriend.viewmodel.HomeViewModel
import com.sbmshukla.flavorfriend.viewmodel.HomeViewModelFactory
import com.sbmshukla.flavorfriend.viewmodel.MealViewModel
import com.sbmshukla.flavorfriend.viewmodel.MealViewModelFactory
import kotlinx.coroutines.launch


class MealFragment : Fragment(R.layout.fragment_meal) {
    private lateinit var binding: FragmentMealBinding
    private lateinit var mealViewModel: MealViewModel
    private lateinit var repository: Repository
    private val args: MealFragmentArgs by navArgs()
    private var mealId: String?= null
    private var mealName: String?=null
    private var mealThumb: String?=null
    private lateinit var currentMeal:Meal
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMealBinding.bind(view)
        repository = (requireActivity().application as FlavorFriendApplication).repository
        mealViewModel = ViewModelProvider(
            this, MealViewModelFactory(requireActivity().application, repository)
        )[MealViewModel::class.java]


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        mealId = args.mealId
        mealName = args.mealName
        mealThumb = args.mealThumb

        binding.mealName = mealName
        binding.mealThumb=mealThumb

        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))

        lifecycleScope.launch {
            mealViewModel.getMealDetails(id= mealId!!)
        }

        mealViewModel.mealDetailsLiveData.observe(viewLifecycleOwner){
            when (it) {
                is Resource.Loading -> {
                    Log.d("RandomMeal", "Loading...")
                    binding.progressBar.visibility=View.VISIBLE
                    binding.imgYoutube.visibility=View.GONE
                }

                is Resource.Success -> {
                    Log.d("RandomMeal", "Successfully Loaded Random Meal...")
                    binding.progressBar.visibility=View.GONE
                    binding.imgYoutube.visibility=View.VISIBLE
                    val result = it.data
                    if (result != null) {
                        currentMeal = result.meals[0]
                        binding.currentMeal = currentMeal
                    }
                }

                is Resource.Error -> {
                    binding.progressBar.visibility=View.GONE
                    binding.imgYoutube.visibility=View.GONE
                    Log.d(
                        "API_Error",
                        "Getting error while trying to load random Meal : ${it.message.toString()}"
                    )
                }
            }
        }

        binding.imgYoutube.setOnClickListener {
            val intent=Intent(Intent.ACTION_VIEW, Uri.parse(currentMeal.strYoutube))
            startActivity(intent)
        }

    }
}