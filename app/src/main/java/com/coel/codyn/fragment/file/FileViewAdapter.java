package com.coel.codyn.fragment.file;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.coel.codyn.R;
import com.coel.codyn.appUtil.cypherUtil.KeyUtil;
import com.coel.codyn.repository.FileTaskRepository;
import com.coel.codyn.room.Key;
import com.coel.codyn.service.FileCryptoView;
import com.coel.codyn.service.FileTask;
import com.coel.codyn.service.TaskView;


public class FileViewAdapter extends ListAdapter<FileCryptoView, FileViewAdapter.FileViewHolder> {
    private static final DiffUtil.ItemCallback<FileCryptoView> DIFF_CALLBACK = new DiffUtil.ItemCallback<FileCryptoView>() {

        @Override
        public boolean areItemsTheSame(@NonNull FileCryptoView oldItem, @NonNull FileCryptoView newItem) {
            Log.d("fileView", "areItemsTheSame: ");
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull FileCryptoView oldItem, @NonNull FileCryptoView newItem) {
            Log.d("fileView", "areContentsTheSame: ");
            return oldItem.getAttr() == newItem.getAttr() &&
                    oldItem.getMode() == newItem.getMode() &&
                    oldItem.getType() == newItem.getType() &&
                    oldItem.getKey().equals(newItem.getKey()) &&
                    oldItem.getDest().equals(newItem.getDest()) &&
                    oldItem.getSource().equals(newItem.getSource());

        }
    };
    private FileViewListener listener;

    public FileViewAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_file, parent, false);
        Log.d("fileView", "onCreateViewHolder: ");
        return new FileViewHolder(itemView);
    }

    public FileCryptoView getFileCryptoView(int i) {
        return getItem(i);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        FileCryptoView curr = getItem(position);
        holder.sourceTxt.setText(curr.getSource());
        holder.destTxt.setText(curr.getDest());
        holder.keyTxt.setText(curr.getKey());
        holder.progressBar.setProgress(curr.getProgress());
        holder.typeTxt.setText(KeyUtil.type2Str(curr.getType()));
        switch (curr.getStat()) {
            case TaskView.FINISHED:
                holder.statTxt.setText("已完成");
                holder.button.setImageResource(R.drawable.ic_pause_black_24dp);
                holder.button.setEnabled(false);
                break;
            case TaskView.CANCELED:
                holder.statTxt.setText("已取消");
                holder.button.setImageResource(R.drawable.ic_pause_black_24dp);
                holder.button.setEnabled(false);
                break;
            case TaskView.ERROR:
                holder.statTxt.setText("错误");
                holder.button.setImageResource(R.drawable.ic_pause_black_24dp);
                holder.button.setEnabled(false);
                break;
            case TaskView.WORKING:
                holder.statTxt.setText("工作中");
                holder.button.setImageResource(R.drawable.ic_pause_black_24dp);
                break;
            case TaskView.WAITING:
                holder.statTxt.setText("等待中");
                holder.button.setImageResource(R.drawable.ic_pause_black_24dp);
                break;
            case TaskView.PAUSING:
                holder.statTxt.setText("暂停中");
                holder.button.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                break;
            default:
        }
        Log.d("fileView", "onBindViewHolder: ");
    }

    public void setListener(FileViewListener listener) {
        this.listener = listener;
    }

    public FileCryptoView getFileTask(int i) {
        return getItem(i);
    }

    public interface FileViewListener {
        void submit(FileCryptoView fileCryptoView, int s);
    }

    public class FileViewHolder extends RecyclerView.ViewHolder {
        private TextView typeTxt;
        private TextView keyTxt;
        private ImageButton button;
        private TextView statTxt;
        private TextView sourceTxt;
        private TextView destTxt;
        private ProgressBar progressBar;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            typeTxt = itemView.findViewById(R.id.type);
            keyTxt = itemView.findViewById(R.id.key);
            button = itemView.findViewById(R.id.bottom);
            statTxt = itemView.findViewById(R.id.stat);
            sourceTxt = itemView.findViewById(R.id.source);
            destTxt = itemView.findViewById(R.id.dest);
            progressBar = itemView.findViewById(R.id.progressBar);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION) {
                        FileCryptoView temp = getFileCryptoView(pos);
                        listener.submit(temp, temp.getStat() == FileTask.PAUSING ? FileTaskRepository.RESUME : FileTaskRepository.PAUSE);
                    }
                }
            });
            Log.d("fileView", "FileViewHolder: ");
        }
    }
}
