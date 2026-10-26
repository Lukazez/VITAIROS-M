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
        val correoTrim = correo.trim()
        val contrasenaTrim = contrasena.trim()
        val nombreTrim = nombre.trim()

        if (correoTrim.isEmpty() || contrasenaTrim.isEmpty() || nombreTrim.isEmpty()) {
            mensajeError = "Todos los campos son obligatorios"
            return
        }
        viewModelScope.launch {
            val existe = usuarioDao.buscarPorCorreo(correoTrim)
            if (existe != null) {
                mensajeError = "El correo ya esta registrado"
            } else {
                val nuevoUsuario = UsuarioEntity(nombre = nombreTrim, correo = correoTrim, contrasena = contrasenaTrim)
                usuarioDao.registrarUsuario(nuevoUsuario)
                mensajeError = "Registro exitoso"
            }
        }
    }

    fun iniciarSesion() {
        val correoTrim = correo.trim()
        val contrasenaTrim = contrasena.trim()

        if (correoTrim.isEmpty() || contrasenaTrim.isEmpty()) {
            mensajeError = "Ingrese correo y contrasena"
            return
        }
        viewModelScope.launch {
            val usuario = usuarioDao.iniciarSesion(correoTrim, contrasenaTrim)
            if (usuario != null) {
                usuarioLogueado = usuario
                mensajeError = ""
            } else {
                mensajeError = "Credenciales incorrectas"
            }
        }
    }
}