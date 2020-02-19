/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connect;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author daniel
 */
public class Constants {

    private static String getPath() {
        String path = "";
        File file = new File(".");
        try {
            path = file.getCanonicalPath();
        } catch (IOException e) {

        }
        return path;
    }

    private static final String DIRECTORY_EXTERNAL_STORAGE = getPath() + "/";
    public static final String DIRECTORY_SEND_FILE_AUTOMATIC = DIRECTORY_EXTERNAL_STORAGE + "ShareFiles/sendAutomatic/";
    public static final String DIRECTORY_SAVE_FILE = DIRECTORY_EXTERNAL_STORAGE + "ShareFiles/media/";
    public static final String DIRECTORY_SAVE_FILE_LOG = DIRECTORY_EXTERNAL_STORAGE + "ShareFiles/data/";

    public static final String REQUEST_TYPE_IMPORT_LIST_FILES_FROM_SERVER = "IMPORT_FROM_LIST_SERVER";
    public static final String REQUEST_TYPE_IMPORT_FROM_SERVER = "IMPORT_FROM_SERVER";
    public static final String REQUEST_TYPE_EXPORT_TO_SERVER = "EXPORT_TO_SERVER";
    public static final String REQUEST_TYPE_EXPORT_TO_SERVER_ALL = "EXPORT_TO_SERVER_ALL";

    public static final int LOCAL_SERVER_PORT_SOCKET = 8080;

    public static final String ADDRESS_FILES = "";

    public static final String EXTENSION_FILE_LOG = ".csv";
    public static final String EXTENSION_FILE_IMAGE = ".jpg";
    public static final String EXTENSION_FILE_VIDEO = ".mp4";
    public static final String EXTENSION_FILE_PDF = ".pdf";

    public static final String ACTION_SEND = "SEND";
    public static final String ACTION_RECEIVED = "RECEIVED";
    public static final String ACTION_REQUEST_SEARCH = "REQUEST_SEARCH";
    public static final String ACTION_END_SEARCH = "END_SEARCH";
    public static final String ACTION_CONNECT = "CONNECT";
    public static final String ACTION_DISCONNECT = "DISCONNECT";
    public static final String ACTION_REQUEST_CONNECT = "REQUEST_CONNECT";
    public static final String ACTION_REQUEST_DISCONNECT = "REQUEST_DISCONNECT";

    public static final String DEVICE_TYPE_CLIENT = "CLIENT";
    public static final String DEVICE_TYPE_GO = "SERVER_LOCAL";

    public static final int LOCAL_SERVER_PORT_SOCKET_UDP = 8989;
    public static final String MESSAGE_DEFAULT_SEARCH_SERVER_UDP_SEND = "89s1YY34dr2ERxxGcVG2H34fOrShDb35s4D6d57exREegsG";
    public static final String MESSAGE_DEFAULT_SEARCH_SERVER_UDP_RECEIVED = "cVG2349s1YdrhDb2fY3d57HexREegs4OrS358GERxxGs4D6";

    public static final String CONNECTION_TYPE_SERVER_LOCAL = "SERVER_LOCAL";

}
