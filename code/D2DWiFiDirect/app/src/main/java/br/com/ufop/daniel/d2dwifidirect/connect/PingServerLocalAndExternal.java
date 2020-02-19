package br.com.ufop.daniel.d2dwifidirect.connect;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import br.com.ufop.daniel.d2dwifidirect.activity.MenuActivity;
import br.com.ufop.daniel.d2dwifidirect.util.Get;

import static br.com.ufop.daniel.d2dwifidirect.util.Constants.ACTION_PING;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.ADDRESS;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.CATEGORY_LOG;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.CONNECTION_TYPE;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.DIRECTORY_SAVE_FILE_LOG;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.DISABLE_WRITE_LOG;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.ENABLE_WRITE_LOG;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.EXTENSION_FILE_LOG;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.NAME_FILE_PING;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.EXCEPTION_HANDLE_FILE;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.EXCEPTION_WRITE_FILE;

/**
 * Created by daniel on 09/12/17.
 */

public class PingServerLocalAndExternal extends IntentService {

    public PingServerLocalAndExternal() {
        super("PingServerLocalAndExternal");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent.getAction().equals(ACTION_PING)) {

            /* Captura os dados */
            String ip = intent.getExtras().getString(ADDRESS); /* Pega o endereço */
            String type_connection = intent.getExtras().getString(CONNECTION_TYPE); /* Pega o tipo da conexão */

            if (MenuActivity.write_log.equals(DISABLE_WRITE_LOG)) {
                return; /* Não grava */
            } else {
                try {
                    if (MenuActivity.permission) { /* Verifica se todas permissoes estão liberadas */
                        if (MenuActivity.write_log.equals(ENABLE_WRITE_LOG)) {

                            /* Inicia a gravação no arquivo */
                            File dir = new File(DIRECTORY_SAVE_FILE_LOG);
                            dir.mkdirs();
                            File file = new File(DIRECTORY_SAVE_FILE_LOG + NAME_FILE_PING + type_connection + System.currentTimeMillis() + EXTENSION_FILE_LOG);

                            try {
                                BufferedWriter writerPing = new BufferedWriter(new FileWriter(file, true));

                                long init = System.currentTimeMillis();

                                /* Dar ping durante 30 segundos */
                                while ((System.currentTimeMillis() - init) < (1000 * 30)) {
                                    if (writerPing != null) {
                                        String data = Get.ping(ip);

//                                        Log.d(CATEGORY_LOG, data);
                                        writerPing.append(data + "\n");
                                        writerPing.flush();
                                    }
                                }

                                /* Fecha */
                                writerPing.close();

                            } catch (Exception e) {
                                Log.e(CATEGORY_LOG, EXCEPTION_HANDLE_FILE);
                            }
                        }
                    }

                } catch (Exception e) {
                    Log.e(CATEGORY_LOG, EXCEPTION_WRITE_FILE);
                }
            }
        }
    }

}
