package id.kelompok7.kreditmobil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import id.kelompok7.kreditmobil.adapter.AdapterApprovalUser;
import id.kelompok7.kreditmobil.adapter.ListAdapter;
import id.kelompok7.kreditmobil.fragment.MainFragment;
import id.kelompok7.kreditmobil.model.ModelApprovalUser;

public class HistoryPengajuan extends AppCompatActivity {

    String nama, nik, npwp, gaji, brand, tipe, merk, dp, tenor, cicilan, status, uid;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentSnapshot result;
    List<ModelApprovalUser> element;
    ImageButton btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_pengajuan);
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        btnBack = findViewById(R.id.back_approval);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(HistoryPengajuan.this, UserDashboard.class);
                intent1.putExtra("uid", uid);
                startActivity(intent1);
                finish();
            }
        });

        db.collection("pengajuan")
                .whereEqualTo("pengajuanId", uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            System.out.println(task.getResult().getDocuments().size());
                            if(task.getResult().getDocuments().size() == 0) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(HistoryPengajuan.this);
                                builder.setTitle("Data Kosong");
                                builder.setMessage("Anda belum mengajukan KPM");
                                builder.setCancelable(true);
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            } else {
                                element = new ArrayList<>();
                                result = task.getResult().getDocuments().get(0);
                                nama = result.get("nama").toString();
                                nik = result.get("nik").toString();
                                npwp = result.get("npwp").toString();
                                gaji = result.get("gaji").toString();
                                brand = result.get("brand").toString();
                                tipe = result.get("tipe").toString();
                                merk = result.get("merk").toString();
                                dp = result.get("dp").toString();
                                tenor = result.get("tenor").toString();
                                cicilan = result.get("cicilan").toString();
                                status = result.get("status").toString();

                                element.add(new ModelApprovalUser(nama, nik, npwp, gaji, brand,tipe, merk, dp, tenor, cicilan, status));

                                AdapterApprovalUser listAdapter= new AdapterApprovalUser(element, HistoryPengajuan.this);
                                RecyclerView recyclerView = findViewById(R.id.rv_approval);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(HistoryPengajuan.this));
                                recyclerView.setAdapter(listAdapter);
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(HistoryPengajuan.this);
                        builder.setTitle("Fail");
                        builder.setMessage("Gagal menampilkan data");
                        builder.setCancelable(true);
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });

    }
}