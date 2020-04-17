package com.coel.codyn.room;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.coel.codyn.cypherUtil.Coder;
import com.coel.codyn.cypherUtil.Hash;

import org.jetbrains.annotations.NotNull;

@Database(entities = {User.class, Key.class}, version = 1, exportSchema = false)
public abstract class CodynDB extends RoomDatabase {
    private static CodynDB codynDB;
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.d("DB", "callback");
            new CodynDB.PopulateDbAsyncTask(codynDB).execute();
        }
    };

    public static synchronized CodynDB getInstance(Context context) {
        if (codynDB == null) {
            RoomDatabase.Builder builder = Room.databaseBuilder(context.getApplicationContext(), CodynDB.class, "codyn_database");
            builder.addCallback(roomCallback);
            codynDB = (CodynDB) builder.build();
            codynDB.getOpenHelper().getWritableDatabase();
        }
        return codynDB;
    }

    @NotNull
    public abstract UserDAO userDAO();

    @NotNull
    public abstract KeyDAO keyDAO();

    //Callback调用线程
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private UserDAO userDAO;

        private PopulateDbAsyncTask(@NotNull CodynDB codynDB) {
            userDAO = codynDB.userDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //插入用户root
            userDAO.insert(new User("root", Coder.Base64_encode2text(Hash.sha256("000000".getBytes()))));
            Log.d(this.getClass().toString(), "insert account");
            return null;
        }
    }
}
