package modelos;

import android.content.Intent;
import android.nfc.cardemulation.HostApduService;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import unimet.deli.MainActivity;

/**
 * Created by MIGUEL on 25/05/2017.
 */


// Modelo correspondientes a las recetas

public class PreReceta{

    //Tienen que coincidir los nombres con los de las variables en la BD
    private String Imagen; // URL de descarga de la Imagen correspondiente a la receta
    private String Nombre; // Titulo de la receta
    private String Categoria;  // Categoria de la receta
    private int Comensales;  // Categoria de la receta
    private String CBusqueda;
    private String Tips;
    private HashMap <String,Object> Ingredientes = new HashMap<>(); // HashMap que contiene los ingredientes correspondientes a dicha receta
    private HashMap <String,Object> Preparacion = new HashMap<>();  // HashMap que contiene los pasos correspondientes a dicha receta
    private String Creador;
    private float Estrellas;
    private String ID; // ID correspondiente a la receta

    public PreReceta(){

    }

    public String getImagen() {
        return Imagen;
    }

    public String getID() {
        return ID;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }

    public void setID(String id) {
        ID = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public HashMap<String, Object> getIngredientes() {
        return Ingredientes;
    }

    public void setIngredientes(HashMap<String, Object> ingredientes) {
        Ingredientes = ingredientes;
    }

    public HashMap<String, Object> getPreparacion() {
        return Preparacion;
    }

    public void setPreparacion(HashMap<String, Object> preparacion) {
        Preparacion = preparacion;
    }

    public int getComensales() {
        return Comensales;
    }

    public void setComensales(int comensales) {
        Comensales = comensales;
    }

    public String getTips() {
        return Tips;
    }

    public void setTips(String tips) {
        Tips = tips;
    }

    public String getCBusqueda() {
        return CBusqueda;
    }

    public void setCBusqueda(String CBusqueda) {
        this.CBusqueda = CBusqueda;
    }

    public String getCreador() {
        return Creador;
    }

    public void setCreador(String creador) {
        Creador = creador;
    }

    public float getEstrellas() {
        return Estrellas;
    }

    public void setEstrellas(float estrellas) {
        Estrellas = estrellas;
    }

    public PreReceta(String imagen, String nombre, String categoria, HashMap<String,Object> ing, HashMap<String,Object> pasos, int comensales,
                     String tips) {
        Imagen = imagen;
        Nombre = nombre;
        Categoria = categoria;
        Ingredientes=ing;
        Preparacion=pasos;
        Comensales=comensales;
        Tips=tips;
    }

    public PreReceta(String imagen, String nombre, String categoria, HashMap<String,Object> ing, HashMap<String,Object> pasos, int comensales,
                     String tips, String clave, String creador, float estrellas, String id) {
        Imagen = imagen;
        Nombre = nombre;
        Categoria = categoria;
        Ingredientes=ing;
        Preparacion=pasos;
        Comensales=comensales;
        Tips=tips;
        CBusqueda = clave;
        Creador = creador;
        Estrellas = estrellas;
        ID= id;

    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("categoria", Categoria);
        result.put("nombre", Nombre);
        result.put("comensales", Comensales);
        result.put("imagen",Imagen);
        result.put("id", ID);
        result.put("tips", Tips);
        result.put("ingredientes", Ingredientes);
        result.put("preparacion", Preparacion);
        result.put("estrellas", Estrellas);
        result.put("creador",Creador);
        result.put("cbusqueda",CBusqueda);

        return result;
    }

}
