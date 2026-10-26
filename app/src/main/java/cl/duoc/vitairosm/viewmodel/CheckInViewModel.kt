package cl.duoc.vitairosm.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.vitairosm.model.RegistroDao
import cl.duoc.vitairosm.model.RegistroEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CheckInViewModel(private val registroDao: RegistroDao) : ViewModel() {

    var animo by mutableStateOf("")
        private set

    var categoria by mutableStateOf("")
        private set

    var nota by mutableStateOf("")
        private set

    val historial: StateFlow<List<RegistroEntity>> = registroDao.obtenerHistorial()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun actualizarAnimo(nuevoAnimo: String) {
        animo = nuevoAnimo
    }

    fun actualizarCategoria(nuevaCategoria: String) {
        categoria = nuevaCategoria
    }

    fun actualizarNota(nuevaNota: String) {
        nota = nuevaNota
    }

    fun guardarRegistro(onExito: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            if (animo.isNotEmpty()) {
                val registro = RegistroEntity(
                    animo = animo,
                    categoria = categoria,
                    nota = nota
                )

                registroDao.agregarRegistro(registro)

                withContext(Dispatchers.Main) {
                    animo = ""
                    categoria = ""
                    nota = ""
                    onExito()
                }
            }
        }
    }
}