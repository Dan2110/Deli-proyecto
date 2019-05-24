package unimet.deli;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Registro extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 234;
    public ImageButton Rimagen;
    public EditText temail,tpassword,tnombre;
    private Uri filepath;
    private Uri downloadUrl;
    private String email,password;
    private StorageReference mStorageRef;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private ManejadorDeSesion sesion;
    private Boolean estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        sesion = new ManejadorDeSesion();
        setSupportActionBar(toolbar);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        Rimagen = (ImageButton) findViewById(R.id.Rimagen);
        temail = (EditText) findViewById(R.id.Rcorreo);
        tpassword = (EditText) findViewById(R.id.Rcontrasena);
        tnombre = (EditText) findViewById(R.id.Rname);

        Rimagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarArchivo();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = temail.getText().toString();
                password = tpassword.getText().toString();
                crearCuenta(email,password);
                Snackbar.make(view, "Creando cuenta...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("Cambio", "onAuthStateChanged:signed_in:" + user.getUid());
                    startActivity(new Intent(Registro.this,Perfil.class));
                } else {
                    // User is signed out
                    Log.d("CambioM", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        sesion.getmAuth().addAuthStateListener(mAuthListener);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            filepath = data.getData();

            Picasso.with(this).load(filepath).fit().centerCrop().into(Rimagen);

        }

    }

    public void logearUsuario(){

        String correo = temail.getText().toString();
        String password = tpassword.getText().toString();

        sesion.getmAuth().signInWithEmailAndPassword(correo, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Logeo", "signInWithEmail:onComplete:" + task.isSuccessful());

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(tnombre.getText().toString())
                                .setPhotoUri(downloadUrl)
                                .build();

                        sesion.getmAuth().getCurrentUser().updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("Correcto", "User profile updated."+ downloadUrl);
                                            Toast.makeText(Registro.this,"Perfil Actualizado" , Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(Registro.this,Perfil.class));
                                        }
                                    }
                                });
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("Logeo", "signInWithEmail:failed", task.getException());
                            Toast.makeText(Registro.this, "autentificACION FALLIDA",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    public void crearCuenta(String email, String password){

        sesion.getmAuth().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Log in", "createUserWithEmail:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if(!task.isSuccessful()) {
                            Toast.makeText(Registro.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }else{
                            Usuario gabo = new Usuario(tnombre.getText().toString());
                            sesion.getUsuariosRef().child(sesion.getmAuth().getCurrentUser().getUid()).getRef().setValue(gabo);
                            guardarImagen();
                        }
                        // ...
                    }
                });
    }


    public void mostrarArchivo(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Seleccione una imagen"),PICK_IMAGE_REQUEST);
    }

    public void guardarImagen(){

        if(filepath!= null) {
            StorageReference perfilesRef = mStorageRef.child("Perfiles/"+temail.getText().toString());

            final ProgressDialog progreso = new ProgressDialog(this);
            progreso.show();
            Log.d("Path",filepath.getPath());
            progreso.setTitle("Subiendo Imagen al Servidor");
            perfilesRef.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        @SuppressWarnings("VisibleForTests")
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            downloadUrl = taskSnapshot.getDownloadUrl();
                            progreso.dismiss();
                            logearUsuario();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            progreso.dismiss();
                            Toast.makeText(Registro.this,exception.getMessage() , Toast.LENGTH_SHORT).show();
                            // ...

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        @SuppressWarnings("VisibleForTests")
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double porcentaje;
                            double bytes = taskSnapshot.getTotalByteCount();
                            porcentaje = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progreso.setMessage((int) porcentaje+" % Subido ...");
                        }
                     });
        }else{
            //error toast
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            sesion.getmAuth().removeAuthStateListener(mAuthListener);
        }
    }

}
