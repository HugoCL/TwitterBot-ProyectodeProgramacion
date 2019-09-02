package Motor;

public class Followers {

    private long id;

    private String nombre;

    private String screenName;

    public Followers(long id, String nombre, String screenName) {
        this.id = id;
        this.nombre = nombre;
        this.screenName = screenName;
    }

    public long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getScreenName() {
        return screenName;
    }
}
