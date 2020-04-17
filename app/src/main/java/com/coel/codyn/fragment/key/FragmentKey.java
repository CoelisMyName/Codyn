package com.coel.codyn.fragment.key;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coel.codyn.ActivityAddEditKey;
import com.coel.codyn.ActivityMain;
import com.coel.codyn.R;
import com.coel.codyn.room.Key;
import com.coel.codyn.viewmodel.MainVM;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class FragmentKey extends Fragment {
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
                        new Intent(getActivity(), ActivityAddEditKey.class), ActivityMain.ADD_KEY_REQUEST);
            }
        });

        final KeyAdapter adapter = new KeyAdapter();
        recyclerView.setAdapter(adapter);

        keyVM = new ViewModelProvider(getActivity()).get(KeyVM.class);
        mainVM = new ViewModelProvider(getActivity()).get(MainVM.class);
        keyVM.getKeys().observe(getViewLifecycleOwner(), new Observer<List<Key>>() {
            @Override
            public void onChanged(List<Key> keys) {
                adapter.setKeys(keys);
            }
        });

        adapter.setOnItemClickListener(new KeyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Key key) {
                Intent intent = new Intent(getContext(), ActivityAddEditKey.class);
                intent.putExtra(ActivityAddEditKey.EXTRA_KEY_ID, key.getId());
                intent.putExtra(ActivityAddEditKey.EXTRA_KEY_TYPE, key.getKey_type());
                intent.putExtra(ActivityAddEditKey.EXTRA_KEY_COMMENT, key.getComment());
                intent.putExtra(ActivityAddEditKey.EXTRA_PUBLIC_KEY, key.getPublic_key());
                intent.putExtra(ActivityAddEditKey.EXTRA_PRIVATE_KEY, key.getPrivate_key());
                startActivityForResult(intent, ActivityMain.EDIT_KEY_REQUEST);
            }

            @Override
            public void onKeyClick(int type, String key) {
                mainVM.setKey_type(type);
                mainVM.setKey(key);
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

}
