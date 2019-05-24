package unimet.deli;

import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import modelos.PreReceta;

public class recipe extends AppCompatActivity {

    private LinearLayout lingredientes;
    private LinearLayout lpasos;
    private TextView tNombre;
    private TextView tCategoria;
    private TextView tComensales;
    private TextView texto;
    private ImageView foto;
    private Buscador buscador;
    private PreReceta Receta;
    private RatingBar rBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("@Deli");

        Intent intent = getIntent();
        String llave = intent.getStringExtra("key");
        String aux=llave;

        lingredientes = (LinearLayout) findViewById(R.id.layIng);
        lpasos = (LinearLayout) findViewById(R.id.layPasos);
        tNombre = (TextView) findViewById(R.id.nombre);
        tCategoria = (TextView) findViewById(R.id.rCategoria);
        tComensales = (TextView) findViewById(R.id.rComensales);
        rBar = (RatingBar) findViewById(R.id.ratingBar);
        foto = (ImageView) findViewById(R.id.foto);
        buscador = new Buscador(recipe.this);
        buscador.BuscarReceta(llave);
            buscador.crearArchivo();
            buscador.leerArchivo();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        rBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Log.d("Estrellas2",Float.toString(rating));
                Snackbar.make(ratingBar, "Estrellas actualizadas", Snackbar.LENGTH_LONG)
                        .setAction("Action",null).show();
                calificar(rating);
            }
        });

    }

    @Override
    //Aca se agregan las diferentes opciones en el menu lateral derecho luego de ser presionado.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipe, menu);
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
            case R.id.action_save:
                Log.d("Archivo","Boton presionado");
                buscador.getTempFile(this,Receta.getNombre());
        }

        return super.onOptionsItemSelected(item);
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

        Receta = actual;
        Log.d("Inicio",Receta.getNombre());
        tNombre.setText(Receta.getNombre());
        Picasso.with(this).load(Receta.getImagen()).fit().centerCrop().into(foto);
        tNombre.setText(Receta.getNombre());
        tCategoria.setText(Receta.getCategoria());
        tComensales.setText(Integer.toString(Receta.getComensales()));
        int cont=0;
        String comida="";
        int largo=0;
        for(int i=1;i<=Receta.getIngredientes().size()/2;i++){
            comida = Receta.getIngredientes().get("Ing".concat(Integer.toString(i))).toString();
            comida= comida.concat(" : ");
            comida = comida.concat(Receta.getIngredientes().get("Ing".concat("C").concat(Integer.toString(i))).toString());
            texto = new TextView(this, null);
            texto.setText(comida);
            texto.setTextSize(24);
            texto.setLeft(15);
            texto.setId(cont);
            lingredientes.addView(texto);
            cont++;
        }
        for(int i=1;i<=Receta.getPreparacion().size();i++){
            comida = Receta.getPreparacion().get("P".concat(Integer.toString(i))).toString();
            texto = new TextView(this,null);
            texto.setText("  "+comida);
            texto.setTextSize(22);
            texto.setId(cont);
            lpasos.addView(texto);
            cont++;
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        getIntent().putExtra("Prueba","Receta");
    }
}
