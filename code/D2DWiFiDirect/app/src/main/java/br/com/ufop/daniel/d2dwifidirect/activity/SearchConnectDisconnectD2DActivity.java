package br.com.ufop.daniel.d2dwifidirect.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import br.com.ufop.daniel.d2dwifidirect.connect.D2DDeviceActionListener;
import br.com.ufop.daniel.d2dwifidirect.connect.D2DReceiver;
import br.com.ufop.daniel.d2dwifidirect.fragment.DeviceFragment;
import br.com.ufop.daniel.d2dwifidirect.fragment.DeviceFragmentList;
import br.com.ufop.daniel.d2dwifidirect.R;
import br.com.ufop.daniel.d2dwifidirect.dao.WriteLOG;

import static br.com.ufop.daniel.d2dwifidirect.util.Constants.ACTION_CONNECT;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.CATEGORY_LOG;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.CONNECTION_TYPE_D2D;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.BTN_NEGATIVE_SEARCH_D2D;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.BTN_POSITIVE_SEARCH_D2D;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.DIALOG_SEARCH_D2D;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.ON_CHANNEL_DISCONNECTED;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.ON_CHANNEL_DISCONNECTED_NO_MANAGER;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.ON_FAILURE_CONNECT_GROUP;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.ON_FAILURE_CREATE_GROUP;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.ON_FAILURE_DISCONNECT_GROUP;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.ON_FAILURE_DISCOVER_PEERS;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.ON_SUCCESS_DISCOVER_PEERS;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.PROGRESS_DIALOG_CONNECT_GROUP_MESSAGE;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.PROGRESS_DIALOG_CONNECT_GROUP_TITLE;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.TITLE_SEARCH_D2D;

public class SearchConnectDisconnectD2DActivity extends Activity implements WifiP2pManager.ChannelListener, D2DDeviceActionListener {

    public boolean isWifiEnabled = false; /* Define se o WiFi está habilitado ou não */
    public boolean retryChannel = false;

    private final IntentFilter intentFilter = new IntentFilter();

    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private BroadcastReceiver receiver = null;

    private static ProgressDialog pd = null;

    public static void destroeDialog() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss(); /* Destroe */
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Remove o nome da aplicação */
        this.setTitle("");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d2d);

        /* Adicionando intenções ao filtro de intencões */
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION); /* Indica uma alteração no status Wi-Fi P2P */
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION); /* Indica uma alteração na lista de pares disponíveis */
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION); /* Indica que o estado da conectividade Wi-Fi P2P mudou */
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION); /* Indica que os detalhes deste dispositivo mudaram */

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
    }

    private void search() {
        if (!isWifiEnabled) {
            /* Habilita o WiFi - D2DReceiver identifica a alteração do estado do WiFi */
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(true);
        }

        WifiP2pDevice device = DeviceFragmentList.getDevice();
        if (device != null && device.status != WifiP2pDevice.AVAILABLE) { /* Se o dispositivo está conectado, inibir a ação do botão */
            return;
        }

        /* Cria alert dialog perguntando a forma como quer se conectar */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(TITLE_SEARCH_D2D);
        builder.setMessage(DIALOG_SEARCH_D2D);

        builder.setPositiveButton(BTN_POSITIVE_SEARCH_D2D, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                createGroup();
            }
        });

        builder.setNegativeButton(BTN_NEGATIVE_SEARCH_D2D, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                discoverPeers();
            }
        });

        AlertDialog alerta = builder.create(); /* Cria o AlertDialog */
        alerta.show(); /* Exibe */
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_direct_discover:
                search();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* Ao desconectar, deve-se limpar os dados da tela -> Usado pelo D2DReceiver */
    public void resetData() {
        destroeDialog();
        DeviceFragmentList dfl = (DeviceFragmentList) getFragmentManager().findFragmentById(R.id.list);
        DeviceFragment df = (DeviceFragment) getFragmentManager().findFragmentById(R.id.element);
        if (dfl != null) {
            dfl.clearPeers(); /* Limpa os pontos */
        }
        if (df != null) {
            df.resetViews(); /* Limpa a view */
        }
    }


    @Override
    public void onChannelDisconnected() {
        if (manager != null && !retryChannel) {
            Toast.makeText(this,ON_CHANNEL_DISCONNECTED, Toast.LENGTH_LONG).show();

            resetData();
            retryChannel = true;
            manager.initialize(this, getMainLooper(), this);

        } else {
            Toast.makeText(this, ON_CHANNEL_DISCONNECTED_NO_MANAGER, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showDetails(WifiP2pDevice device) {
        DeviceFragment df = (DeviceFragment) getFragmentManager().findFragmentById(R.id.element);
        df.showDetails(device);
    }

    @Override
    public void discoverPeers() {
        final DeviceFragmentList dfl = (DeviceFragmentList) getFragmentManager().findFragmentById(R.id.list);
        dfl.onInitiateDiscovery(); /* Inicia */

        Toast.makeText(this, "Channel\n" + channel.toString(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "\nManager\n" + manager.toString(), Toast.LENGTH_SHORT).show();

        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                Toast.makeText(SearchConnectDisconnectD2DActivity.this, ON_SUCCESS_DISCOVER_PEERS, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reasonCode) {
                Toast.makeText(SearchConnectDisconnectD2DActivity.this, ON_FAILURE_DISCOVER_PEERS, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void createGroup() {
        /* Cancela conexões */
        manager.cancelConnect(channel, null);

        /* Remove grupo - desconexão */
        manager.removeGroup(channel, null);

        /* Limpa os serviços locais */
        manager.clearLocalServices(channel, null);

        /* Limpa as requisições de serviços */
        manager.clearServiceRequests(channel, null);

        /* Cria grupo */
        manager.createGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                WriteLOG.getInstanceGeneratorLOG().writeLogConnectDisconnect(ACTION_CONNECT, CONNECTION_TYPE_D2D);
                /* D2DReceiver irá tratar */
            }

            @Override
            public void onFailure(int reason) {
                Log.e(CATEGORY_LOG, ON_FAILURE_CREATE_GROUP);
                Toast.makeText(SearchConnectDisconnectD2DActivity.this, ON_FAILURE_CREATE_GROUP, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void connectGroup(final WifiP2pConfig config) {
        manager.connect(channel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                WriteLOG.getInstanceGeneratorLOG().writeLogConnectDisconnect(ACTION_CONNECT, CONNECTION_TYPE_D2D);

                /* D2DReceiver irá tratar */
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
                pd = ProgressDialog.show(SearchConnectDisconnectD2DActivity.this, PROGRESS_DIALOG_CONNECT_GROUP_TITLE, PROGRESS_DIALOG_CONNECT_GROUP_MESSAGE + config.deviceAddress.toUpperCase(), true, true,
                        new DialogInterface.OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                /* Cancela a conexão */
                                manager.cancelConnect(channel, null);
                            }
                        });
            }

            @Override
            public void onFailure(int reason) {
                Log.e(CATEGORY_LOG, ON_FAILURE_CONNECT_GROUP);
                Toast.makeText(SearchConnectDisconnectD2DActivity.this, ON_FAILURE_CONNECT_GROUP, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void disconnectGroup() {
        final DeviceFragment fragment = (DeviceFragment) getFragmentManager().findFragmentById(R.id.element);
        fragment.resetViews();

        /* Remove do grupo */
        manager.removeGroup(channel, new WifiP2pManager.ActionListener() {

            @Override
            public void onFailure(int reasonCode) {
                Log.e(CATEGORY_LOG, ON_FAILURE_DISCONNECT_GROUP);
            }

            @Override
            public void onSuccess() {
                fragment.getView().setVisibility(View.GONE); /* Oculta a view */
            }

        });
    }


    @Override
    public void onResume() {
        /* Registrar o receptor de transmissão com os valores de intenção a serem correspondidos */
        super.onResume();
        receiver = new D2DReceiver(this, manager, channel);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onPause() {
        /* Cancelar o registro do receptor de transmissão */
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WriteLOG.getInstanceGeneratorLOG().exit();  /* Fechar o buffer do arquivo */
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SendFileD2DAndServerLocalActivity.setAddressServerLocal(""); /* Limpa o endereço do serverlocal, visto que pode estar conectado como D2D */
    }
}