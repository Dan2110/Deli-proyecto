package unimet.deli;

import android.content.Context;
import android.content.Intent;
import android.media.Rating;
import android.os.Parcelable;
import android.support.annotation.IntegerRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import modelos.PreReceta;


/**
 * Created by IAFIGLIOLA on 30/06/2017.
 */

public class RecetaAdapter extends RecyclerView.Adapter<RecetaAdapter.PreRecetaViewHolder> {

    private ArrayList<PreReceta> recetas= new ArrayList<>();
    private static Context contexto;
    private static MainActivity main;

    public ArrayList<PreReceta> getRecetas() {
        return recetas;
    }

    public RecetaAdapter(ArrayList<PreReceta> items, Context conte, MainActivity main) {
        this.recetas = items;
        contexto=conte;
        this.main=main;
    }

    public static class PreRecetaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private View mView;
        private TextView titulo_PreReceta;
        private TextView categoria_PreReceta;
        private TextView clave_PreReceta;
        private RatingBar estrellas;

        /*
           Metodo para ir a la vista de receta detallada correspondiente a la receta sobre la cual se hizo el click
         */
        @Override
        public void onClick(View view) {

            TextView ID = (TextView) view.findViewById(R.id.tvClave);
            String index = ID.getText().toString();
            Intent intent = new Intent(contexto,recipe.class);
            intent.putExtra("key",index);
            contexto.startActivity(intent);
        }

        public PreRecetaViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
            mView.setOnClickListener(this);

        }

        public void setTitulo(String titulo){
            titulo_PreReceta=(TextView)mView.findViewById(R.id.tvTituloReceta_row);
            titulo_PreReceta.setText(titulo);
        }

        public void setCategoria(String categoria){
            categoria_PreReceta=(TextView)mView.findViewById(R.id.tvCategoriaReceta_row);
            categoria_PreReceta.setText(categoria);
        }
        public void setClave(String clave){
            clave_PreReceta=(TextView) mView.findViewById(R.id.tvClave);
            clave_PreReceta.setText(clave);
            clave_PreReceta.setVisibility(View.GONE);
        }

        public void setImage(Context ctx, String link){
            ImageView imagen_preReceta = (ImageView)mView.findViewById(R.id.ivFotoReceta_row);
            Picasso.with(ctx).load(link).into(imagen_preReceta);
        }

        public void setRating(float rating){
            RatingBar estrellas = (RatingBar) mView.findViewById(R.id.ratingBar2);
            estrellas.setRating(rating);
            estrellas.setNumStars(5);
            estrellas.setEnabled(false);
        }
    }

    @Override
    public PreRecetaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_row, parent, false);

            return new PreRecetaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PreRecetaViewHolder holder, int position) {
        holder.setTitulo(recetas.get(position).getNombre());
        holder.setCategoria(recetas.get(position).getCategoria());
        holder.setImage(contexto,recetas.get(position).getImagen());
        holder.setClave(recetas.get(position).getID());
        holder.setRating(recetas.get(position).getEstrellas());
    }

    @Override
    public int getItemCount() {
        return recetas.size();
    }

    }
