package id.kelompok7.kreditmobil;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PengajuanKredit extends AppCompatActivity {

    Spinner spBrand, spTipe, spMerk, spDP, spTenor;
    Button btPengajuan;
    String brand, tipe, merk, dp, tenor, cicilan;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buycar);

        spBrand = findViewById(R.id.spBrand);
        spTipe = findViewById(R.id.spTipe);
        spMerk = findViewById(R.id.spMerkMobil);
        spDP = findViewById(R.id.spUangMuka);
        spTenor = findViewById(R.id.spTenor);
        btPengajuan = findViewById(R.id.btPengajuan);

        // set value spinner brand
        List<String> listBrand = new ArrayList<>();
        listBrand.add("Honda");
        listBrand.add("Mazda");
        listBrand.add("Mitsubishi");

        ArrayAdapter<String> adapterBrand = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                listBrand);
        adapterBrand.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spBrand.setAdapter(adapterBrand);

        // set value spinner tipe

        List<String> listTipe = new ArrayList<>();
        listTipe.add("SUV");
        listTipe.add("Sedan");

        ArrayAdapter<String> adapterTipe = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                listTipe);
        adapterTipe.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spTipe.setAdapter(adapterTipe);

        // set value spinner merk

        List<String> listMerk = new ArrayList<>();
        listMerk.add("SUV");
        listMerk.add("Sedan");

        ArrayAdapter<String> adapterMerk = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                listMerk);
        adapterMerk.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spMerk.setAdapter(adapterMerk);


        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");
        String nama = intent.getStringExtra("nama");
        String nik = intent.getStringExtra("nik");
        String npwp = intent.getStringExtra("npwp");
        String gaji = intent.getStringExtra("gaji");



        btPengajuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> ajuan = new HashMap<>();
                brand = spBrand.getSelectedItem().toString();
                tipe = spTipe.getSelectedItem().toString();
                merk = spMerk.getSelectedItem().toString();
                dp = spDP.getSelectedItem().toString();
                tenor = spTenor.getSelectedItem().toString();
                cicilan = cicilanPerBulan(300000000, Integer.parseInt(dp), Integer.parseInt(tenor));

                ajuan.put("brand", brand);
                ajuan.put("tipe", tipe);
                ajuan.put("merk", merk);
                ajuan.put("dp", dp);
                ajuan.put("tenor", tenor);
                ajuan.put("cicilan", cicilan);
                ajuan.put("nama", nama);
                ajuan.put("nik", nik);
                ajuan.put("npwp", npwp);
                ajuan.put("gaji", gaji);
                ajuan.put("status", "pending");

                AlertDialog.Builder builder = new AlertDialog.Builder(PengajuanKredit.this);
                builder.setTitle("KONFIRMASI");
                builder.setMessage("Apakah anda yakin?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.collection("users").document(uid)
                                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            DocumentSnapshot res = task.getResult();
                                            Object hasil = res.get("pengajuan");
                                            assert hasil != null;
                                            String getHasil = hasil.toString();
                                            if(getHasil.contains(nik)) {
                                                Toast.makeText(PengajuanKredit.this, "Mohon maaf anda telah mengajukan KPM", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(PengajuanKredit.this, "OKE", Toast.LENGTH_SHORT).show();
                                                db.collection("users").document(uid)
                                                        .update("pengajuan", ajuan)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Toast.makeText(PengajuanKredit.this, "Pengajuan anda sedang diproses", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(PengajuanKredit.this, "Pengajuan gagal", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        } else {
                                            Toast.makeText(PengajuanKredit.this, "Diterima", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(PengajuanKredit.this, "OKE", Toast.LENGTH_SHORT).show();
                                        db.collection("users").document(uid)
                                                .update("pengajuan", ajuan)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(PengajuanKredit.this, "Pengajuan anda sedang diproses", Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(PengajuanKredit.this, "Pengajuan gagal", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                });
                    }
                });
                builder.setNeutralButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });


    }

    public String cicilanPerBulan(int harga, int dp, int tenor) {

        double bayarAwal = ((100 - dp) * 0.01) * harga;
        double bayarAkhir = bayarAwal + (bayarAwal * 0.2);
        double cicilan = bayarAkhir / tenor;
        return String.valueOf(Math.round(cicilan));
    };
}
