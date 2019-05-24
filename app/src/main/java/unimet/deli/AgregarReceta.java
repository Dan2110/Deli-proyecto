package unimet.deli;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AgregarReceta extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 234;
    private static final int PICK_ING_REQUEST = 200 ;
    private EditText Anombre,Acomensales;
    private Button addIng,addPas;
    private RadioGroup grupoCategoria;
    private RadioButton aux;
    private ImageButton imagen;
    private Uri filepath;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private Intent propuesta;
    private ArrayList<String> receta;
    private ArrayList<String> categorias = new ArrayList<>();
    private LinearLayout layoutIng,layoutPas;
    private ArrayList<EditText> cantidades = new ArrayList<>();
    private Spinner spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_receta);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        propuesta = new Intent(AgregarReceta.this,Seleccion.class);
        receta = new ArrayList<>();
        grupoCategoria = (RadioGroup) findViewById(R.id.GroupCategoria);
        addIng = (Button) findViewById(R.id.addIng);
        layoutIng = (LinearLayout) findViewById(R.id.layoutIng);
        addPas = (Button) findViewById(R.id.addPas);
        layoutPas = (LinearLayout) findViewById(R.id.layoutPas);
        Anombre = (EditText) findViewById(R.id.Anombre);
        Acomensales = (EditText) findViewById(R.id.Acomensales);
        imagen =(ImageButton) findViewById(R.id.Aimagen);
        spin = (Spinner) findViewById(R.id.spinnerC);
        categorias.add("Desayuno");
        categorias.add("Almuerzo");
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarArchivo();
            }
        });
       ArrayAdapter adapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1,categorias);
        spin.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        addIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(AgregarReceta.this, Seleccion.class);
                inten.putExtra("Origen","AgregarReceta");
                startActivityForResult(inten,PICK_ING_REQUEST);
                overridePendingTransition(R.animator.zoom_forward_in, R.animator.zoom_forward_out);
            }
        });
        addPas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText tPaso = new EditText(AgregarReceta.this,null);
                tPaso.setHint("Paso #");
                layoutPas.addView(tPaso);
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            filepath = data.getData();
            Picasso.with(this).load(filepath).fit().centerCrop().into(imagen);

        }else if(requestCode==PICK_ING_REQUEST && resultCode==RESULT_OK && data!=null){
            int cont=0;
            addIng.setEnabled(false);
            for(String ingrediente: data.getStringArrayListExtra("Ing")){
                TextView label = new TextView(this,null);
                EditText texto = new EditText(this, null);
                label.setText(ingrediente);
                texto.setId(cont);
                layoutIng.addView(label);
                layoutIng.addView(texto);
            }
        }
    }

    private void subirImagen(){

        if(filepath!= null) {
            StorageReference riversRef = mStorageRef.child("Platos/"+mAuth.getCurrentUser().getEmail()+"/"+Anombre.getText());
            final ProgressDialog progreso = new ProgressDialog(this);
            progreso.setTitle("Subiendo...");

            riversRef.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        @SuppressWarnings("VisibleForTests")
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            progreso.dismiss();
                            receta.add(Anombre.getText().toString());
                            aux = (RadioButton) findViewById(grupoCategoria.getCheckedRadioButtonId());
                            if(aux!=null) receta.add(aux.getText().toString()); else Log.d("Error","CodigoA");
                           // receta.add("Desayuno");
                            receta.add(Acomensales.getText().toString());
                            receta.add(downloadUrl.toString());
                            Log.d("Parcial",receta.toString());
                            propuesta.putStringArrayListExtra("Parcial",receta);
                            propuesta.putExtra("Origen","Agregar Receta");
                            startActivity(propuesta);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            progreso.dismiss();
                            Toast.makeText(AgregarReceta.this,exception.getMessage() , Toast.LENGTH_SHORT).show();
                            // ...

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        @SuppressWarnings("VisibleForTests")
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double porcentaje;
                            porcentaje = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progreso.setMessage((int) porcentaje+" % Descargado ...");
                        }
                    });
        }else{
            //error toast
        }
    }

    public void mostrarArchivo(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Foto del platillo"),PICK_IMAGE_REQUEST);
    }

}
