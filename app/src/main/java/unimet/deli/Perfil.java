package unimet.deli;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


public class Perfil extends AppCompatActivity {

    private TextView nombre;
    private ImageView imagen;
    private FirebaseAuth mAuth;
    private Button cerrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        cerrar = (Button) findViewById(R.id.Pcerrar);
        nombre = (TextView) findViewById(R.id.nombre);
        imagen = (ImageView) findViewById(R.id.foto);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null) {
            nombre.setText(mAuth.getCurrentUser().getDisplayName());
           // Log.d("URL", mAuth.getCurrentUser().getPhotoUrl().toString());
            Picasso.with(this).load(mAuth.getCurrentUser().getPhotoUrl()).fit().centerCrop().into(imagen);
        }else{
            Log.d("Fallo","No hay usaurio");
        }
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(Perfil.this,MainActivity.class));
            }
        });

    }
}
