package cl.duoc.vitairosm

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.Color
import androidx.exifinterface.media.ExifInterface
import androidx.core.content.FileProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import cl.duoc.vitairosm.model.ProfilePhotoRepository
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.io.IOException
import java.util.UUID

/** Usa carpetas temporales únicas: nunca lee ni sobrescribe fotos reales. */
@RunWith(AndroidJUnit4::class)
class ProfilePhotoRepositoryTest {
    private lateinit var context: Context
    private lateinit var root: File
    private lateinit var repository: ProfilePhotoRepository

    @Before fun setUp() {
        val target = InstrumentationRegistry.getInstrumentation().targetContext
        root = File(target.cacheDir, "profile_capture/test-${UUID.randomUUID()}").apply { mkdirs() }
        context = object : ContextWrapper(target) {
            override fun getFilesDir() = File(root, "files").apply { mkdirs() }
            override fun getCacheDir() = File(root, "cache").apply { mkdirs() }
        }
        repository = ProfilePhotoRepository(context)
    }

    @After fun tearDown() { root.deleteRecursively() }

    private fun capture(id: Int, color: Int, width: Int = 40, height: Int = 20): String {
        val name = repository.prepareCapture(id)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply { eraseColor(color) }
        context.contentResolver.openOutputStream(repository.captureUri(id, name))!!.use {
            assertTrue(bitmap.compress(Bitmap.CompressFormat.JPEG, 95, it))
        }
        bitmap.recycle()
        return name
    }

    private fun color(id: Int): Int = repository.loadPhoto(id)!!.let {
        val result = it.getPixel(0, 0)
        it.recycle()
        result
    }

    @Test fun fotosSeparadasPersistenAlCrearOtroRepositorio() {
        assertNull(repository.loadPhoto(1))
        repository.finishCapture(1, capture(1, Color.RED), true)!!.recycle()
        assertNull(repository.loadPhoto(2))
        repository.finishCapture(2, capture(2, Color.BLUE), true)!!.recycle()
        repository = ProfilePhotoRepository(context)
        assertTrue(Color.red(color(1)) > 240)
        assertTrue(Color.blue(color(2)) > 240)
        repository.finishCapture(1, capture(1, Color.GREEN), true)!!.recycle()
        assertTrue(Color.green(color(1)) > 240)
        assertTrue(Color.blue(color(2)) > 240)
    }

    @Test fun cancelarConservaFotoAnteriorYEliminaTemporal() {
        repository.finishCapture(1, capture(1, Color.RED), true)!!.recycle()
        val name = capture(1, Color.BLUE)
        assertNull(repository.finishCapture(1, name, false))
        assertTrue(Color.red(color(1)) > 240)
        assertFalse(File(context.cacheDir, "profile_capture/$name").exists())
    }

    @Test fun capturaInvalidaNoReemplazaFotoAnterior() {
        repository.finishCapture(1, capture(1, Color.RED), true)!!.recycle()
        val name = repository.prepareCapture(1)
        try {
            repository.finishCapture(1, name, true)
            fail("Debía rechazar una captura vacía")
        } catch (_: IOException) { }
        assertTrue(Color.red(color(1)) > 240)
        assertFalse(File(context.cacheDir, "profile_capture/$name").exists())
    }

    @Test fun noPermiteGuardarNiBorrarCapturaDeOtraCuenta() {
        val name = capture(1, Color.RED)
        try {
            repository.finishCapture(2, name, true)
            fail("Debía rechazar al propietario incorrecto")
        } catch (_: IllegalArgumentException) { }
        try {
            repository.discardCapture(2, name)
            fail("Debía rechazar el borrado ajeno")
        } catch (_: IllegalArgumentException) { }
        assertTrue(File(context.cacheDir, "profile_capture/$name").exists())
        assertNull(repository.loadPhoto(2))
    }

    @Test fun prepararCapturaSoloLimpiaTemporalesDelMismoUsuario() {
        val first = repository.prepareCapture(1)
        val other = repository.prepareCapture(2)
        repository.prepareCapture(1)
        assertFalse(File(context.cacheDir, "profile_capture/$first").exists())
        assertTrue(File(context.cacheDir, "profile_capture/$other").exists())
    }

    @Test fun normalizaOrientacionYLimitaTamano() {
        val name = capture(1, Color.RED, 2400, 1200)
        val file = File(context.cacheDir, "profile_capture/$name")
        ExifInterface(file.path).apply {
            setAttribute(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_ROTATE_90.toString())
            saveAttributes()
        }
        val bitmap = repository.finishCapture(1, name, true)!!
        assertTrue(maxOf(bitmap.width, bitmap.height) <= 1024)
        assertEquals(bitmap.width * 2, bitmap.height)
        bitmap.recycle()
    }

    @Test fun proveedorNoExponeFotosPermanentes() {
        // Las rutas públicas del FileProvider solo permiten profile_capture/ en caché.
        val target = InstrumentationRegistry.getInstrumentation().targetContext
        val privatePhoto = File(target.filesDir, "profile_photos/user_1.jpg")
        try {
            FileProvider.getUriForFile(target, "${target.packageName}.profilephotos", privatePhoto)
            fail("La foto privada no debe tener una URI pública")
        } catch (_: IllegalArgumentException) { }
    }
}
