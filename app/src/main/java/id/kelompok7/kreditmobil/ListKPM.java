package id.kelompok7.kreditmobil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import id.kelompok7.kreditmobil.adapter.AdapterApprovalAdmin;
import id.kelompok7.kreditmobil.adapter.AdapterApprovalUser;
import id.kelompok7.kreditmobil.model.ModelApprovalAdmin;

public class ListKPM extends AppCompatActivity {

    String nama, merk, id;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private QuerySnapshot result;
    String username;
    private List<ModelApprovalAdmin> element;
    ImageButton backBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval);
        username = getIntent().getStringExtra("username");
        backBtn = (ImageButton) findViewById(R.id.backButtonListApproval);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListKPM.this, AdminDashboard.class);
                intent.putExtra("username",username);
                startActivity(intent);
                finish();
            }
        });

        db.collection("pengajuan")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            element = new ArrayList<>();
                            result = task.getResult();
                            for(DocumentSnapshot document: result) {
                                id = document.getId();
                                nama = document.get("nama").toString();
                                merk = document.get("merk").toString();
                                if(document.get("status").toString().equals("pending")) {
                                    element.add(new ModelApprovalAdmin(id, nama, merk));
                                }

                                AdapterApprovalAdmin listAdapter= new AdapterApprovalAdmin(element, ListKPM.this);
                                RecyclerView recyclerView = findViewById(R.id.recyclerListApproval);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(ListKPM.this));
                                recyclerView.setAdapter(listAdapter);
                            }
                        }
                    }
                });
    }

}
