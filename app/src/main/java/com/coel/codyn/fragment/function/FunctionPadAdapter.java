package com.coel.codyn.fragment.function;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coel.codyn.R;

public class FunctionPadAdapter extends RecyclerView.Adapter<FunctionPadAdapter.ViewHolder> {
    public Onclick onclick;
    private String string;

    FunctionPadAdapter() {
        string = "";
    }

    FunctionPadAdapter(String str) {
        string = str;
    }

    void setOnclick(Onclick onclick) {
        this.onclick = onclick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.function_pad, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.editText.setText(string);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public interface Onclick {
        void onClickFunc(View v, String s, TextView textView);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        EditText editText;
        Button btn_base64_enc;
        Button btn_base64_dec;
        Button btn_ecc_enc;
        Button btn_ecc_dec;
        TextView textView;

        public ViewHolder(View view) {
            super(view);
            editText = view.findViewById(R.id.editText_function);
            btn_base64_enc = view.findViewById(R.id.btn_base64_enc);
            btn_base64_dec = view.findViewById(R.id.btn_base64_dec);
            btn_ecc_enc = view.findViewById(R.id.btn_ECC_enc);
            btn_ecc_dec = view.findViewById(R.id.btn_ECC_dec);
            textView = view.findViewById(R.id.textView_function);

            btn_base64_enc.setOnClickListener(this);
            btn_base64_dec.setOnClickListener(this);
            btn_ecc_enc.setOnClickListener(this);
            btn_ecc_dec.setOnClickListener(this);
            textView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onclick != null && getAdapterPosition() != RecyclerView.NO_POSITION)
                onclick.onClickFunc(v, editText.getText().toString(), textView);
        }
    }

}
