package com.coel.codyn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.coel.codyn.cypherUtil.Coder;
import com.coel.codyn.cypherUtil.Crypto;
import com.coel.codyn.cypherUtil.Function;
import com.coel.codyn.room.Key;
import com.coel.codyn.viewmodel.AdEdVM;

import java.security.KeyPair;

import javax.crypto.SecretKey;
/*
不用null字符串，用空字符串，避免意想不到情况
 */

public class ActivityAddEditKey extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_KEY_ID = "com.coel.codyn.EXTRA_KEY_ID";
    public static final String EXTRA_USER_ID = "com.coel.codyn.EXTRA_USER_ID";
    public static final String EXTRA_KEY_TYPE = "com.coel.codyn.EXTRA_KEY_TYPE";
    public static final String EXTRA_KEY_COMMENT = "com.coel.codyn.EXTRA_KEY_COMMENT";
    public static final String EXTRA_PRIVATE_KEY = "com.coel.codyn.EXTRA_PRIVATE_KEY";
    public static final String EXTRA_PUBLIC_KEY = "com.coel.codyn.EXTRA_PUBLIC_KEY";

    private EditText comment;
    private Button key_type;
    private Button genkey;
    private EditText prikey;
    private EditText pubkey;

    private AdEdVM adEdVM;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addedit_key);

        comment = findViewById(R.id.key_EditText);
        key_type = findViewById(R.id.btn_key_type);
        genkey = findViewById(R.id.btn_keygen);
        prikey = findViewById(R.id.pri_key_EditText);
        pubkey = findViewById(R.id.pub_key_EditText);

        adEdVM = new ViewModelProvider(this).get(AdEdVM.class);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

        adEdVM.getTypeLD().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                int value = adEdVM.getType();
                key_type.setText(adEdVM.key_type_Sting(value));
                boolean isSym = Function.key_type_is_symmetric(value);
                pubkey.setText("");
                prikey.setText("");
                if (isSym) {
                    pubkey.setVisibility(View.GONE);
                } else {
                    pubkey.setVisibility(View.VISIBLE);
                }
                genkey.setEnabled(value != -1);
            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_KEY_ID)) {
            //是编辑已有的密钥
            setTitle(getText(R.string.edit_key));
            int key_type_int = intent.getIntExtra(EXTRA_KEY_TYPE, -1);
            if (key_type_int != -1) {
                adEdVM.setType(key_type_int);
            }
            comment.setText(intent.getStringExtra(EXTRA_KEY_COMMENT));
            prikey.setText(intent.getStringExtra(EXTRA_PRIVATE_KEY));
            pubkey.setText(intent.getStringExtra(EXTRA_PUBLIC_KEY));
        } else {
            //是新建密钥
            setTitle(getText(R.string.add_key));
        }
        Log.d(this.getClass().toString(), "Process Intent Success");


        key_type.setOnClickListener(this);
        genkey.setOnClickListener(this);

        Log.d(this.getClass().toString(), "onCreate success");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addedit_key_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_key:
                saveKey();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveKey() {
        if (adEdVM.getType() == -1) {

            Toast.makeText(this, "Can not insert key without type!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (comment.getText().toString().trim().isEmpty() ||
                (pubkey.getText().toString().trim().isEmpty() &&
                        prikey.getText().toString().trim().isEmpty())) {

            Toast.makeText(this, "Can not insert empty key!", Toast.LENGTH_SHORT).show();
            return;
        }
        if ((!Coder.isBase64(pubkey.getText().toString().trim()) &&
                !pubkey.getText().toString().trim().isEmpty())
                || (!Coder.isBase64(prikey.getText().toString().trim()) &&
                !prikey.getText().toString().trim().isEmpty())) {

            Toast.makeText(this, "Can not insert non-Base64 key!", Toast.LENGTH_SHORT).show();
            return;
        }

        //别忘了密钥长度检查
        Intent data = new Intent();
        data.putExtra(EXTRA_KEY_COMMENT, comment.getText().toString());
        data.putExtra(EXTRA_PUBLIC_KEY, pubkey.getText().toString().trim());
        data.putExtra(EXTRA_PRIVATE_KEY, prikey.getText().toString().trim());
        data.putExtra(EXTRA_KEY_TYPE, adEdVM.getType());
        Log.d("ADD", "Put date");
        if (getIntent().getIntExtra(EXTRA_KEY_ID, -1) != -1) {
            data.putExtra(EXTRA_KEY_ID, getIntent().getIntExtra(EXTRA_KEY_ID, -1));
        }

        setResult(Activity.RESULT_OK, data);
        finish();
    }

    private void KeyGen() {
        switch (adEdVM.getType()) {
            case Key.ECC:
                KeyPair pair = Crypto.ECC_KeyPairGen();
                if (pair == null) {
                    Toast.makeText(this, "Generate ECC Key Pair ERROR", Toast.LENGTH_SHORT).show();
                    return;
                }
                prikey.setText(Coder.Base64_encode2text(pair.getPrivate().getEncoded()));
                pubkey.setText(Coder.Base64_encode2text(pair.getPublic().getEncoded()));
                break;

            case Key.AES:
                SecretKey key = Crypto.AES_KeyGen();
                if (key == null) {
                    Toast.makeText(this, "Generate AES Key ERROR", Toast.LENGTH_SHORT).show();
                    return;
                }
                prikey.setText(Coder.Base64_encode2text(key.getEncoded()));
                break;

            case Key.RSA:
                Toast.makeText(this, "Function Undone", Toast.LENGTH_SHORT).show();
                break;

            case -1:
                Toast.makeText(this, "Choose type first", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_key_type:
                adEdVM.nextType();
                break;

            case R.id.btn_keygen:
                KeyGen();
                break;
        }
    }
}
