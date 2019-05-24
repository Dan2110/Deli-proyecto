package unimet.deli;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import modelos.PreReceta;

// Clase que funciona como controlador de la lista principal
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Lista con las recetas
    private RecyclerView listaReceta;
    private static final int PICK_ING_REQUEST = 100;
    private RecetaAdapter adapter;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView Mnombre,Mcorreo;
    private ImageView foto;
    private ArrayList<PreReceta> solicitadas = new ArrayList<PreReceta>();
    private ArrayList<PreReceta> recetas = new ArrayList<PreReceta>();
    private MenuItem item,item2,item3;
    private static Context context;
    private Intent intent;
    private Buscador buscador;

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("CambioEstado","Pausa");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("CambioEstado","Resumir");
        final int rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        switch(rotation){
            case Surface.ROTATION_0:
                Log.d("Rotation","Vertical");
                break;
            case Surface.ROTATION_90:
                Log.d("Rotation","Horizontal");
                break;
            case Surface.ROTATION_180:
                Log.d("Rotation","Vertical inverso");
                break;
            default:
                Log.d("Rotation","Horizontal inverso");
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Config",Integer.toString(this.getResources().getConfiguration().orientation));
        setContentView(R.layout.activity_main);
        context=this;
        Log.d("CambioEstado","Crear");
        //Configurando la lista de recetas
        mAuth = FirebaseAuth.getInstance();
        listaReceta=(RecyclerView)findViewById(R.id.rvListaRecetas);
        listaReceta.setHasFixedSize(true);
        listaReceta.setLayoutManager(new LinearLayoutManager(this));
        buscador = new Buscador(MainActivity.this);

        // Obtener el Recycler
        listaReceta.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        LinearLayoutManager lManager = new LinearLayoutManager(this);
        listaReceta.setLayoutManager(lManager);

        // Crear un nuevo adaptador
        adapter = new RecetaAdapter(solicitadas,context,this);
        listaReceta.setAdapter(adapter);
        buscador.Buscar10Primeras();

        //Configurando la barra de opciones
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Colocacion del Logo
        getSupportActionBar().setIcon(R.drawable.logo_blanco_sf);
        // Creacion de una instancia del modelo para buscar en la base de datos


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        item = (MenuItem) navigationView.getMenu().getItem(0);
        item2 = (MenuItem) navigationView.getMenu().getItem(1);
        Mnombre = (TextView) navigationView.getHeaderView(0).findViewById(R.id.Mnombre);
        Mcorreo = (TextView) navigationView.getHeaderView(0).findViewById(R.id.Mcorreo);
        foto = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.Mfoto);



        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    item.setTitle("Cerrar Sesion");
                    item2.setEnabled(true);
                    Mnombre.setText(mAuth.getCurrentUser().getDisplayName());
                    Mcorreo.setText(mAuth.getCurrentUser().getEmail());
                    Picasso.with(MainActivity.this).load(mAuth.getCurrentUser().getPhotoUrl()).fit().into(foto);
                    Picasso.with(MainActivity.this).load(mAuth.getCurrentUser().getPhotoUrl()).fit().centerCrop().into(foto);

                } else {
                    // User is signed out
                    item2.setEnabled(false);
                    Mnombre.setText("Chef");
                    Mcorreo.setText("Chef@correo.com");
                    item.setTitle("Iniciar Sesion");
                }
                // ...
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("CambioEstado","Inicio");
        //aqui se recibe el intent de las otras actividades
        mAuth.addAuthStateListener(mAuthListener);
        intent = getIntent();

     }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("CambioEstado","Parado");
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    /*
        Metodo encargado de llenar la lista de recetas con las recetas buscadas en la bdd
     */
    public void loadData(PreReceta receta){
        boolean yaesta=false;
        int indice=-1;
        for(int i=0;i<solicitadas.size();i++){
            if(solicitadas.get(i).getID().equals(receta.getID())){
                indice = i;
                yaesta=true;
                Log.d("Esta",Integer.toString(indice));
            }
        }

        if(yaesta){
           solicitadas.remove(indice);
           yaesta= false;
        }

        solicitadas.add(receta);
        notificacion(receta.getNombre(),receta.getCategoria(),receta.getID());
        adapter.notifyDataSetChanged();
        listaReceta.getAdapter().notifyDataSetChanged();
        Log.d("Agregada",receta.getNombre());
    }

    private void notificacion(String title, String body, String index) {

        Intent intent = new Intent(this,recipe.class);
        intent.putExtra("key",index);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager nM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
       // nM.notify(0,notificationBuilder.build());

    }

     /*
        Metodo encargado de verificar como se debe realizar la busqueda, es decir si hay que buscar por ingredientes o si se mostraran todos
      */
     private void buscara() {
         // si el intent es nulo entonces se muestran  todas las recetas
         if (intent == null) {
            // solicitadas.clear(); // Se vacia la lista de recetas ha mostrar para que no se apilen las recetas
         } else {
             // Si en cambio el intent no es nulo, entonces, puede suceder que se quiera buscar por ingrediente
             // Si la lista de ingredientes seleccionados es nula o vacia, se muestran todas las recetas
             if (intent.getStringArrayListExtra("Ing") == null || intent.getStringArrayListExtra("Ing").isEmpty()) {
                 //solicitadas.clear();
             } else {
                 // Si la lista de ingredientes seleccionados no esta vacia, se busca en la lista de recetas aquellas que contengan dichos ingredientes
                 Log.d("Borrado","antes");
                 solicitadas.clear();
                 Log.d("Borrado","Despues"+solicitadas.toString());
                 adapter.notifyDataSetChanged();
                 listaReceta.getAdapter().notifyDataSetChanged();
                 //buscador.BuscarRecetas(intent.getStringArrayListExtra("Ing"));
             }
         }
         // Notificar al adaptador que se ha realizado un cambio en la lista de solicitadas
         //listaReceta.getAdapter().notifyDataSetChanged();
     }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    //Aca se agregan las diferentes opciones en el menu lateral derecho luego de ser presionado.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            case R.id.action_search:
                // Cambio de actividad del main a la vista de seleccion de ingredientes
                Intent inten = new Intent(MainActivity.this, Seleccion.class);
                inten.putExtra("Origen","Main");
                startActivityForResult(inten,PICK_ING_REQUEST);
                overridePendingTransition(R.animator.zoom_forward_in, R.animator.zoom_forward_out);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_ING_REQUEST && resultCode==RESULT_OK && data!=null){
            Log.d("CamResult","Resultado"+data.getStringExtra("Origen"));
            solicitadas.clear();
            buscador.BuscarRecetas(data.getStringArrayListExtra("Ing"));
        }else{
            Toast.makeText(this,"No ha Seleccionado ningun ingrediente", Toast.LENGTH_SHORT);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            if(item.getTitle().equals("Iniciar Sesion")) {
                startActivity(new Intent(MainActivity.this, SingIn.class));
            }else {
                mAuth.signOut();
                item.setTitle("Iniciar Sesion");
            }
        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(MainActivity.this,AgregarReceta.class));
        } else if (id == R.id.nav_slideshow) {
            startActivity(new Intent(MainActivity.this,Agregar_Receta2.class));
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            startActivity(new Intent(MainActivity.this,AgregarReceta.class));
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
            solicitadas.clear();
            solicitadas.add(new PreReceta("url","Omelete","Desayuno",ing,pas,5,"Ninguno","Cebolla","Creador",5,"-1"));
            adapter.notifyDataSetChanged();
        } else if (id == R.id.nav_send) {
            solicitadas.clear();
            adapter.notifyDataSetChanged();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}