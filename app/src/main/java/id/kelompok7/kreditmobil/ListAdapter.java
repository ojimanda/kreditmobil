package id.kelompok7.kreditmobil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private List<ListElement> mData;
    private LayoutInflater mInflater;
    private Context context;

    public ListAdapter(List<ListElement> itemList, Fragment context) {
        this.mInflater = LayoutInflater.from(context.getActivity());
        this.context = context.getActivity();
        this.mData = itemList;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_listmainuser, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.bindData(mData.get(position));
    }

    public void setItems(List<ListElement> items) {
        mData = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImage;
        TextView brand;
        TextView merk;
        TextView harga;
        TextView tipe;

        ViewHolder(View itemView) {
            super(itemView);
            iconImage = itemView.findViewById(R.id.imageListMain);
            brand = itemView.findViewById(R.id.userBrand);
            merk = itemView.findViewById(R.id.userMerk);
            tipe = itemView.findViewById(R.id.userTipe);
            harga = itemView.findViewById(R.id.userHarga);
        }

        void bindData(final ListElement item) {
            String showBrand = item.getBrand().substring(0,1).toUpperCase() + item.getBrand().substring(1).toLowerCase();
            String showMerk = item.getMerk().substring(0,1).toUpperCase() + item.getMerk().substring(1).toLowerCase();
            String showTipe = item.getTipe().substring(0,1).toUpperCase() + item.getTipe().substring(1).toLowerCase();
            Picasso.get().load(item.getImage()).into(iconImage);
            brand.setText(showBrand);
            merk.setText(showMerk);
            tipe.setText(showTipe);
            harga.setText(item.getHarga());
        }
    }
}