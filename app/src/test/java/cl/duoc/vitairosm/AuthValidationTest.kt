package cl.duoc.vitairosm

import cl.duoc.vitairosm.viewmodel.AuthValidation
import org.junit.Assert.*
import org.junit.Test

class AuthValidationTest {
    @Test fun registroRechazaClavesCortasYEspaciosDeRelleno() {
        listOf("", " ", "a", "1234567", " a       ").forEach {
            assertNotNull(AuthValidation.contrasena(it, true))
        }
        assertNull(AuthValidation.contrasena("12345678", true))
    }

    @Test fun permiteAccederACuentasAnteriores() {
        assertNull(AuthValidation.contrasena("a", false))
        assertNotNull(AuthValidation.contrasena(" ", false))
    }

    @Test fun validaCamposDeRegistro() {
        assertNotNull(AuthValidation.nombre(" "))
        assertNull(AuthValidation.nombre("Ana"))
        listOf("", "ana", "ana@", "@correo.cl", "ana correo@dominio.cl").forEach {
            assertNotNull(AuthValidation.correo(it))
        }
        assertNull(AuthValidation.correo(" ana@ejemplo.cl "))
    }
}
