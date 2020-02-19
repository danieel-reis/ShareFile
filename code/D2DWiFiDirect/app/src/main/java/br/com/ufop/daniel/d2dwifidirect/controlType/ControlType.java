package br.com.ufop.daniel.d2dwifidirect.controlType;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import static br.com.ufop.daniel.d2dwifidirect.util.Constants.CHOOSE_FILE_RESULT_CODE_IMAGE;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.CHOOSE_FILE_RESULT_CODE_PDF;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.CHOOSE_FILE_RESULT_CODE_VIDEO;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.CONNECTION_TYPE_D2D;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.CONNECTION_TYPE_SERVER_LOCAL;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.REQUEST_TYPE_EXPORT_TO_SERVER_ALL;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.REQUEST_TYPE_IMPORT_FROM_SERVER;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.TYPE_D2D;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.TYPE_SERVER_EXTERNAL;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.TYPE_SERVER_LOCAL;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.WAIT;

/**
 * Created by daniel on 03/12/17.
 */

public class ControlType {

    public static void sendFileD2DAndServerLocal(Intent intent, Activity activity, String type, int requestCode) {
        if (requestCode == CHOOSE_FILE_RESULT_CODE_VIDEO || requestCode == CHOOSE_FILE_RESULT_CODE_PDF) {
            Uri uri = intent.getData();

            if (type.equals(TYPE_D2D)) {
                ControlD2DAndLocal.sendDataImageVideoDocument(activity, uri, type, CONNECTION_TYPE_D2D);
            } else if (type.equals(TYPE_SERVER_LOCAL)) {
                ControlD2DAndLocal.sendDataImageVideoDocument(activity, uri, type, CONNECTION_TYPE_SERVER_LOCAL);
            }

        } else if (requestCode == CHOOSE_FILE_RESULT_CODE_IMAGE) {
            Uri uri = intent.getData();
            if (uri != null) {
                /* Apenas uma imagem foi selecionada */
                if (type.equals(TYPE_D2D)) {
                    ControlD2DAndLocal.sendDataImageVideoDocument(activity, uri, type, CONNECTION_TYPE_D2D);
                } else if (type.equals(TYPE_SERVER_LOCAL)) {
                    ControlD2DAndLocal.sendDataImageVideoDocument(activity, uri, type, CONNECTION_TYPE_SERVER_LOCAL);
                }

            } else {
                /* N imagens selecionadas */
                ClipData clipData = intent.getClipData();
                if (clipData != null) {
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        uri = item.getUri();

                        if (type.equals(TYPE_D2D)) {
                            ControlD2DAndLocal.sendDataImageVideoDocument(activity, uri, type, CONNECTION_TYPE_D2D);
                        } else if (type.equals(TYPE_SERVER_LOCAL)) {
                            ControlD2DAndLocal.sendDataImageVideoDocument(activity, uri, type, CONNECTION_TYPE_SERVER_LOCAL);
                        }
                    }
                }

                /* Exibe toast */
                Toast.makeText(activity, WAIT, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void sendFolderD2DAndServerLocal(Activity activity, String type) {
        if (type.equals(TYPE_D2D)) {
            ControlD2DAndLocal.sendDataFolderAutomaticD2DOrImport(activity, type, REQUEST_TYPE_EXPORT_TO_SERVER_ALL, CONNECTION_TYPE_D2D);
        } else if (type.equals(TYPE_SERVER_LOCAL)) {
            ControlD2DAndLocal.sendDataFolderAutomaticD2DOrImport(activity, type, REQUEST_TYPE_EXPORT_TO_SERVER_ALL, CONNECTION_TYPE_SERVER_LOCAL);
        }
    }

    public static void receivedFiles(Activity activity, String type) {
        switch (type) {
            case TYPE_SERVER_LOCAL: ControlD2DAndLocal.sendDataFolderAutomaticD2DOrImport(activity, type, REQUEST_TYPE_IMPORT_FROM_SERVER, CONNECTION_TYPE_SERVER_LOCAL); break;
            case TYPE_SERVER_EXTERNAL: break;
        }
    }
}
