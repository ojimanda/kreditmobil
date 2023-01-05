package id.kelompok7.kreditmobil.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.kelompok7.kreditmobil.R;
import id.kelompok7.kreditmobil.model.ModelApprovalUser;

public class AdapterApprovalUser extends RecyclerView.Adapter<AdapterApprovalUser.ViewHolder> {

    private List<ModelApprovalUser> mData;
    private LayoutInflater inflater;
    private Context context;


    public AdapterApprovalUser(List<ModelApprovalUser> list, Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = list;
    }

    @NonNull
    @Override
    public AdapterApprovalUser.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_approval_user, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterApprovalUser.ViewHolder holder, int position) {
        holder.bindData(mData.get(position));

        if(holder.status.getText().toString().equals("pending")) {
            holder.status.setTextColor(Color.YELLOW);
        } else if(holder.status.getText().toString().equals("denied")) {
            holder.status.setTextColor(Color.RED);
        } else if(holder.status.getText().toString().equals("approved")) {
            holder.status.setTextColor(Color.GREEN);
        } else {
            holder.status.setTextColor(Color.TRANSPARENT);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setItems(List<ModelApprovalUser> list) {
        mData = list;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nama, nik, npwp, gaji, brand, tipe, merk, dp, tenor, cicilan, status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.idNama);
            nik = itemView.findViewById(R.id.idNIK);
            npwp = itemView.findViewById(R.id.idNPWP);
            gaji = itemView.findViewById(R.id.idGaji);
            brand = itemView.findViewById(R.id.idBrand);
            tipe = itemView.findViewById(R.id.idTipe);
            merk = itemView.findViewById(R.id.idMerk);
            dp = itemView.findViewById(R.id.idDP);
            tenor = itemView.findViewById(R.id.idTenor);
            cicilan = itemView.findViewById(R.id.idCicilan);
            status = itemView.findViewById(R.id.idStatus);
        }

        public void bindData(ModelApprovalUser list) {
            nama.setText(list.getNama());
            nik.setText(list.getNik());
            npwp.setText(list.getNpwp());
            gaji.setText(list.getGaji());
            brand.setText(list.getBrand());
            tipe.setText(list.getTipe());
            merk.setText(list.getMerk());
            dp.setText(list.getDp());
            tenor.setText(list.getTenor());
            cicilan.setText(list.getCicilan());
            status.setText(list.getStatus());
        }
    }
}
