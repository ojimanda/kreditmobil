package id.kelompok7.kreditmobil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailApproval extends AppCompatActivity {

    String id;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView nik, nama, npwp, gaji, brand, jenis, dp, tenor;
    Button btTerima, btTolak;
    ImageButton btBack;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listdetails);

        Intent intent = getIntent();
        btBack = findViewById(R.id.btnBackListDetail);
        btTerima = findViewById(R.id.btnApproveListDetail);
        btTolak = findViewById(R.id.btnDenyListDetail);
        nik = findViewById(R.id.approvalNik);
        nama = findViewById(R.id.approvalNama);
        npwp = findViewById(R.id.approvalNpwp);
        gaji = findViewById(R.id.approvalGaji);
        brand = findViewById(R.id.approvalBrand);
        jenis = findViewById(R.id.approvalJenis);
        dp = findViewById(R.id.approvalDp);
        tenor = findViewById(R.id.approvalTenor);

        id = intent.getStringExtra("id");
        pDialog = new ProgressDialog(this);
        pDialog.setTitle("LOADING");
        pDialog.setMessage("Mohon Menunggu");

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetailApproval.this, ListKPM.class));
                finish();
            }
        });

        db.collection("pengajuan")
                .document(id)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot result = task.getResult();
                        nik.setText(result.get("nik").toString());
                        nama.setText(result.get("nama").toString());
                        npwp.setText(result.get("npwp").toString());
                        gaji.setText(result.get("gaji").toString());
                        brand.setText(result.get("brand").toString());
                        jenis.setText(result.get("merk").toString());
                        dp.setText(result.get("dp").toString());
                        tenor.setText(result.get("tenor").toString());

                    }
                });


        btTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialog.show();
                db.collection("pengajuan")
                        .document(id)
                        .update("status", "approved")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(DetailApproval.this, "Pengajuan diterima", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(DetailApproval.this, ListKPM.class));
                                finish();
                                pDialog.dismiss();
                            }
                        });
            }
        });

        btTolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialog.show();
                db.collection("pengajuan")
                        .document(id)
                        .update("status", "denied")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(DetailApproval.this, "Pengajuan ditolak", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(DetailApproval.this, ListKPM.class));
                                finish();
                                pDialog.dismiss();
                            }
                        });
            }
        });
    }
}
