package cl.duoc.vitairosm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cl.duoc.vitairosm.model.UsuarioDao

class AuthViewModelFactory(private val usuarioDao: UsuarioDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(usuarioDao) as T
        }
        throw IllegalArgumentException("ViewModel desconocido")
    }
}