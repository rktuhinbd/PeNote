package com.rkt.penote.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rkt.penote.data.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for accessing the [NoteEntity] table.
 */
@Dao
interface NoteDao {

    /**
     * get all notes
     */
    @Query("SELECT * FROM note")
    fun getNotes(): Flow<List<NoteEntity>>

    /**
     * get note by id
     */
    @Query("SELECT * FROM note WHERE id = :id")
    suspend fun getNoteById(id: Int): NoteEntity?

    /**
     * insert note
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity)

    /**
     * delete note
     */
    @Delete
    suspend fun deleteNote(note: NoteEntity)
}
