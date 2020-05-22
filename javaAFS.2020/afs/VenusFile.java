// Clase de cliente que define la interfaz a las aplicaciones.
// Proporciona la misma API que RandomAccessFile.
package afs;

import java.rmi.*; 
import java.io.*; 

public class VenusFile {
    public static final String cacheDir = "Cache/";

    public VenusFile(Venus venus, String fileName, String mode) throws RemoteException, IOException, FileNotFoundException {
        //@TODO Buscamos el fichero en el cache antes de abrirlo desde el servidor
        File file = new File(cacheDir+fileName);
        if(file.exists()){
            file.open();
        }else{
            try{
                ViceReaderImpl reader = (ViceReaderImpl)venus.srv.download(fileName, mode);
                //A partir de ahi vamos a leer el fichero 
                if(reader != null){
                    List<Byte> b = new ArrayList<>();
                    b.add(reader.read(1024));
                }
                                //RandomAccessFile f2 = new RandomAccessFile(cacheDir+fileName, file);
            }
        }
    }
    public int read(byte[] b) throws RemoteException, IOException {
        b = venus.read(1024);
        return 0;
    }
    public void write(byte[] b) throws RemoteException, IOException {
        return;
    }
    public void seek(long p) throws RemoteException, IOException {
        return;
    }
    public void setLength(long l) throws RemoteException, IOException {
        return;
    }
    public void close() throws RemoteException, IOException {
        return;
    }
}

