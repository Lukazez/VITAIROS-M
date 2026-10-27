package cl.duoc.vitairosm.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlin.jvm.JvmSuppressWildcards

@Dao
interface UsuarioDao {
    @JvmSuppressWildcards
    @Query("SELECT * FROM usuarios WHERE correo = :correo")
    suspend fun buscarPorCorreo(correo: String): UsuarioEntity?

    @JvmSuppressWildcards
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun registrarUsuario(usuario: UsuarioEntity): Long

    @JvmSuppressWildcards
    @Query("SELECT * FROM usuarios WHERE correo = :correo AND contrasena = :contrasena")
    suspend fun iniciarSesion(correo: String, contrasena: String): UsuarioEntity?
}