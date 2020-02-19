package br.com.ufop.daniel.d2dwifidirect.connect;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import br.com.ufop.daniel.d2dwifidirect.activity.SendFileD2DAndServerLocalActivity;
import br.com.ufop.daniel.d2dwifidirect.dao.FileCopy;
import br.com.ufop.daniel.d2dwifidirect.dao.FileOrder;
import br.com.ufop.daniel.d2dwifidirect.dao.WriteLOG;

import static br.com.ufop.daniel.d2dwifidirect.util.Constants.ACTION_CONNECT;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.ACTION_DISCONNECT;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.ACTION_REQUEST_CONNECT;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.ACTION_SEND;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.ADDRESS;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.CATEGORY_LOG;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.CONNECTION_TYPE;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.CONNECTION_TYPE_SERVER_LOCAL;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.DIRECTORY_SEND_FILE_AUTOMATIC;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.EXTENSION_FILE;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.FILE_PATH;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.PORT;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.REQUEST_TYPE;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.REQUEST_TYPE_EXPORT_TO_SERVER;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.REQUEST_TYPE_EXPORT_TO_SERVER_ALL;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.REQUEST_TYPE_IMPORT_FROM_SERVER;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.REQUEST_TYPE_IMPORT_LIST_FILES_FROM_SERVER;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.SOCKET_TIMEOUT;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.EXCEPTION_CLIENT_SOCKET_CLOSE;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.EXCEPTION_CLIENT_SOCKET_OPEN;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.EXCEPTION_RECEIVED_CLIENT_SOCKET;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.EXCEPTION_SEND_CLIENT_SOCKET;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.EXCEPTION_THREAD;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.FILES_NOT_RECEIVED;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.FILE_RECEIVED;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.FILE_SENDING;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.FOLDER_EMPTY;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.TEST_PING;

/**
 * Created by daniel on 26/07/17.
 * Serviço que processa cada solicitação de transferência de arquivo, ou seja, Intent abre uma
 * conexão de socket e escreve o arquivo.
 */

public class D2DAndServerLocalClientServiceTransfer extends IntentService {

    private void exibeToast(final String message) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(D2DAndServerLocalClientServiceTransfer.this.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public D2DAndServerLocalClientServiceTransfer() {
        super("D2DAndServerLocalClientServiceTransfer");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            /* Espera um segundo e tenta enviar o arquivo */
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Log.e(CATEGORY_LOG, EXCEPTION_THREAD);
        }

        Context context = getApplicationContext(); /* Pega o contexto da aplicação */

        if (intent.getAction().equals(ACTION_SEND)) {

            /* Captura os dados */
            String requestType = intent.getExtras().getString(REQUEST_TYPE); /* Pega o tipo da requisição */
            String host = intent.getExtras().getString(ADDRESS); /* Pega o endereço */
            int port = intent.getExtras().getInt(PORT); /* Pega a porta */
            String connection_type = intent.getExtras().getString(CONNECTION_TYPE); /* Pega o tipo da conexão */

            /* Direciona pelo tipo de requisição */
            if (requestType.equals(REQUEST_TYPE_EXPORT_TO_SERVER_ALL)) {
                /* Seta os arquivos da pasta na lista */
                File dir = new File(DIRECTORY_SEND_FILE_AUTOMATIC);
                dir.mkdirs();
                File[] listFiles = dir.listFiles();

                if (listFiles.length > 0) {
                    /* Ordena por tamanho - crescente */
                    FileOrder.quickSortBySize(listFiles, 0, listFiles.length - 1);

                    int size = listFiles.length;
                    int i = 1;
                    for (File file : listFiles) {
                        String filename = file.getName();
                        if (file.exists()) {

                            Socket socket = null;

                            try {
                                /* Gera o Log da requisição de conexão */
                                if (connection_type.equals(CONNECTION_TYPE_SERVER_LOCAL)) {
                                    Toast.makeText(this, TEST_PING, Toast.LENGTH_SHORT).show();
                                    WriteLOG.getInstanceGeneratorLOG().writeLogPing(SendFileD2DAndServerLocalActivity.getAddressServerLocal());
                                    WriteLOG.getInstanceGeneratorLOG().writeLogConnectDisconnect(ACTION_REQUEST_CONNECT, connection_type);
                                }

                                socket = new Socket(host, port); /* Conecta num endereço e porta */
                                socket.setSoTimeout(SOCKET_TIMEOUT);

                                /* Gera o Log de conexão */
                                if (connection_type.equals(CONNECTION_TYPE_SERVER_LOCAL)) {
                                    WriteLOG.getInstanceGeneratorLOG().writeLogConnectDisconnect(ACTION_CONNECT, connection_type);
                                }

                                socket.setKeepAlive(false); /* Desativa a propriedade "keep-alive" que está ativada por padrão */
                                System.setProperty("http.keepAlive", "false");

                                /* Mostra o arquivo a ser enviado */
                                exibeToast(FILE_SENDING + filename + " (" + i++ + "/" + size + ")");

                                /* Pega o OutputStream do socket */
                                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                                /* Seta o tipo da requisição */
                                dos.writeUTF(requestType);
                                dos.flush();

                                /* Copia o arquivo no DataOutputStream */
                                FileCopy.copyFile(file, dos, connection_type);

                            } catch (IOException e) { /* Erro ao criar socket */
                                exibeToast(EXCEPTION_SEND_CLIENT_SOCKET + filename);

                            } finally {
                                if (socket != null) { /* Se existir um socket */
                                    try {
                                        socket.close(); /* Fecha o socket */

                                        /* Gera o Log de desconexão */
                                        if (connection_type.equals(CONNECTION_TYPE_SERVER_LOCAL)) {
                                            WriteLOG.getInstanceGeneratorLOG().writeLogConnectDisconnect(ACTION_DISCONNECT, connection_type);
                                        }

                                        try {
                                            /* Espera um segundo e tenta enviar o arquivo */
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {
                                            Log.e(CATEGORY_LOG, EXCEPTION_THREAD);
                                        }

                                    } catch (IOException e) { /* Erro ao fechar o socket */
                                        Log.e(CATEGORY_LOG, EXCEPTION_CLIENT_SOCKET_CLOSE);
                                    }
                                }
                            }

                        }

                    }
                } else {
                    exibeToast(FOLDER_EMPTY);
                }


            } else if (requestType.equals(REQUEST_TYPE_EXPORT_TO_SERVER)) {
                String fileUri = intent.getExtras().getString(FILE_PATH); /* Pega o caminho do arquivo */
                String fileExtension = intent.getExtras().getString(EXTENSION_FILE); /* Pega a extensão do arquivo */

                Socket socket = new Socket(); /* Instancia a classe */

                try {
                    context.getContentResolver().openInputStream(Uri.parse(fileUri)); /* Tenta abrir o arquivo escolhido pelo usuário */

                    /* Gera o Log da requisição de conexão */
                    if (connection_type.equals(CONNECTION_TYPE_SERVER_LOCAL)) {
                        Toast.makeText(this, TEST_PING, Toast.LENGTH_SHORT).show();
                        WriteLOG.getInstanceGeneratorLOG().writeLogPing(SendFileD2DAndServerLocalActivity.getAddressServerLocal());
                        WriteLOG.getInstanceGeneratorLOG().writeLogConnectDisconnect(ACTION_REQUEST_CONNECT, connection_type);
                    }

                    socket = new Socket(host, port); /* Conecta num endereço e porta */
                    socket.setSoTimeout(SOCKET_TIMEOUT);

                    /* Gera o Log de conexão */
                    if (connection_type.equals(CONNECTION_TYPE_SERVER_LOCAL)) {
                        WriteLOG.getInstanceGeneratorLOG().writeLogConnectDisconnect(ACTION_CONNECT, connection_type);
                    }

                    socket.setKeepAlive(false); /* Desativa a propriedade "keep-alive" que está ativada por padrão */
                    System.setProperty("http.keepAlive", "false");

                    /* Mostra o arquivo a ser enviado */
                    exibeToast(FILE_SENDING + fileUri);

                    /* Pega o OutputStream do socket */
                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                    /* Seta o tipo da requisição */
                    dos.writeUTF(requestType);
                    dos.flush();

                    /* Copia o arquivo no DataOutputStream */
                    FileCopy.copyFile(context.getContentResolver(), fileUri, dos, fileExtension, connection_type);

                } catch (Exception e) { /* Erro ao criar socket ou encontrar arquivo */
                    exibeToast(EXCEPTION_SEND_CLIENT_SOCKET + fileUri);

                } finally {
                    if (socket != null) { /* Se existir um socket */
                        if (socket.isConnected()) { /* Testa se o socket está conectado */
                            try {
                                socket.close(); /* Fecha o socket */

                                /* Gera o Log de desconexão */
                                if (connection_type.equals(CONNECTION_TYPE_SERVER_LOCAL)) {
                                    WriteLOG.getInstanceGeneratorLOG().writeLogConnectDisconnect(ACTION_DISCONNECT, connection_type);
                                }

                            } catch (IOException e) { /* Erro ao fechar o socket */
                                Log.e(CATEGORY_LOG, EXCEPTION_CLIENT_SOCKET_CLOSE);
                            }
                        }
                    }
                }

            } else if (requestType.equals(REQUEST_TYPE_IMPORT_FROM_SERVER) && connection_type.equals(CONNECTION_TYPE_SERVER_LOCAL)) {

                ArrayList<String> list_files_server_local = null;
                Socket socket = null;
                try {
                    socket = new Socket(host, port); /* Conecta num endereço e porta */
                    socket.setSoTimeout(SOCKET_TIMEOUT);

                    socket.setKeepAlive(false); /* Desativa a propriedade "keep-alive" que está ativada por padrão */
                    System.setProperty("http.keepAlive", "false");

                    DataInputStream dis = new DataInputStream(socket.getInputStream());
                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                    /* Seta o tipo da requisição */
                    dos.writeUTF(REQUEST_TYPE_IMPORT_LIST_FILES_FROM_SERVER);
                    dos.flush();

                    /* Pega a lista de arquivos do servidor */
                    list_files_server_local = FileCopy.getListFilesServerLocal(dis);

                } catch (IOException e) { /* Erro ao criar socket */
                    Log.e(CATEGORY_LOG, EXCEPTION_CLIENT_SOCKET_OPEN);

                } finally {
                    if (socket != null) { /* Se existir um socket */
                        try {
                            socket.close(); /* Fecha o socket */

                        } catch (IOException e) { /* Erro ao fechar o socket */
                            Log.e(CATEGORY_LOG, EXCEPTION_CLIENT_SOCKET_CLOSE);
                        }
                    }

                    /* Nenhum arquivo no servidor */
                    if(list_files_server_local == null || list_files_server_local.size() == 0) {
                        exibeToast(FILES_NOT_RECEIVED);
                    }

                    /* Solicita cada arquivo da lista para o servidor local */
                    int cont = 1;
                    int size = list_files_server_local.size();
                    for (String file : list_files_server_local) {
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            Log.e(CATEGORY_LOG, EXCEPTION_THREAD);
                        }

                        try {
                            socket = new Socket(host, port); /* Conecta num endereço e porta */
                            socket.setSoTimeout(SOCKET_TIMEOUT);

                            socket.setKeepAlive(false); /* Desativa a propriedade "keep-alive" que está ativada por padrão */
                            System.setProperty("http.keepAlive", "false");

                            DataInputStream dis = new DataInputStream(socket.getInputStream());
                            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                            /* Seta o tipo da requisição */
                            dos.writeUTF(REQUEST_TYPE_IMPORT_FROM_SERVER);
                            dos.flush();

                            /* Seta o arquivo que quero pegar do servidor local */
                            dos.writeUTF(file);
                            dos.flush();

                            /* Recebe o arquivo */
                            String file_name = FileCopy.copyFile(dis, REQUEST_TYPE_IMPORT_FROM_SERVER);
                            if (file_name != null && file_name.length() > 0) {
                                exibeToast(FILE_RECEIVED + file_name + " (" + cont++ + "/" + size + ")" );
                            } else {
                                exibeToast(EXCEPTION_RECEIVED_CLIENT_SOCKET + file_name);
                            }

                        } catch (IOException e) { /* Erro ao criar socket */
                            Log.e(CATEGORY_LOG, EXCEPTION_CLIENT_SOCKET_OPEN);

                        } finally {
                            if (socket != null) { /* Se existir um socket */
                                try {
                                    socket.close(); /* Fecha o socket */

                                } catch (IOException e) { /* Erro ao fechar o socket */
                                    Log.e(CATEGORY_LOG, EXCEPTION_CLIENT_SOCKET_CLOSE);
                                }
                            }
                        }
                    }
                }

            }

        }
    }
}
