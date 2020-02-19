package br.com.ufop.daniel.d2dwifidirect.dao;

import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;

import org.apache.commons.io.FilenameUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import br.com.ufop.daniel.d2dwifidirect.util.Get;

import static br.com.ufop.daniel.d2dwifidirect.util.Constants.ACTION_RECEIVED;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.ACTION_SEND;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.CATEGORY_LOG;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.CONNECTION_TYPE_SERVER_EXTERNAL;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.DEVICE_TYPE_CLIENT;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.DEVICE_TYPE_GO;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.DIRECTORY_SAVE_FILE;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.EXCEPTION_SAVE_FILE;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.EXCEPTION_WRITE_FILE;

/**
 * Created by daniel on 26/07/17.
 */

public class FileCopy {

    /* Procedimento que trata de copiar o arquivo - Pega do arquivo e insere no stream */
    public static boolean copyFile(ContentResolver contentResolver, String fileUri, DataOutputStream dos,
                                   String extension, String connection_type) {
        try {
            long start_time = System.currentTimeMillis(); /* Começa a contar o tempo */

            /* Pega o tamanho do arquivo */
            long size = Get.getSizeFile(fileUri, contentResolver);

            /* Para o servidor externo, não escrever esses parâmetros */
            if (!connection_type.equals(CONNECTION_TYPE_SERVER_EXTERNAL)) {
                /* Seta a extensão */
                dos.writeUTF(extension);

                /* Seta o tamanho do arquivo */
                dos.writeUTF(String.valueOf(size));
            }

            InputStream is = contentResolver.openInputStream(Uri.parse(fileUri));
            BufferedInputStream bis = new BufferedInputStream(is);

            byte buf[] = new byte[10000]; /* Cria o buffer */
            int len; /* Tamanho da leitura */
            while ((len = bis.read(buf, 0, buf.length)) != -1) { /* Read retorna o tamanho da leitura ou -1 ao finalizar */
                dos.write(buf, 0, len); /* Escreve o buffer no DataOutputStream */
            }
            bis.close();

            dos.flush();

            long end_time = System.currentTimeMillis(); /* Para de contar o tempo */

            WriteLOG.getInstanceGeneratorLOG().writeLogSendReceived(ACTION_SEND, DEVICE_TYPE_CLIENT, size, start_time,
                    end_time, extension, connection_type);

        } catch (IOException e) {
            Log.e(CATEGORY_LOG, EXCEPTION_SAVE_FILE);
            return false; /* Procedimento foi mal sucedido */
        }
        return true; /* Procedimento foi bem sucedido */
    }

    /* Procedimento que trata de copiar o arquivo - Pega do arquivo e insere no stream */
    public static boolean copyFile(File file, DataOutputStream dos, String connection_type) {
        try {
            long start_time = System.currentTimeMillis(); /* Começa a contar o tempo */

            /* Para o servidor externo, não escrever esses parâmetros */
            if (!connection_type.equals(CONNECTION_TYPE_SERVER_EXTERNAL)) {
                /* Seta a extensão */
                dos.writeUTF("." + FilenameUtils.getExtension(file.getAbsolutePath()));

                /* Seta o tamanho do arquivo */
                dos.writeUTF(String.valueOf(file.length()));
            }

            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);

            byte buf[] = new byte[10000]; /* Cria o buffer */
            int len; /* Tamanho da leitura */
            while ((len = bis.read(buf, 0, buf.length)) != -1) { /* Read retorna o tamanho da leitura ou -1 ao finalizar */
                dos.write(buf, 0, len); /* Escreve o buffer no DataOutputStream */
            }
            bis.close();

            dos.flush();

            long end_time = System.currentTimeMillis(); /* Para de contar o tempo */

            WriteLOG.getInstanceGeneratorLOG().writeLogSendReceived(ACTION_SEND, DEVICE_TYPE_CLIENT, file.length(), start_time,
                    end_time,"." + FilenameUtils.getExtension(file.getAbsolutePath()), connection_type);

        } catch (IOException e) {
            Log.e(CATEGORY_LOG, EXCEPTION_SAVE_FILE);
            return false; /* Procedimento foi mal sucedido */
        }
        return true; /* Procedimento foi bem sucedido */
    }

    /* Procedimento que retorna a lista de arquivos do servidor local */
    public static ArrayList<String> getListFilesServerLocal(DataInputStream dis) {
        ArrayList<String> list_files_from_server = new ArrayList<>();
        try {
            while (dis.readBoolean()) {
                list_files_from_server.add(dis.readUTF());
            }
        } catch (Exception e) {
            Log.e(CATEGORY_LOG, EXCEPTION_WRITE_FILE);
        }
        return list_files_from_server;
    }

    /* Procedimento que trata de copiar o arquivo - Pega do stream e insere num arquivo */
    public static String copyFile(DataInputStream dis, String connection_type) {
        String file_name = ""; /* Nome do arquivo */

        try {
            long start_time = System.currentTimeMillis(); /* Começa a contar o tempo */

            /* Pega o tipo da requisição */
            dis.readUTF();

            /* Pega a extensão do arquivo */
            String extension = dis.readUTF();

            /* Pega o tamanho do arquivo */
            int file_size = Integer.parseInt(dis.readUTF());

            File dirs = new File(DIRECTORY_SAVE_FILE);
            if (!dirs.exists()) { /* Se o diretório não existir, então cria o diretório */
                dirs.mkdirs();
            }

            /* Define um nome pro arquivo com base no seu tipo */
            file_name = DIRECTORY_SAVE_FILE + Get.getTypeFile(extension) + "-" + System.currentTimeMillis() + extension;

            /* Copia os dados */
            new File(file_name); /* Cria o arquivo */
            FileOutputStream fos = new FileOutputStream(file_name);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            byte[] buf = new byte[10000];

            int len; /* Tamanho da leitura */
            long size = 0; /* Contador para o tamanho do arquivo */

            do {
                /* Lê os dados do buffer */
                len = dis.read(buf, 0, buf.length);

                /* Escreve os dados no arquivo */
                bos.write(buf, 0, len);

                /* Contabiliza o tamanho */
                size += len;
            } while (len > -1 && size < file_size); /* Read retorna o tamanho da leitura ou -1 ao finalizar */

            bos.flush();

            /* Fecha */
            bos.close();
            fos.close();

            long end_time = System.currentTimeMillis(); /* Para de contar o tempo */

            WriteLOG.getInstanceGeneratorLOG().writeLogSendReceived(ACTION_RECEIVED, DEVICE_TYPE_GO, file_size,
                    start_time, end_time, extension, connection_type);

        } catch (IOException e) {
            Log.e(CATEGORY_LOG, EXCEPTION_SAVE_FILE);
        }
        return file_name;
    }
}
