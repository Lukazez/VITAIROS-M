package cl.duoc.vitairosm.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.vitairosm.model.RegistroDao
import cl.duoc.vitairosm.model.RegistroEntity
import kotlinx.coroutines.*

class CheckInViewModel(private val registroDao: RegistroDao, private val usuarioId: Int) : ViewModel() {
    var animo by mutableStateOf(""); private set
    var categoria by mutableStateOf(""); private set
    var nota by mutableStateOf(""); private set
    var errorAnimo by mutableStateOf<String?>(null); private set
    var errorCategoria by mutableStateOf<String?>(null); private set
    var errorNota by mutableStateOf<String?>(null); private set
    var mensajeError by mutableStateOf<String?>(null); private set
    var guardando by mutableStateOf(false); private set

    fun actualizarAnimo(valor: String) { if (!guardando) { animo = valor; errorAnimo = CheckInValidation.animo(valor); mensajeError = null } }
    fun actualizarCategoria(valor: String) { if (!guardando) { categoria = valor; errorCategoria = CheckInValidation.categoria(valor); mensajeError = null } }
    fun actualizarNota(valor: String) { if (!guardando) { nota = valor; errorNota = CheckInValidation.nota(valor); mensajeError = null } }

    fun guardarRegistro(onExito: () -> Unit) {
        if (guardando) return
        errorAnimo = CheckInValidation.animo(animo)
        errorCategoria = CheckInValidation.categoria(categoria)
        errorNota = CheckInValidation.nota(nota)
        if (errorAnimo != null || errorCategoria != null || errorNota != null) return
        val registro = RegistroEntity(animo = animo, categoria = categoria, nota = nota.trim(), usuarioId = usuarioId)
        guardando = true
        mensajeError = null
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) { registroDao.agregarRegistro(registro) }
                animo = ""; categoria = ""; nota = ""
                onExito()
            } catch (e: CancellationException) { throw e }
            catch (_: Exception) { mensajeError = "No se pudo guardar. Tus datos siguen aquí; intenta nuevamente." }
            finally { guardando = false }
        }
    }
}
