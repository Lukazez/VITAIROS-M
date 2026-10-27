package cl.duoc.vitairosm.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [UsuarioEntity::class, RegistroEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun registroDao(): RegistroDao

    companion object {
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Conserva los registros antiguos sin inventar su propietario.
                db.execSQL("ALTER TABLE registros_emocionales ADD COLUMN usuarioId INTEGER")
            }
        }

        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext, AppDatabase::class.java, "vitairos_db"
            ).addMigrations(MIGRATION_2_3).build().also { instance = it }
        }
    }
}
