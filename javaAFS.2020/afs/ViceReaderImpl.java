// Implementación de la interfaz de servidor que define los métodos remotos
// para completar la descarga de un fichero
package afs;
import java.rmi.*;
import java.rmi.server.*;
import java.io.RandomAccessFile;

public class ViceReaderImpl extends UnicastRemoteObject implements ViceReader {
    private static final String AFSDir = "AFSDir/";
    private RandomAccessFile file;

    public ViceReaderImpl(String fileName, String mode /* añada los parámetros que requiera */)
		    throws RemoteException {
                file = new RandomAccessFile(AFSDir+fileName, mode);
                if(file.exists()){
                    file.open();
                }
                
                //@TODO Instanciar un randomFileAccess asociado al archivo que se quiere leer
    }
    public byte[] read(int tam) throws RemoteException {
        byte[] b = new byte[tam];
        file.read(b, 0, tam);
        return b;
    }
    public void close() throws RemoteException {
        return;
    }
}       

