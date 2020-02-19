package br.com.ufop.daniel.d2dwifidirect.entities;

/**
 * Created by daniel on 04/11/17.
 */

public class DataSendReceived {

    /*
    FORMAT: ACTION (SEND, RECEIVED), DEVICE_TYPE (GO, CLIENT), DEVICE_ADDRESS, DEVICE_NAME,
            TIMESTAMP_INIT, TIMESTAMP_END, TYPE_FILE (pdf, jpg, mp4...), SIZE_FILE (BYTES),
            CONNECTION_TYPE (D2D, SERVER_LOCAL, SERVER_EXTERNAL)
    */
    private String action;
    private String device_type;
    private String device_name;
    private String device_address;
    private String timestamp_init;
    private String timestamp_end;
    private String type_file;
    private String size_file;
    private String connection_type;

    public DataSendReceived() {
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public void setDevice_address(String device_address) {
        this.device_address = device_address;
    }

    public void setTimestamp_init(String timestamp_init) {
        this.timestamp_init = timestamp_init;
    }

    public void setTimestamp_end(String timestamp_end) {
        this.timestamp_end = timestamp_end;
    }

    public void setType_file(String type_file) {
        this.type_file = type_file;
    }

    public void setSize_file(String size_file) {
        this.size_file = size_file;
    }

    public void setConnection_type(String connection_type) {
        this.connection_type = connection_type;
    }

    @Override
    public String toString() {
        return this.action + "," + this.device_type + "," +
                this.device_address + "," + this.device_name + "," +
                this.timestamp_init + "," + this.timestamp_end + "," +
                this.type_file + "," + this.size_file + "," + this.connection_type;
    }
}
