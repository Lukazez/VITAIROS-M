package cl.duoc.vitairosm
import cl.duoc.vitairosm.viewmodel.CheckInValidation
import cl.duoc.vitairosm.model.PasswordHasher
import org.junit.Assert.*
import org.junit.Test

class CompletionTest {
    @Test fun validaLimitesYSelecciones() {
        assertNotNull(CheckInValidation.animo(""))
        assertNotNull(CheckInValidation.animo("inventado"))
        CheckInValidation.animos.forEach { assertNull(CheckInValidation.animo(it)) }
        assertNull(CheckInValidation.nota("x".repeat(200)))
        assertNotNull(CheckInValidation.nota("x".repeat(201)))
        assertNull(CheckInValidation.categoria(""))
        assertNotNull(CheckInValidation.categoria("inventada"))
    }
    @Test fun clavesTienenSalDiferenteYNoSeComparanComoTexto() {
        val hash = PasswordHasher.hash("clave123")
        assertNotEquals(hash, PasswordHasher.hash("clave123"))
        assertTrue(PasswordHasher.verify("clave123", hash))
        assertFalse(PasswordHasher.verify("otra", hash))
        assertFalse(PasswordHasher.verify("clave123", "clave123"))
        assertFalse(PasswordHasher.verify("clave123", listOf("pbkdf2-sha256", "600000", "incorrecto", "incorrecto").joinToString("$")))
    }
}
