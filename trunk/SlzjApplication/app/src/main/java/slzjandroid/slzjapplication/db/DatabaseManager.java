package slzjandroid.slzjapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.concurrent.atomic.AtomicInteger;

import slzjandroid.slzjapplication.utils.CommonUtils;

/**
 * Created by xuyifei on 16/05/23
 */
public class DatabaseManager {
    private static DataBaseHelper helper;
    private static SQLiteDatabase database;
    private static AtomicInteger mAutomicOpenCount = new AtomicInteger();

    public DatabaseManager(Context context) {
        init(context);
    }

    private synchronized void init(Context context) {
        if (helper == null) {
            helper = new DataBaseHelper(context);
        }
    }

    private synchronized SQLiteDatabase openDatabase() {
        mAutomicOpenCount.incrementAndGet();

        if (database == null) {
            database = helper.getWritableDatabase();
        }
        return database;
    }

    protected synchronized void closeDB() {
        if (database != null && mAutomicOpenCount.decrementAndGet() == 0) {
            helper.close();
            database = null;
        }
    }

    public long insert(String table, String nullColumnHack, ContentValues values) {
        openDatabase();
        long result = database.insert(table, nullColumnHack, values);
        closeDB();

        return result;
    }

    public int delete(String table, String whereClause, String[] whereArgs) {
        openDatabase();
        int result = database.delete(table, whereClause, whereArgs);
        closeDB();

        return result;
    }

    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        openDatabase();
        int result = database.update(table, values, whereClause, whereArgs);
        closeDB();

        return result;
    }

    public Cursor query(String table, String[] columns, String selection,
                        String[] selectionArgs, String groupBy, String having,
                        String orderBy) {
        openDatabase();
        return database.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    public Cursor query(String table, String[] columns, String selection,
                        String[] selectionArgs, String groupBy, String having,
                        String orderBy, String limit) {
        openDatabase();
        return database.query(table, columns, selection, selectionArgs, groupBy, having, orderBy,
                limit);
    }

    protected boolean isTableExists(String tableName) {
        boolean result = false;
        String checkSql = SQLanguage.table_master;
        checkSql = String.format(checkSql, tableName);
        Cursor cursor = openDatabase().rawQuery(checkSql, null);
        if (cursor.moveToNext() && cursor.getInt(0) == 1) {
            result = true;
        }
        cursor.close();
        closeDB();

        return result;
    }

    protected String extractStringValueForColumn(Cursor cursor, String columnName) {
        int index = cursor.getColumnIndex(columnName);
        if (index != -1) {
            return cursor.getString(index);
        }
        return "";
    }

    protected long extractLongValueForColumn(Cursor cursor, String columnName) {
        int index = cursor.getColumnIndex(columnName);
        if (index != -1) {
            return cursor.getLong(index);
        }
        return 0L;
    }

    protected int extractIntegerValueForColumn(Cursor cursor, String columnName) {
        int index = cursor.getColumnIndex(columnName);
        if (index != -1) {
            return cursor.getInt(index);
        }
        return 0;
    }

    protected boolean extractBooleanValueForColumn(Cursor cursor, String columnName) {
        int index = cursor.getColumnIndex(columnName);
        if (index == -1) return false;
        return CommonUtils.intToBool(cursor.getInt(index));
    }

    protected float extractFloatValueForColumn(Cursor cursor, String columnName) {
        int index = cursor.getColumnIndex(columnName);
        if (index != -1) {
            return cursor.getFloat(index);
        }
        return 0f;
    }
}
