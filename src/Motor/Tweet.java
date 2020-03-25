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

    public String[] getImagenes(){return imagenes;}
}
