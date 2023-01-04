package id.kelompok7.kreditmobil.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.kelompok7.kreditmobil.R;
import id.kelompok7.kreditmobil.model.ModelHistory;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<ModelHistory> mData;
    private LayoutInflater inflater;
    private Context context;


    public HistoryAdapter(List<ModelHistory> list, Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = list;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_history, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {
        holder.bindData(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setItems(List<ModelHistory> list) {
        mData = list;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView brand, merk, dp, cicilan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            brand = itemView.findViewById(R.id.history_brand);
            merk = itemView.findViewById(R.id.history_merk);
            dp = itemView.findViewById(R.id.history_uangmuka);
            cicilan = itemView.findViewById(R.id.history_cicilan);
        }

        public void bindData(ModelHistory list) {
            brand.setText(list.getBrand());
            merk.setText(list.getMerk());
            dp.setText(list.getDp());
            cicilan.setText(list.getCicilan());
        }
    }
}
