import android.app.DownloadManager
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.mindpalais.android.model.Note
import com.mindpalais.android.model.ResponseModel
import java.io.FileOutputStream

class DatabaseHelper(private val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "test.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_NAME = "responses"
        private const val COLUMN_ID = "id"
        private const val COLUMN_CREATED = "created"
        private const val COLUMN_MESSAGE = "message"
        private const val COLUMN_ACTION = "action"
        private const val COLUMN_PEOPLE = "people"
        private const val COLUMN_STATUS = "status"
        private const val COLUMN_DEADLINE = "deadline"

        // New table constants
        // New table constants
        private const val TABLE_NOTES = "notes"
        private const val COLUMN_NOTE_ID = "id"
        private const val COLUMN_NOTE_CREATED_AT = "createdAt"
        private const val COLUMN_NOTE_NOTE = "note"
        private const val COLUMN_NOTE_TITLE = "title"
        private const val COLUMN_NOTE_GENERATED = "generated"
        private const val COLUMN_NOTE_WORDCLOUD = "wordcloud"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE IF NOT EXISTS $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_CREATED TEXT, $COLUMN_MESSAGE TEXT, `$COLUMN_ACTION` TEXT, $COLUMN_PEOPLE TEXT, $COLUMN_STATUS TEXT, $COLUMN_DEADLINE TEXT)"
        db.execSQL(createTableQuery)

        val createNotesTableQuery = "CREATE TABLE IF NOT EXISTS $TABLE_NOTES ($COLUMN_NOTE_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_NOTE_CREATED_AT TEXT, $COLUMN_NOTE_NOTE TEXT, $COLUMN_NOTE_TITLE TEXT, $COLUMN_NOTE_GENERATED TEXT, $COLUMN_NOTE_WORDCLOUD TEXT)"
        db.execSQL(createNotesTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This method will not be called, as we're not handling schema upgrades in this example
    }

    override fun getWritableDatabase(): SQLiteDatabase {
        val dbFile = context.getDatabasePath(DATABASE_NAME)
        if (!dbFile.exists()) {
            // Create the database file if it doesn't exist
            dbFile.createNewFile()

            // Copy the database file from assets
            copyDatabaseFromAssets(dbFile.absolutePath)
        }
        return super.getWritableDatabase()
    }

    private fun copyDatabaseFromAssets(dbFile: String) {
        // Open the asset file
        val inputStream = context.assets.open(DATABASE_NAME)

        // Create a new output stream to write to the database file
        val outputStream = FileOutputStream(dbFile)

        // Copy the data from the asset file to the output stream
        inputStream.copyTo(outputStream)

        // Flush and close the output stream
        outputStream.flush()
        outputStream.close()

        // Close the input stream
        inputStream.close()
    }

    fun readResponsesFromDatabase(): List<ResponseModel> {
        val responses = mutableListOf<ResponseModel>()

        // Define the columns to select
        val columns = arrayOf(
            COLUMN_CREATED,
            COLUMN_MESSAGE,
            COLUMN_ACTION,
            COLUMN_PEOPLE,
            COLUMN_STATUS,
            COLUMN_DEADLINE
        )

        // Query the database to get the responses data
        val cursor = readableDatabase.query(
            TABLE_NAME,
            columns,
            null,
            null,
            null,
            null,
            null
        )

        // Iterate through the cursor to build the list of ResponseModel objects
        with(cursor) {
            while (moveToNext()) {
                val id = getString(getColumnIndexOrThrow(COLUMN_ID))
                val created = getString(getColumnIndexOrThrow(COLUMN_CREATED))
                val message = getString(getColumnIndexOrThrow(COLUMN_MESSAGE))
                val action = getString(getColumnIndexOrThrow(COLUMN_ACTION))
                val peopleString = getString(getColumnIndexOrThrow(COLUMN_PEOPLE))
                val people = peopleString.split(",").toTypedArray()
                val status = getString(getColumnIndexOrThrow(COLUMN_STATUS))
                val deadline = getString(getColumnIndexOrThrow(COLUMN_DEADLINE))

                val response = ResponseModel(id,created, message, action, people, status, deadline)
                responses.add(response)
            }
        }

        // Close the cursor and return the responses list
        cursor.close()
        return responses
    }

/*    fun getResponses(): Flow<List<ResponseModel>> {
        return flow {
            val cursor = readableDatabase.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
            )

            val responses = mutableListOf<ResponseModel>()
            with(cursor) {
                while (moveToNext()) {
                    val created = getString(getColumnIndexOrThrow(COLUMN_CREATED))
                    val message = getString(getColumnIndexOrThrow(COLUMN_MESSAGE))
                    val action = getString(getColumnIndexOrThrow(COLUMN_ACTION))
                    val peopleString = getString(getColumnIndexOrThrow(COLUMN_PEOPLE))
                    val people = peopleString.split(",").toTypedArray()
                    val status = getString(getColumnIndexOrThrow(COLUMN_STATUS))
                    val deadline = getString(getColumnIndexOrThrow(COLUMN_DEADLINE))
                    responses.add(ResponseModel(created, message, action, people, status, deadline))
                }
            }
            cursor.close()

            emit(responses)
        }.flowOn(Dispatchers.IO)
    }*/

    fun getResponses(): List<ResponseModel> {
        val cursor = readableDatabase.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )
        return cursorToList(cursor)
    }

    private fun cursorToList(cursor: Cursor): List<ResponseModel> {
        val responses = mutableListOf<ResponseModel>()
        with(cursor) {
            while (moveToNext()) {
                val id = getString(getColumnIndexOrThrow(COLUMN_ID))
                val created = getString(getColumnIndexOrThrow(COLUMN_CREATED))
                val message = getString(getColumnIndexOrThrow(COLUMN_MESSAGE))
                val action = getString(getColumnIndexOrThrow(COLUMN_ACTION))
                val peopleString = getString(getColumnIndexOrThrow(COLUMN_PEOPLE))
                val people = peopleString.split(",").toTypedArray()
                val status = getString(getColumnIndexOrThrow(COLUMN_STATUS))
                val deadline = getString(getColumnIndexOrThrow(COLUMN_DEADLINE))
                responses.add(ResponseModel(id,created, message, action, people, status, deadline))
            }
        }
        cursor.close()
        return responses
    }



    fun insertResponseToDatabase(response: ResponseModel): Boolean {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(COLUMN_CREATED, response.created)
        values.put(COLUMN_MESSAGE, response.message)
        values.put(COLUMN_ACTION, response.action)
        values.put(COLUMN_PEOPLE, response.people.joinToString(separator = ","))
        values.put(COLUMN_STATUS, response.status)
        values.put(COLUMN_DEADLINE, response.date)

        val isSuccess = db.insert(TABLE_NAME, null, values) != -1L

        db.close()

        return isSuccess
    }

    fun updateResponseInDatabase(response: ResponseModel): Boolean {
        val contentValues = ContentValues().apply {
            put(COLUMN_MESSAGE, response.message)
            put(COLUMN_ACTION, response.action)
            put(COLUMN_PEOPLE, response.people.joinToString(","))
            put(DownloadManager.COLUMN_STATUS, response.status)
            put(COLUMN_DEADLINE, response.date)
        }
        val rowsAffected = writableDatabase.update(
            TABLE_NAME,
            contentValues,
            "$COLUMN_ID = ?",
            arrayOf(response.id.toString())
        )

        return rowsAffected > 0
    }

    fun deleteResponse(id: String): Boolean {
        val rowsDeleted = writableDatabase.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(id.toString()))
        return rowsDeleted > 0
    }


    fun insertNoteToDatabase(note: Note): Boolean {
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_NOTE_ID, note.id)
            put(COLUMN_NOTE_CREATED_AT, note.createdAt)
            put(COLUMN_NOTE_NOTE, note.note)
            put(COLUMN_NOTE_TITLE, note.title)
            put(COLUMN_NOTE_GENERATED, note.generated.joinToString(separator = ","))
            put(COLUMN_NOTE_WORDCLOUD, note.wordcloud.joinToString(separator = ","))
        }

        val isSuccess = db.insert(TABLE_NOTES, null, values) != -1L

        db.close()

        return isSuccess
    }

    fun getNotes(): List<Note> {
        val cursor = readableDatabase.query(
            TABLE_NOTES,
            null,
            null,
            null,
            null,
            null,
            null
        )
        return noteCursorToList(cursor)
    }

    private fun noteCursorToList(cursor: Cursor): List<Note> {
        val notes = mutableListOf<Note>()
        with(cursor) {
            while (moveToNext()) {
                val id = getString(getColumnIndexOrThrow(COLUMN_NOTE_ID))
                val created = getString(getColumnIndexOrThrow(COLUMN_NOTE_CREATED_AT))
                val note = getString(getColumnIndexOrThrow(COLUMN_NOTE_NOTE))
                val title = getString(getColumnIndexOrThrow(COLUMN_NOTE_TITLE))
                val generatedString = getString(getColumnIndexOrThrow(COLUMN_NOTE_GENERATED))
                val generated = generatedString.split(",").toTypedArray()
                val wordcloudString = getString(getColumnIndexOrThrow(COLUMN_NOTE_WORDCLOUD))
                val wordcloud = wordcloudString.split(",").toTypedArray()
                notes.add(Note(id,created, note, title, generated, wordcloud))
            }
        }
        cursor.close()
        return notes
    }

}
