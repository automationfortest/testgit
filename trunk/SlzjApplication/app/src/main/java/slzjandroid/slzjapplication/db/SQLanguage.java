package slzjandroid.slzjapplication.db;

/**
 * Created by xuyifei on 16/05/23
 */
public class SQLanguage {

    /**
     * 创建user数据表
     *
     * @param tableName 数据表名字
     * @return
     */
    public static String createTableUser(String tableName) {

        return "CREATE TABLE " + tableName + "(" +
                "_id integer  PRIMARY KEY AUTOINCREMENT DEFAULT NULL," +
                "userId  VARCHAR(100) ," +
                "loginName VARCHAR(100) ," +
                "cellphone VARCHAR(100) ," +
                "accessToken VARCHAR(100) ," +
                "userType VARCHAR(100) ," +
                "balance VARCHAR(100) ," +
                "enterpriseInfo VARCHAR(1000) );";
    }

    public static String createHotCityTable(String tableName) {
        return "create table " + tableName + " (" +
                "id integer primary key autoincrement," +
                "city_name  varchar(30)," +
                "cityId varchar(10));";

    }


    public static String createCityTable(String tableName) {
        return "create table " + tableName + " (" +
                "id  integer primary key autoincrement," +
                "city_name  varchar(30)," +
                "cityId  varchar(10)," +
                "pinyin   varchar(50));";
    }


    public static String clearTableUser(String tableName) {
        return "delete from" + tableName;
    }

    public static String table_master = "select count(*) from sqlite_master where " +
            "type=\\'table\\' and name=\\'%1$s\\';";
}
