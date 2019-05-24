package modelos;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import unimet.deli.Seleccion;

/**
 * Created by IAFIGLIOLA on 27/06/2017.
 */

/*
    Clase encargada de recopilar de la BDD la información referente a los ingredientes y sus categorias
 */

public class Ingrediente {

    private DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference(); // referencia a la BDD
    private DataSnapshot datos; // variable donde se almacena la informacion consultada

    public Ingrediente(final Seleccion main) {

        mDatabase.child("Ingredientes").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                datos = dataSnapshot;
                main.loadData(datos);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }
}
