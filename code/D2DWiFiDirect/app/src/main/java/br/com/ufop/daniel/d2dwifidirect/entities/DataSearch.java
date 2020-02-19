package br.com.ufop.daniel.d2dwifidirect.entities;

/**
 * Created by daniel on 04/11/17.
 */

public class DataSearch {

    /*
    FORMAT: ACTION (SEARCH), TIMESTAMP, DEVICE_ADDRESS, DEVICE_NAME,
            DEVICE_STATUS (AVAILABLE, INVITED, CONNECTED, FAILED, UNAVAILABLE, UNKNOWN),
            PEERS, CONNECTION_TYPE (D2D, SERVER_LOCAL, SERVER_EXTERNAL)
    */

    private String action;
    private String timestamp;
    private String device_address;
    private String device_name;
    private String device_status;
    private int peers;
    private String connection_type;

    public void setAction(String action) {
        this.action = action;
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

    public void setPeers(int peers) {
        this.peers = peers;
    }

    public void setConnection_type(String connection_type) {
        this.connection_type = connection_type;
    }

    @Override
    public String toString() {
        return this.action + "," + this.peers + "," + this.timestamp + "," + this.device_address + "," +
                this.device_name + "," + this.device_status + "," + this.connection_type;
    }
}
