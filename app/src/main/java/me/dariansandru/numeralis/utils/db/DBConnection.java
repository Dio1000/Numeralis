package me.dariansandru.numeralis.utils.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.dariansandru.numeralis.domain.Scientist;
import me.dariansandru.numeralis.domain.Admin;

public class DBConnection {
    @SuppressLint("StaticFieldLeak")
    private static Context appContext;
    private static SQLiteOpenHelper dbHelper;

    public static void init(Context context) {
        if (appContext == null) {
            appContext = context.getApplicationContext();
            dbHelper = new SQLiteOpenHelper(appContext, "numeralis.db", null, 1) {
                @Override
                public void onCreate(SQLiteDatabase db) {
                    db.execSQL("CREATE TABLE scientists (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, password TEXT)");
                    db.execSQL("CREATE TABLE admins (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, password TEXT)");
                    db.execSQL("CREATE TABLE admin_permissions (admin_id INTEGER, permission TEXT, " +
                            "FOREIGN KEY (admin_id) REFERENCES admins(id))");
                }

                @Override
                public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                    db.execSQL("DROP TABLE IF EXISTS scientists");
                    db.execSQL("DROP TABLE IF EXISTS admins");
                    db.execSQL("DROP TABLE IF EXISTS admin_permissions");
                    onCreate(db);
                }
            };
        }
    }

    public static void saveScientist(String name, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("password", password);
        db.insert("scientists", null, values);
        db.close();
    }

    public static void saveAllScientists(List<Scientist> scientists) {
        for (Scientist scientist : scientists) saveScientist(scientist.getName(), scientist.getPassword());
    }

    public static List<Scientist> getAllScientists() {
        List<Scientist> scientists = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM scientists", null);

        if (cursor.moveToFirst()) {
            do {
                scientists.add(new Scientist(cursor.getString(1), cursor.getString(2)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return scientists;
    }

    public static void saveAdmin(Admin admin) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", admin.getName());
        values.put("password", admin.getPassword());

        long adminId = db.insert("admins", null, values);

        for (String permission : admin.getPermissions()) {
            ContentValues permValues = new ContentValues();
            permValues.put("admin_id", adminId);
            permValues.put("permission", permission);
            db.insert("admin_permissions", null, permValues);
        }

        db.close();
    }

    public static void saveAllAdmins(List<Admin> admins){
        for (Admin admin : admins) saveAdmin(admin);
    }

    public static List<Admin> getAllAdmins() {
        List<Admin> admins = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM admins", null);

        if (cursor.moveToFirst()) {
            do {
                int adminId = cursor.getInt(0);
                String name = cursor.getString(1);
                String password = cursor.getString(2);

                List<String> permissions = getPermissionsForAdmin(adminId);

                admins.add(new Admin(name, password, permissions));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return admins;
    }

    private static List<String> getPermissionsForAdmin(int adminId) {
        List<String> permissions = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT permission FROM admin_permissions WHERE admin_id = ?",
                new String[]{String.valueOf(adminId)});

        if (cursor.moveToFirst()) {
            do {
                permissions.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return permissions;
    }

    public static String getScientistPassword(String name) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT password FROM scientists WHERE name = ?", new String[]{name});

        String password = null;
        if (cursor.moveToFirst()) {
            password = cursor.getString(0);
        }

        cursor.close();
        db.close();
        return password;
    }

    public static String getAdminPassword(String name) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT password FROM admins WHERE name = ?", new String[]{name});

        String password = null;
        if (cursor.moveToFirst()) {
            password = cursor.getString(0);
        }

        cursor.close();
        db.close();
        return password;
    }

    public static List<String> getAllUsers() {
        List<String> users = new ArrayList<>();

        List<Scientist> scientists = getAllScientists();
        List<Admin> admins = getAllAdmins();
        for (Scientist scientist : scientists){
            users.add(scientist.getName() + " " + scientist.getPassword());
        }

        for (Admin admin : admins){
            users.add(admin.getName() + " " + admin.getPassword());
        }

        return users;
    }

    public static void logAllUsers() {
        List<String> allUsers = getAllUsers();
        for (String user : allUsers) {
            Toast.makeText(appContext, user, Toast.LENGTH_SHORT).show();
        }
    }
}
