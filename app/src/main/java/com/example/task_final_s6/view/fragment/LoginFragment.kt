package com.example.task_final_s6.view.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.task_final_s6.R
import com.example.task_final_s6.databinding.FragmentLoginBinding
import com.example.task_final_s6.model.GetAllUserResponseItem
import com.example.task_final_s6.datastore.UserManager
import com.example.task_final_s6.network.ApiUserServices
import com.example.task_final_s6.repository.UserRepository
import com.example.task_final_s6.viewmodel.UserViewModel
import com.example.task_final_s6.viewmodel.UserViewModelFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginFragment : Fragment(R.layout.fragment_login) {
    private var _binding: FragmentLoginBinding? = null
    private lateinit var viewModelUserApi: UserViewModel
    private lateinit var userLoginManager: UserManager


    private val apiUserServices = ApiUserServices.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentLoginBinding.bind(view)
        _binding = binding

        _binding!!.btnLogin.setOnClickListener {
            initUserApiViewModel()
        }

        _binding!!.tvRegister.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun initUserApiViewModel() {
        viewModelUserApi = ViewModelProvider(
            this, UserViewModelFactory(UserRepository(apiUserServices))
        ).get(UserViewModel::class.java)
        viewModelUserApi.liveDataUserApi.observe(viewLifecycleOwner){
            if(it.isNotEmpty()){
                loginAuth(it)
            }
        }
        viewModelUserApi.getAllUser()
    }

    private fun loginAuth(list: List<GetAllUserResponseItem>) {
        userLoginManager = UserManager(requireContext())

        //inputan user
        val inputanEmail = _binding!!.edtEmail.text.toString()
        val inputanPassword = _binding!!.edtPassword.text.toString()

        if (inputanEmail.isNotEmpty() && inputanPassword.isNotEmpty()) {
            for (i in list.indices) {
                if (inputanEmail == list[i].email && inputanPassword == list[i].password) {
                    Toast.makeText(requireContext(), "Berhasil login", Toast.LENGTH_SHORT).show()
                    GlobalScope.launch {
                        userLoginManager.setBoolean(true)
                        userLoginManager.saveData(
                            list[i].alamat,
                            list[i].email,
                            list[i].id,
                            list[i].image,
                            list[i].name,
                            list[i].password,
                            list[i].tanggalLahir,
                            list[i].username
                        )
                    }
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_loginFragment_to_homeFragment)
                }else if (inputanEmail == null && inputanPassword == null){
                    Toast.makeText(
                        requireContext(),
                        "Email atau password salah!",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        } else {
            Toast.makeText(requireContext(), "Semua field harus diisi", Toast.LENGTH_SHORT).show()
        }
    }
}