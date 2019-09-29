package Motor;

import java.io.Serializable;

/***
 * Clase que alamcena la información básica de un tweet.
 */

public class Tweet implements Serializable {

    private String mensaje;
    private String nombre;
    private String imagen;
    private long id;

    public Tweet(String mensaje, long id, String nombre, String imagen) {
        this.mensaje = mensaje;
        this.id = id;
        this.nombre = nombre;
        this.imagen = imagen;
    }

    public String getMensaje() {
        return mensaje;
    }

    public long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getImagen() {
        return imagen;
    }
}
