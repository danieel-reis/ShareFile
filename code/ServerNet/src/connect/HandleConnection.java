/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connect;

import static connect.Constants.DIRECTORY_SEND_FILE_AUTOMATIC;
import static connect.Constants.REQUEST_TYPE_EXPORT_TO_SERVER;
import static connect.Constants.REQUEST_TYPE_EXPORT_TO_SERVER_ALL;
import static connect.Constants.REQUEST_TYPE_IMPORT_FROM_SERVER;
import static connect.Constants.REQUEST_TYPE_IMPORT_LIST_FILES_FROM_SERVER;
import dao.FileCopy;
import dao.FileOrder;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author daniel
 */
public class HandleConnection implements Runnable {

    private Socket connection;
    private DataInputStream dis;
    private DataOutputStream dos;
    private String deviceAddress;
    private String deviceName;

    public HandleConnection(Socket connection, String deviceName, String deviceAddress) {
        this.connection = connection;
        this.deviceName = deviceName;
        this.deviceAddress = deviceAddress;
    }

    @Override
    public void run() {
        System.out.println("Executando...");
        try {
            dis = new DataInputStream(connection.getInputStream());
            dos = new DataOutputStream(connection.getOutputStream());
            String typeRequest = dis.readUTF();
            System.out.println(typeRequest);

            /* Ação a ser feita */
            if (typeRequest.equals(REQUEST_TYPE_EXPORT_TO_SERVER) || typeRequest.equals(REQUEST_TYPE_EXPORT_TO_SERVER_ALL)) {
                System.out.println("Ação: Exportar para servidor");

                /* Receber mensagem */
                String fileName = FileCopy.copyFile(dis, deviceAddress, deviceName, typeRequest);

                if (fileName.equals("")) {
                    System.out.println("Erro ao receber: " + fileName);
                } else {
                    System.out.println("Arquivo recebido: " + fileName);
                }

            } else if (typeRequest.equals(REQUEST_TYPE_IMPORT_FROM_SERVER)) {
                System.out.println("Ação: Importar do servidor");

                /* Arquivo solicitado */
                File file = new File(dis.readUTF());

                /* Se o arquivo ainda existir no servidor */
                if (file.exists()) {

                    /* Seta o tipo da requisição */
                    dos.writeUTF(file.getAbsolutePath());

                    /* Copia o arquivo no DataOutputStream */
                    FileCopy.copyFyle(file, dos, deviceAddress, deviceName);
                    System.out.println("Arquivo enviado: " + file.getCanonicalPath());

                } else {
                    System.out.println("Erro ao enviar: " + file.getCanonicalPath());
                }

            } else if (typeRequest.equals(REQUEST_TYPE_IMPORT_LIST_FILES_FROM_SERVER)) {
                System.out.println("Ação: Lista de arquivos do servidor");

                /* Enviar mensagem */
                File dir = new File(DIRECTORY_SEND_FILE_AUTOMATIC);
                dir.mkdirs();

                /* Seta os arquivos da pasta na lista */
                File[] listFiles = dir.listFiles();

                if (listFiles.length > 0) {
                    /* Ordena por tamanho - crescente */
                    FileOrder.quickSortBySize(listFiles, 0, listFiles.length - 1);

                    for (File file : listFiles) {
                        if (file.exists()) {
                            dos.writeBoolean(true);
                            dos.flush();

                            /* Seta o tipo da requisição */
                            dos.writeUTF(file.getAbsolutePath());
                        }
                    }
                    dos.writeBoolean(false);
                    dos.flush();
                }

            }
            if (dis != null) {
                dis.close();
            }
            if (dos != null) {
                dos.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
