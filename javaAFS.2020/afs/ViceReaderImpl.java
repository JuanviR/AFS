// Implementación de la interfaz de servidor que define los métodos remotos
// para completar la descarga de un fichero
package afs;

import java.io.*;
import java.rmi.*;
import java.rmi.server.*;

public class ViceReaderImpl extends UnicastRemoteObject implements ViceReader {
    private static final String AFSDir = "AFSDir/";
    public RandomAccessFile file;
    public int offset;

    public ViceReaderImpl(String fileName, String mode /* añada los parámetros que requiera */)
            throws RemoteException, FileNotFoundException {
        file = new RandomAccessFile(AFSDir + fileName, mode);
        offset = 0;
             
    }
    public byte[] read(int tam) throws IOException {
        byte[] b;
        long size = file.length();
        if(offset == size){
            return null;
        }
        else if(size - offset < tam){
            b = new byte[(int) size - offset];
            file .read(b, offset, (int) size - offset);
            offset = (int) size;
        }else{
            b = new byte[tam];
            file.read(b, offset, tam);
            offset += tam;
        }
        
        return b;
    }
    public void close() throws RemoteException {
        return;
    }
}       

