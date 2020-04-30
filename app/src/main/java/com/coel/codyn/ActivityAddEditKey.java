package com.coel.codyn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.coel.codyn.appUtil.ViewUtil;
import com.coel.codyn.appUtil.cypherUtil.Coder;
import com.coel.codyn.appUtil.cypherUtil.KeyUtil;
import com.coel.codyn.appUtil.cypherUtil.crypto.AES;
import com.coel.codyn.appUtil.cypherUtil.crypto.ECC;
import com.coel.codyn.appUtil.cypherUtil.crypto.RSA;
import com.coel.codyn.room.Key;
import com.coel.codyn.viewmodel.AdEdVM;

import java.security.KeyPair;
import java.util.Objects;

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

    private EditText txtcomment;
    private Button txttype;
    private Button genkey;
    private EditText prikey;
    private EditText pubkey;

    private AdEdVM adEdVM;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addedit_key);

        txtcomment = findViewById(R.id.key_EditText);
        txttype = findViewById(R.id.btn_key_type);
        genkey = findViewById(R.id.btn_keygen);
        prikey = findViewById(R.id.pri_key_EditText);
        pubkey = findViewById(R.id.pub_key_EditText);

        txtcomment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

        adEdVM = new ViewModelProvider(this).get(AdEdVM.class);
        adEdVM.getTypeLD().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer index) {
                prikey.setText("");
                pubkey.setText("");

                if (index == -1) {
                    txttype.setText("密钥类型");
                    genkey.setEnabled(false);
                    pubkey.setVisibility(View.GONE);
                    prikey.setVisibility(View.GONE);
                } else {
                    genkey.setEnabled(true);
                    txttype.setText(KeyUtil.type2Str(AdEdVM.type_list[index]));
                    prikey.setVisibility(View.VISIBLE);
                    pubkey.setVisibility(KeyUtil.isSymmetric(AdEdVM.type_list[index])
                            ? View.GONE : View.VISIBLE);
                }
            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_KEY_ID)) {
            //是编辑已有的密钥
            setTitle(R.string.edit_key);
            int key_type_int = intent.getIntExtra(EXTRA_KEY_TYPE, -1);
            adEdVM.setType(key_type_int);
            txtcomment.setText(intent.getStringExtra(EXTRA_KEY_COMMENT));
            prikey.setText(intent.getStringExtra(EXTRA_PRIVATE_KEY));
            pubkey.setText(intent.getStringExtra(EXTRA_PUBLIC_KEY));
        } else {
            //是新建密钥
            setTitle(R.string.add_key);
        }

        txttype.setOnClickListener(this);
        genkey.setOnClickListener(this);
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
            ViewUtil.showToast(this, "Can not insert key without type!");
            //Toast.makeText(this, "Can not insert key without type!", Toast.LENGTH_SHORT).show();
            return;
        }

        String comment = txtcomment.getText().toString().trim();
        String pri = prikey.getText().toString().trim();
        String pub = pubkey.getText().toString().trim();

        //为空情况
        if (comment.isEmpty()) {
            ViewUtil.showToast(this, "Can not insert empty key!");
            //Toast.makeText(this, "Can not insert empty key!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (KeyUtil.isSymmetric(adEdVM.getType()) && pri.isEmpty()) {
            ViewUtil.showToast(this, "Can not insert empty key!");
            //Toast.makeText(this, "Can not insert empty key!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!KeyUtil.isSymmetric(adEdVM.getType()) && pub.isEmpty() && pri.isEmpty()) {
            ViewUtil.showToast(this, "Can not insert empty key!");
            //Toast.makeText(this, "Can not insert empty key!", Toast.LENGTH_SHORT).show();
            return;
        }

        //唯一情况
        if (KeyUtil.isSymmetric(adEdVM.getType()) && Coder.isBase64(pri)) {
            Intent data = new Intent();
            data.putExtra(EXTRA_KEY_COMMENT, comment);
            data.putExtra(EXTRA_PUBLIC_KEY, "");
            data.putExtra(EXTRA_PRIVATE_KEY, pri);
            data.putExtra(EXTRA_KEY_TYPE, adEdVM.getType());

            if (getIntent().getIntExtra(EXTRA_KEY_ID, -1) != -1) {
                data.putExtra(EXTRA_KEY_ID, getIntent().getIntExtra(EXTRA_KEY_ID, -1));
            }

            setResult(Activity.RESULT_OK, data);
            finish();
            return;
        }

        if (!KeyUtil.isSymmetric(adEdVM.getType())) {
            if ((Coder.isBase64(pri) || pri.isEmpty()) && (Coder.isBase64(pub) || pub.isEmpty())) {
                Intent data = new Intent();
                data.putExtra(EXTRA_KEY_COMMENT, comment);
                data.putExtra(EXTRA_PUBLIC_KEY, pub);
                data.putExtra(EXTRA_PRIVATE_KEY, pri);
                data.putExtra(EXTRA_KEY_TYPE, adEdVM.getType());

                if (getIntent().getIntExtra(EXTRA_KEY_ID, -1) != -1) {
                    data.putExtra(EXTRA_KEY_ID, getIntent().getIntExtra(EXTRA_KEY_ID, -1));
                }

                setResult(Activity.RESULT_OK, data);
                finish();
                return;
            }
        }

        Toast.makeText(this, "Incorrect Format", Toast.LENGTH_SHORT).show();
    }

    private void keyGen() {
        switch (adEdVM.getType()) {
            case Key.ECC_INT:
                try {
                    KeyPair pair = ECC.keyPairGen();
                    prikey.setText(Coder.Base64_encode2text(pair.getPrivate().getEncoded()));
                    pubkey.setText(Coder.Base64_encode2text(pair.getPublic().getEncoded()));
                } catch (Exception e) {
                    e.printStackTrace();
                    ViewUtil.showToast(this, "Error Occurred");
                }
                break;

            case Key.AES_INT:
                try {
                    SecretKey key = AES.keyGen();
                    prikey.setText(Coder.Base64_encode2text(key.getEncoded()));
                } catch (Exception e) {
                    e.printStackTrace();
                    ViewUtil.showToast(this, "Error Occurred");
                }
                break;

            case Key.RSA_INT:
                try {
                    KeyPair pair = RSA.keyPairGen();
                    prikey.setText(Coder.Base64_encode2text(pair.getPrivate().getEncoded()));
                    pubkey.setText(Coder.Base64_encode2text(pair.getPublic().getEncoded()));
                } catch (Exception e) {
                    e.printStackTrace();
                    ViewUtil.showToast(this, "Error Occurred");
                }
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
                keyGen();
                break;
        }
    }
}
