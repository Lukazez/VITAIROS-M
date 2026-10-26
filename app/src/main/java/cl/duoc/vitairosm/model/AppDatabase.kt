package cl.duoc.vitairosm.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [UsuarioEntity::class, RegistroEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun registroDao(): RegistroDao
}