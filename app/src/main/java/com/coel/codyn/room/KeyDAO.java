package com.coel.codyn.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface KeyDAO {
    //插入密钥
    @Insert
    void insert(Key key);

    //更新密钥
    @Update
    void update(Key key);

    //删除密钥
    @Delete
    void delete(Key key);

    //选取对应的用户id的密钥
    @Query("SELECT * FROM key_table WHERE user_id LIKE :uid")
    LiveData<List<Key>> find_keys(int uid);

    @Query("SELECT * FROM key_table WHERE user_id LIKE :uid AND key_type LIKE :type")
    LiveData<List<Key>> find_keys(int uid,int type);
}
