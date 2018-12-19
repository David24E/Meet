package io.github.david24e.CSC306MeetingApp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import io.github.david24e.CSC306MeetingApp.models.Meeting;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String DATABASE_NAME = "meetings.db";
    private static final String TABLE_NAME = "meetings_table";
    public static final String COL0 = "ID";
    public static final String COL1 = "TITLE";
    public static final String COL2 = "LOCATION";
    public static final String COL3 = "NOTES";
    public static final String COL4 = "DATE";
    public static final String COL5 = "TIME";
    public static final String COL6 = "ATTENDEE";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " +
                TABLE_NAME + " ( " +
                COL0 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL1 + " TEXT, " +
                COL2 + " TEXT, " +
                COL3 + " TEXT, " +
                COL4 + " TEXT, " +
                COL5 + " TEXT, " +
                COL6 + " TEXT )";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    /*
     * Insert a new meeting into the database
     * @param meeting
     * @return
     */
    public boolean addMeeting(Meeting meeting) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, meeting.getTitle());
        contentValues.put(COL2, meeting.getLocation());
        contentValues.put(COL3, meeting.getNotes());
        contentValues.put(COL4, meeting.getDate());
        contentValues.put(COL5, meeting.getTime());
        contentValues.put(COL6, meeting.getTime());

        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /*
     * Retrieve all meetings from database
     * @return
     */
    public Cursor getAllMeetings(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    /*
     * Update a meeting where id = @param 'id'
     * Replace the current meeting with @param 'meeting'
     * @param meeting
     * @param id
     * @return
     */
    public boolean updateMeeting(Meeting meeting, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, meeting.getTitle());
        contentValues.put(COL2, meeting.getLocation());
        contentValues.put(COL3, meeting.getNotes());
        contentValues.put(COL4, meeting.getDate());
        contentValues.put(COL5, meeting.getTime());
        contentValues.put(COL6, meeting.getTime());

        int update = db.update(TABLE_NAME, contentValues, COL0 + " = ? ", new String[] {String.valueOf(id)} );

        if(update != 1) {
            return false;
        }
        else{
            return true;
        }
    }

    /*
     * Retrieve the meeting unique id from the database using @param
     * @param meeting
     * @return
     */
    public Cursor getMeetingID(Meeting meeting){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME  +
                " WHERE " + COL1 + " = '" + meeting.getTitle() + "'" +
                " AND " + COL2 + " = '" + meeting.getLocation() + "'";
        return db.rawQuery(sql, null);
    }

    public Integer deleteMeeting(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[] {String.valueOf(id)});
    }


}
