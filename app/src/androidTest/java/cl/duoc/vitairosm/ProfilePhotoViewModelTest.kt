package cl.duoc.vitairosm

import android.content.ActivityNotFoundException
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import androidx.lifecycle.ViewModelStore
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import cl.duoc.vitairosm.model.ProfilePhotoRepository
import cl.duoc.vitairosm.viewmodel.ProfilePhotoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.util.UUID

/** Comprueba estado y recuperación; la cámara real se prueba por separado. */
@RunWith(AndroidJUnit4::class)
class ProfilePhotoViewModelTest {
    private val store = ViewModelStore()
    private lateinit var root: File
    private lateinit var model: ProfilePhotoViewModel
    private val target = InstrumentationRegistry.getInstrumentation().targetContext

    @Before fun setUp() = runBlocking {
        root = File(target.cacheDir, "profile_capture/flow-${UUID.randomUUID()}").apply { mkdirs() }
        val context = object : ContextWrapper(target) {
            override fun getFilesDir() = File(root, "files").apply { mkdirs() }
            override fun getCacheDir() = File(root, "cache").apply { mkdirs() }
        }
        withContext(Dispatchers.Main) {
            model = ProfilePhotoViewModel(ProfilePhotoRepository(context), 1)
            store.put("test", model)
        }
        awaitReady()
    }

    @After fun tearDown() = runBlocking {
        withContext(Dispatchers.Main) { store.clear() }
        root.deleteRecursively()
        Unit
    }

    private suspend fun awaitReady() = withTimeout(5_000) {
        while (withContext(Dispatchers.Main) { model.ocupado }) delay(20)
    }

    @Test fun ausenciaDeCamaraYPermisoRechazadoPermitenReintentar() = runBlocking {
        withContext(Dispatchers.Main) { model.tomarFoto { throw ActivityNotFoundException() } }
        awaitReady()
        withContext(Dispatchers.Main) {
            assertEquals("No hay una aplicación de cámara disponible en este dispositivo.", model.mensaje)
            model.tomarFoto { throw SecurityException() }
        }
        awaitReady()
        withContext(Dispatchers.Main) {
            assertEquals("No se pudo acceder a la cámara. Revisa los permisos de la aplicación Cámara en Ajustes.", model.mensaje)
            assertNull(model.foto)
        }
    }

    @Test fun exitoYCancelacionActualizanElEstadoSinPerderFoto() = runBlocking {
        var uri: Uri? = null
        withContext(Dispatchers.Main) { model.tomarFoto { uri = it } }
        withTimeout(5_000) { while (withContext(Dispatchers.Main) { uri == null }) delay(20) }
        val bitmap = Bitmap.createBitmap(30, 30, Bitmap.Config.ARGB_8888).apply { eraseColor(Color.RED) }
        target.contentResolver.openOutputStream(uri!!)!!.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it) }
        bitmap.recycle()
        withContext(Dispatchers.Main) { model.completarCaptura(true) }
        awaitReady()
        val previous = withContext(Dispatchers.Main) {
            assertNotNull(model.foto)
            assertEquals("Foto de perfil guardada.", model.mensaje)
            model.foto
        }
        withContext(Dispatchers.Main) {
            uri = null
            model.tomarFoto { uri = it }
        }
        withTimeout(5_000) { while (withContext(Dispatchers.Main) { uri == null }) delay(20) }
        withContext(Dispatchers.Main) { model.completarCaptura(false) }
        awaitReady()
        withContext(Dispatchers.Main) {
            assertSame(previous, model.foto)
            assertEquals("Captura cancelada. Tu foto no cambió.", model.mensaje)
        }
    }
}
