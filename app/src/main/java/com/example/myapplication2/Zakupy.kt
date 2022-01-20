package com.example.myapplication2
import android.content.Context
import androidx.room.*

@Entity
data class Zakupy(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    var przedmiot:String?,
    var ok: Boolean?
)
@Dao
interface ZakupyDao {
    @Query( "SELECT * FROM Zakupy")
    fun getAll(): List<Zakupy>

    @Insert
    fun insert(vararg zakup: Zakupy)

    @Delete
    fun delete(zakup: Zakupy)

    @Update
    fun done(vararg zakup: Zakupy)
}

@Database(entities = [Zakupy::class], version = 1)
abstract class ZakupyDB : RoomDatabase() {
    abstract fun ZakupyDao(): ZakupyDao
    companion object { //obiekt ktory nie jest usuwany, mozna sie do niego odwolywac z roznych czesci programu
        private var instance: ZakupyDB? = null //zmienna w ktorej bedziemy przechowywac baze danych

        fun getInstance(context: Context): ZakupyDB? {
            if (instance == null) { //jezeli to pierwsze wywolanie to zostaje otwarta baza danych
                synchronized(ZakupyDB::class) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                        ZakupyDB::class.java, "zakupy-database"  ).allowMainThreadQueries()
                        .build()
                }
            }
            return instance
        }
    }
}