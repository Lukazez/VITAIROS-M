package cl.duoc.vitairosm

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import cl.duoc.vitairosm.model.AppDatabase
import cl.duoc.vitairosm.model.RegistroEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegistroIsolationTest {
    @Test fun cadaCuentaSoloLeeYBorraSusRegistros() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        try {
            val dao = db.registroDao()
            val idA = dao.agregarRegistro(RegistroEntity(animo = "Bien", categoria = "", nota = "A", usuarioId = 1)).toInt()
            dao.agregarRegistro(RegistroEntity(animo = "Bien", categoria = "", nota = "B", usuarioId = 2))
            dao.agregarRegistro(RegistroEntity(animo = "Bien", categoria = "", nota = "Anterior"))
            assertEquals(listOf("A"), dao.obtenerHistorial(1).first().map { it.nota })
            assertEquals(listOf("B"), dao.obtenerHistorial(2).first().map { it.nota })
            assertEquals(0, dao.eliminarRegistro(idA, 2))
            assertEquals(1, dao.obtenerHistorial(1).first().size)
            assertEquals(1, dao.eliminarRegistro(idA, 1))
            assertTrue(dao.obtenerHistorial(1).first().isEmpty())
            assertEquals(1, dao.obtenerHistorial(2).first().size)
        } finally {
            db.close()
        }
    }
}
