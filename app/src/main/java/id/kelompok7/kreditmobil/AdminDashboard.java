package id.kelompok7.kreditmobil;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.content.DialogInterface;
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
                startActivity(new Intent(AdminDashboard.this, ListKPM.class).putExtra("username", username));
                finish();

            }
        });

        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboard.this, TambahMobil.class).putExtra("username", username));
                finish();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminDashboard.this);
                builder.setTitle("Log Out");
                builder.setMessage("Are you sure?");
                builder.setCancelable(true);
                builder.setNeutralButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(AdminDashboard.this, Login.class));
                        finish();
                    }
                });
                AlertDialog alertDialog1 = builder.create();
                alertDialog1.show();

            }
        });

    }
}