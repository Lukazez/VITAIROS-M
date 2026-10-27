package cl.duoc.vitairosm.viewmodel

import android.content.ActivityNotFoundException
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.vitairosm.model.ProfilePhotoRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfilePhotoViewModel(
    private val repository: ProfilePhotoRepository,
    private val usuarioId: Int
) : ViewModel() {
    var foto by mutableStateOf<Bitmap?>(null)
        private set
    var ocupado by mutableStateOf(true)
        private set
    var mensaje by mutableStateOf<String?>(null)
        private set
    // El ViewModel conserva la captura y su propietario durante rotaciones.
    private var capturaPendiente: String? = null

    init {
        viewModelScope.launch {
            try {
                foto = withContext(Dispatchers.IO) { repository.loadPhoto(usuarioId) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                mensaje = "No se pudo cargar la foto. Puedes tomar una nueva."
            } finally {
                ocupado = false
            }
        }
    }

    fun tomarFoto(abrirCamara: (Uri) -> Unit) {
        if (ocupado) return
        ocupado = true
        mensaje = null
        viewModelScope.launch {
            try {
                val name = withContext(Dispatchers.IO) { repository.prepareCapture(usuarioId) }
                capturaPendiente = name
                abrirCamara(repository.captureUri(usuarioId, name))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                mensaje = when (e) {
                    is ActivityNotFoundException -> "No hay una aplicación de cámara disponible en este dispositivo."
                    is SecurityException -> "No se pudo acceder a la cámara. Revisa los permisos de la aplicación Cámara en Ajustes."
                    else -> "No se pudo abrir la cámara. Intenta nuevamente."
                }
                descartarPendiente()
                ocupado = false
            }
        }
    }

    fun completarCaptura(aceptada: Boolean) {
        val name = capturaPendiente ?: return
        capturaPendiente = null
        viewModelScope.launch {
            try {
                val nueva = withContext(Dispatchers.IO) { repository.finishCapture(usuarioId, name, aceptada) }
                if (nueva != null) foto = nueva
                mensaje = if (aceptada) "Foto de perfil guardada." else "Captura cancelada. Tu foto no cambió."
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                mensaje = "No se pudo guardar la foto. Tu foto anterior se conserva. Intenta nuevamente."
            } finally {
                ocupado = false
            }
        }
    }

    private suspend fun descartarPendiente() {
        val name = capturaPendiente ?: return
        capturaPendiente = null
        withContext(Dispatchers.IO) { repository.discardCapture(usuarioId, name) }
    }
}
