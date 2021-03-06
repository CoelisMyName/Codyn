package com.coel.codyn.fragment.file;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coel.codyn.FileCryptoService;
import com.coel.codyn.R;
import com.coel.codyn.activitydata.main.Info;
import com.coel.codyn.appUtil.FileUtils;
import com.coel.codyn.appUtil.SystemUtil;
import com.coel.codyn.appUtil.ViewUtil;
import com.coel.codyn.repository.FileTaskRepository;
import com.coel.codyn.service.FileCryptoView;
import com.coel.codyn.service.FileTask;
import com.coel.codyn.viewmodel.MainVM;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;

public class FragmentFile extends Fragment {
    public static final int OPEN_FILE_REQUEST = 1;
    public static final int EXTERNAL_STORAGE_PERMISSION_REQUEST = 1;
    private MainVM mainVM;
    private FileVM fileVM;
    private FileTaskBuilder builder;
    private FileCryptoService.CryptoBinder binder;
    private FileViewAdapter adapter = new FileViewAdapter();
    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (FileCryptoService.CryptoBinder) service;
            Log.d("binder", "onServiceConnected: binder");
            showList();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            binder = null;
            Log.d("binder", "onServiceConnected: binder set null");
        }
    };

    public void showList(){
        fileVM.setFcvListLiveData(binder.getDisplay());
        fileVM.getFcvListLiveData().observe(this, new Observer<List<FileCryptoView>>() {
            @Override
            public void onChanged(List<FileCryptoView> fileCryptoViews) {
                adapter.submitList(fileCryptoViews);
            }
        });
        Log.d("binder", "onServiceConnected: submitList");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_file, container, false);//填充布局
        fileVM = new ViewModelProvider(this).get(FileVM.class);
        mainVM = new ViewModelProvider(requireActivity()).get(MainVM.class);
        builder = new FileTaskBuilder();
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        Intent intent = new Intent(requireActivity(), FileCryptoService.class);
        requireActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE);

        FloatingActionButton fab = root.findViewById(R.id.floatingActionButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!builder.isReady()) {
                    ViewUtil.showToast(requireContext(), "你还没选择密钥");
                } else {
                    permissionForFile();
                }
            }
        });
        mainVM.getInfo().observe(getViewLifecycleOwner(), new Observer<Info>() {
            @Override
            public void onChanged(Info info) {
                if (info != null && info.getTYPE() != Info.DEFAULT) {
                    builder.setAttr(info.getATTR());
                    builder.setType(info.getTYPE());
                    builder.setKey(info.getKey());
                } else {
                    builder.clear();
                }
                fileVM.setFileTaskBuilderLiveData(builder);
            }
        });

        adapter.setListener(new FileViewAdapter.FileViewListener() {
            @Override
            public void submit(FileCryptoView fileCryptoView, int s) {
                if (binder != null) {
                    binder.submit((FileTask) fileCryptoView, s);
                }
            }
        });
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (viewHolder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                    binder.submit((FileTask) adapter.getFileCryptoView(viewHolder.getAdapterPosition()), FileTaskRepository.REMOVE);
                    ViewUtil.showToast(getContext(), "任务移除");
                }
            }
        }).attachToRecyclerView(recyclerView);
        return root;
    }

    private void permissionForFile() {//权限检查
        ArrayList<String> array = new ArrayList<>(2);
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            array.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            array.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (array.size() > 0) {
            String[] permissions = array.toArray(new String[array.size()]);
            ActivityCompat.requestPermissions(requireActivity(), permissions, EXTERNAL_STORAGE_PERMISSION_REQUEST);
            return;
        }
        openFile();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_REQUEST) {
            for (int i : grantResults) {
                if (i != PackageManager.PERMISSION_GRANTED)
                    ViewUtil.showToast(requireContext(), "你拒绝了文件权限");
                return;
            }
            openFile();
        }
    }

    private void openFile() throws SecurityException {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, OPEN_FILE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_FILE_REQUEST && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();

            Log.d("file", "onActivityResult: " + FileUtils.getPath(requireContext(), uri));
            String path = FileUtils.getPath(requireContext(), uri);
            File file = new File(path);
            Log.d("open file", "onActivityResult: " + path);
            builder.setSource(path);
            FileDialogFragment dialog = FileDialogFragment.newInstance("新文件任务");

            dialog.setListener(new FileDialogFragment.DialogListener() {
                @Override
                public void setMode(int i) {
                    Log.d("openfile", "setMode: " + SystemUtil.getExternalFilesDir(requireContext()) + "/" + file.getName());
                    builder.setMode(i);
                    if (i == Cipher.ENCRYPT_MODE) {
                        builder.setDest(SystemUtil.getExternalFilesDir(requireContext()) + "/" + file.getName() + ".encrypt");
                    } else if (i == Cipher.DECRYPT_MODE) {
                        builder.setDest(SystemUtil.getExternalFilesDir(requireContext()) + "/" + file.getName() + ".decrypt");
                    }
                    try {
                        FileTask fileTask = builder.build();
                        if (fileTask == null) {
                            ViewUtil.showToast(requireContext(), "创建任务失败");
                        } else {
                            if (binder != null) {
                                binder.submit(fileTask, FileTaskRepository.START);
                                ViewUtil.showToast(requireContext(), "创建任务成功");
                            } else {
                                ViewUtil.showToast(requireContext(), "创建任务失败");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            dialog.show(requireActivity().getSupportFragmentManager(), "文件任务");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().unbindService(conn);
    }
}
