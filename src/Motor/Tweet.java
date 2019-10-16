package Motor;

import java.io.Serializable;

/***
 * Clase que alamcena la información básica de un tweet.
 */

public class Tweet implements Serializable {

    private String mensaje;
    private String nombre;
    private String screenName;
    private String imagen;
    private String[] imagenes;
    private long id;

    public Tweet(String mensaje, long id, String nombre, String screenName, String imagen,String[] imagenes) {
        this.mensaje = mensaje;
        this.id = id;
        this.nombre = nombre;
        this.screenName = screenName;
        this.imagen = imagen;
        this.imagenes = imagenes;
    }

    public String getScreenName() {
        return screenName;
    }

    String getMensaje() {
        return mensaje;
    }

    public long getId() {
        return id;
    }

    String getNombre() {
        return nombre;
    }

    String getImagen() {
        return imagen;
    }

    String[] getImagenes(){return imagenes;}
}
