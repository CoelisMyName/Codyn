package com.coel.codyn.fragment.file;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class FragmentFile extends Fragment {
    public static final int OPEN_FILE_REQUEST = 1;
    public static final int EXTERNAL_STORAGE_PERMISSION_REQUEST = 0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private boolean permission(){//权限检查
        ArrayList<String> array = new ArrayList<>(2);
        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ){
            array.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ){
            array.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(array.size() > 0){
            String[] permissions = array.toArray(new String[array.size()]);
            ActivityCompat.requestPermissions(requireActivity(), permissions,EXTERNAL_STORAGE_PERMISSION_REQUEST);
            return false;
        }
        return true;
    }

    private void openFile() throws SecurityException{
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent,OPEN_FILE_REQUEST);
        FragmentFile.class.getClassLoader();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == OPEN_FILE_REQUEST && resultCode == Activity.RESULT_OK){
            Uri uri = data.getData();
            if ("file".equalsIgnoreCase(uri.getScheme())){
                String path = uri.getPath();
                createTask(path);
            }
        }
    }

    private void createTask(String path){

    }

    private BufferedInputStream createBufferedInputStream(String path) throws FileNotFoundException {
        File file = new File(path);
        FileInputStream inputStream = new FileInputStream(file);
        return new BufferedInputStream(inputStream);
    }

    private int readBlock(BufferedInputStream inputStream, byte[] bytes) throws IOException {
        return inputStream.read(bytes,0,bytes.length);
    }

    private void writeBlock(BufferedOutputStream outputStream, byte[] bytes) throws IOException {
        outputStream.write(bytes,0,bytes.length);
    }

    private BufferedOutputStream createBufferedOutputStream(String path) throws FileNotFoundException {
        File file = new File(path);
        FileOutputStream outputStream = new FileOutputStream(file);
        return new BufferedOutputStream(outputStream);
    }
}
