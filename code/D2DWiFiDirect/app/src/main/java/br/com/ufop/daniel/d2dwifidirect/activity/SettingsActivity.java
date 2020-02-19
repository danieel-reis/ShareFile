package br.com.ufop.daniel.d2dwifidirect.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import br.com.ufop.daniel.d2dwifidirect.R;

import static br.com.ufop.daniel.d2dwifidirect.util.Constants.APP_NAME;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.DISABLE_SHOW_FILE_RECEIVED;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.DISABLE_WRITE_LOG;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.ENABLE_SHOW_FILE_RECEIVED;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.ENABLE_WRITE_LOG;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.SHOW_FILE_RECEIVED;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.WRITE_LOG;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.DIALOG_SETTINGS_OPEN_FILE_RECEIVED_DISABLE;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.DIALOG_SETTINGS_OPEN_FILE_RECEIVED_ENABLE;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.DIALOG_SETTINGS_WRITE_LOG_DISABLE;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.DIALOG_SETTINGS_WRITE_LOG_ENABLE;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.NO;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.TITLE_SETTINGS_OPEN_FILE_RECEIVED;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.TITLE_SETTINGS_WRITE_LOG;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.YES;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Altera o título */
        this.setTitle("");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        /* Atualiza os botões com as preferências */
        load_preferences();
    }

    private void load_preferences() {
        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton_enable_disable_write_log);
        if (MenuActivity.write_log.equals(ENABLE_WRITE_LOG)) {
            imageButton.setImageResource(R.drawable.ic_pencil);
        } else if (MenuActivity.write_log.equals(DISABLE_WRITE_LOG)) {
            imageButton.setImageResource(R.drawable.ic_pencil_off);
        }

        imageButton = (ImageButton) findViewById(R.id.imageButton_enable_disable_file_received);
        if (MenuActivity.show_file_received.equals(ENABLE_SHOW_FILE_RECEIVED)) {
            imageButton.setImageResource(R.drawable.ic_visibility);
        } else if (MenuActivity.show_file_received.equals(DISABLE_SHOW_FILE_RECEIVED)) {
            imageButton.setImageResource(R.drawable.ic_visibility_off);
        }
    }

    public void enable_disable_write_log(View view) {
        final ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton_enable_disable_write_log);

        /* Cria alert dialog perguntando a forma como quer se conectar */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(TITLE_SETTINGS_WRITE_LOG);
        if (MenuActivity.write_log.equals(ENABLE_WRITE_LOG)) {
            builder.setMessage(DIALOG_SETTINGS_WRITE_LOG_DISABLE);
        } else if (MenuActivity.write_log.equals(DISABLE_WRITE_LOG)) {
            builder.setMessage(DIALOG_SETTINGS_WRITE_LOG_ENABLE);
        }

        builder.setPositiveButton(YES, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                if (MenuActivity.write_log.equals(ENABLE_WRITE_LOG)) {
                    MenuActivity.write_log = DISABLE_WRITE_LOG;
                    imageButton.setImageResource(R.drawable.ic_pencil_off);

                } else if (MenuActivity.write_log.equals(DISABLE_WRITE_LOG)) {
                    MenuActivity.write_log = ENABLE_WRITE_LOG;
                    imageButton.setImageResource(R.drawable.ic_pencil);
                }

                SharedPreferences pref = getSharedPreferences(APP_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(WRITE_LOG, MenuActivity.write_log);
                editor.commit();
            }
        });

        builder.setNegativeButton(NO, null);

        AlertDialog alerta = builder.create(); /* Cria o AlertDialog */
        alerta.show(); /* Exibe */
    }

    public void enable_disable_file_received(View view) {
        final ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton_enable_disable_file_received);

        /* Cria alert dialog perguntando a forma como quer se conectar */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(TITLE_SETTINGS_OPEN_FILE_RECEIVED);
        if (MenuActivity.show_file_received.equals(ENABLE_SHOW_FILE_RECEIVED)) {
            builder.setMessage(DIALOG_SETTINGS_OPEN_FILE_RECEIVED_DISABLE);
        } else if (MenuActivity.show_file_received.equals(DISABLE_SHOW_FILE_RECEIVED)) {
            builder.setMessage(DIALOG_SETTINGS_OPEN_FILE_RECEIVED_ENABLE);
        }

        builder.setPositiveButton(YES, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                if (MenuActivity.show_file_received.equals(ENABLE_SHOW_FILE_RECEIVED)) {
                    MenuActivity.show_file_received = DISABLE_SHOW_FILE_RECEIVED;
                    imageButton.setImageResource(R.drawable.ic_visibility_off);

                } else if (MenuActivity.show_file_received.equals(DISABLE_SHOW_FILE_RECEIVED)) {
                    MenuActivity.show_file_received = ENABLE_SHOW_FILE_RECEIVED;
                    imageButton.setImageResource(R.drawable.ic_visibility);
                }

                SharedPreferences pref = getSharedPreferences(APP_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(SHOW_FILE_RECEIVED, MenuActivity.show_file_received);
                editor.commit();
            }
        });

        builder.setNegativeButton(NO, null);

        AlertDialog alerta = builder.create(); /* Cria o AlertDialog */
        alerta.show(); /* Exibe */
    }
}
