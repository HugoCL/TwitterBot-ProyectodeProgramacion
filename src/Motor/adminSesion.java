package Motor;

import java.io.*;

public class adminSesion {

    public void Serializar(TwitterBot bot) throws IOException {
        FileOutputStream salida = new FileOutputStream("Sesion.out");
        ObjectOutputStream salidaO = new ObjectOutputStream(salida);

        salidaO.writeObject(bot);
    }

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
