package modelos;

/**
 * Created by IAFIGLIOLA on 05/06/2017.
 */
// Modelo de de los ingredientes mostrados en la lista de ingredientes que se desplega en Seleccion
public class ChildInfo {

    private String sequence = "";
    private String name = "";
    private boolean seleccionado = false;

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }
}