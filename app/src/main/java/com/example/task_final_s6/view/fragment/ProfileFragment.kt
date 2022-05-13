package com.example.task_final_s6.view.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.bumptech.glide.Glide
import com.example.task_final_s6.R
import com.example.task_final_s6.databinding.FragmentProfileBinding
import com.example.task_final_s6.datastore.UserManager
import com.example.task_final_s6.network.ApiUserServices
import com.example.task_final_s6.permission.AppPermission
import com.example.task_final_s6.repository.UserRepository
import com.example.task_final_s6.viewmodel.UserViewModel
import com.example.task_final_s6.viewmodel.UserViewModelFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var appPermission: AppPermission

    private val pickImage = 100
    private lateinit var userLoginManager: UserManager
    private lateinit var viewModelUser: UserViewModel


    private var apiUserInterface = ApiUserServices.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initField()
        binding!!.btnLogout.setOnClickListener {
            logout()
        }
        binding!!.btnUpdate.setOnClickListener {
            updateData()
        }

        binding.ProfileImage.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }
    }


    //init original data to the profile page
    private fun initField() {
        userLoginManager = UserManager(requireContext())
        userLoginManager.image.asLiveData().observe(viewLifecycleOwner) {
            Glide.with(binding!!.ProfileImage.context)
                .load(it)
                .error(R.drawable.ic_person)
                .override(100, 100)
                .into(binding!!.ProfileImage)
        }
        userLoginManager.name.asLiveData().observe(viewLifecycleOwner) {
            binding!!.edtFullName.setText(it.toString())
        }
        userLoginManager.dateOfBirth.asLiveData().observe(viewLifecycleOwner) {
            binding!!.edtDateOfBirth.setText(it.toString())
        }
        userLoginManager.address.asLiveData().observe(viewLifecycleOwner) {
            binding!!.edtAddress.setText(it.toString())
        }
        userLoginManager.email.asLiveData().observe(viewLifecycleOwner) {
            binding!!.edtEmail.setText(it.toString())
        }
        userLoginManager.username.asLiveData().observe(viewLifecycleOwner) {
            binding!!.edtUsername.setText(it.toString())
        }
        userLoginManager.password.asLiveData().observe(viewLifecycleOwner) {
            binding!!.edtPassword.setText(it.toString())
            binding!!.edtConfirmPassword.setText(it.toString())
        }
    }

    //log out function, will erase user data that stored in data store
    private fun logout() {
        userLoginManager = UserManager(requireContext())
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Apakah anda yakin ingin logout?")
            .setNegativeButton("TIDAK") { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }
            .setPositiveButton("YA") { _: DialogInterface, _: Int ->
                GlobalScope.launch {
                    userLoginManager.clearData()
                }
                val mIntent = activity?.intent
                activity?.finish()
                startActivity(mIntent)
            }.show()
    }

    //function to update data, will call function updateUser that provided by view model
    private fun updateData() {
        userLoginManager = UserManager(requireContext())

        var id = ""
        val alamat = binding!!.edtAddress.text.toString()
        val email = binding!!.edtEmail.text.toString()
        val image = "http://placeimg.com/640/480/city"
        val username = binding!!.edtUsername.text.toString()
        val tanggalLahir = binding!!.edtDateOfBirth.text.toString()
        val password = binding!!.edtPassword.text.toString()
        val namaLengkap = binding!!.edtFullName.text.toString()
        //get id for current user
        userLoginManager.IDuser.asLiveData().observe(viewLifecycleOwner) {
            id = it.toString()
        }
        AlertDialog.Builder(requireContext())
            .setTitle("Update data")
            .setMessage("Yakin ingin mengupdate data?")
            .setNegativeButton("TIDAK") { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }
            .setPositiveButton("YA") { _: DialogInterface, _: Int ->
                viewModelUser = ViewModelProvider(
                    this,
                    UserViewModelFactory(UserRepository(apiUserInterface))
                ).get(UserViewModel::class.java)

                viewModelUser.updateUser(
                    id,
                    alamat,
                    email,
                    image,
                    username,
                    tanggalLahir,
                    password,
                    namaLengkap
                )

                //change previous data that stored in data store
                Toast.makeText(requireContext(), "Update data berhasil", Toast.LENGTH_SHORT).show()
                GlobalScope.launch {
                    userLoginManager.saveData(
                        alamat,
                        email,
                        id,
                        image,
                        namaLengkap,
                        password,
                        tanggalLahir,
                        username
                    )
                }
                //restart activity
                val mIntent = activity?.intent
                activity?.finish()
                startActivity(mIntent)
            }.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}