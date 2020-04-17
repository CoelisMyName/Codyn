package com.coel.codyn.fragment.key;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coel.codyn.R;
import com.coel.codyn.room.Key;

import java.util.ArrayList;
import java.util.List;

public class KeyAdapter extends RecyclerView.Adapter<KeyAdapter.KeyHolder> {
    private List<Key> keys = new ArrayList<>();
    private OnItemClickListener listener;


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
        holder.type.setText(currKey.getKey_type());
        String pri = currKey.getPrivate_key();
        String pub = currKey.getPublic_key();
        if (pri == null || pri.length() == 0) {
            holder.pri.setVisibility(View.GONE);
            holder.pri_key.setVisibility(View.GONE);
        } else {
            holder.pri_key.setText(pri);
        }
        if (pub == null || pub.length() == 0) {
            holder.pub.setVisibility(View.GONE);
            holder.pub_key.setVisibility(View.GONE);
        } else {
            holder.pub_key.setText(pub);
        }

    }

    @Override
    public int getItemCount() {
        return keys.size();
    }

    public void setKeys(List<Key> keys) {
        this.keys = keys;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Key key);

        void onKeyClick(int type, String key);
    }

    public class KeyHolder extends RecyclerView.ViewHolder {
        private TextView comment;
        private TextView type;
        private ImageView image;
        private TextView pub;
        private TextView pri;
        private TextView pub_key;
        private TextView pri_key;

        public KeyHolder(@NonNull View itemView) {
            super(itemView);

            comment = itemView.findViewById(R.id.text_comment);
            type = itemView.findViewById(R.id.text_key_type);
            image = itemView.findViewById(R.id.icon_key);
            pub = itemView.findViewById(R.id.text_public_key_tag);
            pri = itemView.findViewById(R.id.text_private_key_tag);
            pub_key = itemView.findViewById(R.id.text_public_key);
            pri_key = itemView.findViewById(R.id.text_private_key);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION)
                        listener.onItemClick(keys.get(pos));
                }
            });

            pub_key.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION) {
                        listener.onKeyClick(keys.get(pos).getKey_type(),
                                keys.get(pos).getPublic_key());
                    }
                }
            });

            pri_key.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION) {
                        listener.onKeyClick(keys.get(pos).getKey_type(),
                                keys.get(pos).getPublic_key());
                    }
                }
            });

        }
    }
}
