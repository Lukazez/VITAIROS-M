package cl.duoc.vitairosm

import android.database.sqlite.SQLiteDatabase
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
class RegistroMigrationTest {
    @Test fun migracionConservaCuentasYRegistrosSinAsignarlosAOtraPersona() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val name = "migration-test-${System.nanoTime()}.db"
        val file = context.getDatabasePath(name)
        file.parentFile?.mkdirs()
        SQLiteDatabase.openOrCreateDatabase(file, null).use { old ->
            old.execSQL("CREATE TABLE usuarios (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nombre TEXT NOT NULL, correo TEXT NOT NULL, contrasena TEXT NOT NULL)")
            old.execSQL("CREATE TABLE registros_emocionales (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, animo TEXT NOT NULL, categoria TEXT NOT NULL, nota TEXT NOT NULL, fechaHora INTEGER NOT NULL)")
            old.execSQL("INSERT INTO usuarios VALUES (1, 'Prueba', 'prueba@ejemplo.cl', 'a')")
            old.execSQL("INSERT INTO registros_emocionales VALUES (1, 'Bien', '', 'Anterior', 1)")
            old.version = 2
        }
        val db = Room.databaseBuilder(context, AppDatabase::class.java, name)
            .addMigrations(AppDatabase.MIGRATION_2_3).build()
        try {
            assertNotNull(db.usuarioDao().iniciarSesion("prueba@ejemplo.cl", "a"))
            assertTrue(db.registroDao().obtenerHistorial(1).first().isEmpty())
            db.openHelper.readableDatabase.query("SELECT nota, usuarioId FROM registros_emocionales WHERE id = 1").use {
                assertTrue(it.moveToFirst())
                assertEquals("Anterior", it.getString(0))
                assertTrue(it.isNull(1))
            }
            db.registroDao().agregarRegistro(RegistroEntity(animo = "Bien", categoria = "", nota = "Nuevo", usuarioId = 1))
            assertEquals(listOf("Nuevo"), db.registroDao().obtenerHistorial(1).first().map { it.nota })
        } finally {
            db.close()
            context.deleteDatabase(name)
        }
    }
}
