package com.example.task_final_s6.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.task_final_s6.R
import com.example.task_final_s6.view.adapter.MovieAdapter
import com.example.task_final_s6.databinding.FragmentHomeBinding
import com.example.task_final_s6.datastore.UserManager
import com.example.task_final_s6.model.Movie
import com.example.task_final_s6.viewmodel.MovieViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val movieViewModel by viewModels<MovieViewModel>()
    private lateinit var userManager : UserManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.tvUsername.text = "Welcome, ${args.username}!"
//        binding.tvUsername.text = "Welcome, ${Preferences().getLoggedInUser(requireContext())}!"
        getUserProfile()
        movieViewModel.movie.observe(viewLifecycleOwner) { setMovieData(it) }
        movieViewModel.isLoading.observe(viewLifecycleOwner) { showLoading(it) }
        binding.ibProfile.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setMovieData(movie: List<Movie>) {
        binding.apply {
            val movieAdapter = MovieAdapter(movie)
            rvMovies.setHasFixedSize(true)
            rvMovies.layoutManager = LinearLayoutManager(activity)
            rvMovies.adapter = movieAdapter
            movieAdapter.setOnItemClickCallback(object :
                MovieAdapter.OnItemClickCallback {
                override fun onItemClicked(data: Movie) {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToDetailFragment(
                            data
                        )
                    )
                }
            })
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getUserProfile() {
        userManager = UserManager(requireContext())
        userManager.username.asLiveData().observe(viewLifecycleOwner) {
            _binding!!.tvUsername.text = "Welcome, $it"
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}