package id.kelompok7.kreditmobil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    EditText txNama, txEmail, txPassword;
    Button btRegister;
    TextView txLogin;
    private ProgressDialog progressDialog;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // initialize variable
        txNama = findViewById(R.id.txNama);
        txEmail = findViewById(R.id.txEmail);
        txPassword = findViewById(R.id.txPassword);
        btRegister = findViewById(R.id.btRegister);
        txLogin = findViewById(R.id.txLogin);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mohon Menunggu");
        progressDialog.setMessage("Menyimpan data");


        txLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, Login.class));
                finish();
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getNama = txNama.getText().toString();
                String getEmail = txEmail.getText().toString();
                String getPassword = txPassword.getText().toString();

                if(getNama.equals("")) {
                    txNama.setError("Masukkan nama anda");
                } else if(getEmail.equals("")) {
                    txEmail.setError("Masukkan email anda");
                } else if(getPassword.equals("")) {
                    txPassword.setError("Masukkan password anda");
                } else {
                    if(getNama.length() < 6) {
                        txNama.setError("Minimal panjang nama adalah 6 karakter");
                    } else if(!emailValidation(getEmail)) {
                        txEmail.setError("Input email salah");
                    } else if(!passwordValidation(getPassword)) {
                        txPassword.setError("Input password salah");
                    } else {
                        saveData(getNama, getEmail, getPassword);
                    }
                }

            }
        });
    }

    private void saveData(String nama, String email, String password) {
        Map<String, Object> user =new HashMap<>();
        user.put("nama", nama);
        user.put("email", email);
        user.put("password", password);
        user.put("role", "customer");

        progressDialog.show();
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(Register.this, "Berhasil mendaftarkan akun", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        startActivity(new Intent(Register.this, Login.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this, "Gagal mendaftarkan akun", Toast.LENGTH_SHORT).show();
                        System.out.println(e.getLocalizedMessage());
                        progressDialog.dismiss();
                    }
                });
    }


    public Boolean emailValidation(String email) {
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public Boolean passwordValidation(String password) {
        // start with string, at least 1 digit, 1 lowercase, 1 uppercase, 1 specialCharacter, no allowed whitespace,
        // min 8 character and max 20character
        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}