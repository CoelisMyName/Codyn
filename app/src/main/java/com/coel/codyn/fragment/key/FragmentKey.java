package com.coel.codyn.fragment.key;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coel.codyn.ActivityAddEditKey;
import com.coel.codyn.R;
import com.coel.codyn.appUtil.SystemUtil;
import com.coel.codyn.main.Info;
import com.coel.codyn.room.Key;
import com.coel.codyn.viewmodel.MainVM;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class FragmentKey extends Fragment {
    public static final int ADD_KEY_REQUEST = 1;
    public static final int EDIT_KEY_REQUEST = 2;
    private KeyVM keyVM;
    private MainVM mainVM;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_key, container, false);
        Log.d(this.getClass().toString(), "onCreateView");

        RecyclerView recyclerView = root.findViewById(R.id.recycler_key);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        FloatingActionButton fab = root.findViewById(R.id.fab_key_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(getActivity(), ActivityAddEditKey.class), ADD_KEY_REQUEST);
            }
        });

        final KeyAdapter adapter = new KeyAdapter();
        recyclerView.setAdapter(adapter);

        keyVM = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(KeyVM.class);
        mainVM = new ViewModelProvider(getActivity()).get(MainVM.class);
        keyVM.getKeys().observe(getViewLifecycleOwner(), new Observer<List<Key>>() {
            @Override
            public void onChanged(List<Key> keys) {
                adapter.setKeys(keys);
            }
        });

        adapter.setKeyListListener(new KeyAdapter.KeyListListener() {
            @Override
            public void editKey(Key key) {
                startEditKeyAct(key);
            }

            @Override
            public void updateInfo(Info i) {
                mainVM.updateInfo(i);
            }

            @Override
            public void clipBoard(String key) {
                SystemUtil.setClipboard(getContext(), key);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return 0;
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_KEY_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data.getIntExtra(ActivityAddEditKey.EXTRA_KEY_TYPE, -1) == -1) {
                Toast.makeText(getContext(), "Could not create new key! Error!", Toast.LENGTH_SHORT).show();
                return;
            }

            //从data获得值
            Key key = new Key(keyVM.getUser_id(),
                    data.getIntExtra(ActivityAddEditKey.EXTRA_KEY_TYPE, -1),
                    data.getStringExtra(ActivityAddEditKey.EXTRA_KEY_COMMENT),
                    data.getStringExtra(ActivityAddEditKey.EXTRA_PRIVATE_KEY),
                    data.getStringExtra(ActivityAddEditKey.EXTRA_PUBLIC_KEY));

            //插入新钥匙
            keyVM.insertKey(key);
            Log.d("DDAA", "HERE");
            Toast.makeText(getContext(), "Key saved!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (requestCode == EDIT_KEY_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data.getIntExtra(ActivityAddEditKey.EXTRA_KEY_TYPE, -1) == -1 ||
                    data.getIntExtra(ActivityAddEditKey.EXTRA_KEY_ID, -1) == -1) {
                Toast.makeText(getContext(), "Could not update! Error!", Toast.LENGTH_SHORT).show();
                return;
            }

            Key key = new Key(keyVM.getUser_id(),
                    data.getIntExtra(ActivityAddEditKey.EXTRA_KEY_TYPE, -1),
                    data.getStringExtra(ActivityAddEditKey.EXTRA_KEY_COMMENT),
                    data.getStringExtra(ActivityAddEditKey.EXTRA_PRIVATE_KEY),
                    data.getStringExtra(ActivityAddEditKey.EXTRA_PUBLIC_KEY));

            key.setId(data.getIntExtra(ActivityAddEditKey.EXTRA_KEY_ID, -1));

            //更新钥匙
            keyVM.updateKey(key);

            Toast.makeText(getContext(), "Key saved!", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("ERR", "requestCode: " + requestCode + " resultCode: " + resultCode);
    }

    private void startEditKeyAct(Key k) {

    }
}
