package id.kelompok7.kreditmobil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

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

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Toast.makeText(UserDashboard.this, "Home", Toast.LENGTH_SHORT).show();
                        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
                        setFragment(new MainFragment());
                        break;
                    case R.id.nav_pengajuan:
                        Toast.makeText(UserDashboard.this, "Pengajuan", Toast.LENGTH_SHORT).show();
                        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
                        setFragment(new PengajuanFragment());
                        break;
                    case R.id.nav_history:
                        Toast.makeText(UserDashboard.this, "History", Toast.LENGTH_SHORT).show();
                        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
                        setFragment(new HistoryFragment());
                        break;
                    case R.id.nav_review:
                        Toast.makeText(UserDashboard.this, "Review", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(UserDashboard.this, "Log Out", Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

    }

    private void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();
    }
}