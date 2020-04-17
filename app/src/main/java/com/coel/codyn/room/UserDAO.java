package com.coel.codyn.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDAO {

    //插入用户
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(User user);

    //更新用户
    @Update(onConflict = OnConflictStrategy.ABORT)
    void update(User user);

    //删除用户
    @Delete
    void delete(User user);

    //选取用户名对应的用户
    @Query("SELECT * FROM user_table WHERE user_name LIKE :name")
    LiveData<User> find_user(String name);

    //选取所有用户
    @Query("SELECT * FROM user_table")
    List<User> find_all_users();
}
