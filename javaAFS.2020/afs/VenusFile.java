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
    private ViceWriter writer;
    private RandomAccessFile file;
    private final Venus venus;
    private final String mode;
    private final String fileName;
    private long initialLength;

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
        if(file == null){
            //El fichero aun es remoto
            //Se crea una copia en el cache
            //Se supone que se puede escribir, si no es el caso tendremos un error mas adelante
            file = new RandomAccessFile(cacheDir + fileName, "rw");
            byte[] temp = new byte[1024];
            temp = null;
            temp = reader.read(1024);
            while(b != null){
                file.write(temp);
                temp = new byte[1024];
                temp = reader.read(1024);
            }
            initialLength = file.length();
        }

        //Ahora tenemos el fichero en cache
        if(file.length() == 0){
            file.seek(0);
        }else{
            file.seek(file.length() - 1); //Nos colocamos en el ultimo sitio del fichero para escribir
        }
        file.write(b); //Escribimos en el fichero
        
    }

    public void seek(final long p) throws RemoteException, IOException {
        return;
    }

    public void setLength(final long l) throws RemoteException, IOException {
        return;
    }

    public void close() throws RemoteException, IOException {
        if(file == null){
            //El fichero es remoto, podemos suponer que no hubo modificaciones
            reader.close();
        }else{
            //En ese caso puede haber modificaciones
            reader.close(); //Vamos a necesitar un writer
            //@TODO verificar que hay modificaciones antes de hacer el writer
            writer = venus.srv.upload(fileName, mode); 
            // Tenemos acceso al fichero para poder escribir dentro
            
            byte[] temp;
            if(file.length() - initialLength == 0){
                //No se hace nada 
                writer.close();
                temp = null;
            }else if(file.length() - initialLength < 1024){
                temp = new byte[(int) (file.length() - initialLength)];
                file.read(temp, (int)initialLength, temp.length);
                initialLength = file.length();
            }else{
                temp = new byte[1024];
                file.read(temp, (int)initialLength, temp.length);
                initialLength += 1024;
            }
            while(temp != null){
                writer.write(temp);
                if(file.length() - initialLength == 0){
                    writer.close();
                    temp = null;
                }else if(file.length() - initialLength < 1024){
                    temp = new byte[(int) (file.length() - initialLength)];
                    file.read(temp, (int)initialLength, temp.length);
                    initialLength = file.length();
                }else{
                    temp = new byte[1024];
                    file.read(temp, (int)initialLength, temp.length);
                    initialLength += 1024;
                }
            }

            writer.close();
        }
    }
}

