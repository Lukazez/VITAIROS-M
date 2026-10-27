package cl.duoc.vitairosm.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RegistroDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun agregarRegistro(registro: RegistroEntity): Long

    @Query("SELECT * FROM registros_emocionales WHERE usuarioId = :usuarioId ORDER BY fechaHora DESC")
    fun obtenerHistorial(usuarioId: Int): Flow<List<RegistroEntity>>

    @Query("DELETE FROM registros_emocionales WHERE id = :registroId AND usuarioId = :usuarioId")
    fun eliminarRegistro(registroId: Int, usuarioId: Int): Int
}