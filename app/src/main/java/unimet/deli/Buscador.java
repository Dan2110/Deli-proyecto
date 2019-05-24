package unimet.deli;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import modelos.Ingrediente;
import modelos.PreReceta;

/**
 * Created by IAFIGLIOLA on 06/08/2017.
 */

public class Buscador {

    private ArrayList<String> claves;
    private DatabaseReference mDatabase;
    private MainActivity contexto;
    private recipe contexto2;
    private File local;
    //private String filename = "myFile";
    //private String content = "Hello World";
    //private FileOutputStream outputStream;

    public Buscador(){

    }

    public File getTempFile(Context context,String url){
        File file = null;
        try {
            String fileName = Uri.parse(url).getLastPathSegment();
            file = File.createTempFile(fileName,null,context.getCacheDir());
        }catch(IOException e){

        }

        try
        {
            OutputStreamWriter fout=
                    new OutputStreamWriter(
                            context.openFileOutput("prueba_int.txt", Context.MODE_PRIVATE));
            fout.write("Texto de prueba.");
            fout.close();
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al escribir fichero a memoria interna");
        }

        return file;
    }


    public void crearArchivo() {
        String filename = "myfile";
        String string = "Hello world!";
        FileOutputStream outputStream;
        Log.d("File",contexto2.getFilesDir().getAbsolutePath());
        try {
            outputStream = contexto2.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void leerArchivo() {
        String filename = "myfile";
        FileInputStream outputStream;
        Log.d("File",contexto2.getFilesDir().getAbsolutePath());
        try {
            outputStream = contexto2.openFileInput(filename);
            int content;
            while ((content = outputStream.read()) != -1) {
                // convert to char and display it
                System.out.print((char) content);
                Log.d("Char", Character.toString((char) content));
            }
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Buscador(MainActivity conte){
        contexto = conte;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        claves = new ArrayList();
    }

    public Buscador(recipe conte){
        contexto2 = conte;
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void Buscar10Primeras(){
        mDatabase.child("Recetas").orderByKey().limitToFirst(10).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren() ) {
                    Log.d("10 Primeros","10 PRimeros");
                    PreReceta aux = data.getValue(PreReceta.class);
                    contexto.loadData(aux);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void BuscarReceta(String ID){
        mDatabase.child("Recetas").orderByKey().equalTo(ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PreReceta solicitada = new PreReceta();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    solicitada = data.getValue(PreReceta.class);
                   // Log.d("Solicitada",solicitada.getNombre());
                }

                contexto2.cargarReceta(solicitada);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void actualizar(Map<String, Object> childUpdates){
        mDatabase.updateChildren(childUpdates);
    }

    public void BuscarRecetas(List<String> ingred){
        Collections.sort(ingred);
        claves.clear();
        String key="";
        for(String single: ingred){
            key=key.concat(single+",");
        }

           mDatabase.child("Recetas").orderByChild("cbusqueda").equalTo(key).addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {
                   for (DataSnapshot data : dataSnapshot.getChildren()) {
                       System.out.println("Entre:"+data.getValue().toString());
                       PreReceta aux = data.getValue(PreReceta.class);
                       contexto.loadData(aux);
                   }
               }
               @Override
               public void onCancelled(DatabaseError databaseError) {

               }
           });

    }

    public void prueba(){

    }

}
