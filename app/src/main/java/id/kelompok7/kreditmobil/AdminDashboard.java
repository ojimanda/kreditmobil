package id.kelompok7.kreditmobil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class AdminDashboard extends AppCompatActivity {
    ImageView iv1, iv2;
    TextView adminTextName;
    ImageButton btn;
    String username;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentSnapshot user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        username = getIntent().getStringExtra("username");


        iv1 = (ImageView) findViewById(R.id.imageViewAdminKPM);
        iv2 = (ImageView) findViewById(R.id.imageViewAdminTmbMob);
        btn = (ImageButton) findViewById(R.id.logOutBtnAdmin);
        adminTextName = (TextView) findViewById(R.id.textViewAdminName);
        adminTextName.setText(username);

        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboard.this, ListKPM.class));
                finish();

            }
        });

        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboard.this, TambahMobil.class));
                finish();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboard.this, Login.class));
                finish();
            }
        });

    }
}