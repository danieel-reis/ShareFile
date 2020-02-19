package br.com.ufop.daniel.d2dwifidirect.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Toast;

import br.com.ufop.daniel.d2dwifidirect.R;

import static br.com.ufop.daniel.d2dwifidirect.util.Constants.APP_NAME;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.ENABLE_SHOW_FILE_RECEIVED;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.ENABLE_WRITE_LOG;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.SHOW_FILE_RECEIVED;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.TYPE;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.TYPE_D2D;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.TYPE_FOLDER;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.TYPE_SERVER_EXTERNAL;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.TYPE_SERVER_LOCAL;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.WRITE_LOG;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.DIALOG_SEND_D2D;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.DIALOG_SEND_SERVER_EXTERNAL;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.DIALOG_SEND_SERVER_LOCAL;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.EXCEPTION_OPEN_FOLDER;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.NO;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.PERMISSION_DENIED;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.TITLE_SEND_D2D;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.TITLE_SEND_SERVER_EXTERNAL;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.TITLE_SEND_SERVER_LOCAL;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.YES;

/**
 * Created by daniel on 03/12/17.
 */

public class MenuActivity extends Activity {

    public static boolean permission = false; /* Inicializa as permissões como false, pra depois verificar todas */

    public static String write_log = ENABLE_WRITE_LOG; /* Indica se é permitido ou não escrever o log */
    public static String show_file_received = ENABLE_SHOW_FILE_RECEIVED;

    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Remove o nome da aplicação */
        this.setTitle("");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        /* Carrega as preferências do usuário */
        SharedPreferences pref = getSharedPreferences(APP_NAME, MODE_PRIVATE);
        write_log = pref.getString(WRITE_LOG, ENABLE_WRITE_LOG);  /* identificador e valor default */
        show_file_received = pref.getString(SHOW_FILE_RECEIVED, ENABLE_SHOW_FILE_RECEIVED);  /* identificador e valor default */
    }

    private void verifyPermissions() {
        // Pergunta sobre as permissões
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.GET_ACCOUNTS
        }, 1);
    }

    public void btn_send_D2D(View view) {
        /* Cria alert dialog informando */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(TITLE_SEND_D2D);
        builder.setMessage(DIALOG_SEND_D2D);

        builder.setPositiveButton(YES, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                type = TYPE_D2D;
                verifyPermissions();
            }
        });

        builder.setNegativeButton(NO, null);

        AlertDialog alerta = builder.create(); /* Cria o AlertDialog */
        alerta.show(); /* Exibe */
    }

    public void btn_send_server_local(View view) {
        /* Cria alert dialog informando */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(TITLE_SEND_SERVER_LOCAL);
        builder.setMessage(DIALOG_SEND_SERVER_LOCAL);

        builder.setPositiveButton(YES, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                type = TYPE_SERVER_LOCAL;
                verifyPermissions();
            }
        });

        builder.setNegativeButton(NO, null);

        AlertDialog alerta = builder.create(); /* Cria o AlertDialog */
        alerta.show(); /* Exibe */
    }

    public void btn_send_server_external(View view) {
        /* Cria alert dialog informando */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(TITLE_SEND_SERVER_EXTERNAL);
        builder.setMessage(DIALOG_SEND_SERVER_EXTERNAL);

        builder.setPositiveButton(YES, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                type = TYPE_SERVER_EXTERNAL;
                verifyPermissions();
            }
        });

        builder.setNegativeButton(NO, null);

        AlertDialog alerta = builder.create(); /* Cria o AlertDialog */
        alerta.show(); /* Exibe */
    }

    public void btn_settings(View view) {
        Intent it = new Intent(this, SettingsActivity.class);
        startActivity(it);
    }

    public void btn_received(View view) {
        type = TYPE_FOLDER;
        verifyPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        permission = true; /* Inicializa como true */
        for (int i = 0; i < grantResults.length; ++i) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                permission = false; /* Se encontrar alguma falsa, altera o status pra falsa */
            }
        }

        if (permission) { /* Se as permissões tiverem liberadas */
            if (type == TYPE_SERVER_LOCAL) {
                Intent intent = new Intent(this, SendFileD2DAndServerLocalActivity.class);
                intent.putExtra(TYPE, type);
                startActivity(intent);

            } else if (type == TYPE_SERVER_EXTERNAL) {
                Intent intent = new Intent(this, SendFileServerExternalActivity.class);
                startActivity(intent);

            } else if (type == TYPE_D2D) {
                Intent intent = new Intent(this, SearchConnectDisconnectD2DActivity.class);
                startActivity(intent);

            } else if (type == TYPE_FOLDER) {
//                /* Abre a pasta que contém os arquivos recebidos - preenche na lista */
//                try {
//                    Intent intent = new Intent(this, FolderActivity.class);
//                    startActivity(intent);
//                } catch (Exception e) {
                    /* Cria alerta */
                    Toast.makeText(this, EXCEPTION_OPEN_FOLDER, Toast.LENGTH_SHORT).show();
//                }
            }

        } else {
            /* Permissão não foi concedida -> Negada */
            Toast.makeText(this, PERMISSION_DENIED, Toast.LENGTH_SHORT).show();
        }
    }
}
