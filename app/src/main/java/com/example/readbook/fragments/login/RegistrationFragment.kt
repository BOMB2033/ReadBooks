package com.example.readbook.fragments.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.readbook.R
import com.example.readbook.databinding.FragmentRegistrationBinding
import com.example.readbook.repository.DataViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class RegistrationFragment : Fragment() {
    private lateinit var binding:FragmentRegistrationBinding
    private val viewModel: DataViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            buttonRegistration.setOnClickListener {
                it.findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
            }
            buttonRegistration.setOnClickListener {
                val email = editTextLogin.text.toString()
                val password = editTextPassword.text.toString()
                when{
                    email.isEmpty() && password.isEmpty() -> showMessage("Заполните поля")
                    else -> toLogin(email, password)
                }
            }
        }
    }
    private fun showMessage(message:String){
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
    private fun handleException(exception:Exception){
        try {
            throw exception
        } catch (e: FirebaseAuthWeakPasswordException) {
            showMessage("Слабый пароль")
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            showMessage("Неверные учетные данные")
        } catch (e: FirebaseAuthUserCollisionException) {
            showMessage("Пользователь уже существует")
        } catch (e: Exception) {
            showMessage("Ошибка регистрации")
        }
    }
    private fun toLogin(email:String,password:String){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                if (it.isSuccessful) {
                    showMessage("Регистрация прошла успешно!")
                    viewModel.addUser()
                    binding.root.findNavController().popBackStack()
                }else
                    handleException(it.exception!!)
            }
    }

}