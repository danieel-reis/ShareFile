package br.com.ufop.daniel.d2dwifidirect.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.net.InetAddress;

import br.com.ufop.daniel.d2dwifidirect.activity.SendFileD2DAndServerLocalActivity;
import br.com.ufop.daniel.d2dwifidirect.activity.SearchConnectDisconnectD2DActivity;
import br.com.ufop.daniel.d2dwifidirect.controlType.ControlType;
import br.com.ufop.daniel.d2dwifidirect.connect.D2DDeviceActionListener;
import br.com.ufop.daniel.d2dwifidirect.R;
import br.com.ufop.daniel.d2dwifidirect.dao.WriteLOG;
import br.com.ufop.daniel.d2dwifidirect.connect.D2DGOFileCopyTaskAsync;

import static br.com.ufop.daniel.d2dwifidirect.util.Constants.ACTION_REQUEST_CONNECT;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.ACTION_REQUEST_DISCONNECT;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.CONNECTION_TYPE_D2D;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.TYPE;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.TYPE_D2D;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.BTN_CONNECT;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.BTN_DISCONNECT;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.BTN_NEGATIVE_DISCONNECT_D2D;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.BTN_POSITIVE_DISCONNECT_D2D;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.BTN_SEND;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.CLASSIFICATION;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.CLIENT;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.DIALOG_DISCONNECT_D2D;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.GO;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.STATUS_CLIENT;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.TITLE_DISCONNECT_D2D;

/**
 * Created by daniel on 26/07/17.
 * Representa todos detalhes de uma conexão
 */

public class DeviceFragment extends Fragment implements WifiP2pManager.ConnectionInfoListener {

    private View view = null;
    private static WifiP2pDevice device;
    private static WifiP2pInfo info;

    public static WifiP2pInfo getInfo() {
        return DeviceFragment.info;
    }

    public static WifiP2pDevice getDevice() {
        return DeviceFragment.device;
    }

    public static InetAddress getOwnerGroup() {
        return DeviceFragment.info.groupOwnerAddress;
    }

    public static String getHostAddress() {
        return DeviceFragment.info.groupOwnerAddress.getHostAddress();
    }

    private void executeFileCopyTaskAsync() {
        new D2DGOFileCopyTaskAsync(getActivity(), view.findViewById(R.id.status_text)).execute();
    }

    @Override
    public void onConnectionInfoAvailable(final WifiP2pInfo info) {
        SearchConnectDisconnectD2DActivity.destroeDialog(); /* Destroe Dialog */

        this.info = info;
        this.getView().setVisibility(View.VISIBLE);

        TextView tv1 = (TextView) view.findViewById(R.id.device_info);

        /* Mostra o tipo (go ou client) */
        String type = CLASSIFICATION + (info.isGroupOwner == true ? GO : CLIENT);
        tv1.setText(type);

        /* Grupo foi formado e o dispositivo é um GO */
        if (info.groupFormed && info.isGroupOwner) {
            /* Executa tarefa assíncrona */
            executeFileCopyTaskAsync();

            /* Oculta o botão que permite enviar */
            view.findViewById(R.id.btn_send).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.text_btn_send).setVisibility(View.INVISIBLE);

        } else if (info.groupFormed) { /* Grupo foi formado e o dispositivo é um client */
            /* Mostra o botão que permite enviar */
            Button btn_send = view.findViewById(R.id.btn_send);
            btn_send.setVisibility(View.VISIBLE);
            btn_send.setBackground(getResources().getDrawable(R.drawable.ic_upload));
            TextView tv_send = view.findViewById(R.id.text_btn_send);
            tv_send.setVisibility(View.VISIBLE);
            tv_send.setText(BTN_SEND);

            /* Muda o status */
            ((TextView) view.findViewById(R.id.status_text)).setText(STATUS_CLIENT);
        }

        /* Altera o texto do botão */
        Button btn = view.findViewById(R.id.btn_connect_disconnect);
        btn.setBackground(getResources().getDrawable(R.drawable.ic_disconnect));
        TextView tv = view.findViewById(R.id.text_btn_connect_disconnect);
        tv.setText(BTN_DISCONNECT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.element, null); /* Seta o layout da view para o layout do elemento */

        /* OnClick do Botão de Conectar */
        view.findViewById(R.id.btn_connect_disconnect).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView tv = view.findViewById(R.id.text_btn_connect_disconnect);

                if (tv.getText().equals(BTN_CONNECT)) {
                    /* Conectar */
                    WriteLOG.getInstanceGeneratorLOG().writeLogConnectDisconnect(ACTION_REQUEST_CONNECT, CONNECTION_TYPE_D2D);

                    WifiP2pConfig config = new WifiP2pConfig();
                    config.deviceAddress = device.deviceAddress;
                    config.wps.setup = WpsInfo.PBC;

                    /* Solicita Conexão */
                    ((D2DDeviceActionListener) getActivity()).connectGroup(config);

                } else if (tv.getText().equals(BTN_DISCONNECT)) {

                    /* Desconectar */
                    if (tv.getText().equals(BTN_DISCONNECT)) {

                        /* Cria alert dialog perguntando a forma como quer se conectar */
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(TITLE_DISCONNECT_D2D);
                        builder.setMessage(DIALOG_DISCONNECT_D2D);
                        builder.setPositiveButton(BTN_POSITIVE_DISCONNECT_D2D, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                                WriteLOG.getInstanceGeneratorLOG().writeLogConnectDisconnect(ACTION_REQUEST_DISCONNECT, CONNECTION_TYPE_D2D);

                                /* Solicita Desconexão */
                                ((D2DDeviceActionListener) getActivity()).disconnectGroup();
                            }
                        });
                        builder.setNegativeButton(BTN_NEGATIVE_DISCONNECT_D2D, null);
                        AlertDialog alerta = builder.create(); /* Cria o AlertDialog */
                        alerta.show(); /* Exibe */
                    } else {
                        resetViews();
                    }
                }
            }
        });

        /* OnClick do Botão que permite enviar algo */
        view.findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SendFileD2DAndServerLocalActivity.class);
                intent.putExtra(TYPE, TYPE_D2D);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (intent != null) {
            ControlType.sendFileD2DAndServerLocal(intent, getActivity(), TYPE_D2D, requestCode);
        }
    }

    /* Atualiza os dados na interface -> Mostra os detalhes */
    public void showDetails(WifiP2pDevice device) {
        this.device = device;
        this.getView().setVisibility(View.VISIBLE);

        TextView tv1 = (TextView) view.findViewById(R.id.device_info);
        tv1.setText(device.toString());
    }

    /* Limpar os campos */
    public void resetViews() {
        /* Altera o texto do botão de Desconectar */
        Button btn = view.findViewById(R.id.btn_connect_disconnect);
        btn.setBackground(getResources().getDrawable(R.drawable.ic_connect));
        TextView tv = view.findViewById(R.id.text_btn_connect_disconnect);
        tv.setText(BTN_CONNECT);

        TextView tv1 = (TextView) view.findViewById(R.id.device_info);
        TextView tv2 = (TextView) view.findViewById(R.id.status_text);

        tv1.setText("");
        tv2.setText("");

        /* Oculta o botão que permite enviar */
        view.findViewById(R.id.btn_send).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.text_btn_send).setVisibility(View.INVISIBLE);

        this.getView().setVisibility(View.GONE); /* Oculta o conteúdo da view */
    }

}
