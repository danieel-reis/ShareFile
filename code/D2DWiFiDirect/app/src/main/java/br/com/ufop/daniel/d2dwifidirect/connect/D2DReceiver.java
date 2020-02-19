package br.com.ufop.daniel.d2dwifidirect.connect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;

import br.com.ufop.daniel.d2dwifidirect.fragment.DeviceFragment;
import br.com.ufop.daniel.d2dwifidirect.fragment.DeviceFragmentList;
import br.com.ufop.daniel.d2dwifidirect.R;
import br.com.ufop.daniel.d2dwifidirect.dao.WriteLOG;
import br.com.ufop.daniel.d2dwifidirect.activity.SearchConnectDisconnectD2DActivity;

import static br.com.ufop.daniel.d2dwifidirect.util.Constants.ACTION_DISCONNECT;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.CONNECTION_TYPE_D2D;

/**
 * Created by daniel on 26/07/17.
 */

public class D2DReceiver extends BroadcastReceiver {

    private SearchConnectDisconnectD2DActivity activity; /* Activity */
    private WifiP2pManager manager; /* Gerenciador do p2p */
    private WifiP2pManager.Channel channel; /* Canal do p2p */

    /* Construtor */
    public D2DReceiver(SearchConnectDisconnectD2DActivity activity, WifiP2pManager manager, WifiP2pManager.Channel channel) {
        super();
        this.activity = activity;
        this.manager = manager;
        this.channel = channel;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) { /* Pega a ação da intent */

            case WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION:
                /* Verifique se o Wi-Fi está ativado e notifica a atividade apropriada */
                int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1); /* Pega o status (Estado do WiFi) */

                if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                    activity.isWifiEnabled = true; /* Modo WiFi Direct habilitado */

                } else {
                    activity.isWifiEnabled = false; /* Modo WiFi Direct desabilitado */
                    activity.resetData(); /* Desconectado - Limpar dados da tela */
                }
                break;

            case WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION:
                /* Solicita pontos disponíveis do gerenciador wifi p2p -> chamada assíncrona
                 * Chama o WifiP2pManager.requestPeers () para obter uma lista de pares atuais */
                if (manager != null) { /* Se o gerenciador do p2p existir */
                    WifiP2pManager.PeerListListener list = (WifiP2pManager.PeerListListener) activity.getFragmentManager().findFragmentById(R.id.list);
                    manager.requestPeers(channel, list);
                }
                break;

            case WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION:
                /* Responde a novas conexões ou desconexões */
                if (manager != null) { /* Se o gerenciador do p2p existir */
                    NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO); /* Pega informações da rede */

                    if (networkInfo.isConnected()) { /* Conectado */
                        /* Solicita informações de conexão p/ encontrar o IP do proprietário do grupo e conecta com o outro dispositivo */
                        DeviceFragment fragment = (DeviceFragment) activity.getFragmentManager().findFragmentById(R.id.element);
                        manager.requestConnectionInfo(channel, fragment);

                    } else { /* Desconectado */
                        activity.resetData();

                        WriteLOG.getInstanceGeneratorLOG().writeLogConnectDisconnect(ACTION_DISCONNECT, CONNECTION_TYPE_D2D);
                    }
                }
                break;

            case WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION:
                /* Responde a mudança do estado wifi deste dispositivo */
                DeviceFragmentList fragment = (DeviceFragmentList) activity.getFragmentManager().findFragmentById(R.id.list); /* Pega o fragmento */
                fragment.updateThisDevice((WifiP2pDevice) intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE)); /* Passa os dados do dispositivo para o fragmento */
                break;

        }
    }
}
