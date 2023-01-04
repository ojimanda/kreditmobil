package id.kelompok7.kreditmobil.config;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(@Nullable Context context) {
        super(context, "kreditmobil.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("CREATE TABLE history (historyId TEXT PRIMARY KEY,username TEXT NOT NULL, brand TEXT NOT NULL, merk TEXT NOT NULL, tenor TEXT NOT NULL, cicilan TEXT NOT NULL, dp TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("DROP TABLE IF EXISTS history");
    }

    public boolean insertHistory(String historyId, String username, String brand, String merk,String dp, String tenor,String cicilan) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("historyId", historyId);
        contentValues.put("username", username);
        contentValues.put("brand", brand);
        contentValues.put("dp", dp);
        contentValues.put("merk", merk);
        contentValues.put("tenor", tenor);
        contentValues.put("cicilan", cicilan);

        long res = DB.insert("history", null, contentValues);
        return res != -1;
    }

//    public boolean updateUserData(String text) {
//        SQLiteDatabase DB = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//
//        contentValues.put("text", text);
//
//        Cursor cursor = DB.rawQuery("SELECT * FROM siswa WHERE username=?", new String[]{username});
//        if (cursor.getCount() > 0) {
//            long res = DB.update("siswa", contentValues, "username=?", new String[]{username});
//            return res != -1;
//        } else {
//            return false;
//        }
//    }

    public boolean deleteHistory(String historyId) {
        SQLiteDatabase DB = this.getWritableDatabase();

        Cursor cursor = DB.rawQuery("SELECT * FROM history WHERE historyId=?", new String[]{historyId});
        if (cursor.getCount() > 0) {
            long res = DB.delete("blockWord", "text=?", new String[]{historyId});
            return res != -1;
        } else {
            return false;
        }
    }

    public Cursor getHistories() {
        SQLiteDatabase DB = this.getWritableDatabase();

        Cursor cursor = DB.rawQuery("SELECT * FROM history", null);

        return cursor;
    }

    public Cursor getOneHistory(String historyId) {

        SQLiteDatabase DB = this.getWritableDatabase();

        Cursor cursor = DB.rawQuery("SELECT * FROM history WHERE historyId=?", null);

        return cursor;
    }
}
