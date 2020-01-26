package com.zainal.notes.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.zainal.notes.db.DatabaseContract.NoteColumns.Companion.DATE
import com.zainal.notes.db.DatabaseContract.NoteColumns.Companion.DESCRIPTION
import com.zainal.notes.db.DatabaseContract.NoteColumns.Companion.TABLE_NAME
import com.zainal.notes.db.DatabaseContract.NoteColumns.Companion.TITLE
import com.zainal.notes.db.DatabaseContract.NoteColumns.Companion._ID
import com.zainal.notes.entity.Note
import java.sql.SQLException

class NoteHelper(context: Context) {

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var dataBaseHelper: DatabaseHelper
        private var INSTANCE: NoteHelper? = null
        private lateinit var database: SQLiteDatabase

        fun getInstance(context: Context): NoteHelper {
            if (INSTANCE == null) {
                synchronized(SQLiteOpenHelper::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = NoteHelper(context)
                    }
                }
            }
            return INSTANCE as NoteHelper
        }
    }

    init {
        dataBaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun close() {
        dataBaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    /**
     * get all note data from database
     *
     * @return cursor from queryAll
     */
    fun queryAll(): Cursor {
        return database.query(DATABASE_TABLE,null,null,null,null,null,"$_ID ASC")
    }

    /**
     * get note data base on parameter id
     *
     * @param id id note from search
     * @return cursor result queryAll
     */
    fun queryById(id: String): Cursor {
        return database.query(DATABASE_TABLE, null,"$_ID = ?", arrayOf(id), null, null, null, null)
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$_ID  = ?", arrayOf(id))
    }

    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$_ID = '$id'", null)
    }

    fun getAllNotes(): ArrayList<Note> {
        val arrayList = ArrayList<Note>()
        val cursor = database.query(DATABASE_TABLE, null, null, null, null, null, "$_ID ASC", null)
        cursor.moveToFirst()
        var note: Note
        if (cursor.count > 0) {
            do {
                note = Note()
                note.id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID))
                note.title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE))
                note.description = cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION))
                note.date = cursor.getString(cursor.getColumnIndexOrThrow(DATE))
            } while (!cursor.isAfterLast)
        }
        cursor.close()
        return arrayList
    }

    fun insertNote(note: Note): Long {
        val args = ContentValues()
        args.put(TITLE, note.title)
        args.put(DESCRIPTION, note.description)
        args.put(DATE, note.date)
        return database.insert(DATABASE_TABLE, null, args)
    }

    fun updateNote(note: Note): Int {
        val args = ContentValues()
        args.put(TITLE, note.title)
        args.put(DESCRIPTION, note.description)
        args.put(DATE, note.date)
        return database.update(DATABASE_TABLE, args, _ID  + "= '" + note.id + "'", null)
    }

    fun deleteNote(id: Int): Int {
        return database.delete(TABLE_NAME, "_ID = '$id'", null)
    }
}