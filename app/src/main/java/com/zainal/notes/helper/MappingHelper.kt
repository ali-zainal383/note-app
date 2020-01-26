package com.zainal.notes.helper

import android.database.Cursor
import com.zainal.notes.db.DatabaseContract.NoteColumns.Companion.DATE
import com.zainal.notes.db.DatabaseContract.NoteColumns.Companion.DESCRIPTION
import com.zainal.notes.db.DatabaseContract.NoteColumns.Companion.TITLE
import com.zainal.notes.db.DatabaseContract.NoteColumns.Companion._ID
import com.zainal.notes.entity.Note

object MappingHelper {
    fun mapCursorToArrayList(notesCursor: Cursor): ArrayList<Note> {
        val notesList = ArrayList<Note>()
        notesCursor.moveToFirst()
        while (notesCursor.moveToNext()) {
            val id = notesCursor.getInt(notesCursor.getColumnIndexOrThrow(_ID))
            val title = notesCursor.getString(notesCursor.getColumnIndexOrThrow(TITLE))
            val description = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DESCRIPTION))
            val date = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DATE))
            notesList.add(Note(id, title, description, date))
        }
        return notesList
    }
}