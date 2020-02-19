package dao;

import br.com.ufop.daniel.d2dwifidirect.entities.DataSendReceived;
import static connect.Constants.ACTION_RECEIVED;
import static connect.Constants.ACTION_SEND;
import static connect.Constants.CONNECTION_TYPE_SERVER_LOCAL;
import static connect.Constants.DEVICE_TYPE_CLIENT;
import static connect.Constants.DEVICE_TYPE_GO;
import static connect.Constants.DIRECTORY_SAVE_FILE;
import connect.MainWindown;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.commons.io.FilenameUtils;

/**
 * Created by daniel on 26/07/17.
 */
public class FileCopy {

    private static void insertLOG(String device_address, String device_name, String action, String device_type, long size, long startTime, long endTime, String fileExtension) {
        DataSendReceived data = new DataSendReceived();
        data.setAction(action);
        data.setDevice_type(device_type);
        data.setDevice_name(device_name);
        data.setDevice_address(device_address);
        data.setSize_file(String.valueOf(size));
        data.setTimestamp_init(String.valueOf(startTime));
        data.setTimestamp_end(String.valueOf(endTime));
        data.setType_file(fileExtension);
        data.setConnection_type(CONNECTION_TYPE_SERVER_LOCAL);
        WriteLOG.getInstanceGeneratorLOG().writeLog(data);

        String message = "Tempo gasto = " + (endTime - startTime) + " ms"
                + "\nTamanho do arquivo = " + size + " bytes";
        MainWindown.appendText(message);

    }

    /* Procedimento que trata de copiar o arquivo */
    public static String copyFile(DataInputStream dis, String device_address, String device_name, String typeRequest) {
        String file_name = "";
        /* Nome do arquivo */

        try {
            /* Começa a contar o tempo */
            long start_time = System.currentTimeMillis();

            /* Pega a extensão do arquivo */
            String extension = dis.readUTF();

            /* Pega o tamanho do arquivo */
            int file_size = Integer.parseInt(dis.readUTF());

            File dirs = new File(DIRECTORY_SAVE_FILE);
            if (!dirs.exists()) {
                /* Se o diretório não existir, então cria o diretório */
                dirs.mkdirs();
            }

            /* Define um nome pro arquivo com base no seu tipo */
            file_name = DIRECTORY_SAVE_FILE + System.currentTimeMillis() + extension;
            MainWindown.appendText("Arquivo recebido: " + file_name);

            /* Copia os dados */
            new File(file_name);
            /* Cria o arquivo */
            FileOutputStream fos = new FileOutputStream(file_name);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            byte[] buf = new byte[10000];

            int len;
            /* Tamanho da leitura */
            long size = 0;
            /* Contador para o tamanho do arquivo */

            do {
                /* Lê os dados do buffer */
                len = dis.read(buf, 0, buf.length);

                /* Escreve os dados no arquivo */
                bos.write(buf, 0, len);

                /* Contabiliza o tamanho */
                size += len;
            } while (len > -1 && size < file_size);
            /* Read retorna o tamanho da leitura ou -1 ao finalizar */

            bos.flush();

            /* Fecha */
            bos.close();
            fos.close();

            long end_time = System.currentTimeMillis();
            /* Para de contar o tempo */

            insertLOG(device_address, device_name, ACTION_RECEIVED, DEVICE_TYPE_GO, file_size, start_time, end_time, extension);

        } catch (IOException e) {
            System.err.println("    Erro ao gravar o arquivo: " + e.getMessage());
        }
        return file_name;
    }

    /* Procedimento que trata de copiar o arquivo */
    public static boolean copyFyle(File file, DataOutputStream dos, String device_address, String device_name) {
        try {
            /* Começa a contar o tempo */
            long start_time = System.currentTimeMillis();

            /* Seta a extensão */
            dos.writeUTF("." + FilenameUtils.getExtension(file.getAbsolutePath()));

            /* Seta o tamanho do arquivo */
            dos.writeUTF(String.valueOf(file.length()));

            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);

            /* Cria o buffer */
            byte buf[] = new byte[10000];

            /* Tamanho da leitura */
            int len;
            while ((len = bis.read(buf, 0, buf.length)) != -1) {
                /* Read retorna o tamanho da leitura ou -1 ao finalizar */
                dos.write(buf, 0, len);
                /* Escreve o buffer no DataOutputStream */
            }
            bis.close();

            dos.flush();

            /* Para de contar o tempo */
            long end_time = System.currentTimeMillis();

            insertLOG(device_address, device_name, ACTION_SEND, DEVICE_TYPE_CLIENT, file.length(), start_time, end_time, "." + FilenameUtils.getExtension(file.getAbsolutePath()));

        } catch (IOException e) {
            System.err.println("    Erro ao gravar o arquivo: " + e.getMessage());
            return false;
            /* Procedimento foi mal sucedido */
        }
        return true;
        /* Procedimento foi bem sucedido */
    }
}
