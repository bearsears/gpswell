package com.example.ssj.gpswell;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ssj on 08/02/18.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
    private static String DB_PATH = "";
    private static final String DB_NAME = "gpsWell.sqlite";
    private SQLiteDatabase myDatabase;
    private final Context myContext;

    /* Constructor
     * Takes and keeps a reference of the passed context in order to access
     * to the application assets and resources @param context
     */

     public DataBaseHelper(Context context) {
         super(context, DB_NAME, null, 1);
         //DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
         DB_PATH = context.getDatabasePath(DB_NAME).getPath();

         /*
         if (android.os.Build.VERSION.SDK_INT >= 17) {
             DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
         } else {
             DB_PATH = "/data/data" + context.getPackageName() + "/databases/";
         }
         */

         this.myContext = context;

     }


     /* Creates a empty database on the system and rewrites it with
      * with your own database
      */

     public void createDatabase() throws IOException {
            boolean dbExist = checkDataBase();

            if (dbExist) {
                // do nothing - database already exist
            } else {
                // by calling this method and empty database will be
                // created into the default system path of your application
                // so we are gonna be able to overwrite that database with our
                // database.
                try {
                    copyDataBase();
                } catch (IOException e) {
                    throw new Error("Error copying database");
                }
            }

     }

    /**
     * Check if the database already exist to avoid re-coping the file each tim you
     * open the application. @return true if it exists, false if it doesn't
     */

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;

        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(DB_PATH, null,
                    SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            //Database doesn't exist yet.
        }

        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null;
    }

    /** Copies your database from your local assets-folder to the just created
     *  empty database in the systems folder, from where it can be accessed
     *  and handled. This is done by transferring bytestream.
     */

    private void copyDataBase() throws IOException {
        // Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db;
        String outFileName = DB_PATH + DB_NAME;

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(DB_PATH);

        //transfer bytes from the input file to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException {
        // Open the database
        String myPath = DB_PATH + DB_NAME;
        myDatabase = SQLiteDatabase.openDatabase(DB_PATH, null,
                SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close() {
        if (myDatabase != null)
            myDatabase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {

    }

        // add your public helper methods to access and get content from the database.
        // you could return cursors by doing "return myDataBase.query(...)" so it'd
        // be easy

    public List<Welldata> getWelldata() {
        List<Welldata> welldata = new ArrayList<>();

        try {
            //get all rows from Welldata
            String query = "SELECT * FROM Wells";
            SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH, null,
                    SQLiteDatabase.OPEN_READONLY);
            Cursor cursor = db.rawQuery(query, null);

            String licence, status;
            String surface, uwi;
            double blat, blong;

            while (cursor.moveToNext()) {

                licence = cursor.getString(0);
                status = cursor.getString(1);
                surface = cursor.getString(2);
                uwi = cursor.getString(3);
                blat = cursor.getDouble(4);
                blong = cursor.getDouble(5);

                Welldata welldata1 = new Welldata(licence, status,
                        surface, uwi, blat, blong);

                welldata.add(welldata1);
                }

            cursor.close();

            } catch(Exception e) {
                Log.d("DB", e.getMessage());
            }

        return welldata;
    }

}


