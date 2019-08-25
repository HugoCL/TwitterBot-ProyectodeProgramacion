package Motor;

/***
 * Clase que alamcena la información básica de un tweet.
 */

public class Tweet {

    private String mensaje;

    private long id;

    private String nombre;

    public Tweet(String mensaje, long id, String nombre) {
        this.mensaje = mensaje;
        this.id = id;
        this.nombre = nombre;
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
}
