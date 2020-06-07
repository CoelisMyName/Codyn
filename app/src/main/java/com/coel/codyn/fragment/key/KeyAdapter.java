package com.coel.codyn.fragment.key;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coel.codyn.R;
import com.coel.codyn.activitydata.main.Info;
import com.coel.codyn.appUtil.cypherUtil.Coder;
import com.coel.codyn.appUtil.cypherUtil.KeyUtil;
import com.coel.codyn.room.Key;

import java.util.ArrayList;
import java.util.List;

public class KeyAdapter extends RecyclerView.Adapter<KeyAdapter.KeyHolder> {
    private List<Key> keys = new ArrayList<>();
    private KeyListListener listener;


    @NonNull
    @Override
    public KeyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_key, parent, false);
        return new KeyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull KeyHolder holder, int position) {
        Key currKey = keys.get(position);
        holder.comment.setText(currKey.getComment());
        holder.type.setText(KeyUtil.type2Str(currKey.getKey_type()));

        String pri = currKey.getPrivate_key();
        String pub = currKey.getPublic_key();
        if (KeyUtil.isSymmetric(currKey.getKey_type())) {
            holder.pub.setVisibility(View.GONE);
            holder.pri.setText(Key.SYMMETRIC_KEY_STR + ": " + pri);
        } else {
            holder.pri.setText(Key.PRIVATE_KEY_STR + ": " + pri);
            holder.pub.setText(Key.PUBLIC_KEY_STR + ": " + pub);

            if (pri.length() == 0) {
                holder.pri.setVisibility(View.GONE);
            }
            if (pub.length() == 0) {
                holder.pub.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return keys.size();
    }

    public Key getKey(int i){
        return keys.get(i);
    }

    public void setKeys(List<Key> keys) {
        this.keys = keys;
        notifyDataSetChanged();
    }

    public void setKeyListListener(KeyListListener listener) {
        this.listener = listener;
    }

    //接口
    public interface KeyListListener {

        void editKey(Key key);

        void updateInfo(Info i);

        void clipBoard(String key);

        void popMenu(View view, int t, int a,String k);
    }

    public class KeyHolder extends RecyclerView.ViewHolder {
        private TextView comment;
        private TextView type;
        private TextView pub;
        private TextView pri;

        private KeyHolder(@NonNull View itemView) {
            super(itemView);

            comment = itemView.findViewById(R.id.text_comment);
            type = itemView.findViewById(R.id.text_key_type);
            pub = itemView.findViewById(R.id.text_public_key);
            pri = itemView.findViewById(R.id.text_private_key);

            //注册点击长按事件

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION)
                        listener.editKey(keys.get(pos));
                }
            });

            pub.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION) {
                        //listener.clipBoard(keys.get(pos).getPublic_key());
                        listener.popMenu(v,keys.get(pos).getKey_type(),Key.PUBLIC_KEY,keys.get(pos).getPublic_key());
                    }
                    return true;
                }
            });

            pub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION) {
                        Key key = keys.get(pos);
                        Info info = new Info(key.getKey_type(), Key.PUBLIC_KEY,
                                Coder.Base64_decode2bin(key.getPublic_key()));
                        listener.updateInfo(info);
                    }
                }
            });

            pri.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION) {
                        //listener.clipBoard(keys.get(pos).getPrivate_key());
                        listener.popMenu(v,keys.get(pos).getKey_type(),Key.PRIVATE_KEY,keys.get(pos).getPrivate_key());
                    }
                    return true;
                }
            });

            pri.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION) {
                        Key key = keys.get(pos);
                        Info info = new Info(key.getKey_type(),
                                KeyUtil.isSymmetric(key.getKey_type()) ? Key.SYMMETRIC_KEY : Key.PRIVATE_KEY,
                                Coder.Base64_decode2bin(key.getPrivate_key()));
                        listener.updateInfo(info);
                    }
                }
            });

        }
    }
}
