package unimet.deli;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by IAFIGLIOLA on 04/06/2017.
 */

import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;

import java.util.LinkedHashMap;

import modelos.ChildInfo;
import modelos.GroupInfo;
import modelos.Ingrediente;

public class Seleccion extends AppCompatActivity implements View.OnClickListener{

    private ArrayList<String> seleccionados=new ArrayList<>();
    private LinkedHashMap<String, GroupInfo> subjects = new LinkedHashMap<String, GroupInfo>();
    private ArrayList<GroupInfo> deptList = new ArrayList<GroupInfo>();
    private ArrayList<String> ingredientes = new ArrayList<>();
    private FloatingActionButton fab2;
    private Button enviar;
    private Button enviar2;
    private AutoCompleteTextView actv;
    private CustomAdapter listAdapter;
    private ArrayAdapter<String> adapter;
    private ExpandableListView simpleExpandableListView;
    public Ingrediente ingrediente;
    private String origen;
    private Intent intento;
    private MainActivity main;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.seleccion);

        intento = getIntent();
        origen = intento.getStringExtra("Origen");

        fab2 = (FloatingActionButton) findViewById(R.id.fab2);

        if(origen.equals("Main")){
            this.setTitle("Busqueda de Recetas");
        }else{
            this.setTitle("Agregar Receta 2/3");
        }
        // add data for displaying in expandable list view
        //loadData();
        ingrediente = new Ingrediente(this);
        //get reference of the ExpandableListView
        simpleExpandableListView = (ExpandableListView) findViewById(R.id.lseleccion);
        // create the adapter by passing your ArrayList data
        listAdapter = new CustomAdapter(Seleccion.this, deptList,1);
        // attach the adapter to the expandable list view
        simpleExpandableListView.setAdapter(listAdapter);
        //addProduct("Entrada","Cebolla");
        //expand all the Groups
        //Getting the instance of AutoCompleteTextView
        actv = (AutoCompleteTextView)findViewById(R.id.actv);
        adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item,ingredientes);
        actv.setThreshold(1);//will start working from first character
        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        actv.setTextColor(Color.GRAY);

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String seleccionado = actv.getText().toString();
                findIng(seleccionado);
            }
        });

    }

    @Override
    //Aca se agregan las diferentes opciones en el menu lateral derecho luego de ser presionado.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_filtro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            // Metodo que se ejecuta cuando se presiona el boton
            case R.id.action_play:

                seleccionados.clear();
                for (int i = 0; i < subjects.size(); i++) {
                    GroupInfo categoria = deptList.get(i);
                    for (int j = 0; j < categoria.getProductList().size(); j++) {
                        if (categoria.getProductList().get(j).isSeleccionado()) {
                            seleccionados.add(categoria.getProductList().get(j).getName());
                        }
                    }
                }

                if(!seleccionados.isEmpty()) {
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra("Ing", seleccionados);
                    Seleccion.this.setResult(RESULT_OK, intent);
                }else{
                    Seleccion.this.setResult(RESULT_CANCELED);
                }
                finish();

            default:
        }

        return super.onOptionsItemSelected(item);
    }

    private void findIng(String producto){
        collapseAll();
        for (int i = 0; i < subjects.size(); i++) {
            GroupInfo categoria = deptList.get(i);
            for (int j = 0; j < categoria.getProductList().size(); j++) {
                if (categoria.getProductList().get(j).getName().equals(producto)) {
                    simpleExpandableListView.expandGroup(i);
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
            simpleExpandableListView.expandGroup(i);
        }
    }

    //method to collapse all groups
    private void collapseAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            simpleExpandableListView.collapseGroup(i);
        }
    }

    //load some initial data into out list
    public void loadData(DataSnapshot dataSnapshot){

        if(dataSnapshot!= null) {
            for (DataSnapshot categoria : dataSnapshot.getChildren()) {
                Log.d("DataSnapshot", categoria.getKey() + categoria.getValue().toString());
                for (DataSnapshot hijo : categoria.getChildren()) {
                    addProduct(categoria.getKey(), hijo.getValue().toString());
                }
                listAdapter.notifyDataSetChanged();
            }
        }

    }

    // Metodo que acomoda la información por categorias
    private int addProduct(String department, String product){
        ingredientes.add(product);
        adapter.notifyDataSetChanged();
        int groupPosition = 0;

        //check the hash map if the group already exists
        GroupInfo headerInfo = subjects.get(department);
        //add the group if doesn't exists
        if(headerInfo == null){
            headerInfo = new GroupInfo();
            headerInfo.setName(department);
            subjects.put(department, headerInfo);
            deptList.add(headerInfo);
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
        groupPosition = deptList.indexOf(headerInfo);
        return groupPosition;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Seleccion.this, MainActivity.class);
        seleccionados.clear();
        for (int i = 0; i < subjects.size(); i++) {
            GroupInfo categoria = deptList.get(i);
            for (int j = 0; j < categoria.getProductList().size(); j++) {
                if (categoria.getProductList().get(j).isSeleccionado()) {
                    seleccionados.add(categoria.getProductList().get(j).getName());
                }
            }
        }

        Log.d("Seleccionados", seleccionados.toString());
        intent.putStringArrayListExtra("Ing", seleccionados);
        if (origen!= null && origen.equals("Agregar Receta")) {
            overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
        } else {
            this.startActivity(intent);
            overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}
