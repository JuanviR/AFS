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
    private ViceReaderImpl reader;
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
        VenusFile.comand("mkdir " + cacheDir); // shell
        final File file2 = new File(cacheDir + fileName);
        if (!file2.exists()) {
            reader = (ViceReaderImpl) venus.srv.download(fileName, mode); //It's seems that this line generate an error
    
            System.out.println("Data downloaded");
            // A partir de ahi vamos a leer el fichero
            if (reader != null) {
               // final List<Byte> b = new ArrayList<>();
                // @TODO Leer todo el fichero para guardarlo en el cache local
                file = new RandomAccessFile(cacheDir + fileName, "rw");

                file.write(reader.read(1024));

            }
            // RandomAccessFile f2 = new RandomAccessFile(cacheDir+fileName, file);

        } else {
            System.out.println("Exist");
            file = new RandomAccessFile(cacheDir + fileName, mode);

        }

        file.close();
    }

    public int read(byte[] b) throws RemoteException, IOException {
        b = reader.read(1024);
        final String file = "cliente.txt";
        VenusFile.comand("rm " + file);
        VenusFile.comand("echo >> " + file);
        try (FileOutputStream fileOuputStream = new FileOutputStream(file)) {
            fileOuputStream.write(b);
        } catch (final Exception e) {
        }

        return 0;
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

    // shell
    private static void comand(final String cmd) {
        try {
            final Process p = Runtime.getRuntime().exec(cmd);
            final BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        } catch (final java.io.IOException ex)
    {
      ex.printStackTrace ();
    }
  }
}

