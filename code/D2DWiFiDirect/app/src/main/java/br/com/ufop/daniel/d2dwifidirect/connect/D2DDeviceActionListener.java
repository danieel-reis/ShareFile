package br.com.ufop.daniel.d2dwifidirect.connect;

import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;

/**
 * Created by daniel on 26/07/17.
 * Lista de ações do dispositivo
 */

public interface D2DDeviceActionListener {

    void showDetails(WifiP2pDevice device);

    void createGroup();

    void discoverPeers();

    void connectGroup(WifiP2pConfig config);

    void disconnectGroup();
}
