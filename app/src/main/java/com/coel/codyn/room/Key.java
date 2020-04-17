package com.coel.codyn.room;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/*

user_key表
key_index:int FOREIGN KEY user表
key_type:text [ECC][AES][RSA]
key_comment:text
private_key:text
public_key:text

*/
@Entity(tableName = "key_table", foreignKeys = @ForeignKey(entity = User.class, parentColumns = "id",
        childColumns = "user_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
        indices = {@Index(value = "id", unique = true), @Index(value = "user_id")})
public class Key {
    public static final int ECC = 0x100;
    public static final int AES = 0x200;
    public static final int RSA = 0x300;

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int user_id;

    private int key_type;
    private String comment;
    private String private_key;
    private String public_key;

    public Key(int user_id, int key_type, String comment, String private_key, String public_key) {
        this.user_id = user_id;
        this.key_type = key_type;
        this.comment = comment;
        this.private_key = private_key;
        this.public_key = public_key;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKey_type() {
        return key_type;
    }

    public void setKey_type(int key_type) {
        this.key_type = key_type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPrivate_key() {
        return private_key;
    }

    public void setPrivate_key(String private_key) {
        this.private_key = private_key;
    }

    public String getPublic_key() {
        return public_key;
    }

    public void setPublic_key(String public_key) {
        this.public_key = public_key;
    }

}