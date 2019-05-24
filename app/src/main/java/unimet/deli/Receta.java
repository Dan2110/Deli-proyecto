package unimet.deli;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import modelos.ChildInfo;
import modelos.GroupInfo;
import modelos.PreReceta;

/**
 * Created by IAFIGLIOLA on 22/05/2017.
 */

public class Receta extends Activity{

    private TextView nombre,item;
    private TextView descripcion,tip;
    private ExpandableListView listaRecetas;
    private ImageView foto;
    private ArrayList ingredientes =new ArrayList<String>();
    private ArrayList pasos =new ArrayList<String>();
    private Context context;
    private Buscador buscador;
    private PreReceta Receta;
    private RatingBar rBar;
    private CustomAdapter listAdapter;
    private ArrayList<GroupInfo> listContent = new ArrayList<GroupInfo>();
    private LinkedHashMap<String, GroupInfo> subjects = new LinkedHashMap<String, GroupInfo>();
    private ArrayList<EditText> cantidades = new ArrayList<>();
    private LinearLayout layoutP;


    @Override
    protected void onPause() {
        super.onPause();
        for(EditText cuadro: cantidades){
            Log.d("Cantidades",cuadro.getText()+" en " +Integer.toString(cuadro.getId()));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String llave = intent.getStringExtra("key");
        String aux=llave;
        setContentView(R.layout.receta_detallada);

        layoutP = (LinearLayout) findViewById(R.id.lay);
        /*
        View aux5 = this.getLayoutInflater().inflate(R.layout.items,null);
        for(int i=0;i<5;i++) {
            EditText texto = new EditText(this, null);
            texto.setText("Taco");
            texto.setId(i);
            texto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Tecla",Integer.toString(v.getId()));
                }
            });
            cantidades.add(texto);
            layoutP.addView(texto);
        }
        //layoutP.addView(aux5);
        */
        nombre = (TextView) findViewById(R.id.nombre);
        descripcion = (TextView) findViewById(R.id.descripcion);
        rBar = (RatingBar) findViewById(R.id.ratingBar);
        //lingredientes.setLayoutParams(new LinearLayout.LayoutParams(100,100));
        foto = (ImageView) findViewById(R.id.foto);/*
        listAdapter = new CustomAdapter(Receta.this, listContent,2);
        listaRecetas = (ExpandableListView) findViewById(R.id.listaReceta);
        listaRecetas.setAdapter(listAdapter);
        context=this;
        buscador = new Buscador(Receta.this);
        Log.d("LLAve",llave);
        if(llave.equals("-1")){
        */
            HashMap<String,Object> ing = new HashMap<>();
            ing.put("Ing1","Cebolla");
            ing.put("IngC1","dos");
            ing.put("Ing2","Cebolla");
            ing.put("IngC2","dos");
            ing.put("Ing3","Cebolla");
            ing.put("IngC3","dos");
            ing.put("Ing4","Cebolla");
            ing.put("IngC4","dos");
            HashMap<String,Object> pas = new HashMap<>();
            pas.put("P1","aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            pas.put("P2","aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            pas.put("P3","aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            pas.put("P4","aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

         /*
           cargarReceta(new PreReceta("url","Pescado","Desayuno",ing,pas,5,"Ninguno","Cebolla","Creador",5,"-1"));
        }else {
            buscador.BuscarReceta(llave);
        }
        //Para que al darle click a un elemento de la lista haga algo

        rBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Log.d("Estrellas2",Float.toString(rating));
                Snackbar.make(ratingBar, "Estrellas actualizadas", Snackbar.LENGTH_LONG)
                        .setAction("Action",null).show();
                calificar(rating);
            }
        });
        */
    }

    public void calificar(float rating){
        rating = (rating+Receta.getEstrellas())/2;
        Receta.setEstrellas(rating);
        Map<String, Object> postValues = Receta.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Recetas/" + Receta.getID(), postValues);
        buscador.actualizar(childUpdates);
    }

    public void cargarReceta(PreReceta actual){
        subjects.clear();
        listContent.clear();

        Receta = actual;
        Log.d("Inicio",Receta.getNombre());
        nombre.setText(Receta.getNombre());
        Picasso.with(context).load(Receta.getImagen()).fit().centerCrop().into(foto);
        nombre.setText(Receta.getNombre());
        descripcion.setText(Receta.getCategoria());
        int cont=0;
        String comida="";
        int largo=0;
        for(int i=1;i<=Receta.getIngredientes().size()/2;i++){
            comida = Receta.getIngredientes().get("Ing".concat(Integer.toString(i))).toString();
            comida= comida.concat(" : ");
            comida = comida.concat(Receta.getIngredientes().get("Ing".concat("C").concat(Integer.toString(i))).toString());
            addProduct("Ingredientes", comida);
        }
        for(int i=1;i<=Receta.getPreparacion().size();i++){
            comida = Receta.getPreparacion().get("P".concat(Integer.toString(i))).toString();
            addProduct("Preparacion", comida);
        }
        addProduct("Tips",Receta.getTips());
        listAdapter.notifyDataSetChanged();
        expandAll();
    }

    private void findIng(String producto){
        collapseAll();
        for (int i = 0; i < subjects.size(); i++) {
            GroupInfo categoria = listContent.get(i);
            for (int j = 0; j < categoria.getProductList().size(); j++) {
                if (categoria.getProductList().get(j).getName().equals(producto)) {
                    listaRecetas.expandGroup(i);
                    categoria.getProductList().get(j).setSeleccionado(true);
                    break;
                }
            }
        }
    }


    //method to expand all groups
    private void expandAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            listaRecetas.expandGroup(i);
        }
    }

    //method to collapse all groups
    private void collapseAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            listaRecetas.collapseGroup(i);
        }
    }

    // Metodo que acomoda la información por categorias
    private int addProduct(String department, String product){
        ingredientes.add(product);
        listAdapter.notifyDataSetChanged();
        int groupPosition = 0;

        //check the hash map if the group already exists
        GroupInfo headerInfo = subjects.get(department);
        //add the group if doesn't exists
        if(headerInfo == null){
            headerInfo = new GroupInfo();
            headerInfo.setName(department);
            subjects.put(department, headerInfo);
            listContent.add(headerInfo);
        }

        //get the children for the group
        ArrayList<ChildInfo> productList = headerInfo.getProductList();
        //size of the children list
        int listSize = productList.size();
        //add to the counter
        listSize++;

        //create a new child and add that to the group
        ChildInfo detailInfo = new ChildInfo();
        detailInfo.setSequence(String.valueOf(listSize));
        detailInfo.setName(product);
        productList.add(detailInfo);
        headerInfo.setProductList(productList);

        //find the group position inside the list
        groupPosition = listContent.indexOf(headerInfo);
        return groupPosition;
    }


}
