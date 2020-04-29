package com.coel.codyn.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

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
    public static final int ECC_INT = 0x100;
    public static final int AES_INT = 0x200;
    public static final int RSA_INT = 0x300;
    public final static String ECC_STR = "ECC";
    public final static String RSA_STR = "RSA";
    public final static String AES_STR = "AES";

    public static final int PUBLIC_KEY = 1;
    public static final int PRIVATE_KEY = 2;
    public static final int SYMMETRIC_KEY = 3;
    public static final String PUBLIC_KEY_STR = "公钥";
    public static final String PRIVATE_KEY_STR = "私钥";
    public static final String SYMMETRIC_KEY_STR = "对称密钥";

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private int user_id;

    @NonNull
    private int key_type;
    @NonNull
    private String comment;
    @NonNull
    private String private_key;
    @NonNull
    private String public_key;

    public Key(int user_id, int key_type, @NotNull String comment, @NotNull String private_key, @NotNull String public_key) {
        this.user_id = user_id;
        this.key_type = key_type;
        this.comment = comment;
        this.private_key = private_key;
        this.public_key = public_key;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUserId(int user_id) {
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

    @NotNull
    public String getComment() {
        return comment;
    }

    public void setComment(@NotNull String comment) {
        this.comment = comment;
    }

    @NotNull
    public String getPrivate_key() {
        return private_key;
    }

    public void setPrivate_key(@NotNull String private_key) {
        this.private_key = private_key;
    }

    @NotNull
    public String getPublic_key() {
        return public_key;
    }

    public void setPublic_key(@NotNull String public_key) {
        this.public_key = public_key;
    }

}
