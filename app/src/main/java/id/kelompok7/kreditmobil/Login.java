package id.kelompok7.kreditmobil;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    EditText txEmail, txPassword;
    Button btLogin;
    TextView txRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // initialize variable

        txEmail = findViewById(R.id.etEmail);
        txPassword = findViewById(R.id.etPassword);
        btLogin = findViewById(R.id.btLogin);
        txRegister = findViewById(R.id.txRegister);

    }
}