package com.coel.codyn.fragment.key;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coel.codyn.ActivityAddEditKey;
import com.coel.codyn.ActivityDisplayQR;
import com.coel.codyn.R;
import com.coel.codyn.activitydata.main.Info;
import com.coel.codyn.appUtil.JSONUtil;
import com.coel.codyn.appUtil.QRUtil;
import com.coel.codyn.appUtil.SystemUtil;
import com.coel.codyn.appUtil.ViewUtil;
import com.coel.codyn.room.Key;
import com.coel.codyn.viewmodel.MainVM;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;

import java.util.List;

public class FragmentKey extends Fragment {
    public static final int ADD_KEY_REQUEST = 1;
    public static final int EDIT_KEY_REQUEST = 2;
    private KeyVM keyVM;
    private MainVM mainVM;
    private PopupMenu popupMenu;

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

        keyVM = new ViewModelProvider(requireActivity()).get(KeyVM.class);
        mainVM = new ViewModelProvider(requireActivity()).get(MainVM.class);
        keyVM.getKeys().observe(getViewLifecycleOwner(), new Observer<List<Key>>() {
            @Override
            public void onChanged(List<Key> keys) {
                adapter.submitList(keys);
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

            @Override
            public void popMenu(View view, int t, int a, String k) {
                popupMenu(view, t, a, k);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (viewHolder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                    keyVM.deleteKey(adapter.getKey(viewHolder.getAdapterPosition()));
                    ViewUtil.showToast(getContext(), "Key Delete");
                }
            }

        }).attachToRecyclerView(recyclerView);

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
        Intent intent = new Intent(getActivity(), ActivityAddEditKey.class);
        intent.putExtra(ActivityAddEditKey.EXTRA_KEY_ID, k.getId());
        intent.putExtra(ActivityAddEditKey.EXTRA_KEY_COMMENT, k.getComment());
        intent.putExtra(ActivityAddEditKey.EXTRA_KEY_TYPE, k.getKey_type());
        intent.putExtra(ActivityAddEditKey.EXTRA_PRIVATE_KEY, k.getPrivate_key());
        intent.putExtra(ActivityAddEditKey.EXTRA_PUBLIC_KEY, k.getPublic_key());
        intent.putExtra(ActivityAddEditKey.EXTRA_USER_ID, k.getUser_id());
        startActivityForResult(intent, EDIT_KEY_REQUEST);
    }

    private void startDisplayQRAct(Bitmap bm) {
        Intent intent = new Intent(getActivity(), ActivityDisplayQR.class);
        ActivityDisplayQR.bm = bm;
        /*
        //Bundle缓存区1MB，太小了惹
        Bundle bundle = new Bundle();
        bundle.putParcelable(ActivityDisplayQR.EXTRA_QR_IMAGE,bm);
        intent.putExtra(ActivityDisplayQR.BUNDLE, bundle);
        */
        startActivity(intent);
        Log.d("startDisplayQRAct", "success");
    }

    private void popupMenu(View view, int t, int a, String k) {
        if (popupMenu == null) {
            popupMenu = new PopupMenu(requireContext(), view);
            popupMenu.getMenuInflater().inflate(R.menu.key_menu, popupMenu.getMenu());
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.d("popupMenu", "success");
                switch (item.getItemId()) {
                    case R.id.copy:
                        SystemUtil.setClipboard(requireContext(), k);
                        break;
                    case R.id.qrcode:
                        try {
                            Bitmap bm = QRUtil.create(JSONUtil.JSONString(JSONUtil.createKeyJSON(t, a, k)));

                            Log.d("popupMenu", "success " + bm.getByteCount());
                            startDisplayQRAct(bm);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }
}
