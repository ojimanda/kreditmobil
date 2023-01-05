package id.kelompok7.kreditmobil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import id.kelompok7.kreditmobil.fragment.HistoryFragment;
import id.kelompok7.kreditmobil.fragment.MainFragment;
import id.kelompok7.kreditmobil.fragment.PengajuanFragment;
import id.kelompok7.kreditmobil.fragment.ReviewFragment;

public class UserDashboard extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    LinearLayout logOut;
    String uid;
    String namaUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        Toast.makeText(this, uid, Toast.LENGTH_SHORT).show();
        setFragment(new MainFragment());

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigation_view);
        logOut = findViewById(R.id.logOut);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.app_open, R.string.app_close);

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();





        View headerView =navigationView.getHeaderView(0);
        TextView txHeader = headerView.findViewById(R.id.headerName);


        db.collection("users")
                        .document(uid).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot result = task.getResult();
                                String name = Objects.requireNonNull(result.get("nama")).toString();
                                txHeader.setText("Hello, "+ name);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        txHeader.setText("Hello, Brow");
                    }
                });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
                        setFragment(new MainFragment());
                        break;
                    case R.id.nav_pengajuan:
                        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
                        setFragment(new PengajuanFragment());
                        break;
                    case R.id.nav_list:
                        Intent intent1 = new Intent(UserDashboard.this, HistoryPengajuan.class);
                        intent1.putExtra("uid", uid);
                        startActivity(intent1);
                        finish();
                        break;
                    case R.id.nav_history:
                        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
                        setFragment(new HistoryFragment());
                        break;
                    case R.id.nav_review:
                        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
                        toolbar.setTitle("Data Pengajuan dan Rincian Cicilan");
                        setFragment(new ReviewFragment());
                        break;

                }
                drawerLayout.closeDrawer(GravityCompat.START);
                item.setChecked(true);
                return true;
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(UserDashboard.this);
                builder.setTitle("Log Out");
                builder.setMessage("Are you sure?");
                builder.setCancelable(true);
                builder.setNeutralButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        drawerLayout.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(UserDashboard.this, Login.class));
                        finish();
                    }
                });
                builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog alertDialog1 = builder.create();
                alertDialog1.show();
            }
        });

    }

    private void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();
    }

    public String getUid() {
        return uid;
    }

}