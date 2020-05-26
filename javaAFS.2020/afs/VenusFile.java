// Clase de cliente que define la interfaz a las aplicaciones.
// Proporciona la misma API que RandomAccessFile.
package afs;

import java.rmi.*;
import java.util.ArrayList;

import java.util.List;
import java.io.*;

//


public class VenusFile {
    public static final String cacheDir = "Cache/";
    private ViceReader reader;
    private RandomAccessFile file;
    private final Venus venus;
    private final String mode;
    private final String fileName;

    public VenusFile(final Venus venus, final String fileName, final String mode)
            throws RemoteException, IOException, FileNotFoundException {
        // @TODO Buscamos el fichero en el cache antes de abrirlo desde el servidor
        this.venus = venus;
        this.fileName = fileName;
        this.mode = mode;
        reader = null;
        this.file = null;
        final File file2 = new File(cacheDir + fileName);

        if(!file2.exists()){
            //EL fichero no existe en cache
            reader = venus.srv.download(fileName, mode); //Tenemos acceso al fichero remoto para poder leerlo mas tarde
        }else{
            //El fichero si existe
            file = new RandomAccessFile(cacheDir + fileName, mode); //No podemos hacer eso antes porque sino creariamos el fichero en el cache. 
        }
        /* if (!file2.exists()) {
            reader = venus.srv.download(fileName, mode); 
            System.out.println("Data downloaded");
            // A partir de ahi vamos a leer el fichero
            if (reader != null) {
               // final List<Byte> b = new ArrayList<>();
                // @TODO Leer todo el fichero para guardarlo en el cache local
                file = new RandomAccessFile(cacheDir + fileName, "rwd");
                byte[] b = new byte[1024];
                b = null;
                b = reader.read(1024);
                while(b != null){
                    file.write(b);
                    b = new byte[1024];
                    b = reader.read(1024);
                }

            }
            // RandomAccessFile f2 = new RandomAccessFile(cacheDir+fileName, file);

        } else {
            System.out.println("Exist");
            file = new RandomAccessFile(cacheDir + fileName, mode);

        } */ //Esta parte coresponde a la parte de descarga del fichero que vamos a necesitar mas tarde

    }

    public int read(byte[] b) throws RemoteException, IOException {
        if(file == null){
            //El fichero es remoto
            byte[] temp;
            if(b.length < 1024){
                temp = new byte[b.length];
            }else{
                temp = new byte[1024];
            }
            int i = 0;
            temp = reader.read(temp.length);
            while(temp != null){
                for (byte c : temp) {
                    b[i] = c;
                    i++;
                }
            temp = new byte[1024];
            if(b.length - i == 0){
                return i;
            }else if(b.length - i < 1024){
                temp = new byte[b.length - i];
            }else{
                temp = new byte[1024];
            }
            temp = reader.read(temp.length);
            }

            return i;
        }else{
            //El fichero no es remoto
            int res = file.read(b);
            System.out.println("Res: \n"+res);
            return res;
        }
        //final String file = "cliente.txt";
        //VenusFile.comand("rm " + file);
        //VenusFile.comand("echo >> " + file);
        //try (FileOutputStream fileOuputStream = new FileOutputStream(file)) {
        //    fileOuputStream.write(b);
        //} catch (final Exception e) {
        //}

    }

    public void write(final byte[] b) throws RemoteException, IOException {
        return;
    }

    public void seek(final long p) throws RemoteException, IOException {
        return;
    }

    public void setLength(final long l) throws RemoteException, IOException {
        return;
    }

    public void close() throws RemoteException, IOException {
        return;
    }
}

