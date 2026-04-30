package com.example.financeapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.financeapp.data.database.AppDatabase
import com.example.financeapp.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.security.MessageDigest

/**
 * AuthViewModel - ViewModel que maneja la autenticación de usuarios
 * */

class AuthViewModel(application: Application) : AndroidViewModel(application){
    private val userDao = AppDatabase.getDatabase(application).userDao()
    private val _currendUser = MutableStateFlow<User?>(null)
    val currentUser : StateFlow<User?> = _currendUser

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun login(email: String, password:String){
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            try {
                val user = userDao.getUserByEmail(email)
                if(user != null && user.passwordHash == hashPassword(password)){
                    /**Login exitoso*/
                    _currendUser.value = user
                    _authState.value = AuthState.Success
                }else{
                    _authState.value = AuthState.Error("Email o contraseña incorrecta")
                }
            }catch (e: Exception){
                _authState.value = AuthState.Error("Error al iniciar sesión: ${e.message}")
            }
        }
    }

    fun register(name: String, email: String, password: String){
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            try {
                val existingUser = userDao.getUserByEmail(email)
                if(existingUser != null){
                    _authState.value = AuthState.Error("El email ya está registrado")
                    return@launch
                }
                val newUser = User(
                    name = name,
                    email = email,
                    passwordHash = hashPassword(password)
                )
                val userId = userDao.insert(newUser)
                _currendUser.value = newUser.copy(id = userId)
                _authState.value = AuthState.Success
            }catch (e: Exception){
                _authState.value = AuthState.Error("Error al registrar: ${e.message}")
            }
        }
    }

    fun logout(){
        _currendUser.value = null
        _authState.value = AuthState.Idle
    }

    private fun hashPassword(password: String): String{
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString (" "){ "%02x".format(it) }
    }

    sealed class AuthState{
        object Idle : AuthState()
        object Loading : AuthState()
        object Success : AuthState()
        data class Error(val message: String): AuthState()
    }
}