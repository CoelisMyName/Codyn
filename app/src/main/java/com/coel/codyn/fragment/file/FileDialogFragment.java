package com.coel.codyn.fragment.file;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.coel.codyn.R;

import javax.crypto.Cipher;

public class FileDialogFragment extends DialogFragment {
    public static final String TITLE = "tittle";
    private DialogListener listener;
    private FileVM fileVM;

    public static FileDialogFragment newInstance(String tittle) {
        FileDialogFragment fragment = new FileDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, tittle);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialogfragment, container, false);
        fileVM = new ViewModelProvider(this).get(FileVM.class);
        return root;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        assert getArguments() != null;
        String tittle = getArguments().getString(TITLE);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(tittle);
        builder.setPositiveButton("加密", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (listener != null) {
                    listener.setMode(Cipher.ENCRYPT_MODE);
                }
                dismiss();
            }
        });
        builder.setNeutralButton("解密", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null) {
                    listener.setMode(Cipher.DECRYPT_MODE);
                }
                dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });
        return builder.create();
    }

    public void setListener(DialogListener listener) {
        this.listener = listener;
    }

    public interface DialogListener {
        void setMode(int i);
    }
}
