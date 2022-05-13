package com.example.task_final_s6.view.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import com.example.task_final_s6.R
import com.example.task_final_s6.databinding.FragmentSplashBinding
import com.example.task_final_s6.datastore.UserManager

class SplashFragment : Fragment(R.layout.fragment_splash) {
    private var fragmentSplashBinding: FragmentSplashBinding? = null

    private lateinit var UserManager: UserManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentSplashBinding.bind(view)
        fragmentSplashBinding = binding

        UserManager = UserManager(requireContext())
        Handler(Looper.getMainLooper()).postDelayed({
            UserManager.boolean.asLiveData().observe(viewLifecycleOwner) {
                if (it == true) {
                    Navigation.findNavController(view)
                        .navigate(R.id.action_splashFragment_to_homeFragment)
                } else {
                    Navigation.findNavController(view)
                        .navigate(R.id.action_splashFragment_to_loginFragment)
                }
            }
        }, 4000)
    }

}