package Motor;

import java.io.*;
import java.util.ArrayList;

public class AdminBackup {
    private static AdminBackup INSTANCE = null;
    private AdminBackup(){}
    private synchronized static void createInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AdminBackup();
        }
    }
    public static AdminBackup getInstance() {
        if (INSTANCE == null) createInstance();
        return INSTANCE;
    }
    /***
     * Método que Serializa la instancia de Twitter, lo que permite guardar todos los tokens de sesion
     * @param tweets Objeto de tipo TwitterBot que posee la instancia ya creada con todos sus parametros
     * @throws IOException Excepcion en caso de problemas con el fichero
     */
    public void serializar(ArrayList<Tweet> tweets) throws IOException {
        FileOutputStream salida = new FileOutputStream("Backup.out");
        ObjectOutputStream salidaO = new ObjectOutputStream(salida);

        salidaO.writeObject(tweets);
    }

    /***
     * Metodo que des-serializa la sesion guardada, si es que existiere
     * @return Retorna la instancia serializada del Bot, null en caso contrario.
     */
    public ArrayList<Tweet> deserializar(){
        try
        {
            FileInputStream file = new FileInputStream("Backup.out");
            ObjectInputStream in = new ObjectInputStream(file);

            ArrayList<Tweet> tweetsDeserealizados = (ArrayList<Tweet>) in.readObject();

            in.close();
            file.close();
            return tweetsDeserealizados;
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
