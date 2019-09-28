package Motor;

import java.io.*;

/***
 * Clase encargada de la administración de la sesión de una cuenta de Twitter
 */
public class AdminSesion {
    private static AdminSesion INSTANCE = null;
    // Constructor privado
    private AdminSesion(){}
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple
    private synchronized static void createInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AdminSesion();
        }
    }
    public static AdminSesion getInstance() {
        if (INSTANCE == null) createInstance();
        return INSTANCE;
    }
    /***
     * Método que Serializa la instancia de Twitter, lo que permite guardar todos los tokens de sesion
     * @param bot Objeto de tipo TwitterBot que posee la instancia ya creada con todos sus parametros
     * @throws IOException Excepcion en caso de problemas con el fichero
     */
    public void Serializar(TwitterBot bot) throws IOException {
        FileOutputStream salida = new FileOutputStream("Sesion.out");
        ObjectOutputStream salidaO = new ObjectOutputStream(salida);

        salidaO.writeObject(bot);
    }

    /***
     * Metodo que des-serializa la sesion guardada, si es que existiere
     * @return Retorna la instancia serializada del Bot, null en caso contrario.
     */
    public TwitterBot desSerializar(){

        try
        {
            FileInputStream file = new FileInputStream("Sesion.out");
            ObjectInputStream in = new ObjectInputStream(file);

            TwitterBot botDesSerializado = (TwitterBot)in.readObject();

            in.close();
            file.close();
            return botDesSerializado;
        }

        catch(IOException ex)
        {
            System.out.println("Archivo no encontrado. No se puede recuperar la sesión");
            return null;
        }

        catch(ClassNotFoundException ex)
        {
            System.out.println("Problema interno con la des-serialización. Clase no encontrada.");
            return null;
        }
    }
}
