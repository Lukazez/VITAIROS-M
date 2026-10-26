package cl.duoc.vitairosm.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.vitairosm.model.UsuarioDao
import cl.duoc.vitairosm.model.UsuarioEntity
import kotlinx.coroutines.launch

class AuthViewModel(private val usuarioDao: UsuarioDao) : ViewModel() {

    var correo by mutableStateOf("")
        private set
    var contrasena by mutableStateOf("")
        private set
    var nombre by mutableStateOf("")
        private set
    var mensajeError by mutableStateOf("")
        private set
    var usuarioLogueado by mutableStateOf<UsuarioEntity?>(null)
        private set

    fun actualizarCorreo(valor: String) { correo = valor }
    fun actualizarContrasena(valor: String) { contrasena = valor }
    fun actualizarNombre(valor: String) { nombre = valor }

    fun registrarUsuario() {
        if (correo.isEmpty() || contrasena.isEmpty() || nombre.isEmpty()) {
            mensajeError = "Todos los campos son obligatorios"
            return
        }
        viewModelScope.launch {
            val existe = usuarioDao.buscarPorCorreo(correo)
            if (existe != null) {
                mensajeError = "El correo ya esta registrado"
            } else {
                val nuevoUsuario = UsuarioEntity(nombre = nombre, correo = correo, contrasena = contrasena)
                usuarioDao.registrarUsuario(nuevoUsuario)
                mensajeError = "Registro exitoso"
            }
        }
    }

    fun iniciarSesion() {
        if (correo.isEmpty() || contrasena.isEmpty()) {
            mensajeError = "Ingrese correo y contrasena"
            return
        }
        viewModelScope.launch {
            val usuario = usuarioDao.iniciarSesion(correo, contrasena)
            if (usuario != null) {
                usuarioLogueado = usuario
                mensajeError = ""
            } else {
                mensajeError = "Credenciales incorrectas"
            }
        }
    }
}