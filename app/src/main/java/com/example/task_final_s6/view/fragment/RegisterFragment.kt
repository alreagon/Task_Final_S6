package com.example.task_final_s6.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.task_final_s6.R
import com.example.task_final_s6.databinding.FragmentRegisterBinding
import com.example.task_final_s6.network.ApiUserServices
import com.example.task_final_s6.repository.UserRepository
import com.example.task_final_s6.viewmodel.UserViewModel
import com.example.task_final_s6.viewmodel.UserViewModelFactory

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
//    private val userViewModel by viewModels<UserViewModel>()

    private val apiUserServices = ApiUserServices.getInstance()


    private lateinit var viewModelUserApi : UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.btnRegister.setOnClickListener {
            tambahUser()
        }
    }

    //get all user input, then call postUser function
    private fun tambahUser(){
        val nama = binding!!.edtName.text.toString()
        val username = binding!!.edtUsername.text.toString()
        val alamat = binding!!.edtAddress.text.toString()
        val tanggalLahir = binding!!.edtDateOfBirth.text.toString()
        val image = "http://placeimg.com/640/480/people/sepia"
        val password = binding!!.edtPassword.text.toString()
        val email = binding!!.edtEmail.text.toString()
        val konfirmasiPassword = binding!!.edtConfirmPassword.text.toString()

        //check if all fields is not empty
        if (nama.isNotEmpty() && username.isNotEmpty() && alamat.isNotEmpty() &&
            tanggalLahir.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty() &&
            konfirmasiPassword.isNotEmpty()
        ) {
            //check similarity of password and konfirmasiPassword
            if (password == konfirmasiPassword) {
                postUser(alamat, email, image, username, tanggalLahir, password, nama)
            } else {
                Toast.makeText(requireContext(),"Password dan konfirmasi password harus sama", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Semua field harus diisi", Toast.LENGTH_SHORT).show()
        }
    }

    //post user is function to use addNewUser function that provided by view model
    private fun postUser(
        alamat: String,
        email: String,
        image: String,
        username: String,
        tanggalLahir: String,
        password: String,
        name: String
    ) {
        viewModelUserApi = ViewModelProvider(
            viewModelStore, UserViewModelFactory(UserRepository(apiUserServices))
        ).get(UserViewModel::class.java)
        viewModelUserApi.addNewUser(alamat, email, image, username, tanggalLahir, password, name)

        // toast
        Toast.makeText(requireContext(), "Registrasi berhasil", Toast.LENGTH_SHORT).show()
        Navigation.findNavController(requireView()).navigate(R.id.action_registerFragment_to_loginFragment)
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}