package slzjandroid.slzjapplication.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by xuyifei on 16/5/24.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    //类没有实例化,是不能用作父类构造器的参数,必须声明为静态

    private static final String DBNAME = "xingyun.db"; //数据库名称
    public static String TABLE_USER = "xingyun_user";
    public static String HOT_CITY_TAB_NAME = "hot_city_table";
    public static String CITY_TAB_NAME = "city_table";
    private static final int VERSION = 2; //数据库版本


    public DataBaseHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLanguage.createTableUser(TABLE_USER));
        db.execSQL(SQLanguage.createHotCityTable(HOT_CITY_TAB_NAME));
        db.execSQL(SQLanguage.createCityTable(CITY_TAB_NAME));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion == 2) {
            db.execSQL(SQLanguage.createTableUser(TABLE_USER));
            db.execSQL(SQLanguage.createHotCityTable(HOT_CITY_TAB_NAME));
            db.execSQL(SQLanguage.createCityTable(CITY_TAB_NAME));
        }
    }
}
