package br.com.ufop.daniel.d2dwifidirect.util;

import android.os.Environment;

/**
 * Created by daniel on 26/07/17.
 */

public final class Constants {

    public static final String NAME_FILE_LOG = "log";
    public static final String NAME_FILE_PING = "ping";

    public static final String ADDRESS_GOOGLE_DRIVE = "docs.google.com";

    private static final String DIRECTORY_EXTERNAL_STORAGE = Environment.getExternalStorageDirectory() + "/";
    public static final String DIRECTORY_SEND_FILE_AUTOMATIC = DIRECTORY_EXTERNAL_STORAGE + "ShareFiles/sendAutomatic/";
    public static final String DIRECTORY_SAVE_FILE = DIRECTORY_EXTERNAL_STORAGE + "ShareFiles/media/";
    public static final String DIRECTORY_SAVE_FILE_LOG = DIRECTORY_EXTERNAL_STORAGE + "ShareFiles/data/";

    public static final String CATEGORY_LOG = "ShareFiles";

    public static final String EXTENSION_FILE_LOG = ".csv";
    public static final String TYPE_FILE_IMAGE_FULL = "image/jpeg";
    public static final String EXTENSION_FILE_IMAGE = ".jpg";
    public static final String TYPE_FILE_VIDEO_FULL = "video/mp4";
    public static final String EXTENSION_FILE_VIDEO = ".mp4";
    public static final String TYPE_FILE_PDF_FULL = "application/pdf";
    public static final String EXTENSION_FILE_PDF = ".pdf";

    public static final int SOCKET_TIMEOUT = 5000;
    public static final int PORT_SOCKET = 8888;
    public static final int LOCAL_SERVER_PORT_SOCKET = 8080;

    public static final int LOCAL_SERVER_PORT_SOCKET_UDP = 8989;
    public static final String MESSAGE_DEFAULT_SEARCH_SERVER_UDP_SEND = "89s1YY34dr2ERxxGcVG2H34fOrShDb35s4D6d57exREegsG";
    public static final String MESSAGE_DEFAULT_SEARCH_SERVER_UDP_RECEIVED = "cVG2349s1YdrhDb2fY3d57HexREegs4OrS358GERxxGs4D6";

    public static final String FILE_PATH = "PATH_FILE";
    public static final String EXTENSION_FILE = "EXTENSION_FILE";
    public static final String ADDRESS = "ADDRESS";
    public static final String PORT = "PORT";

    public static final int CHOOSE_FILE_RESULT_CODE_IMAGE = 1;
    public static final int CHOOSE_FILE_RESULT_CODE_VIDEO = 2;
    public static final int CHOOSE_FILE_RESULT_CODE_PDF = 3;
    public static final int CHOOSE_OPEN_GOOGLE_DRIVE = 4;

    public static final String ACTION_PING = "PING";
    public static final String ACTION_SEND = "SEND";
    public static final String ACTION_RECEIVED = "RECEIVED";
    public static final String ACTION_REQUEST_SEARCH = "REQUEST_SEARCH";
    public static final String ACTION_END_SEARCH = "END_SEARCH";
    public static final String ACTION_CONNECT = "CONNECT";
    public static final String ACTION_DISCONNECT = "DISCONNECT";
    public static final String ACTION_REQUEST_CONNECT = "REQUEST_CONNECT";
    public static final String ACTION_REQUEST_DISCONNECT = "REQUEST_DISCONNECT";

    public static final String DEVICE_TYPE_CLIENT = "CLIENT";
    public static final String DEVICE_TYPE_GO = "GO";

    public static final String WRITE_LOG = "WRITE_LOG";
    public static final String ENABLE_WRITE_LOG = "ENABLE_WRITE_LOG";
    public static final String DISABLE_WRITE_LOG = "DISABLE_WRITE_LOG";

    public static final String APP_NAME = "D2DWiFiDirect";

    public static final String SHOW_FILE_RECEIVED = "SHOW_FILE_RECEIVED";
    public static final String ENABLE_SHOW_FILE_RECEIVED = "ENABLE_SHOW_FILE_RECEIVED";
    public static final String DISABLE_SHOW_FILE_RECEIVED = "DISABLE_SHOW_FILE_RECEIVED";

    public static final String REQUEST_TYPE = "REQUEST_TYPE";
    public static final String REQUEST_TYPE_EXPORT_TO_SERVER = "EXPORT_TO_SERVER";
    public static final String REQUEST_TYPE_EXPORT_TO_SERVER_ALL = "EXPORT_TO_SERVER_ALL";
    public static final String REQUEST_TYPE_IMPORT_FROM_SERVER = "IMPORT_FROM_SERVER";
    public static final String REQUEST_TYPE_IMPORT_LIST_FILES_FROM_SERVER = "IMPORT_FROM_LIST_SERVER";

    public static final String CONNECTION_TYPE = "CONNECTION_TYPE";
    public static final String CONNECTION_TYPE_D2D = "D2D";
    public static final String CONNECTION_TYPE_SERVER_LOCAL = "SERVER_LOCAL";
    public static final String CONNECTION_TYPE_SERVER_EXTERNAL = "SERVER_EXTERNAL";

    public static final String TYPE = "TYPE";
    public static final String TYPE_D2D = "D2D";
    public static final String TYPE_SERVER_LOCAL = "Computador";
    public static final String TYPE_SERVER_EXTERNAL = "Google Drive";
    public static final String TYPE_FOLDER = "Pasta";

    public static final String TITLE_NOTIFICATION_SEND = "Enviados";
    public static final String TITLE_NOTIFICATION_RECEIVED = "Recebidos";
    public static final String TITLE_NOTIFICATION_SEND_ERROR = "Erro ao Enviar";
    public static final String TITLE_NOTIFICATION_RECEIVED_ERROR = "Erro ao Receber";

}
