package br.com.ufop.daniel.d2dwifidirect.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.OpenFileActivityBuilder;

import org.apache.commons.io.FilenameUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import br.com.ufop.daniel.d2dwifidirect.R;
import br.com.ufop.daniel.d2dwifidirect.connect.PingServerLocalAndExternal;
import br.com.ufop.daniel.d2dwifidirect.dao.FileCopy;
import br.com.ufop.daniel.d2dwifidirect.dao.FileOrder;
import br.com.ufop.daniel.d2dwifidirect.dao.WriteLOG;
import br.com.ufop.daniel.d2dwifidirect.util.Get;

import static br.com.ufop.daniel.d2dwifidirect.util.Constants.ACTION_CONNECT;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.ACTION_DISCONNECT;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.ACTION_PING;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.ACTION_REQUEST_CONNECT;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.ADDRESS;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.ADDRESS_GOOGLE_DRIVE;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.CATEGORY_LOG;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.CHOOSE_FILE_RESULT_CODE_IMAGE;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.CHOOSE_FILE_RESULT_CODE_PDF;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.CHOOSE_FILE_RESULT_CODE_VIDEO;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.CHOOSE_OPEN_GOOGLE_DRIVE;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.CONNECTION_TYPE;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.CONNECTION_TYPE_SERVER_EXTERNAL;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.DIRECTORY_SEND_FILE_AUTOMATIC;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.TYPE_FILE_IMAGE_FULL;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.TYPE_FILE_PDF_FULL;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.TYPE_FILE_VIDEO_FULL;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.TYPE_SERVER_EXTERNAL;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.DIALOG_PING;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.DIALOG_SEND_FOLDER;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.EXCEPTION_CONNECTED_DRIVE;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.EXCEPTION_OPEN_DRIVE;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.EXCEPTION_READ_FILE;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.EXCEPTION_SEND_CLIENT_SOCKET;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.EXCEPTION_SEND_FILE_DRIVE;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.EXCEPTION_THREAD;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.FILE_SENDING;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.FOLDER_EMPTY;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.FOLDER_OPEN_GOOGLE_DRIVE;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.NO;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.OPEN_GOOGLE_DRIVE;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.OPEN_GOOGLE_DRIVE_DESCRIPTION;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.TEST_PING;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.TITLE_PING;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.TITLE_SEND_FOLDER;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.WAIT;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.YES;

/**
 * Created by daniel on 03/12/17.
 */

public class SendFileServerExternalActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private Boolean connected;
    private Activity activity;
    private DriveId mFileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        connected = false;

        /* Remove o nome da aplicação */
        this.setTitle("");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_types);

        /* Altera a imagem e título de acordo com o tipo */
        ImageButton imageButtonTypeImage = (ImageButton) findViewById(R.id.type_image);
        ImageButton imageButtonDisconnectOrImportFile = (ImageButton) findViewById(R.id.btn_disconnectOrImportFile);
        TextView textView1 = (TextView) findViewById(R.id.type_name);
        textView1.setText(TYPE_SERVER_EXTERNAL);
        TextView textView2 = (TextView) findViewById(R.id.textEndOrImport);
        TextView textView3 = (TextView) findViewById(R.id.textDisconnectOrReturn);

        imageButtonTypeImage.setBackgroundResource(R.drawable.type_ic_drive);
        imageButtonDisconnectOrImportFile.setBackgroundResource(R.drawable.ic_folder_google_drive);
        textView2.setText(OPEN_GOOGLE_DRIVE);
        textView3.setText(OPEN_GOOGLE_DRIVE_DESCRIPTION);

        activity = this;

        imageButtonTypeImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                /* Cria alert dialog perguntando se realmente deseja gerar os pings */
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(TITLE_PING);
                builder.setMessage(DIALOG_PING);

                builder.setPositiveButton(YES, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        /* Gera os Pings */
                        Toast.makeText(activity, TEST_PING, Toast.LENGTH_SHORT).show();

                        /* Inicia serviço de transferência com os dados da intent */
                        Intent serviceIntent = new Intent(activity, PingServerLocalAndExternal.class);

                        /* Seta a ação do serviço */
                        serviceIntent.setAction(ACTION_PING);

                        /* Seta o endereço */
                        serviceIntent.putExtra(ADDRESS, ADDRESS_GOOGLE_DRIVE);

                        /* Seta o tipo da requisição */
                        serviceIntent.putExtra(CONNECTION_TYPE, CONNECTION_TYPE_SERVER_EXTERNAL);

                        /* Inicia o serviço */
                        activity.startService(serviceIntent);
                    }
                });

                builder.setNegativeButton(NO, null);
                AlertDialog alerta = builder.create(); /* Cria o AlertDialog */
                alerta.show(); /* Exibe */
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Drive.API).addScope(Drive.SCOPE_FILE).
                    addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        }

        /* Tenta se conectar */
        Toast.makeText(this, TEST_PING, Toast.LENGTH_SHORT).show();
        WriteLOG.getInstanceGeneratorLOG().writeLogPing(ADDRESS_GOOGLE_DRIVE);
        WriteLOG.getInstanceGeneratorLOG().writeLogConnectDisconnect(ACTION_REQUEST_CONNECT, CONNECTION_TYPE_SERVER_EXTERNAL);
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) {
            /* Desconecte a conexão do cliente da API do Google */
            WriteLOG.getInstanceGeneratorLOG().writeLogConnectDisconnect(ACTION_DISCONNECT, CONNECTION_TYPE_SERVER_EXTERNAL);
            mGoogleApiClient.disconnect();
            connected = false;
        }
        super.onPause();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        /* Gera Log após conectar na API do Google */
        WriteLOG.getInstanceGeneratorLOG().writeLogConnectDisconnect(ACTION_CONNECT, CONNECTION_TYPE_SERVER_EXTERNAL);
        connected = true;
    }

    @Override
    public void onConnectionSuspended(int cause) {
        /* Conexão suspensa */
        Log.i(CATEGORY_LOG, "GoogleApiClient conexão suspensa!");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        /* Falha de conexão com a API */
        Log.i(CATEGORY_LOG, "GoogleApiClient connection failed: " + result.toString());

        if (!result.hasResolution()) {
            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(this, result.getErrorCode(), 0).show();
            return;
        }

        /**
         *  The failure has a resolution. Resolve it.
         *  Called typically when the app is not yet authorized, and an  authorization
         *  dialog is displayed to the user.
         */
        try {
            result.startResolutionForResult(this, 1);
        } catch (IntentSender.SendIntentException e) {
            Log.e(CATEGORY_LOG, "Exception while starting resolution activity", e);
        }
    }

    /* OnClick do Botão que Abre a Galeria -> Imagem */
    public void btn_send_image(View view) {
        if (connected) {
            /* Permitir que o usuário escolha uma imagem da Galeria ou de outras aplicações registradas */
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType(TYPE_FILE_IMAGE_FULL);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); /* Permite selecionar várias imagens */
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true); /* Permite selecionar apenas arquivos locais */
            startActivityForResult(intent, CHOOSE_FILE_RESULT_CODE_IMAGE);
        } else {
            Toast.makeText(activity, EXCEPTION_CONNECTED_DRIVE, Toast.LENGTH_SHORT).show();
        }
    }

    /* OnClick do Botão que Abre a Galeria -> Vídeo */
    public void btn_send_video(View view) {
        if (connected) {
            /* Permitir que o usuário escolha um vídeo da Galeria ou de outras aplicações registradas */
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType(TYPE_FILE_VIDEO_FULL);
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true); /* Permite selecionar apenas arquivos locais */
            startActivityForResult(intent, CHOOSE_FILE_RESULT_CODE_VIDEO);
        } else {
            Toast.makeText(activity, EXCEPTION_CONNECTED_DRIVE, Toast.LENGTH_SHORT).show();
        }
    }

    /* OnClick do Botão que Abre os Documentos -> PDF */
    public void btn_send_document(View view) {
        if (connected) {
            /* Permitir que o usuário escolha um documento PDF ou de outras aplicações registradas */
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType(TYPE_FILE_PDF_FULL);
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true); /* Permite selecionar apenas arquivos locais */
            startActivityForResult(intent, CHOOSE_FILE_RESULT_CODE_PDF);
        } else {
            Toast.makeText(activity, EXCEPTION_CONNECTED_DRIVE, Toast.LENGTH_SHORT).show();
        }
    }

    /* OnClick do Botão que Envia todos arquivos de uma pasta */
    public void btn_send_folder(View view) {
        if (connected) {
            /* Cria alert dialog perguntando se realmente deseja enviar os dados daquela pasta */
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(TITLE_SEND_FOLDER);
            builder.setMessage(DIALOG_SEND_FOLDER);

            builder.setPositiveButton(YES, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    /* Solicita o envio */
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
                            if (file.exists()) {
                                sendDataImageVideoDocument(file, i++, size);
                            }
                        }
                    } else {
                        Toast.makeText(activity, FOLDER_EMPTY, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            builder.setNegativeButton(NO, null);
            AlertDialog alerta = builder.create(); /* Cria o AlertDialog */
            alerta.show(); /* Exibe */
        }
    }

    /* OnClick do Botão voltar */
    public void btn_disconnectOrImport(View view) {
        if (connected) {
            /* Cria novo conteúdo */
            Drive.DriveApi.newDriveContents(mGoogleApiClient).setResultCallback(
                    new ResultCallback<DriveApi.DriveContentsResult>() {
                        @Override
                        public void onResult(DriveApi.DriveContentsResult result) {
                            if (result.getStatus().isSuccess()) {
                                /* Toast informando que está abrindo o drive */
                                Toast.makeText(activity, FOLDER_OPEN_GOOGLE_DRIVE, Toast.LENGTH_SHORT).show();

                                IntentSender intentSender = Drive.DriveApi
                                        .newOpenFileActivityBuilder()
//                                .setMimeType(new String[]{TYPE_FILE_IMAGE_FULL, TYPE_FILE_VIDEO_FULL, TYPE_FILE_PDF_FULL})
                                        .build(mGoogleApiClient);
                                try {
                                    startIntentSenderForResult(intentSender, CHOOSE_OPEN_GOOGLE_DRIVE, null, 0, 0, 0);
                                } catch (IntentSender.SendIntentException e) {
                                    Toast.makeText(activity, EXCEPTION_OPEN_DRIVE, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
        } else {
            Toast.makeText(activity, EXCEPTION_CONNECTED_DRIVE, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent intent) {
        if (requestCode == CHOOSE_FILE_RESULT_CODE_VIDEO || requestCode == CHOOSE_FILE_RESULT_CODE_PDF) {
            if (intent != null && intent.getData() != null) {
                sendDataImageVideoDocument(intent.getData());
            }
        } else if (requestCode == CHOOSE_FILE_RESULT_CODE_IMAGE) {
            if (intent != null) {
                if (intent.getData() != null) {
                    /* Apenas uma imagem foi selecionada */
                    sendDataImageVideoDocument(intent.getData());
                } else {
                    /* N imagens selecionadas */
                    ClipData clipData = intent.getClipData();
                    if (clipData != null) {
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            ClipData.Item item = clipData.getItemAt(i);
                            Uri uri = item.getUri();
                            sendDataImageVideoDocument(uri);
                        }
                    }
                }
            }

        } else if (requestCode == CHOOSE_OPEN_GOOGLE_DRIVE) {
            if (resultCode == RESULT_OK) {
                mFileId = (DriveId) intent.getParcelableExtra(OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);
                String url = "https://drive.google.com/open?id=" + mFileId.getResourceId();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        }
    }

    private void sendDataImageVideoDocument(final Uri uri) {
        if (uri != null) {
            try {
            /* Espera um segundo e tenta enviar o arquivo */
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Log.e(CATEGORY_LOG, EXCEPTION_THREAD);
            }

            /* Cria novo conteúdo */
            Drive.DriveApi.newDriveContents(mGoogleApiClient).setResultCallback(
                    new ResultCallback<DriveApi.DriveContentsResult>() {
                        @Override
                        public void onResult(DriveApi.DriveContentsResult result) {
                            if (result.getStatus().isSuccess()) {

                                /* Envia o arquivo */
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

                                final DriveContents driveContents = result.getDriveContents();

                                /* Pega o OutputStream do socket */
                                OutputStream outputStream = driveContents.getOutputStream();
                                DataOutputStream dos = new DataOutputStream(outputStream);

                                /* Pega a extensão do arquivo */
                                final String extension = Get.getExtension(uri, activity.getApplicationContext());

                                try {
                                    /* Copia o arquivo no DataOutputStream */
                                    FileCopy.copyFile(activity.getContentResolver(), uri.toString(), dos, extension, CONNECTION_TYPE_SERVER_EXTERNAL);

                                    dos.flush();
                                    dos.close();

                                } catch (IOException e) {

                                }

                                /* Cria um nome pro arquivo */
                                final String name = String.valueOf(System.currentTimeMillis());

                                /* Escreve arquivo */
                                MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                        .setTitle(name + "." + extension)
                                        .setMimeType(activity.getContentResolver().getType(uri))
                                        .setStarred(true).build();

                                /* Cria o arquivo no drive */
                                Drive.DriveApi.getRootFolder(mGoogleApiClient)
                                        .createFile(mGoogleApiClient, changeSet, driveContents)
                                        .setResultCallback(new ResultCallback<DriveFolder.DriveFileResult>() {
                                            @Override
                                            public void onResult(DriveFolder.DriveFileResult result) {
                                                if (result.getStatus().isSuccess()) {
                                                    Toast.makeText(activity.getApplicationContext(), FILE_SENDING + uri.toString(), Toast.LENGTH_SHORT).show();
                                                }
                                                return;
                                            }
                                        });
                            }
                        }
                    });
        }
    }

    private void sendDataImageVideoDocument(final File file, final int i, final int size) {
        if (file != null && file.exists()) {
            try {
            /* Espera um segundo e tenta enviar o arquivo */
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Log.e(CATEGORY_LOG, EXCEPTION_THREAD);
            }

            /* Cria novo conteúdo */
            Drive.DriveApi.newDriveContents(mGoogleApiClient).setResultCallback(
                    new ResultCallback<DriveApi.DriveContentsResult>() {
                        @Override
                        public void onResult(DriveApi.DriveContentsResult result) {
                            if (result.getStatus().isSuccess()) {

                                /* Envia o arquivo */
                                /* Exibe toast */
                                Toast.makeText(activity, WAIT, Toast.LENGTH_SHORT).show();

                                final DriveContents driveContents = result.getDriveContents();

                                /* Pega o OutputStream do socket */
                                OutputStream outputStream = driveContents.getOutputStream();
                                DataOutputStream dos = new DataOutputStream(outputStream);

                                try {
                                    /* Copia o arquivo no DataOutputStream */
                                    FileCopy.copyFile(file, dos, CONNECTION_TYPE_SERVER_EXTERNAL);

                                    dos.flush();
                                    dos.close();

                                } catch (IOException e) {

                                }

                                /* Cria um nome pro arquivo */
                                final String name = String.valueOf(System.currentTimeMillis());

                                /* Pega a extensão do arquivo */
                                final String extension = "." + FilenameUtils.getExtension(file.getAbsolutePath());

                                /* Escreve arquivo */
                                MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                        .setTitle(name + extension)
                                        .setMimeType(Get.getMimeType(extension))
                                        .setStarred(true).build();

                                /* Cria o arquivo no drive */
                                Drive.DriveApi.getRootFolder(mGoogleApiClient)
                                        .createFile(mGoogleApiClient, changeSet, driveContents)
                                        .setResultCallback(new ResultCallback<DriveFolder.DriveFileResult>() {
                                            @Override
                                            public void onResult(DriveFolder.DriveFileResult result) {
                                                if (result.getStatus().isSuccess()) {
                                                    Toast.makeText(activity.getApplicationContext(), FILE_SENDING + file.getName()
                                                            + " (" + i + "/" + size + ")", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(activity.getApplicationContext(), EXCEPTION_SEND_CLIENT_SOCKET + file.getName()
                                                            + " (" + i + "/" + size + ")", Toast.LENGTH_SHORT).show();
                                                }
                                                return;
                                            }
                                        });
                            }
                        }
                    });
        }
    }
}

