package br.com.ufop.daniel.d2dwifidirect.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import br.com.ufop.daniel.d2dwifidirect.R;
import br.com.ufop.daniel.d2dwifidirect.connect.PingServerLocalAndExternal;
import br.com.ufop.daniel.d2dwifidirect.connect.ServerLocalUdpClientThread;
import br.com.ufop.daniel.d2dwifidirect.controlType.ControlType;
import br.com.ufop.daniel.d2dwifidirect.fragment.DeviceFragment;
import br.com.ufop.daniel.d2dwifidirect.util.Get;

import static br.com.ufop.daniel.d2dwifidirect.util.Constants.ACTION_PING;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.ADDRESS;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.CATEGORY_LOG;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.CHOOSE_FILE_RESULT_CODE_IMAGE;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.CHOOSE_FILE_RESULT_CODE_PDF;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.CHOOSE_FILE_RESULT_CODE_VIDEO;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.CONNECTION_TYPE;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.CONNECTION_TYPE_SERVER_LOCAL;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.TYPE;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.TYPE_D2D;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.TYPE_FILE_IMAGE_FULL;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.TYPE_FILE_PDF_FULL;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.TYPE_FILE_VIDEO_FULL;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.TYPE_SERVER_LOCAL;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.DIALOG_IMPORT_FOLDER;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.DIALOG_PING;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.DIALOG_SEND_FOLDER;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.EXCEPTION_DEVICE_NOT_FOUND;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.EXCEPTION_NETWORK_WIFI_UNAVAILABLE;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.EXCEPTION_THREAD;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.IMPORT;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.IMPORT_DESCRIPTION;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.NO;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.RETURN;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.RETURN_DESCRIPTION;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.SEARCH_SERVER_LOCAL;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.TEST_PING;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.TITLE_IMPORT_FOLDER;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.TITLE_PING;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.TITLE_SEND_FOLDER;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.YES;

/**
 * Created by daniel on 03/12/17.
 */

public class SendFileD2DAndServerLocalActivity extends Activity {

    private String type;

    private static String addressServerLocal = "";
    public static long timestamp = 0;
    private static int cont = 0;
    private static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Remove o nome da aplicação */
        this.setTitle("");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_types);

        /* Pega o tipo */
        Intent it = getIntent();
        Bundle params = it.getExtras();
        type = params.getString(TYPE);

        /* Altera a imagem e título de acordo com o tipo */
        ImageButton imageButtonTypeImage = (ImageButton) findViewById(R.id.type_image);
        ImageButton imageButtonDisconnectOrImportFile = (ImageButton) findViewById(R.id.btn_disconnectOrImportFile);
        TextView textView1 = (TextView) findViewById(R.id.type_name);
        textView1.setText(type);
        TextView textView2 = (TextView) findViewById(R.id.textEndOrImport);
        TextView textView3 = (TextView) findViewById(R.id.textDisconnectOrReturn);

        switch (type) {
            case TYPE_D2D:
                imageButtonTypeImage.setBackgroundResource(R.drawable.type_ic_device);
                imageButtonDisconnectOrImportFile.setBackgroundResource(R.drawable.ic_remove);
                textView2.setText(RETURN);
                textView3.setText(RETURN_DESCRIPTION);
                break;
            case TYPE_SERVER_LOCAL:
                imageButtonTypeImage.setBackgroundResource(R.drawable.type_ic_computer);
                imageButtonDisconnectOrImportFile.setBackgroundResource(R.drawable.ic_download);
                textView2.setText(IMPORT);
                textView3.setText(IMPORT_DESCRIPTION);
                searchAddressServerLocal();
                break;
        }

        activity = this;

        if (type.equals(TYPE_SERVER_LOCAL)) {
            imageButtonTypeImage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    /* Cria alert dialog perguntando se realmente deseja gerar os pings */
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle(TITLE_PING);
                    builder.setMessage(DIALOG_PING);

                    builder.setPositiveButton(YES, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            if (verifyAddressServer()) {
                                /* Gera os Pings */
                                Toast.makeText(activity, TEST_PING, Toast.LENGTH_SHORT).show();

                                /* Inicia serviço de transferência com os dados da intent */
                                Intent serviceIntent = new Intent(activity, PingServerLocalAndExternal.class);

                                /* Seta a ação do serviço */
                                serviceIntent.setAction(ACTION_PING);

                                /* Seta o endereço */
                                serviceIntent.putExtra(ADDRESS, getAddressServerLocal());

                                /* Seta o tipo da requisição */
                                serviceIntent.putExtra(CONNECTION_TYPE, CONNECTION_TYPE_SERVER_LOCAL);

                                /* Inicia o serviço */
                                activity.startService(serviceIntent);
                            }
                        }
                    });

                    builder.setNegativeButton(NO, null);
                    AlertDialog alerta = builder.create(); /* Cria o AlertDialog */
                    alerta.show(); /* Exibe */
                    return false;
                }
            });
        }
    }

    public static void setAddressServerLocal(String address) {
        addressServerLocal = address;
    }

    public static String getAddressServerLocal() {
        return addressServerLocal;
    }

    public static String getMyAddress() {
        return Get.getMyAddress(activity);
    }

    /* Usando 10.0.2.2 no emulador */
    private void searchAddressServerLocal() {
        /* Se a rede estiver disponível */
        if (Get.getNetworkAvailableWiFi(this)) {

            String myAddress = Get.getMyAddress(this);
            /* Se meu endereço for válido */
            if (myAddress != null && !myAddress.equals("")) {

                /* Descobre o endereço do servidor */
                if ((System.currentTimeMillis() - timestamp) > 60000) {
                    cont = 0; /* Reseta o contador após um minuto */
                }

                /* Se a última busca foi a pelo menos 10 segundos atrás e se foram buscados uma ou nenhuma vez */
                if (timestamp == 0 || (cont < 2 && (System.currentTimeMillis() - timestamp) > 10000)) {
                    timestamp = System.currentTimeMillis(); /* Seta a hora atual */

                    /* Permite realizar uma nova busca */
                    if (addressServerLocal == null || addressServerLocal.equals("")) {
                        Toast.makeText(this, SEARCH_SERVER_LOCAL, Toast.LENGTH_SHORT).show();

                        try {
                            new ServerLocalUdpClientThread(myAddress, this).start();
                        } catch (Exception e) {
                            Log.e(CATEGORY_LOG, EXCEPTION_THREAD);
                        }
                    }
                    cont++; /* Indica que foi feita mais uma busca */
                }
            } else {
                Toast.makeText(this, EXCEPTION_NETWORK_WIFI_UNAVAILABLE, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Boolean verifyAddressServer() {
        if (type.equals(TYPE_SERVER_LOCAL) && !Get.getNetworkAvailableWiFi(this)) { /* Se a rede estiver disponível */
            Toast.makeText(this, EXCEPTION_NETWORK_WIFI_UNAVAILABLE, Toast.LENGTH_SHORT).show();
            return false;
        }

        switch (type) {
            case TYPE_D2D:
                if (DeviceFragment.getHostAddress() == null || DeviceFragment.getHostAddress().equals("")) {
                    Toast.makeText(this, EXCEPTION_DEVICE_NOT_FOUND, Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
            case TYPE_SERVER_LOCAL:
                if (getAddressServerLocal() == null || getAddressServerLocal().equals("")) {
                    Toast.makeText(this, EXCEPTION_DEVICE_NOT_FOUND, Toast.LENGTH_SHORT).show();
                    searchAddressServerLocal();
                    return false;
                }
                break;
        }
        return true;
    }

    /* OnClick do Botão que Abre a Galeria -> Imagem */
    public void btn_send_image(View view) {
        if (verifyAddressServer()) {
            /* Permitir que o usuário escolha uma imagem da Galeria ou de outras aplicações registradas */
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType(TYPE_FILE_IMAGE_FULL);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); /* Permite selecionar várias imagens */
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true); /* Permite selecionar apenas arquivos locais */
            startActivityForResult(intent, CHOOSE_FILE_RESULT_CODE_IMAGE);
        }
    }

    /* OnClick do Botão que Abre a Galeria -> Vídeo */
    public void btn_send_video(View view) {
        if (verifyAddressServer()) {
            /* Permitir que o usuário escolha um vídeo da Galeria ou de outras aplicações registradas */
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType(TYPE_FILE_VIDEO_FULL);
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true); /* Permite selecionar apenas arquivos locais */
            startActivityForResult(intent, CHOOSE_FILE_RESULT_CODE_VIDEO);
        }
    }

    /* OnClick do Botão que Abre os Documentos -> PDF */
    public void btn_send_document(View view) {
        if (verifyAddressServer()) {
            /* Permitir que o usuário escolha um documento PDF ou de outras aplicações registradas */
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType(TYPE_FILE_PDF_FULL);
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true); /* Permite selecionar apenas arquivos locais */
            startActivityForResult(intent, CHOOSE_FILE_RESULT_CODE_PDF);
        }
    }

    /* OnClick do Botão que Envia todos arquivos de uma pasta */
    public void btn_send_folder(View view) {
        if (verifyAddressServer()) {
            final Activity activity = this;
            /* Cria alert dialog perguntando se realmente deseja enviar os dados daquela pasta */
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(TITLE_SEND_FOLDER);
            builder.setMessage(DIALOG_SEND_FOLDER);

            builder.setPositiveButton(YES, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    /* Solicita o envio */
                    ControlType.sendFolderD2DAndServerLocal(activity, type);
                }
            });

            builder.setNegativeButton(NO, null);
            AlertDialog alerta = builder.create(); /* Cria o AlertDialog */
            alerta.show(); /* Exibe */
        }
    }

    /* OnClick do Botão voltar */
    public void btn_disconnectOrImport(View view) {
        if (type.equals(TYPE_D2D)) {
            finish();

        } else if (type.equals(TYPE_SERVER_LOCAL)) {
            if (verifyAddressServer()) {
                /* Cria alert dialog perguntando se realmente deseja receber os dados do servidor */
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(TITLE_IMPORT_FOLDER);
                builder.setMessage(DIALOG_IMPORT_FOLDER);

                builder.setPositiveButton(YES, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        ControlType.receivedFiles(activity, type);
                    }
                });

                builder.setNegativeButton(NO, null);
                AlertDialog alerta = builder.create(); /* Cria o AlertDialog */
                alerta.show(); /* Exibe */
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (intent != null) {
            ControlType.sendFileD2DAndServerLocal(intent, this, type, requestCode);
        }
    }
}
