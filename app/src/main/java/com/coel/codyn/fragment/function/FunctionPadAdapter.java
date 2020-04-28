package com.coel.codyn.fragment.function;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.coel.codyn.R;

import org.spongycastle.util.encoders.Hex;

public class FunctionPadAdapter extends RecyclerView.Adapter<FunctionPadAdapter.ViewHolder> {
    private FunctionPad pad;
    private PadListener listener;

    void setListener(PadListener listener) {
        this.listener = listener;
    }

    public void setPad(FunctionPad pad) {
        this.pad = pad;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.function_pad, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.editText.setText(FunctionPad.getSaveStr());
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binreg.setText(new String(Hex.encode(pad.getBin())));
        holder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (listener != null)
                    listener.saveStr(s.toString());
            }
        });
        holder.txt_out.setText(pad.getOuttxt());
        holder.cardView.setVisibility(pad.getBin().length == 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public interface PadListener {
        void saveStr(String s);

        void clipBoard(String s);

        void base64Enc(String s);

        void base64Dec(String s);

        void base64Dec2Reg(String s);

        void sha256Txt(String s);

        void sha256Bin(byte[] bin);

        void encrypt(byte[] bin);

        void decrypt(byte[] bin);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //第一个框的控件
        EditText editText;
        Button base64_enc;
        Button base64_dec;
        Button base64_dec2reg;
        Button txt_sha256;
        Button txt_enc;
        Button txt_dec;

        //第二个框的控件
        CardView cardView;
        TextView binreg;
        Button bin_sha256;
        Button bin_enc;
        Button bin_dec;

        //第三个框的控件
        TextView txt_out;

        public ViewHolder(View view) {
            super(view);
            editText = view.findViewById(R.id.editText);
            base64_enc = view.findViewById(R.id.btn_base64_enc);
            base64_dec = view.findViewById(R.id.btn_base64_dec);
            base64_dec2reg = view.findViewById(R.id.btn_base64_dec2reg);
            txt_sha256 = view.findViewById(R.id.btn_txt_sha256);
            txt_enc = view.findViewById(R.id.btn_txt_enc);
            txt_dec = view.findViewById(R.id.btn_txt_dec);

            cardView = view.findViewById(R.id.card_reg);
            binreg = view.findViewById(R.id.txt_reg);
            bin_sha256 = view.findViewById(R.id.btn_bin_sha256);
            bin_enc = view.findViewById(R.id.btn_bin_enc);
            bin_dec = view.findViewById(R.id.btn_bin_dec);

            txt_out = view.findViewById(R.id.txt_out);

            base64_enc.setOnClickListener(this);
            base64_dec.setOnClickListener(this);
            base64_dec2reg.setOnClickListener(this);
            txt_sha256.setOnClickListener(this);
            txt_enc.setOnClickListener(this);
            txt_dec.setOnClickListener(this);
            bin_sha256.setOnClickListener(this);
            bin_enc.setOnClickListener(this);
            bin_dec.setOnClickListener(this);
            txt_out.setOnClickListener(this);
        }

        public String getText() {
            return editText.getText().toString();
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                switch (v.getId()) {
                    case R.id.btn_base64_enc:
                        listener.base64Enc(getText());
                        break;

                    case R.id.btn_base64_dec:
                        listener.base64Dec(getText());
                        break;

                    case R.id.btn_base64_dec2reg:
                        listener.base64Dec2Reg(getText());
                        break;

                    case R.id.btn_txt_sha256:
                        listener.sha256Txt(getText());
                        break;

                    case R.id.btn_txt_enc:
                        listener.encrypt(getText().getBytes());
                        break;

                    case R.id.btn_txt_dec:
                        listener.decrypt(getText().getBytes());
                        break;

                    case R.id.btn_bin_sha256:
                        listener.sha256Bin(pad.getBin());
                        break;

                    case R.id.btn_bin_enc:
                        listener.encrypt(pad.getBin());
                        break;

                    case R.id.btn_bin_dec:
                        listener.decrypt(pad.getBin());
                        break;

                    case R.id.txt_out:
                        listener.clipBoard(pad.getOuttxt());
                        break;
                }
            }
        }
    }

}
