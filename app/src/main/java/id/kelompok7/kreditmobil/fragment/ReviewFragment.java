package id.kelompok7.kreditmobil.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import id.kelompok7.kreditmobil.R;
import id.kelompok7.kreditmobil.UserDashboard;
import id.kelompok7.kreditmobil.config.DBHelper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    DBHelper db;
    Spinner spBrand, spTipe, spMerk, spDP, spTenor;
    TextView txBrand, txMerk, txDP, txTenor, txCicilan;
    Button btHitung;
    ImageView imgCar;
    ProgressDialog progressDialog;

    public ReviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReviewFragment newInstance(String param1, String param2) {
        ReviewFragment fragment = new ReviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.activity_review, container, false);

        // initialize variable
        spBrand = view.findViewById(R.id.brandSp);
        spTipe = view.findViewById(R.id.tipeSp);
        spMerk = view.findViewById(R.id.merkSp);
        spDP = view.findViewById(R.id.dpSp);
        spTenor = view.findViewById(R.id.tenorSp);
        btHitung = view.findViewById(R.id.btHitung);
        txBrand = view.findViewById(R.id.txBrand);
        txMerk = view.findViewById(R.id.txMerk);
        txCicilan = view.findViewById(R.id.txCicilan);
        txDP = view.findViewById(R.id.txDp);
        txTenor = view.findViewById(R.id.txTenor);
        txBrand.setVisibility(View.INVISIBLE);
        txMerk.setVisibility(View.INVISIBLE);
        txCicilan.setVisibility(View.INVISIBLE);
        txTenor.setVisibility(View.INVISIBLE);
        txDP.setVisibility(View.INVISIBLE);

        // set spinner value

        List<String> listBrand = new ArrayList<>();
        listBrand.add("Honda");
        listBrand.add("Mazda");
        listBrand.add("Mitsubishi");

        ArrayAdapter<String> adapterBrand = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item,
                listBrand);
        adapterBrand.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spBrand.setAdapter(adapterBrand);

        // set value spinner tipe

        List<String> listTipe = new ArrayList<>();
        listTipe.add("SUV");
        listTipe.add("Sedan");

        ArrayAdapter<String> adapterTipe = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item,
                listTipe);
        adapterTipe.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spTipe.setAdapter(adapterTipe);

        // set value spinner merk

        List<String> listMerk = new ArrayList<>();
        listMerk.add("SUV");
        listMerk.add("Sedan");

        ArrayAdapter<String> adapterMerk = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item,
                listMerk);
        adapterMerk.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spMerk.setAdapter(adapterMerk);

        db = new DBHelper(getContext());





        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Mohon Menunggu");
        progressDialog.setMessage("Menghitung Cicilan");


        UserDashboard userDashboard = (UserDashboard) getActivity();
        assert userDashboard != null;
        String uid = userDashboard.getUid();

        btHitung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String brand = spBrand.getSelectedItem().toString();
                String merk = spMerk.getSelectedItem().toString();
                String dp = spDP.getSelectedItem().toString();
                String tenor = spTenor.getSelectedItem().toString();
                String cicilan =cicilanPerBulan(300000000, Integer.parseInt(dp), Integer.parseInt(tenor));

                String uuid = UUID.randomUUID().toString().split("-")[0];

                boolean insertSuccess = db.insertHistory(uuid, uid, brand, merk,dp, tenor, cicilan);

                if(insertSuccess) {
                    Toast.makeText(userDashboard, "Berhasil", Toast.LENGTH_SHORT).show();
                    txBrand.setVisibility(View.VISIBLE);
                    txMerk.setVisibility(View.VISIBLE);
                    txCicilan.setVisibility(View.VISIBLE);
                    txTenor.setVisibility(View.VISIBLE);
                    txDP.setVisibility(View.VISIBLE);
                    txBrand.setText(brand);
                    txMerk.setText(merk);
                    txDP.setText(dp);
                    txTenor.setText(tenor);
                    txCicilan.setText(cicilan);
                } else {
                    Toast.makeText(userDashboard, "Gagal menghitung data", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });




        return view;

    }

    public String cicilanPerBulan(int harga, int dp, int tenor) {

        double bayarAwal = ((100 - dp) * 0.01) * harga;
        double bayarAkhir = bayarAwal + (bayarAwal * 0.2);
        double cicilan = bayarAkhir / tenor;
        return String.valueOf(Math.round(cicilan));
    };
}