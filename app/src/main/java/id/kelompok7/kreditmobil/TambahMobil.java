package id.kelompok7.kreditmobil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TambahMobil extends AppCompatActivity {

    EditText etBrand, etTipe, etMerk, etHarga;
    ImageView picMob;
    ImageButton backBtn;
    Button saveBtn, chooseBtn;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef;
    private ProgressDialog progressDialog;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambahmobil);

        etBrand = (EditText) findViewById(R.id.editTextTmbMobBrand);
        etTipe = (EditText) findViewById(R.id.editTextTmbMobTipe);
        etMerk = (EditText) findViewById(R.id.editTextTmbMobMerk);
        etHarga = (EditText) findViewById(R.id.editTextTmbMobHarga);

        saveBtn = (Button) findViewById(R.id.addBtnTmbMob);
        chooseBtn = (Button) findViewById(R.id.choosePicBtnTmbMob);

        backBtn = (ImageButton) findViewById(R.id.backBtnTmbMob);

        picMob = (ImageView) findViewById(R.id.imageViewMobTmbMob);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mohon Menunggu");
        progressDialog.setMessage("Menyimpan data");

        storageRef = storage.getReference();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TambahMobil.this, AdminDashboard.class));
                finish();
            }
        });

        chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                resultLauncher.launch(gallery);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getBrand = etBrand.getText().toString();
                String getTipe = etTipe.getText().toString();
                String getMerk = etMerk.getText().toString();
                String getHarga = etHarga.getText().toString();

                if(getBrand.equals("")) {
                    etBrand.setError("Brand tidak boleh kosong");
                } else if(getTipe.equals("")) {
                    etTipe.setError("Tipe tidak boleh kosong");
                } else if(getMerk.equals("")) {
                    etMerk.setError("Merk tidak boleh kosong");
                } else if(getHarga.equals("")){
                    etHarga.setError("Harga tidak boleh kosong");
                } else if(filePath == null){
                    Toast.makeText(TambahMobil.this, "Picture tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else{
                    StorageReference ref = storageRef.child("images/" + UUID.randomUUID().toString());
                    progressDialog.show();
                    ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String brand = getBrand.toLowerCase();
                            String tipe = getTipe.toLowerCase();
                            String merk = getMerk.toLowerCase();
                            String harga = getHarga.toLowerCase();
//                            System.out.println(ref.getPath());
                            saveData(brand, tipe, merk, harga, Uri.parse(ref.getPath()));
                        }
                    });
                }

            }
        });


    }


    ActivityResultLauncher<Intent> resultLauncher =
            registerForActivityResult(new
                            ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK ){
                        Intent data = result.getData();
                        if(data != null && data.getData() != null)
                        {
                            filePath = data.getData();
                            picMob.setImageURI(filePath);
                        }
                    }
                }
            });

    private void saveData(String brand, String tipe, String merk, String harga, Uri image) {
        Map<String, Object> mobil =new HashMap<>();

        mobil.put("harga", harga);
        mobil.put("photoUri", image);

        db.collection("mobil").document(brand).collection(tipe).document(merk).set(mobil)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(TambahMobil.this, "Berhasil menambahkan list mobil", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                startActivity(new Intent(TambahMobil.this, TambahMobil.class));
                                finish();

                            }
                        })
//                addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Toast.makeText(TambahMobil.this, "Berhasil menambahkan list mobil", Toast.LENGTH_SHORT).show();
//                        progressDialog.dismiss();
//                    }
//
//                }

//                )
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TambahMobil.this, "Gagal menambahkan list mobil", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }
}
