package Motor;

import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;

/***
 * Clase que alamcena la información básica de un tweet.
 */

public class Tweet implements Serializable {

    private SimpleStringProperty mensaje;
    private SimpleStringProperty nombre;
    private long id;

    public Tweet(String mensaje, long id, String nombre) {
        this.mensaje = new SimpleStringProperty(mensaje);
        this.id = id;
        this.nombre = new SimpleStringProperty(nombre);
    }

    public String getMensaje() {
        return mensaje.get();
    }

    public long getId() {
        return id;
    }

    public String getNombre() {
        return nombre.get();
    }
}
