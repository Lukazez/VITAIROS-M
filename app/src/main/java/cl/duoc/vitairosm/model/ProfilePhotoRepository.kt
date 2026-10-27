package cl.duoc.vitairosm.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import android.net.Uri
import android.util.AtomicFile
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException

/** Archivos privados por ID de Room. No modifica cuentas, contraseñas ni registros. */
class ProfilePhotoRepository(private val context: Context) {
    private val photos = File(context.filesDir, "profile_photos")
    private val captures = File(context.cacheDir, "profile_capture")

    private fun photo(usuarioId: Int): AtomicFile {
        require(usuarioId > 0)
        return AtomicFile(File(photos, "user_$usuarioId.jpg"))
    }

    private fun capture(usuarioId: Int, name: String): File {
        require(usuarioId > 0)
        require(name.startsWith("user_${usuarioId}_") && name.endsWith(".jpg"))
        require(File(name).name == name && '/' !in name && '\\' !in name)
        return File(captures, name)
    }

    fun prepareCapture(usuarioId: Int): String {
        require(usuarioId > 0)
        if (!captures.isDirectory && !captures.mkdirs()) throw IOException("No se pudo crear la carpeta")
        // Retira capturas abandonadas solo de esta cuenta (por ejemplo, tras cerrar el proceso).
        captures.listFiles()?.filter { it.name.startsWith("user_${usuarioId}_") }?.forEach { it.delete() }
        return File.createTempFile("user_${usuarioId}_", ".jpg", captures).name
    }

    fun captureUri(usuarioId: Int, name: String): Uri = FileProvider.getUriForFile(
        context.applicationContext, "${context.packageName}.profilephotos", capture(usuarioId, name)
    )

    fun discardCapture(usuarioId: Int, name: String) {
        capture(usuarioId, name).delete()
    }

    fun loadPhoto(usuarioId: Int): Bitmap? {
        val stored = photo(usuarioId)
        if (!stored.baseFile.exists() && !File(stored.baseFile.path + ".bak").exists()) return null
        return stored.openRead().use { BitmapFactory.decodeStream(it) }
            ?: throw IOException("La foto guardada no es válida")
    }

    /** Una cancelación o imagen inválida nunca reemplaza la foto anterior. Llamar desde IO. */
    fun finishCapture(usuarioId: Int, name: String, accepted: Boolean): Bitmap? {
        val source = capture(usuarioId, name)
        try {
            if (!accepted) return null
            val bitmap = decodePhoto(source)
            try {
                if (!photos.isDirectory && !photos.mkdirs()) throw IOException("No se pudo guardar la foto")
                val target = photo(usuarioId)
                val output = target.startWrite()
                try {
                    if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 88, output)) {
                        throw IOException("No se pudo codificar la foto")
                    }
                    target.finishWrite(output)
                } catch (e: Exception) {
                    target.failWrite(output)
                    throw e
                }
                return bitmap
            } catch (e: Exception) {
                bitmap.recycle()
                throw e
            }
        } finally {
            source.delete()
        }
    }

    private fun decodePhoto(source: File): Bitmap {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(source.path, bounds)
        if (bounds.outWidth <= 0 || bounds.outHeight <= 0) throw IOException("Captura vacía o inválida")
        var sample = 1
        while (maxOf(bounds.outWidth, bounds.outHeight) / sample > 1024) sample *= 2
        val decoded = BitmapFactory.decodeFile(source.path, BitmapFactory.Options().apply { inSampleSize = sample })
            ?: throw IOException("No se pudo leer la captura")
        try {
            val exif = ExifInterface(source.path)
            val matrix = Matrix().apply {
                when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
                    ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> setScale(-1f, 1f)
                    ExifInterface.ORIENTATION_ROTATE_180 -> setRotate(180f)
                    ExifInterface.ORIENTATION_FLIP_VERTICAL -> setScale(1f, -1f)
                    ExifInterface.ORIENTATION_TRANSPOSE -> { setRotate(90f); postScale(-1f, 1f) }
                    ExifInterface.ORIENTATION_ROTATE_90 -> setRotate(90f)
                    ExifInterface.ORIENTATION_TRANSVERSE -> { setRotate(-90f); postScale(-1f, 1f) }
                    ExifInterface.ORIENTATION_ROTATE_270 -> setRotate(-90f)
                }
            }
            val oriented = Bitmap.createBitmap(decoded, 0, 0, decoded.width, decoded.height, matrix, true)
            if (oriented !== decoded) decoded.recycle()
            // Al recodificar no se copian metadatos de ubicación de la cámara.
            return oriented
        } catch (e: Exception) {
            decoded.recycle()
            throw e
        }
    }
}
