package cl.duoc.vitairosm.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/** Mantiene la comprobación de credenciales fuera de la interfaz y del DAO. */
class UsuarioRepository(private val dao: UsuarioDao) {
    suspend fun iniciarSesion(correo: String, contrasena: String): UsuarioEntity? {
        val usuario = dao.buscarPorCorreo(correo) ?: return null
        return withContext(Dispatchers.Default) {
            usuario.takeIf { PasswordHasher.verify(contrasena, it.contrasena) }
        }
    }
}
