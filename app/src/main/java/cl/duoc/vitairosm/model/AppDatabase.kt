package cl.duoc.vitairosm.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [UsuarioEntity::class, RegistroEntity::class], version = 4, exportSchema = false)
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

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                val accounts = mutableListOf<Pair<Int, String>>()
                db.query("SELECT id, contrasena FROM usuarios").use { cursor ->
                    while (cursor.moveToNext()) accounts += cursor.getInt(0) to cursor.getString(1)
                }
                accounts.forEach { (id, password) ->
                    db.execSQL("UPDATE usuarios SET contrasena = ? WHERE id = ?",
                        arrayOf<Any>(PasswordHasher.hash(password), id))
                }
            }
        }

        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext, AppDatabase::class.java, "vitairos_db"
            ).addMigrations(MIGRATION_2_3, MIGRATION_3_4).build().also { instance = it }
        }
    }
}
