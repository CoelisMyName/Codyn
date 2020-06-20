package com.coel.codyn.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

/*

users表项
id:int PRI
user_name:text UNI
password_sha256:text

用本机keystore随机生成aes密钥解密加密的密钥

*/
//TODO 先做不加密的数据库
@Entity(tableName = "user_table", indices = {@Index(value = {"user_name"}, unique = true)})
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String user_name;//用户名

    @NonNull
    private String password_hash;

    public User() {
    }

    public User(@NotNull String name, @NotNull String password_hash) {
        this.user_name = name;
        this.password_hash = password_hash;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NotNull
    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(@NotNull String name) {
        this.user_name = name;
    }

    @NotNull
    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(@NotNull String password_hash) {
        this.password_hash = password_hash;
    }
}
