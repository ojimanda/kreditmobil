package id.kelompok7.kreditmobil.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.kelompok7.kreditmobil.DetailApproval;
import id.kelompok7.kreditmobil.R;
import id.kelompok7.kreditmobil.model.ModelApprovalAdmin;
import id.kelompok7.kreditmobil.model.ModelApprovalUser;

public class AdapterApprovalAdmin extends RecyclerView.Adapter<AdapterApprovalAdmin.ViewHolder> {

    private List<ModelApprovalAdmin> mData;
    private LayoutInflater inflater;
    private Context context;


    public AdapterApprovalAdmin(List<ModelApprovalAdmin> list, Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = list;
    }

    @NonNull
    @Override
    public AdapterApprovalAdmin.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_listkpm, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterApprovalAdmin.ViewHolder holder, int position) {
        holder.bindData(mData.get(position));

        holder.btDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailApproval.class);
                intent.putExtra("id", holder.btDetails.getContentDescription());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setItems(List<ModelApprovalAdmin> list) {
        mData = list;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nama, merk;
        Button btDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.nameListElementApproval);
            merk = itemView.findViewById(R.id.merkListElementApproval);
            btDetails = itemView.findViewById(R.id.btnDetailsListElementApproval);
        }

        public void bindData(ModelApprovalAdmin list) {
            nama.setText(list.getNama());
            merk.setText(list.getMerk());
            btDetails.setContentDescription(list.getId());
        }
    }
}