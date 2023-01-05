package id.kelompok7.kreditmobil.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import id.kelompok7.kreditmobil.PengajuanKredit;
import id.kelompok7.kreditmobil.R;
import id.kelompok7.kreditmobil.UserDashboard;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PengajuanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PengajuanFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView txNama, txNIK, txNPWP, txgaji;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<String> brandMobil;
    private Button btNext;

    public PengajuanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PengajuanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PengajuanFragment newInstance(String param1, String param2) {
        PengajuanFragment fragment = new PengajuanFragment();
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
        View view = inflater.inflate(R.layout.activity_pengajuan, container, false);


        // initialize variable
        txNama = view.findViewById(R.id.namaPengajuan);
        txNIK = view.findViewById(R.id.nikPengajuan);
        txNPWP = view.findViewById(R.id.npwpPengajuan);
        txgaji = view.findViewById(R.id.gajiPengajuan);
        btNext = view.findViewById(R.id.btLanjut);

        UserDashboard userDashboard = (UserDashboard) getActivity();
        assert userDashboard != null;
        String uid = userDashboard.getUid();


        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama = txNama.getText().toString();
                String nik = txNIK.getText().toString();
                String npwp = txNPWP.getText().toString();
                String gaji = txgaji.getText().toString();

                if (nama.equals("")) {
                    txNama.setError("Masukkan nama anda");
                } else if (nik.equals("")) {
                    txNIK.setError("Masukkan NIK anda");
                } else if (npwp.equals("")) {
                    txNPWP.setError("Masukkan NPWP anda");
                } else if (gaji.equals("")) {
                    txgaji.setError("Masukkan jumlah gaji anda");
                } else {
                    if (nik.length() != 16) {
                        txNIK.setError("NIK yang anda masukkan salah");
                    } else if (npwp.length() != 16) {
                        txNPWP.setError("NPWP yang anda masukkan salah");
                    } else {
                        Intent intent = new Intent(getContext(), PengajuanKredit.class);
                        intent.putExtra("uid", uid);
                        intent.putExtra("nama", nama);
                        intent.putExtra("nik", nik);
                        intent.putExtra("npwp", npwp);
                        intent.putExtra("gaji", gaji);
                        startActivity(intent);
                    }
                }
            }
        });
        return view;
    }
}