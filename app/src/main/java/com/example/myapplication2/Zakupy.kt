package com.example.myapplication2
import android.content.Context
import androidx.room.*
import java.util.*

@Entity
data class Zakupy(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    var przedmiot:String?,
    var ok: Boolean?,
    var lista:Int?
)
@Entity
data class Lista(
    @PrimaryKey(autoGenerate = true) var id:Int=0,
    var nazwa:String?,
    var data: String?
)

@Dao
interface ZakupyDao {
    @Query( "SELECT * FROM Zakupy")
    fun getAll(): List<Zakupy>

    @Query( "SELECT * FROM Zakupy WHERE lista=:nrlisty") //wybieram tylko te elementy, które maja odpowiedni numer listy
    fun getzakupyzlisty(nrlisty:Int): List<Zakupy>

    @Insert
    fun insert(vararg zakup: Zakupy)

    @Delete
    fun delete(zakup: Zakupy)

    @Update
    fun done(vararg zakup: Zakupy)
}

@Dao
interface ListaDao {
    @Query( "SELECT * FROM Lista")
    fun getAll(): List<Lista>

    @Query( "SELECT * FROM Lista WHERE id=:nrlisty")
    fun getlistabynr(nrlisty:Int): List<Lista>

    @Query( "SELECT max(id) FROM Lista") //wybierz ostatnia liste element ze zbioru
    fun getlistamax(): Int

    @Insert
    fun insert(vararg pozycja: Lista)

    @Delete
    fun delete(pozycja: Lista)

    @Update
    fun done(vararg pozycja: Lista)
}


@Database(entities = [Zakupy::class , Lista::class] , version = 1)
abstract class ZakupyDB : RoomDatabase() {
    abstract fun ZakupyDao(): ZakupyDao
    abstract fun ListaDao(): ListaDao //dzieki temu podpowiadaja sie metody DAO

    companion object { //obiekt ktory nie jest usuwany, mozna sie do niego odwolywac z roznych czesci programu
        private var instance: ZakupyDB? = null //zmienna w ktorej bedziemy przechowywac baze danych

        fun getInstance(context: Context): ZakupyDB? {
            if (instance == null) { //jezeli to pierwsze wywolanie to zostaje otwarta baza danych
                synchronized(ZakupyDB::class) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                        ZakupyDB::class.java, "zakupy-bazadanych"  ).allowMainThreadQueries()
                        .build()
                }
            }
            return instance
        }
    }
}
