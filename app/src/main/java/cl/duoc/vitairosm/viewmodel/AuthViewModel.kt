package cl.duoc.vitairosm.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.vitairosm.model.UsuarioDao
import cl.duoc.vitairosm.model.UsuarioEntity
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class AuthViewModel(private val usuarioDao: UsuarioDao) : ViewModel() {
    var correo by mutableStateOf("")
        private set
    var contrasena by mutableStateOf("")
        private set
    var nombre by mutableStateOf("")
        private set
    var errorCorreo by mutableStateOf<String?>(null)
        private set
    var errorContrasena by mutableStateOf<String?>(null)
        private set
    var errorNombre by mutableStateOf<String?>(null)
        private set
    var mensajeError by mutableStateOf("")
        private set
    var mensajeExito by mutableStateOf("")
        private set
    var procesando by mutableStateOf(false)
        private set
    var usuarioLogueado by mutableStateOf<UsuarioEntity?>(null)
        private set

    fun actualizarCorreo(valor: String) { correo = valor; errorCorreo = null; mensajeError = ""; mensajeExito = "" }
    fun actualizarContrasena(valor: String) { contrasena = valor; errorContrasena = null; mensajeError = ""; mensajeExito = "" }
    fun actualizarNombre(valor: String) { nombre = valor; errorNombre = null; mensajeError = ""; mensajeExito = "" }

    fun limpiarMensajes() {
        errorCorreo = null
        errorContrasena = null
        errorNombre = null
        mensajeError = ""
        mensajeExito = ""
    }

    private fun validar(esRegistro: Boolean): Boolean {
        limpiarMensajes()
        errorNombre = if (esRegistro) AuthValidation.nombre(nombre) else null
        // Las reglas nuevas son para crear cuentas; no bloquean cuentas anteriores.
        errorCorreo = if (esRegistro) AuthValidation.correo(correo)
            else if (correo.isBlank()) "Ingresa tu correo" else null
        errorContrasena = AuthValidation.contrasena(contrasena, esRegistro)
        return errorNombre == null && errorCorreo == null && errorContrasena == null
    }

    fun registrarUsuario() {
        if (procesando || !validar(true)) return
        val nuevo = UsuarioEntity(nombre = nombre.trim(), correo = correo.trim(), contrasena = contrasena.trim())
        procesando = true
        viewModelScope.launch {
            try {
                if (usuarioDao.buscarPorCorreo(nuevo.correo) != null) {
                    errorCorreo = "El correo ya está registrado"
                } else {
                    usuarioDao.registrarUsuario(nuevo)
                    contrasena = ""
                    mensajeExito = "Cuenta creada. Ahora inicia sesión."
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                mensajeError = "No se pudo crear la cuenta. Intenta nuevamente."
            } finally {
                procesando = false
            }
        }
    }

    fun iniciarSesion() {
        if (procesando || !validar(false)) return
        val correoIngresado = correo.trim()
        val claveIngresada = contrasena.trim()
        procesando = true
        viewModelScope.launch {
            try {
                val usuario = usuarioDao.iniciarSesion(correoIngresado, claveIngresada)
                if (usuario != null) {
                    contrasena = ""
                    usuarioLogueado = usuario
                } else {
                    mensajeError = "Credenciales incorrectas"
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                mensajeError = "No se pudo iniciar sesión. Intenta nuevamente."
            } finally {
                procesando = false
            }
        }
    }

    fun cerrarSesion() {
        usuarioLogueado = null
        correo = ""
        contrasena = ""
        nombre = ""
        limpiarMensajes()
    }
}
