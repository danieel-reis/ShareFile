package br.com.ufop.daniel.d2dwifidirect.connect;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.FilenameUtils;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import br.com.ufop.daniel.d2dwifidirect.activity.MenuActivity;
import br.com.ufop.daniel.d2dwifidirect.dao.FileCopy;
import br.com.ufop.daniel.d2dwifidirect.util.Get;

import static br.com.ufop.daniel.d2dwifidirect.util.Constants.CATEGORY_LOG;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.CONNECTION_TYPE_D2D;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.ENABLE_SHOW_FILE_RECEIVED;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.PORT_SOCKET;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.EXCEPTION_GO_SOCKET;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.FILE_RECEIVED;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.STATUS_GO;

/**
 * Created by daniel on 26/07/17.
 * Tarefa assíncrona no servidor de arquivos -> Socket que aceita a conexão e grava os dados
 */

public class D2DGOFileCopyTaskAsync extends AsyncTask<Void, Void, String> {

    private Context context;
    private TextView statusText;
    private String fileName;

    public D2DGOFileCopyTaskAsync(Context context, View statusText) {
        this.context = context;
        this.statusText = (TextView) statusText;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT_SOCKET);

            /* Abre o socket e fica travado esperando a conexão do client -> modo accept */
            Socket socket = serverSocket.accept();
            socket.setKeepAlive(false); /* Desativa a propriedade "keep-alive" que está ativada por padrão */
            System.setProperty("http.keepAlive", "false");

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            serverSocket.close(); /* Fecha o socket */

            fileName = FileCopy.copyFile(dis, CONNECTION_TYPE_D2D); /* Recebe o arquivo */

//            String name[] = fileName.split(File.separator);
//            Notification.showNotification(context, name[name.length - 1], TITLE_NOTIFICATION_RECEIVED);

            return fileName; /* Retorna o caminho do arquivo copiado */

        } catch (IOException e) { /* Erro ao criar socket ou arquivo */
            Log.e(CATEGORY_LOG, EXCEPTION_GO_SOCKET);

//            String name[] = fileName.split(File.separator);
//            Notification.showNotification(context, name[name.length - 1], TITLE_NOTIFICATION_RECEIVED_ERROR);

            return null; /* Erro */
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        statusText.setText(STATUS_GO);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (s != null) {
            statusText.setText(FILE_RECEIVED + s);
            /* Mostra o arquivo recebido */
            Toast.makeText(context, FILE_RECEIVED + s, Toast.LENGTH_SHORT).show();

            /* Se for permitido mostrar o arquivo após recebê-lo */
            if (MenuActivity.show_file_received.equals(ENABLE_SHOW_FILE_RECEIVED)) {

                File file = new File(fileName);
                String extension = FilenameUtils.getExtension(file.getAbsolutePath());
                String typeFile = Get.getMimeType(extension);
                if (!typeFile.equals("")) {
                    try {
                        /* Mostra o arquivo recebido */
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.parse("file://" + s), typeFile);
                        context.startActivity(intent);
                    } catch(Exception e) {

                    }
                }
            }
        }

        /* Reinicia a tarefa assíncrona */
        try {
            this.cancel(true);
            new D2DGOFileCopyTaskAsync(context, statusText).execute();
        } catch (Throwable throwable) {

        }
    }
}
