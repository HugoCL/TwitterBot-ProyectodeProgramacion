package Motor;

import javafx.beans.property.SimpleStringProperty;

/***
 * Clase que alamcena la información básica de un tweet.
 */

public class Tweet {

    public SimpleStringProperty mensaje = new SimpleStringProperty();
    public SimpleStringProperty nombre = new SimpleStringProperty();
    private long id;

    public Tweet(String mensaje, long id, String nombre) {
        this.mensaje = new SimpleStringProperty(mensaje);
        this.id = id;
        this.nombre = new SimpleStringProperty(nombre);
    }
    public Tweet(){}

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
