package modelos;

/**
 * Created by IAFIGLIOLA on 05/06/2017.
 */
import java.util.ArrayList;

import modelos.ChildInfo;

// Modelo correspondiente a las categorias de la lista desplegable de seleccion

public class GroupInfo {
    //
    private String name;
    private ArrayList<ChildInfo> list = new ArrayList<ChildInfo>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ChildInfo> getProductList() {
        return list;
    }

    public void setProductList(ArrayList<ChildInfo> productList) {
        this.list = productList;
    }

}