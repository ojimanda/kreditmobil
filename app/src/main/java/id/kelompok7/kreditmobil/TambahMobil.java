package id.kelompok7.kreditmobil;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TambahMobil extends AppCompatActivity {

    EditText etBrand, etTipe, etMerk, etHarga;
    ImageView picMob;
    ImageButton backBtn;
    Button saveBtn, chooseBtn;
    String username;
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
        username = getIntent().getStringExtra("username");

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
                startActivity(new Intent(TambahMobil.this, AdminDashboard.class).putExtra("username", username));
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
                    Task<Uri> urlTask = ref.putFile(filePath).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if(!task.isSuccessful()){
                                throw task.getException();
                            }
                            return ref.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            String brand = getBrand.toLowerCase();
                            String tipe = getTipe.toLowerCase();
                            String merk = getMerk.toLowerCase();
                            String harga = getHarga.toLowerCase();
                            Uri downloadUri = task.getResult();
                            saveData(brand, tipe, merk, harga, downloadUri);
                            progressDialog.dismiss();
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

        Map<String, Object> listMobil =  new HashMap<>();
        listMobil.put("brand", brand);
        listMobil.put("tipe", tipe);
        listMobil.put("harga", harga);
        listMobil.put("photoUri", image);

        Map<String, Object> mobil =new HashMap<>();

        mobil.put("harga", harga);
        mobil.put("photoUri", image);

        String value = brand+"-"+tipe;
        System.out.println(value);

        db.collection("mobil").document(brand).collection(tipe).document(merk).set(mobil)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                db.collection("listMobil").document(merk).set(listMobil);

//                                db.collection("mobil").document(brand)
//                                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                                if(task.isSuccessful()) {
//                                                    System.out.println(task.getResult().get("tipe"));
//                                                    if(task.getResult().get("tipe") != null
////                                                            && task.getResult().exists()
//                                                    ) {
//                                                        db.collection("mobil").document(brand)
//                                                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                                                    @Override
//                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                                Object res = task.getResult().get("tipe");
//                                                assert res != null;
//                                                String[] data = res.toString().replace("[", "").replace("]", "").split(",");
//
//                                                ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(data));
//                                                if(!arrayList.contains(value)){
//                                                    DocumentReference doc = db.collection("mobil").document(brand);
//                                                    doc.update("tipe", FieldValue.arrayUnion(value));
//                                                }
//                                                                    }
//                                                                });
//                                                    } else {
//                                                        DocumentReference doc = db.collection("mobil").document(brand);
//                                                        doc.update("tipe", FieldValue.arrayUnion(value));
//                                                    }
//                                                }
//                                            }
//                                        }).addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                System.out.println("ERROR");
//                                                DocumentReference doc = db.collection("mobil").document(brand);
//                                                doc.update("tipe", FieldValue.arrayUnion(value));
//                                            }
//                                        });

                                db.collection("mobil").document(brand).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            Object result = task.getResult().get("tipe");
                                            System.out.println(result);
                                            if (result == null) {
                                                Map<String, Object> doc = new HashMap<>();
                                                ArrayList<String> arrayList = new ArrayList<>();
                                                arrayList.add(value);
                                                doc.put("tipe", arrayList);
                                                db.collection("mobil").document(brand)
                                                        .set(doc)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                AlertDialog.Builder builder = new AlertDialog.Builder(TambahMobil.this);
                                                                builder.setTitle("Success");
                                                                builder.setMessage("Berhasil menambahkan mobil");
                                                                builder.setCancelable(true);
                                                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        startActivity(new Intent(TambahMobil.this, AdminDashboard.class).putExtra("username", username));
                                                                        finish();
                                                                    }
                                                                });
                                                                AlertDialog alert = builder.create();
                                                                alert.show();
                                                            }
                                                        });
                                            } else {
                                                String[] newRes = result.toString().replace("[", "").replace("]", "").split(",");
//<<<<<<< HEAD
                                                ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(newRes));
                                                arrayList.add(value);
                                                DocumentReference doc = db.collection("mobil").document(brand);
                                                doc.update("tipe", FieldValue.arrayUnion(value));
                                                System.out.println("SUCCESS UPDATE DATA");
                                                AlertDialog.Builder builder = new AlertDialog.Builder(TambahMobil.this);
                                                builder.setTitle("Success");
                                                builder.setMessage("Berhasil Update mobil");
                                                builder.setCancelable(true);
                                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        startActivity(new Intent(TambahMobil.this, AdminDashboard.class).putExtra("username", username));
                                                        finish();
                                                    }
                                                });
                                                AlertDialog alert = builder.create();
                                                alert.show();
//                                                System.out.println(newRes);
//                                                Map<String, Object> doc =  new HashMap<>();
//                                                ArrayList<String> data = new ArrayList<>();
//                                                for(String res: newRes) {
//                                                    data.add(res);
//                                                }
//                                                data.add(value);
//                                                doc.put("tipe", data);
//                                                db.collection("mobil").document(brand)
//                                                        .update("tipe", doc)
//                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                            @Override
//                                                            public void onSuccess(Void unused) {
//                                                                System.out.println("SUCCESS UPDATE DATA");
//                                                            }
//                                                        });
//=======
//                                                System.out.println(newRes);
//                                                Map<String, Object> doc = new HashMap<>();
//                                                ArrayList<String> data = new ArrayList<>();
//                                                for (String res : newRes) {
//                                                    data.add(res);
//                                                }
//                                                data.add(value);
//                                                doc.put("tipe", data);
//                                                db.collection("mobil").document(brand)
//                                                        .update("tipe", doc)
//                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                            @Override
//                                                            public void onSuccess(Void unused) {
//
//                                                            }
//                                                        });
//>>>>>>> ab019badfd30296a53b57946be37e748fb111aa7
                                            }
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(TambahMobil.this);
                                        builder.setTitle("Fail");
                                        builder.setMessage("Gagal menambahkan mobil");
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

//                                db.collection("mobil").document(brand)
//                                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                                if (task.isSuccessful()) {
//                                                    if (task.getResult().get("tipe") != null && task.getResult().exists()) {
//                                                        db.collection("mobil").document(brand)
//                                                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                                                    @Override
//                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                                                        Object res = task.getResult().get("tipe");
//                                                                        assert res != null;
//                                                                        String[] data = res.toString().replace("[", "").replace("]", "").split(",");
//                                                                        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(data));
//                                                                        if (!arrayList.contains(value)) {
//                                                                            DocumentReference doc = db.collection("mobil").document(brand);
//                                                                            doc.update("tipe", FieldValue.arrayUnion(value));
//                                                                        }
//                                                                    }
//                                                                });
//                                                    } else {
//                                                        DocumentReference doc = db.collection("mobil").document(brand);
//                                                        doc.update("tipe", FieldValue.arrayUnion(value));
//                                                    }
//                                                }
//                                            }
//                                        }).addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//
//                                            }
//                                        });
                            }
                        });

    }
}
