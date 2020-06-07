package com.coel.codyn.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.coel.codyn.room.CodynDB;
import com.coel.codyn.room.Key;
import com.coel.codyn.room.KeyDAO;
import com.coel.codyn.room.User;
import com.coel.codyn.room.UserDAO;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CodynRepository {
    private KeyDAO keyDAO;
    private UserDAO userDAO;

    public CodynRepository(Application application) {
        CodynDB codynDB = CodynDB.getInstance(application);
        keyDAO = codynDB.keyDAO();
        userDAO = codynDB.userDAO();
    }

    //插入用户
    public void insertUser(User user) {
        new InsertUserAsyncTask(userDAO).execute(user);
    }

    //更新用户
    public void updateUser(User user) {
        new UpdateUserAsyncTask(userDAO).execute(user);
    }

    //删除用户
    public void deleteUser(User user) {
        new DeleteUserAsyncTask(userDAO).execute(user);
    }

    //返回所有用户
    public List<User> getAllUsers() {
        return userDAO.find_all_users();
    }

    //返回所有用户
    public LiveData<List<User>> getAllUsersLD() {
        return userDAO.find_all_usersLD();
    }

    //从初始化表中查询
    public LiveData<User> find_userLD(String name) {
        return userDAO.find_userLD(name);
    }

    public User[] find_user(String name) {
        return userDAO.find_user(name);
    }

    //从初始化表中查询
    public LiveData<User> find_userLD(int id) {
        return userDAO.find_userLD(id);
    }

    //插入密钥
    public void insertKey(Key key) {
        new InsertKeyAsyncTask(keyDAO).execute(key);
    }

    //更新密钥
    public void updateKey(Key key) {
        new UpdateKeyAsyncTask(keyDAO).execute(key);
    }

    //删除密钥
    public void deleteKey(Key key) {
        new DeleteKeyAsyncTask(keyDAO).execute(key);
    }

    public LiveData<List<Key>> find_keys(int uid) {
        return keyDAO.find_keys(uid);
    }

    public LiveData<List<Key>> find_keys(int uid,int type) {
        return keyDAO.find_keys(uid,type);
    }


    //以下是同步数据库子线程
    private static class InsertUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDAO userDAO;

        private InsertUserAsyncTask(UserDAO userDAO) {
            this.userDAO = userDAO;
        }

        @Override
        protected Void doInBackground(@NotNull User... users) {
            userDAO.insert(users[0]);
            return null;
        }
    }

    private static class UpdateUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDAO userDAO;

        private UpdateUserAsyncTask(UserDAO userDAO) {
            this.userDAO = userDAO;
        }

        @Override
        protected Void doInBackground(@NotNull User... users) {
            userDAO.update(users[0]);
            return null;
        }
    }

    private static class DeleteUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDAO userDAO;

        private DeleteUserAsyncTask(UserDAO userDAO) {
            this.userDAO = userDAO;
        }

        @Override
        protected Void doInBackground(@NotNull User... users) {
            userDAO.delete(users[0]);
            return null;
        }
    }

    private static class InsertKeyAsyncTask extends AsyncTask<Key, Void, Void> {
        private KeyDAO keyDAO;

        private InsertKeyAsyncTask(KeyDAO keyDAO) {
            this.keyDAO = keyDAO;
        }

        @Override
        protected Void doInBackground(Key... keys) {
            keyDAO.insert(keys[0]);
            return null;
        }
    }

    private static class UpdateKeyAsyncTask extends AsyncTask<Key, Void, Void> {
        private KeyDAO keyDAO;

        private UpdateKeyAsyncTask(KeyDAO keyDAO) {
            this.keyDAO = keyDAO;
        }

        @Override
        protected Void doInBackground(Key... keys) {
            keyDAO.update(keys[0]);
            return null;
        }
    }

    private static class DeleteKeyAsyncTask extends AsyncTask<Key, Void, Void> {
        private KeyDAO keyDAO;

        private DeleteKeyAsyncTask(KeyDAO keyDAO) {
            this.keyDAO = keyDAO;
        }

        @Override
        protected Void doInBackground(Key... keys) {
            keyDAO.delete(keys[0]);
            return null;
        }
    }
}
