package cl.duoc.vitairosm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.vitairosm.model.RegistroDao
import cl.duoc.vitairosm.model.RegistroEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(private val registroDao: RegistroDao) : ViewModel() {

    val historial: StateFlow<List<RegistroEntity>> = registroDao.obtenerHistorial()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Funcion para eliminar un registro llamando al DAO en segundo plano
    fun borrarRegistro(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            registroDao.eliminarRegistro(id)
        }
    }

    fun programarRecordatorioDiario(context: android.content.Context) {
        val constraints = androidx.work.Constraints.Builder()
            .setRequiredNetworkType(androidx.work.NetworkType.NOT_REQUIRED)
            .build()

        val solicitudUnica =
            androidx.work.OneTimeWorkRequestBuilder<cl.duoc.vitairosm.worker.RecordatorioWorker>()
                .setInitialDelay(5, java.util.concurrent.TimeUnit.SECONDS)
                .setConstraints(constraints)
                .build()

        androidx.work.WorkManager.getInstance(context).enqueue(solicitudUnica)
    }

}