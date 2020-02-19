package br.com.ufop.daniel.d2dwifidirect.controlType;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import java.io.InputStream;

import br.com.ufop.daniel.d2dwifidirect.activity.SendFileD2DAndServerLocalActivity;
import br.com.ufop.daniel.d2dwifidirect.connect.D2DAndServerLocalClientServiceTransfer;
import br.com.ufop.daniel.d2dwifidirect.fragment.DeviceFragment;
import br.com.ufop.daniel.d2dwifidirect.util.Get;

import static br.com.ufop.daniel.d2dwifidirect.util.Constants.ACTION_SEND;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.ADDRESS;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.CONNECTION_TYPE;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.EXTENSION_FILE;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.FILE_PATH;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.LOCAL_SERVER_PORT_SOCKET;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.PORT;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.PORT_SOCKET;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.REQUEST_TYPE;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.REQUEST_TYPE_EXPORT_TO_SERVER;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.TYPE_D2D;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.TYPE_SERVER_LOCAL;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.EXCEPTION_READ_FILE;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.EXCEPTION_SEND_FILE_DRIVE;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.WAIT;

/**
 * Created by daniel on 03/12/17.
 */

public class ControlD2DAndLocal {

    /* Prepara e solicita o início do serviço */
    protected static void sendDataImageVideoDocument(Activity activity, Uri uri, String type, String connectionType) {
        if (uri != null) {
            /* Testa se a URI é do Google Drive */
            if ("com.google.android.apps.docs.storage".equals(uri.getAuthority())) {
                Toast.makeText(activity, EXCEPTION_SEND_FILE_DRIVE, Toast.LENGTH_SHORT).show();
                return;
            }

            /* Tenta ler o arquivo */
            try {
                InputStream is = activity.getContentResolver().openInputStream(uri);
                if (is == null) {
                    Toast.makeText(activity, EXCEPTION_READ_FILE, Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e) {
                Toast.makeText(activity, EXCEPTION_READ_FILE, Toast.LENGTH_SHORT).show();
                return;
            }

            /* Exibe toast */
            Toast.makeText(activity, WAIT, Toast.LENGTH_SHORT).show();


            /* Inicia serviço de transferência com os dados da intent */
            Intent serviceIntent = new Intent(activity, D2DAndServerLocalClientServiceTransfer.class);

            /* Seta a ação do serviço */
            serviceIntent.setAction(ACTION_SEND);

            /* Seta o tipo da requisição */
            serviceIntent.putExtra(REQUEST_TYPE, REQUEST_TYPE_EXPORT_TO_SERVER);

            /* Seta o endereço e porta */
            String address = "";
            int port = 0;
            if (type.equals(TYPE_D2D)) {
                address = DeviceFragment.getHostAddress();
                port = PORT_SOCKET;

            } else if (type.equals(TYPE_SERVER_LOCAL)) {
                address = SendFileD2DAndServerLocalActivity.getAddressServerLocal();
                port = LOCAL_SERVER_PORT_SOCKET;
            }
            serviceIntent.putExtra(ADDRESS, address);
            serviceIntent.putExtra(PORT, port);

            /* Seta o tipo da conexão */
            serviceIntent.putExtra(CONNECTION_TYPE, connectionType);

            /* Seta a uri -> caminho do arquivo */
            serviceIntent.putExtra(FILE_PATH, uri.toString());

            /* Seta a extensão do arquivo */
            serviceIntent.putExtra(EXTENSION_FILE, "." + Get.getExtension(uri, activity.getApplicationContext()));

            /* Verifica se o endereço é válido */
            if (Get.verifyAddressIP(activity, address)) {
                /* Inicia o serviço */
                activity.startService(serviceIntent);
            }
        }
    }

    /* Prepara e solicita o início do serviço */
    protected static void sendDataFolderAutomaticD2DOrImport(Activity activity, String type, String requestType, String connectionType) {
        /* Cria a intent -> Serviço de Transferência */
        Intent serviceIntent = new Intent(activity, D2DAndServerLocalClientServiceTransfer.class);

        /* Seta a ação do serviço */
        serviceIntent.setAction(ACTION_SEND);

        /* Seta o tipo da requisição */
        serviceIntent.putExtra(REQUEST_TYPE, requestType);

        /* Seta o endereço e porta */
        String address = "";
        int port = 0;
        if (type.equals(TYPE_D2D)) {
            address = DeviceFragment.getHostAddress();
            port = PORT_SOCKET;

        } else if (type.equals(TYPE_SERVER_LOCAL)) {
            address = SendFileD2DAndServerLocalActivity.getAddressServerLocal();
            port = LOCAL_SERVER_PORT_SOCKET;
        }
        serviceIntent.putExtra(ADDRESS, address);
        serviceIntent.putExtra(PORT, port);

        /* Seta o tipo da conexão */
        serviceIntent.putExtra(CONNECTION_TYPE, connectionType);

        /* Verifica se o endereço é válido */
        if (Get.verifyAddressIP(activity, address)) {
            /* Inicia o serviço */
            activity.startService(serviceIntent);
        }
    }
}
