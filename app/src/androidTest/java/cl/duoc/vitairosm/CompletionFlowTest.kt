package cl.duoc.vitairosm

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.lifecycle.ViewModelStore
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import cl.duoc.vitairosm.model.*
import cl.duoc.vitairosm.viewmodel.*
import cl.duoc.vitairosm.worker.ReminderScheduler
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class CompletionFlowTest {
    private class FakeDao : RegistroDao {
        var fail = false
        var calls = 0
        var last: RegistroEntity? = null
        val entered = CountDownLatch(1)
        val release = CountDownLatch(1)
        override fun agregarRegistro(registro: RegistroEntity): Long {
            calls++; entered.countDown()
            check(release.await(5, TimeUnit.SECONDS))
            if (fail) error("Fallo simulado")
            last = registro; return 1
        }
        override fun obtenerHistorial(usuarioId: Int): Flow<List<RegistroEntity>> = flowOf(emptyList())
        override fun eliminarRegistro(registroId: Int, usuarioId: Int) = 0
    }
    @Test fun validaSinGuardarYRecuperaErrorSinPerderDatos() = runBlocking {
        val dao = FakeDao(); val store = ViewModelStore()
        val model = withContext(Dispatchers.Main) { CheckInViewModel(dao, 7).also { store.put("test", it) } }
        try {
            var callbacks = 0
            withContext(Dispatchers.Main) {
                model.guardarRegistro { callbacks++ }
                assertNotNull(model.errorAnimo)
                model.actualizarAnimo("Bien"); model.actualizarNota("x".repeat(201))
                model.guardarRegistro { callbacks++ }; assertNotNull(model.errorNota)
                assertEquals(0, dao.calls)
                model.actualizarNota("Mi nota"); dao.fail = true
                model.guardarRegistro { callbacks++ }
                model.guardarRegistro { callbacks++ }
                assertTrue(model.guardando)
            }
            check(dao.entered.await(5, TimeUnit.SECONDS)); dao.release.countDown()
            withTimeout(5_000) { while (withContext(Dispatchers.Main) { model.guardando }) delay(10) }
            withContext(Dispatchers.Main) {
                assertEquals(1, dao.calls); assertEquals(0, callbacks)
                assertEquals("Mi nota", model.nota); assertNotNull(model.mensajeError)
                dao.fail = false; model.guardarRegistro { callbacks++ }
            }
            withTimeout(5_000) { while (withContext(Dispatchers.Main) { model.guardando }) delay(10) }
            assertEquals(7, dao.last!!.usuarioId)
            assertEquals("Mi nota", dao.last!!.nota)
            assertEquals(1, callbacks)
        } finally { dao.release.countDown(); withContext(Dispatchers.Main) { store.clear() } }
    }
    @Test fun nuevasCuentasGuardanHashYPermitenAcceso() = runBlocking {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val db = Room.inMemoryDatabaseBuilder(ctx, AppDatabase::class.java).build()
        val store = ViewModelStore()
        val model = withContext(Dispatchers.Main) { AuthViewModel(db.usuarioDao()).also { store.put("test", it) } }
        try {
            withContext(Dispatchers.Main) {
                model.actualizarNombre("Prueba"); model.actualizarCorreo("prueba@ejemplo.cl")
                model.actualizarContrasena("clave123"); model.registrarUsuario()
            }
            withTimeout(20_000) { while (withContext(Dispatchers.Main) { model.procesando }) delay(20) }
            val stored = db.usuarioDao().buscarPorCorreo("prueba@ejemplo.cl")!!
            assertNotEquals("clave123", stored.contrasena)
            assertTrue(PasswordHasher.verify("clave123", stored.contrasena))
            withContext(Dispatchers.Main) { model.actualizarContrasena("clave123"); model.iniciarSesion() }
            withTimeout(20_000) { while (withContext(Dispatchers.Main) { model.procesando }) delay(20) }
            withContext(Dispatchers.Main) {
                assertEquals(stored.id, model.usuarioLogueado!!.id)
                model.cerrarSesion(); assertNull(model.usuarioLogueado); assertEquals("", model.contrasena)
            }
        } finally { withContext(Dispatchers.Main) { store.clear() }; db.close() }
    }
    @Test fun recordatorioEsPeriodicoDe24Horas() {
        val spec = ReminderScheduler.dailyRequest().workSpec
        assertEquals(TimeUnit.HOURS.toMillis(24), spec.intervalDuration)
        assertEquals(TimeUnit.HOURS.toMillis(24), spec.initialDelay)
    }
}
