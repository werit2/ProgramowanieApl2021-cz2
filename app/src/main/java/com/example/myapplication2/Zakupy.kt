package com.example.myapplication2
import androidx.room.*

@Entity
data class Zakupy(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    val przedmiot:String?,
    val ok: Boolean?
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
}