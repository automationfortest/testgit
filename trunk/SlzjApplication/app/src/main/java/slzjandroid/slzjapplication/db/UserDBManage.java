package slzjandroid.slzjapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import slzjandroid.slzjapplication.dto.LoginUser;

/**
 * Created by xuyifei on 16/5/24.
 */
public class UserDBManage extends DatabaseManager {
    String tableName = DataBaseHelper.TABLE_USER;
    private static LoginUser currentUser;


    public UserDBManage(Context context) {
        super(context);
    }

    public long insertUser(LoginUser user) {
        if (user == null) {
            return -1;
        }
        long rowId = insert(tableName, null, user.toContentValues());
        currentUser = null;
        return rowId;
    }

    public synchronized LoginUser findUser(String userId) {
        if (currentUser == null) {
            Cursor cursor = query(tableName, UserColumns._all, UserColumns.userId + "=?", new String[]{userId}, null, null, null);
            if (cursor.moveToFirst()) {
                currentUser = extractUserFromCursor(cursor);
            }
            cursor.close();
            closeDB();
        }
        return currentUser;
    }

    public void updateUser(LoginUser loginUser) {
        ContentValues contentValues = loginUser.toContentValues();
        update(tableName, contentValues, UserColumns.rowId + "=?", new String[]{loginUser.getId()});

        Cursor cursor = query(tableName, UserColumns._all, UserColumns.rowId +
                "=?", new String[]{loginUser.getId()}, null, null, null);
        if (cursor.moveToFirst()) {
            currentUser = extractUserFromCursor(cursor);
        }
        cursor.close();
        closeDB();

    }

    public void clearDB() {
        SQLanguage.clearTableUser(tableName);
    }

    private LoginUser extractUserFromCursor(Cursor cursor) {
        LoginUser user = new LoginUser();
        user.setLoginName(extractStringValueForColumn(cursor, UserColumns.loginName));
        user.setCellphone(extractStringValueForColumn(cursor, UserColumns.cellphone));
        user.setAccessToken(extractStringValueForColumn(cursor, UserColumns.accessToken));
        user.setUserType(extractStringValueForColumn(cursor, UserColumns.userType));
        user.setBalance(extractStringValueForColumn(cursor, UserColumns.balance));
        user.setEnterpriseInfo(LoginUser.enterpriseInfoFromJson(extractStringValueForColumn(cursor, UserColumns.enterpriseInfo)));

        return user;
    }

    public boolean userIsExist(String userId) {
        boolean result = false;
        Cursor cursor = query(tableName, new String[]{UserColumns.userId},
                UserColumns.userId + "=?", new String[]{userId}, null, null, null);
        if (cursor.moveToFirst()) {
            result = true;
        }
        cursor.close();
        closeDB();
        return result;
    }

    public interface UserColumns {
        String rowId = "_id";
        String userId = "userId";
        String loginName = "loginName";
        String cellphone = "cellphone";
        String accessToken = "accessToken";
        String userType = "userType";
        String balance = "balance";
        String enterpriseInfo = "enterpriseInfo";

        String[] _all = new String[]{rowId, userId, loginName, cellphone, accessToken, userType, balance, enterpriseInfo};

    }

}
