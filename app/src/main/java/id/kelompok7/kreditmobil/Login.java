package id.kelompok7.kreditmobil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class Login extends AppCompatActivity {

    EditText txEmail, txPassword;
    Button btLogin;
    TextView txRegister;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private QuerySnapshot result;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // initialize variable

        txEmail = findViewById(R.id.etEmail);
        txPassword = findViewById(R.id.etPassword);
        btLogin = findViewById(R.id.btLogin);
        txRegister = findViewById(R.id.txRegister);
        
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mohon menunggu");
        progressDialog.setMessage("Memeriksa data");

        txRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
                finish();
            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getEmail = txEmail.getText().toString();
                String getPassword = txPassword.getText().toString();
                if(getEmail.equals("")) {
                    txEmail.setError("Masukkan email anda");
                } else if(getPassword.equals("")){
                    txPassword.setError("Masukkan password anda");
                }
                else {
                    progressDialog.show();
                    db.collection("users")
                            .whereEqualTo("email", getEmail)
                            .whereEqualTo("password", getPassword)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()) {
                                        result = task.getResult();
                                        DocumentSnapshot document = result.getDocuments().get(0);
                                        String uid = document.getId();
                                        String username = document.get("nama").toString();
                                        String role = document.get("role").toString();
                                        if(role.equals("admin")){
                                            Toast.makeText(Login.this, "Login berhasil", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                            Intent intent = new Intent(Login.this, AdminDashboard.class);
                                            intent.putExtra("username",username);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else{

                                            Toast.makeText(Login.this, "Login berhasil", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                            Intent intent1 = new Intent(Login.this, UserDashboard.class);
                                            intent1.putExtra("uid", uid);
                                            startActivity(intent1);
                                            finish();
                                        }
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(Login.this, "Gagal login", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(Login.this, "Email/Password anda salah", Toast.LENGTH_SHORT).show();
                                    txEmail.setText("");
                                    txPassword.setText("");
                                }
                            });
                }
            }
        });

    }
}