package com.coel.codyn.fragment.function;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coel.codyn.R;
import com.coel.codyn.activitydata.main.Info;
import com.coel.codyn.appUtil.SystemUtil;
import com.coel.codyn.appUtil.ViewUtil;
import com.coel.codyn.appUtil.cypherUtil.Coder;
import com.coel.codyn.appUtil.cypherUtil.Hash;
import com.coel.codyn.appUtil.cypherUtil.crypto.AES;
import com.coel.codyn.appUtil.cypherUtil.crypto.ECC;
import com.coel.codyn.appUtil.cypherUtil.crypto.RSA;
import com.coel.codyn.room.Key;
import com.coel.codyn.viewmodel.MainVM;

import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class FragmentFunction extends Fragment {
    private static volatile String tempInStr;
    private FunctionVM functionVM;
    private MainVM mainVM;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        functionVM = new ViewModelProvider(this).get(FunctionVM.class);//获得view model
        mainVM = new ViewModelProvider(requireActivity()).get(MainVM.class);

        View root = inflater.inflate(R.layout.fragment_function, container, false);//填充布局

        //初始化recycler view
        RecyclerView recyclerView = root.findViewById(R.id.recycler_function);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        final FunctionPadAdapter adapter = new FunctionPadAdapter();
        recyclerView.setAdapter(adapter);
        functionVM.getFunctionPad().observe(getViewLifecycleOwner(), new Observer<FunctionPad>() {
            @Override
            public void onChanged(FunctionPad functionPad) {
                adapter.setPad(functionPad);
            }
        });

        adapter.setListener(new FunctionPadAdapter.PadListener() {
            @Override
            public void saveStr(String s) {
                FunctionPad.setSaveStr(s);
            }

            @Override
            public void clipBoard(String s) {
                SystemUtil.setClipboard(getContext(), s);
                ViewUtil.showToast(getContext(), "复制到剪贴板");
            }

            @Override
            public void base64Enc(String s) {
                functionVM.updateOuttxt(Coder.Base64_encode2text(s.getBytes()));
            }

            @Override
            public void base64Dec(String s) {
                try {
                    functionVM.updateOuttxt(Coder.Base64_decode2text(s));
                } catch (Exception e) {
                    e.printStackTrace();
                    ViewUtil.showToast(getContext(), "解码出错");
                }
            }

            @Override
            public void base64Dec2Reg(String s) {
                try {
                    functionVM.updateBin(Coder.Base64_decode2bin(s));
                } catch (Exception e) {
                    e.printStackTrace();
                    ViewUtil.showToast(getContext(), "解码出错");
                }
            }

            @Override
            public void sha256Txt(String s) {
                functionVM.updateOuttxt(Coder.Base64_encode2text(Hash.sha256(s.getBytes())));
            }

            @Override
            public void sha256Bin(byte[] bin) {
                functionVM.updateOuttxt(Coder.Base64_encode2text(Hash.sha256(bin)));
            }

            @Override
            public void encrypt(byte[] bin) {
                Info ins = mainVM.getInfo().getValue();
                if (ins.getATTR() != Info.DEFAULT) {
                    CryptoAtask task = new CryptoAtask(CryptoAtask.ENCRYPT, ins.getTYPE(), ins.getATTR());
                    task.execute(ins.getKey(), bin);
                }
            }

            @Override
            public void decrypt(byte[] bin) {
                Info ins = mainVM.getInfo().getValue();
                if (ins.getATTR() != Info.DEFAULT) {
                    CryptoAtask task = new CryptoAtask(CryptoAtask.DECRYPT, ins.getTYPE(), ins.getATTR());
                    task.execute(ins.getKey(), bin);
                }
            }
        });

        return root;
    }

    public class CryptoAtask extends AsyncTask<byte[], Void, String> {
        public final static int ENCRYPT = 1;
        public final static int DECRYPT = 2;

        private final int MODE;
        private final int TYPE;
        private final int ATTR;

        public CryptoAtask(int mode, int key_type, int key_attr) {
            MODE = mode;
            TYPE = key_type;
            ATTR = key_attr;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            functionVM.updateOuttxt("运算中");
        }

        @Override
        protected String doInBackground(byte[]... bytes) {
            if (bytes.length < 2) {
                Log.d(this.getClass().toString(), "argument less than 2");
                return "错误";
            }
            byte[] key_bin = bytes[0], text = bytes[1], outcome = new byte[0];
            String out = "未知模式";
            try {
                if (MODE == ENCRYPT) {
                    switch (TYPE) {
                        case Key.ECC_INT:
                            if (ATTR == Key.PUBLIC_KEY) {
                                PublicKey key = ECC.publicKey(key_bin);
                                outcome = ECC.encrypt(key, text);
                            }
                            if (ATTR == Key.PRIVATE_KEY) {
                                PrivateKey key = ECC.privateKey(key_bin);
                                outcome = ECC.encrypt(key, text);
                            }
                            break;

                        case Key.AES_INT:
                            if (ATTR == Key.SYMMETRIC_KEY) {
                                SecretKeySpec key = AES.keyDecode(key_bin);
                                if (bytes.length == 2) {
                                    outcome = AES.encrypt(key, text);
                                } else {
                                    outcome = AES.encrypt(key, text, new IvParameterSpec(bytes[2]));
                                }
                            }
                            break;

                        case Key.RSA_INT:
                            if (ATTR == Key.PUBLIC_KEY) {
                                PublicKey key = RSA.publicKey(key_bin);
                                outcome = RSA.encrypt(key, text);
                            }
                            if (ATTR == Key.PRIVATE_KEY) {
                                PrivateKey key = RSA.privateKey(key_bin);
                                outcome = RSA.encrypt(key, text);
                            }
                            break;

                        default:
                            outcome = null;
                    }

                    if (outcome == null) {
                        return "错误";
                    } else {
                        out = Coder.Base64_encode2text(outcome);
                    }

                }
                if (MODE == DECRYPT) {
                    switch (TYPE) {
                        case Key.ECC_INT:
                            if (ATTR == Key.PUBLIC_KEY) {
                                PublicKey key = ECC.publicKey(key_bin);
                                outcome = ECC.decrypt(key, text);
                            }
                            if (ATTR == Key.PRIVATE_KEY) {
                                PrivateKey key = ECC.privateKey(key_bin);
                                outcome = ECC.decrypt(key, text);
                            }
                            break;

                        case Key.AES_INT:
                            if (ATTR == Key.SYMMETRIC_KEY) {
                                SecretKeySpec key = AES.keyDecode(key_bin);
                                if (bytes.length == 2) {
                                    outcome = AES.decrypt(key, text);
                                } else {
                                    outcome = AES.decrypt(key, text, new IvParameterSpec(bytes[2]));
                                }
                            }
                            break;

                        case Key.RSA_INT:
                            if (ATTR == Key.PUBLIC_KEY) {
                                PublicKey key = RSA.publicKey(key_bin);
                                outcome = RSA.decrypt(key, text);
                            }
                            if (ATTR == Key.PRIVATE_KEY) {
                                PrivateKey key = RSA.privateKey(key_bin);
                                outcome = RSA.decrypt(key, text);
                            }
                            break;

                        default:
                            outcome = null;
                    }

                    if (outcome == null) {
                        return "错误";
                    } else {
                        out = new String(outcome);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "密码学错误";
            }

            return out;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            functionVM.updateOuttxt(s);
        }
    }
}
