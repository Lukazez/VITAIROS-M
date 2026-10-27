package cl.duoc.vitairosm.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "registros_emocionales")
data class RegistroEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val animo: String,
    val categoria: String,
    val nota: String,
    val fechaHora: Long = System.currentTimeMillis(),
    val usuarioId: Int? = null
)