package br.com.ufop.daniel.d2dwifidirect.dao;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import br.com.ufop.daniel.d2dwifidirect.activity.MenuActivity;
import br.com.ufop.daniel.d2dwifidirect.activity.SendFileD2DAndServerLocalActivity;
import br.com.ufop.daniel.d2dwifidirect.entities.DataConnectDisconnect;
import br.com.ufop.daniel.d2dwifidirect.entities.DataSearch;
import br.com.ufop.daniel.d2dwifidirect.entities.DataSendReceived;
import br.com.ufop.daniel.d2dwifidirect.fragment.DeviceFragment;
import br.com.ufop.daniel.d2dwifidirect.fragment.DeviceFragmentList;
import br.com.ufop.daniel.d2dwifidirect.util.Get;

import static br.com.ufop.daniel.d2dwifidirect.util.Constants.CATEGORY_LOG;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.CONNECTION_TYPE_D2D;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.DEVICE_TYPE_CLIENT;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.DEVICE_TYPE_GO;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.DIRECTORY_SAVE_FILE;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.DIRECTORY_SAVE_FILE_LOG;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.DIRECTORY_SEND_FILE_AUTOMATIC;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.DISABLE_WRITE_LOG;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.ENABLE_WRITE_LOG;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.EXTENSION_FILE_LOG;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.NAME_FILE_LOG;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.CLOSE_BUFFER_FILE;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.EXCEPTION_CLOSE_FILE;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.EXCEPTION_HANDLE_FILE;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.EXCEPTION_WRITE_FILE;

/**
 * Created by daniel on 20/09/17.
 */

public class WriteLOG {

    private Writer writer;
    private static WriteLOG writeLOG;

    public static WriteLOG getInstanceGeneratorLOG() {
        if (writeLOG == null) {
            writeLOG = new WriteLOG();
        }
        return writeLOG;
    }

    private WriteLOG() {
        initWriteFile();
    }

    private String getDeviceType(String connection_type) {
        try {
            if (connection_type.equals(CONNECTION_TYPE_D2D) && DeviceFragment.getInfo() != null) {
                return (DeviceFragment.getInfo().isGroupOwner == true ? DEVICE_TYPE_GO : DEVICE_TYPE_CLIENT);
            } else if (!connection_type.equals(CONNECTION_TYPE_D2D)) {
                return DEVICE_TYPE_CLIENT;
            } else {
                return "-";
            }
        } catch (Exception e) {
            return "-";
        }
    }

    private String getDeviceName(String connection_type) {
        try {
            if (connection_type.equals(CONNECTION_TYPE_D2D) && DeviceFragmentList.getDevice() != null) {
                return DeviceFragmentList.getDevice().deviceName;
            } else if (!connection_type.equals(CONNECTION_TYPE_D2D) && android.os.Build.MODEL != null) {
                return android.os.Build.MODEL;
            } else {
                return "-";
            }
        } catch (Exception e) {
            return "-";
        }
    }

    private String getDeviceAddress(String connection_type) {
        try {
            if (connection_type.equals(CONNECTION_TYPE_D2D) && DeviceFragmentList.getDevice() != null) {
                return DeviceFragmentList.getDevice().deviceAddress;
            } else if (!connection_type.equals(CONNECTION_TYPE_D2D) && SendFileD2DAndServerLocalActivity.getMyAddress() != null) {
                return SendFileD2DAndServerLocalActivity.getMyAddress();
            } else {
                return "-";
            }
        } catch (Exception e) {
            return "-";
        }
    }

    private String getDeviceStatus(String connection_type) {
        try {
            if (connection_type.equals(CONNECTION_TYPE_D2D) && DeviceFragmentList.getDevice() != null) {
                return Get.getDeviceStatus(DeviceFragmentList.getDevice().status);
            } else {
                return "-";
            }
        } catch (Exception e) {
            return "-";
        }
    }

    private String getDeviceAddressOwnerGroup(String connection_type) {
        try {
            if (connection_type.equals(CONNECTION_TYPE_D2D) && DeviceFragment.getOwnerGroup() != null) {
                return DeviceFragment.getOwnerGroup().getHostAddress();
            } else {
                return "-";
            }
        } catch (Exception e) {
            return "-";
        }
    }

    private String getDeviceNameOwnerGroup(String connection_type) {
        try {
            if (connection_type.equals(CONNECTION_TYPE_D2D) && DeviceFragment.getOwnerGroup() != null) {
                return DeviceFragment.getOwnerGroup().getHostName();
            } else {
                return "-";
            }
        } catch (Exception e) {
            return "-";
        }
    }

    /* Inicia a gravação no arquivo */
    private void initWriteFile() {
        if (writer == null) { /* Se ainda não tiver iniciado */
            if (MenuActivity.permission) { /* Verifica se todas permissoes estão liberadas */
                if (MenuActivity.write_log.equals(ENABLE_WRITE_LOG)) {
                    File dir = new File(DIRECTORY_SAVE_FILE_LOG);
                    dir.mkdirs();
                    dir = new File(DIRECTORY_SAVE_FILE);
                    dir.mkdirs();
                    dir = new File(DIRECTORY_SEND_FILE_AUTOMATIC);
                    dir.mkdirs();

                    File file = new File(DIRECTORY_SAVE_FILE_LOG + NAME_FILE_LOG + System.currentTimeMillis() + EXTENSION_FILE_LOG);

                    try {
                        writer = new BufferedWriter(new FileWriter(file, true));
                    } catch (Exception e) {
                        Log.e(CATEGORY_LOG, EXCEPTION_HANDLE_FILE);
                    }
                }
            }
        }
    }

    public void exit() {
        if (writer != null) {
            try {
                writer.close();
                writer = null;
                Log.d(CATEGORY_LOG, CLOSE_BUFFER_FILE);
            } catch (IOException e) {
                Log.e(CATEGORY_LOG, EXCEPTION_CLOSE_FILE);
            }
        }
    }

    public void writeLogSendReceived(String action, String device_type, long size, long start_time, long end_time, String file_extension, String connection_type) {
        if (MenuActivity.write_log.equals(DISABLE_WRITE_LOG)) {
            exit(); /* Para de gravar */
        } else {
            try {
                initWriteFile(); /* Se não tiver inicializado, tenta inicializar a escrita no arquivo */

                if (writer != null) {
                    DataSendReceived data = new DataSendReceived();
                    data.setAction(action);
                    data.setDevice_type(device_type);
                    data.setDevice_name(getDeviceName(connection_type));
                    data.setDevice_address(getDeviceAddress(connection_type));
                    data.setSize_file(String.valueOf(size));
                    data.setTimestamp_init(String.valueOf(start_time));
                    data.setTimestamp_end(String.valueOf(end_time));
                    data.setType_file(file_extension);
                    data.setConnection_type(connection_type);

//                    Log.d(CATEGORY_LOG, data.toString());
                    writer.append(data + "\n");
                    writer.flush();
                }
            } catch (IOException e) {
                Log.e(CATEGORY_LOG, EXCEPTION_WRITE_FILE);
            }
        }
    }

    public void writeLogSearch(String ACTION, int peers, String connection_type) {
        if (MenuActivity.write_log.equals(DISABLE_WRITE_LOG)) {
            exit(); /* Para de gravar */
        } else {
            try {
                initWriteFile(); /* Se não tiver inicializado, tenta inicializar a escrita no arquivo */

                if (writer != null) {
                    DataSearch data = new DataSearch();
                    data.setDevice_name(getDeviceName(connection_type));
                    data.setDevice_address(getDeviceAddress(connection_type));
                    data.setDevice_status(getDeviceStatus(connection_type));
                    data.setAction(ACTION);
                    data.setPeers(peers);
                    data.setTimestamp(String.valueOf(System.currentTimeMillis()));
                    data.setConnection_type(connection_type);

//                    Log.d(CATEGORY_LOG, data.toString());
                    writer.append(data + "\n");
                    writer.flush();
                }
            } catch (IOException e) {
                Log.e(CATEGORY_LOG, EXCEPTION_WRITE_FILE);
            }
        }
    }

    public void writeLogConnectDisconnect(String ACTION, String connection_type) {
        if (MenuActivity.write_log.equals(DISABLE_WRITE_LOG)) {
            exit(); /* Para de gravar */
        } else {
            try {
                initWriteFile(); /* Se não tiver inicializado, tenta inicializar a escrita no arquivo */

                if (writer != null) {
                    DataConnectDisconnect data = new DataConnectDisconnect();
                    data.setDevice_name(getDeviceName(connection_type));
                    data.setDevice_address(getDeviceAddress(connection_type));
                    data.setDevice_status(getDeviceStatus(connection_type));
                    data.setAction(ACTION);
                    data.setTimestamp(String.valueOf(System.currentTimeMillis()));
                    data.setDevice_destiny_name(getDeviceNameOwnerGroup(connection_type));
                    data.setDevice_destiny_address(getDeviceAddressOwnerGroup(connection_type));
                    data.setDevice_type(getDeviceType(connection_type));
                    data.setConnection_type(connection_type);

//                    Log.d(CATEGORY_LOG, data.toString());
                    writer.append(data + "\n");
                    writer.flush();
                }
            } catch (IOException e) {
                Log.e(CATEGORY_LOG, EXCEPTION_WRITE_FILE);
            }
        }
    }

    public void writeLogPing(String ip) {
        if (MenuActivity.write_log.equals(DISABLE_WRITE_LOG)) {
            exit(); /* Para de gravar */
        } else {
            try {
                initWriteFile(); /* Se não tiver inicializado, tenta inicializar a escrita no arquivo */

                if (writer != null) {
                    String data = Get.ping(ip);

//                    Log.d(CATEGORY_LOG, data.toString());
                    writer.append(data + "\n");
                    writer.flush();
                }
            } catch (IOException e) {
                Log.e(CATEGORY_LOG, EXCEPTION_WRITE_FILE);
            }
        }
    }

}
