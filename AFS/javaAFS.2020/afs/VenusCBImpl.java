// Implementación de la interfaz de cliente que define los métodos remotos
// para gestionar callbacks
package afs;

import java.io.File;
import java.rmi.*;
import java.rmi.server.*;

public class VenusCBImpl extends UnicastRemoteObject implements VenusCB {
    public VenusCBImpl() throws RemoteException {
    }

    public void invalidate(String fileName /* añada los parámetros que requiera */) throws RemoteException {
            System.out.println("Voy a invalidar el fichero: "+fileName);
            File file = new File("Cache/"+fileName);
            System.out.println("Existencia del fichero: "+file.exists());
            if(file.exists()){
                file.delete();
            }
            //Delete del file
        return;
    }
}

