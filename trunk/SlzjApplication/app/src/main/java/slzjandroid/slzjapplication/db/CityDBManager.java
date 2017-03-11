package slzjandroid.slzjapplication.db;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import slzjandroid.slzjapplication.dto.Hotvalue;
import slzjandroid.slzjapplication.dto.Value;


public class CityDBManager {
    private DataBaseHelper helper;
    private SQLiteDatabase db;

    public CityDBManager(Context context) {
        helper = new DataBaseHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    /**
     * add values
     *
     * @param values
     */
    public void add(List<Value> values) {
        db.beginTransaction();    //开始事务
        try {
            for (Value value : values) {
                db.execSQL("INSERT INTO city_table VALUES(null, ?, ?, ?)", new Object[]{value.getName(), value.getCityId(), value.getPinyin()});
            }
            db.setTransactionSuccessful();    //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    /**
     * add values
     *
     * @param values
     */
    public void addHotcity(List<Hotvalue> values) {
        db.beginTransaction();    //开始事务
        try {
            for (Hotvalue value : values) {
                db.execSQL("INSERT INTO hot_city_table VALUES(null, ?, ?)", new Object[]{value.getName(), value.getCityId()});
            }
            db.setTransactionSuccessful();    //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    public Value queryCityCode(String cityName) {
        Value value = new Value();
        Cursor cursor = db.query("city_table", new String[]{"city_name", "cityId", "pinyin"}, "city_name=?", new String[]{cityName}, null, null, null);
        while (cursor.moveToNext()) {
            value.setName(cursor.getString(cursor.getColumnIndex("city_name")));
            value.setCityId(cursor.getString(cursor.getColumnIndex("cityId")));
            value.setPinyin(cursor.getString(cursor.getColumnIndex("pinyin")));
        }
        return value;
    }

    /**
     * query all persons, return list
     *
     * @return List<Person>
     */
    public List<Value> query() {
        ArrayList<Value> values = new ArrayList<Value>();
        Cursor c = queryTheCursor();
        while (c.moveToNext()) {
            Value value = new Value();
            value.setName(c.getString(c.getColumnIndex("city_name")));
            value.setCityId(c.getString(c.getColumnIndex("cityId")));
            value.setPinyin(c.getString(c.getColumnIndex("pinyin")));
            values.add(value);
        }
        c.close();
        return values;
    }

    public List<Hotvalue> queryHotCity() {
        ArrayList<Hotvalue> values = new ArrayList<Hotvalue>();
        Cursor c = queryHotTheCursor();
        while (c.moveToNext()) {
            Hotvalue value = new Hotvalue();
            value.setName(c.getString(c.getColumnIndex("city_name")));
            value.setCityId(c.getString(c.getColumnIndex("cityId")));
            values.add(value);
        }
        c.close();
        return values;
    }

    /**
     * query all persons, return cursor
     *
     * @return Cursor
     */
    public Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM city_table", null);
        return c;
    }

    /**
     * query all persons, return cursor
     *
     * @return Cursor
     */
    public Cursor queryHotTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM hot_city_table", null);
        return c;
    }

    /**
     * close database
     */
    public void closeDB() {
        db.close();
    }

    public void deleteDB() {
        db.delete("hot_city_table", null, null);
        db.delete("city_table", null, null);
    }
}
