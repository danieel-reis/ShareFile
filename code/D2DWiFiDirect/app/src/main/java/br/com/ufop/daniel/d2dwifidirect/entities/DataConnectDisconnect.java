package br.com.ufop.daniel.d2dwifidirect.entities;

/**
 * Created by daniel on 04/11/17.
 */

public class DataConnectDisconnect {

    /*
    FORMAT: ACTION (REQUEST_CONNECT, CONNECT, DISCONNECT), DEVICE_TYPE (GO, CLIENT), TIMESTAMP,
            DEVICE_ADDRESS, DEVICE_NAME, DEVICE_STATUS (AVAILABLE, INVITED, CONNECTED, FAILED, UNAVAILABLE, UNKNOWN),
            CONNECTION_TYPE (D2D, SERVER_LOCAL, SERVER_EXTERNAL)
    */
    private String action;
    private String device_type;
    private String timestamp;
    private String device_address;
    private String device_name;
    private String device_status;
    private String device_destiny_address;
    private String device_destiny_name;
    private String connection_type;

    public void setAction(String action) {
        this.action = action;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setDevice_address(String device_address) {
        this.device_address = device_address;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public void setDevice_status(String device_status) {
        this.device_status = device_status;
    }

    public void setDevice_destiny_address(String device_destiny_address) {
        this.device_destiny_address = device_destiny_address;
    }

    public void setDevice_destiny_name(String device_destiny_name) {
        this.device_destiny_name = device_destiny_name;
    }

    public void setConnection_type(String connection_type) {
        this.connection_type = connection_type;
    }

    @Override
    public String toString() {
        return this.action + "," + this.device_type + "," + this.timestamp + "," +
                this.device_address + "," + this.device_name + "," + this.device_status + "," +
                this.device_destiny_address + "," + this.device_destiny_name + "," + this.connection_type;
    }
}
