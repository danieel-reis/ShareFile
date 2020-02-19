package br.com.ufop.daniel.d2dwifidirect.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.text.format.Formatter;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static android.content.Context.WIFI_SERVICE;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.DEVICE_STATUS_AVAILABLE;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.DEVICE_STATUS_CONNECTED;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.DEVICE_STATUS_FAILED;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.DEVICE_STATUS_INVITED;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.DEVICE_STATUS_UNAVAILABLE;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.DEVICE_STATUS_UNKNOWN;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.EXCEPTION_SERVER_LOCAL_NOT_FOUND;

/**
 * Created by daniel on 19/11/17.
 */

public class Get {

    /* Realiza um ping e retorna: rtt MIN, AVG, MAX, MDEV -> mínimo (ms), média (ms), máximo (ms), desvio padrão (ms) */
    public static String ping(String ip) {
        String reply = "";
        String command = "ping -c 5 " + ip;
        try {
            Scanner S = new Scanner(Runtime.getRuntime().exec(command).getInputStream());
            while (S.hasNextLine()) {
                reply = S.nextLine();
            }

            String r[] = reply.split(" ");
            String t[] = r[3].split("/");

            String min = t[0];
            String avg = t[1];
            String max = t[2];
            String mdev = t[3];

            return "RTT" + "," + min + "," + avg + "," + max + "," + mdev;

        } catch (Exception ex) {
            return "";
        }
    }

    public static boolean getNetworkAvailableWiFi(Activity activity) {
        ConnectivityManager conmag = (ConnectivityManager) activity.getApplicationContext().
                getSystemService(Context.CONNECTIVITY_SERVICE);
        conmag.getActiveNetworkInfo();

        /* Verifica o WIFI está disponível */
        if (conmag.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()) {
            return true;
        }
        return false;
    }

    public static boolean getNetworkAvailable(Activity activity) {
        ConnectivityManager conmag = (ConnectivityManager) activity.getApplicationContext().
                getSystemService(Context.CONNECTIVITY_SERVICE);
        conmag.getActiveNetworkInfo();

        /* Verifica o WIFI */
        if (conmag.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()) {
            return true;
        }
        /* Verifica o 3G */
        else if (conmag.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public static String getMyAddress(Activity activity) {
        /* Pega o meu IP na Rede */
        WifiManager wm = (WifiManager) activity.getApplicationContext().getSystemService(WIFI_SERVICE);
        return Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    }

    public static boolean verifyAddressIP(Activity activity, String serverIP) {
        /* Verifica o endereço */
        try {
            int cont = 0;
            int p1 = -1;
            int p2 = -1;
            int p3 = -1;
            for (int i = 0; i < serverIP.length(); i++) {
                if (serverIP.charAt(i) == '.') {
                    cont++;

                    if (p1 < 0) {
                        p1 = i;
                    } else if (p2 < 0) {
                        p2 = i;
                    } else if (p3 < 0) {
                        p3 = i;
                    }
                }
            }

            if (cont == 3) {
                /* Tenta converter cada valor */
                Integer.parseInt(serverIP.substring(0, p1));
                Integer.parseInt(serverIP.substring(p1 + 1, p2));
                Integer.parseInt(serverIP.substring(p2 + 1, p3));
                Integer.parseInt(serverIP.substring(p3 + 1, serverIP.length()));
                return true;

            } else {
                Toast.makeText(activity, EXCEPTION_SERVER_LOCAL_NOT_FOUND, Toast.LENGTH_SHORT).show();
                return false;
            }

        } catch (Exception ex) {
            Toast.makeText(activity, EXCEPTION_SERVER_LOCAL_NOT_FOUND, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /* Retorna o status do dispositivo */
    public static String getDeviceStatus(int deviceStatus) {
        String status;
        switch (deviceStatus) {
            case WifiP2pDevice.AVAILABLE:
                status = DEVICE_STATUS_AVAILABLE;
                break;
            case WifiP2pDevice.INVITED:
                status = DEVICE_STATUS_INVITED;
                break;
            case WifiP2pDevice.CONNECTED:
                status = DEVICE_STATUS_CONNECTED;
                break;
            case WifiP2pDevice.FAILED:
                status = DEVICE_STATUS_FAILED;
                break;
            case WifiP2pDevice.UNAVAILABLE:
                status = DEVICE_STATUS_UNAVAILABLE;
                break;
            default:
                status = DEVICE_STATUS_UNKNOWN;
                break;
        }
//        String message = "            Status do dispositivo: " + deviceStatus + " (" + status + ")";
//        Log.d(CATEGORY_LOG, message);

        return status;
    }

    /* Retorna a extensão de um arquivo dado a sua URI */
    public static String getExtension(Uri uri, Context context) {
        ContentResolver cR = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String type = mime.getExtensionFromMimeType(cR.getType(uri));
        return type;
    }

    /* Retorna o mimeType de um arquivo dado a sua URL */
    public static String getMimeType(String extension) {
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        if (mimeType == null || mimeType.equals("") || mimeType.equals("null")) {
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.substring(1, extension.length()));
        }
        return mimeType;
    }

    /* Retorna o tipo do arquivo de acordo com o mimeType */
    public static String getTypeFile(String extension) {
        String mimeType = getMimeType(extension);
        String m[] = mimeType.split("/");
        return m[0].toUpperCase();
    }

    /* Retorna o tamanho do arquivo */
    public static long getSizeFile(String fileUri, ContentResolver contentResolver) {
        long size = 0;
        try {
            InputStream is = contentResolver.openInputStream(Uri.parse(fileUri));
            BufferedInputStream bis = new BufferedInputStream(is);

            byte buf[] = new byte[10000]; /* Cria o buffer */
            int len; /* Tamanho da leitura */
            int offset = 0; /* Começa a escrever do início do arquivo */
            while ((len = bis.read(buf, offset, 10000)) != -1) { /* Read retorna o tamanho da leitura ou -1 ao finalizar */
                size += len;
            }
            bis.close();
        } catch (Exception e) {
            return 0;
        }
        return size;
    }
}
