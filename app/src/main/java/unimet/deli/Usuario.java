package unimet.deli;

import java.util.HashMap;

/**
 * Created by IAFIGLIOLA on 05/08/2017.
 */

public class Usuario {

    //private String Id;
    private String Nombre;
    private HashMap<String,Object> favoritas = new HashMap<>();
    private HashMap<String,Object> agregadas = new HashMap<>();

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public Usuario(){

    }

    public Usuario(String Nombre){
        this.Nombre = Nombre;
        favoritas.put("Prueba","Arepa");
        favoritas.put("Prueba","Arroz");
    }

}
