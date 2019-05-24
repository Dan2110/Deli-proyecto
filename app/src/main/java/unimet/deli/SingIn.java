package unimet.deli;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SingIn extends AppCompatActivity {

    private EditText email,clave;
    private Button iniciar,registro;
    private ManejadorDeSesion sesion;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_in);
        sesion = new ManejadorDeSesion();
        email = (EditText) findViewById(R.id.Scorreo);
        clave = (EditText) findViewById(R.id.Sclave);
        iniciar = (Button) findViewById(R.id.Sacceder);
        registro = (Button) findViewById(R.id.Sregistro);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    startActivity(new Intent(SingIn.this,MainActivity.class));
                } else {
                    // User is signed out
                }
                // ...
            }
        };

        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = email.getText().toString();
                String password = clave.getText().toString();
                sesion.logearUsuario(SingIn.this,correo,password);
            }
        });
        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SingIn.this,Registro.class));
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        sesion.getmAuth().addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        sesion.getmAuth().removeAuthStateListener(mAuthListener);
    }
}
