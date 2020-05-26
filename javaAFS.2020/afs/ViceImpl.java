// Implementación de la interfaz de servidor que define los métodos remotos
// para iniciar la carga y descarga de ficheros
package afs;

import java.io.FileNotFoundException;
import java.rmi.*;
import java.rmi.server.*;

public class ViceImpl extends UnicastRemoteObject implements Vice {
  /**
   *
   */

  public ViceImpl() throws RemoteException {
  }

  public ViceReader download(String fileName, String mode /* añada los parámetros que requiera */)
      throws RemoteException {
    // @TODO Crear instancia ViceReaderImpl que se retornara
    //Si hizo esto pero ...
            System.out.println("Data downloaded desde ViceImpl");
      ViceReaderImpl reader = null;
      
      try {
        reader = new ViceReaderImpl(fileName, mode);
      

      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      return reader;       
    }
    public ViceWriter upload(String fileName /* añada los parámetros que requiera */)
          throws RemoteException {
        return null;
    }
}
