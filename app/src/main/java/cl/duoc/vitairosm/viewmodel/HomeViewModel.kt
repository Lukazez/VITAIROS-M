package cl.duoc.vitairosm.viewmodel

import androidx.compose.runtime.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.vitairosm.model.RegistroDao
import cl.duoc.vitairosm.model.RegistroEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(private val registroDao: RegistroDao, private val usuarioId: Int) : ViewModel() {

    val historial: StateFlow<List<RegistroEntity>> = registroDao.obtenerHistorial(usuarioId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    var mensajeError by mutableStateOf<String?>(null); private set
    var borrando by mutableStateOf(false); private set
    fun borrarRegistro(id: Int) {
        if (borrando) return
        borrando = true
        mensajeError = null
        viewModelScope.launch {
            try { withContext(Dispatchers.IO) { registroDao.eliminarRegistro(id, usuarioId) } }
            catch (e: CancellationException) { throw e }
            catch (_: Exception) { mensajeError = "No se pudo borrar el registro. Intenta nuevamente." }
            finally { borrando = false }
        }
    }
}
