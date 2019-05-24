package unimet.deli;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by IAFIGLIOLA on 14/09/2017.
 */

public class ManejadorDeSesion {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference usuariosRef;

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public void setmAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    public FirebaseAuth.AuthStateListener getmAuthListener() {
        return mAuthListener;
    }

    public void setmAuthListener(FirebaseAuth.AuthStateListener mAuthListener) {
        this.mAuthListener = mAuthListener;
    }

    public DatabaseReference getUsuariosRef() {
        return usuariosRef;
    }

    public void setUsuariosRef(DatabaseReference usuariosRef) {
        this.usuariosRef = usuariosRef;
    }

    public ManejadorDeSesion(){
        mAuth = FirebaseAuth.getInstance();
        usuariosRef = FirebaseDatabase.getInstance().getReference().child("Usuarios");
    }

    public void logearUsuario(final Context context, String correo, String password){

        mAuth.signInWithEmailAndPassword(correo, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(context, task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


}
