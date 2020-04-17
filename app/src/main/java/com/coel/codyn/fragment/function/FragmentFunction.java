package com.coel.codyn.fragment.function;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coel.codyn.R;
import com.coel.codyn.cypherUtil.Coder;
import com.coel.codyn.cypherUtil.Crypto;
import com.coel.codyn.viewmodel.MainVM;

import java.security.PrivateKey;
import java.security.PublicKey;

public class FragmentFunction extends Fragment {
    private static volatile String tempInStr;
    private FunctionVM functionVM;
    private MainVM mainVM;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        functionVM = new ViewModelProvider(this).get(FunctionVM.class);//获得view model
        mainVM = new ViewModelProvider(getActivity()).get(MainVM.class);

        View root = inflater.inflate(R.layout.fragment_function, container, false);//填充布局

        //初始化recycler view
        RecyclerView recyclerView = root.findViewById(R.id.recycler_function);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        final FunctionPadAdapter adapter = new FunctionPadAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnclick(new FunctionPadAdapter.Onclick() {
            @Override
            public void onClickFunc(View v, String s, TextView textView) {
                onClickInterface(v, s, textView);
            }
        });

        return root;
    }

    public void onClickInterface(View v, String s, TextView textView) {
        int attr;
        switch (v.getId()) {
            case R.id.btn_base64_enc:
                textView.setText(Coder.Base64_encode2text(s.getBytes()));
                break;

            case R.id.btn_base64_dec:
                if (!Coder.isBase64(s)) {
                    Toast.makeText(getContext(), R.string.decode_error, Toast.LENGTH_SHORT).show();
                    return;
                }
                textView.setText(Coder.Base64_decode2text(s));
                break;
            case R.id.btn_ECC_enc:
                attr = mainVM.getKey_attr().getValue();

                if (attr == MainVM.PUBLIC_KEY) {
                    PublicKey publicKey = Crypto.ECC_public_key(Coder.Base64_decode2bin(mainVM.getKey().getValue()));
                    if (publicKey != null) {
                        new Atask_ECC_PUB(publicKey, textView, Atask_ECC_PUB.ENC_MODE).execute(s.getBytes());
                    }
                } else if (attr == MainVM.PRIVATE_KEY) {
                    PrivateKey privateKey = Crypto.ECC_private_key(Coder.Base64_decode2bin(mainVM.getKey().getValue()));
                    if (privateKey != null) {
                        new Atask_ECC_PRI(privateKey, textView, Atask_ECC_PRI.DEC_MODE).execute(s.getBytes());
                    }
                }

                break;
            case R.id.btn_ECC_dec:
                if (!Coder.isBase64(s)) {
                    Toast.makeText(getContext(), R.string.decode_error, Toast.LENGTH_SHORT).show();
                    return;
                }

                attr = mainVM.getKey_attr().getValue();
                byte[] cyp = Coder.Base64_decode2bin(s);
                if (attr == MainVM.PUBLIC_KEY) {
                    PublicKey publicKey = Crypto.ECC_public_key(Coder.Base64_decode2bin(mainVM.getKey().getValue()));
                    if (publicKey != null) {
                        new Atask_ECC_PUB(publicKey, textView, Atask_ECC_PUB.DEC_MODE).execute(cyp);
                    }
                } else if (attr == MainVM.PRIVATE_KEY) {
                    PrivateKey privateKey = Crypto.ECC_private_key(Coder.Base64_decode2bin(mainVM.getKey().getValue()));
                    if (privateKey != null) {
                        new Atask_ECC_PRI(privateKey, textView, Atask_ECC_PRI.DEC_MODE).execute(cyp);
                    }
                }

                break;
            case R.id.textView_function:
                setClipboard(textView.getText().toString());
                break;
            default:
                break;
        }
    }

    private void setClipboard(String str) {
        ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("Label", str);
        assert cm != null;
        cm.setPrimaryClip(mClipData);
        Toast.makeText(getContext(), R.string.copy_success, Toast.LENGTH_SHORT).show();
    }

    public static class Atask_ECC_PUB extends AsyncTask<byte[], Void, String> {
        public static final int ENC_MODE = 1;
        public static final int DEC_MODE = 2;
        private TextView txtv;
        private int mode;
        private PublicKey publicKey;

        public Atask_ECC_PUB(PublicKey publicKey, TextView v, int mode) {
            txtv = v;
            this.mode = mode;
            this.publicKey = publicKey;
        }

        @Override
        protected void onPreExecute() {
            if (mode == ENC_MODE)
                txtv.setText("加密中");
            else
                txtv.setText("解密中");
        }

        @Override
        protected void onPostExecute(String s) {
            txtv.setText(s);
        }

        @Override
        protected String doInBackground(byte[]... bytes) {
            byte[] text = bytes[0];
            byte[] cyph = null;
            if (mode == ENC_MODE) {
                cyph = Crypto.ECC_encrypt(publicKey, text);
            } else if (mode == DEC_MODE) {
                cyph = Crypto.ECC_decrypt(publicKey, text);
            }

            if (cyph == null) {
                if (mode == ENC_MODE)
                    return "加密错误";
                else if (mode == DEC_MODE)
                    return "解密错误";
            }
            if (mode == ENC_MODE)
                return Coder.Base64_encode2text(cyph);
            else if (mode == DEC_MODE)
                return new String(cyph);
            return "";

        }
    }

    public static class Atask_ECC_PRI extends AsyncTask<byte[], Void, String> {
        public static final int ENC_MODE = 1;
        public static final int DEC_MODE = 2;
        private TextView txtv;
        private int mode;
        private PrivateKey privateKey;

        public Atask_ECC_PRI(PrivateKey privateKey, TextView v, int mode) {
            txtv = v;
            this.mode = mode;
            this.privateKey = privateKey;
        }

        @Override
        protected void onPreExecute() {
            if (mode == ENC_MODE)
                txtv.setText("加密中");
            else
                txtv.setText("解密中");
        }

        @Override
        protected void onPostExecute(String s) {
            txtv.setText(s);
        }

        @Override
        protected String doInBackground(byte[]... bytes) {
            byte[] text = bytes[0];
            byte[] cyph = null;
            if (mode == ENC_MODE) {
                cyph = Crypto.ECC_encrypt(privateKey, text);
            } else if (mode == DEC_MODE) {
                cyph = Crypto.ECC_decrypt(privateKey, text);
            }

            if (cyph == null) {
                if (mode == ENC_MODE)
                    return "加密错误";
                else if (mode == DEC_MODE)
                    return "解密错误";
            }

            if (mode == ENC_MODE)
                return Coder.Base64_encode2text(cyph);
            else if (mode == DEC_MODE)
                return new String(cyph);
            return "";
        }
    }

}
