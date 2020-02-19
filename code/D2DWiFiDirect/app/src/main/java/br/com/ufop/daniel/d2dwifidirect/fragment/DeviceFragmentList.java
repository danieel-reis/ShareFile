package br.com.ufop.daniel.d2dwifidirect.fragment;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.ufop.daniel.d2dwifidirect.adapter.PeerListAdapter;
import br.com.ufop.daniel.d2dwifidirect.connect.D2DDeviceActionListener;
import br.com.ufop.daniel.d2dwifidirect.R;
import br.com.ufop.daniel.d2dwifidirect.dao.WriteLOG;
import br.com.ufop.daniel.d2dwifidirect.util.Get;

import static br.com.ufop.daniel.d2dwifidirect.util.Constants.ACTION_END_SEARCH;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.ACTION_REQUEST_SEARCH;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.CONNECTION_TYPE_D2D;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.DIALOG_SEARCH_PEERS;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.TITLE_SEARCH_PEERS;

/**
 * Created by daniel on 26/07/17.
 */

public class DeviceFragmentList extends ListFragment implements WifiP2pManager.PeerListListener {

    private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    private static WifiP2pDevice device;
    ProgressDialog pd = null;
    View view = null;

    public static WifiP2pDevice getDevice() {
        return DeviceFragmentList.device;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.setListAdapter(new PeerListAdapter(getActivity(), R.layout.device_in_list, peers));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.list, null);
        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        WifiP2pDevice device = (WifiP2pDevice) getListAdapter().getItem(position);
        ((D2DDeviceActionListener) getActivity()).showDetails(device); /* Mostra os detalhes do dispositivo selecionado */
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peerList) {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
        peers.clear();
        peers.addAll(peerList.getDeviceList());
        ((PeerListAdapter) getListAdapter()).notifyDataSetChanged();

        WriteLOG.getInstanceGeneratorLOG().writeLogSearch(ACTION_END_SEARCH, peers.size(), CONNECTION_TYPE_D2D);

//        if (peers.size() == 0) {
//            message = "Nenhum dispositivo encontrado!";
//            WriteLOG.getInstanceGeneratorLOG().writeLogSendReceived(message);
//            Log.d(CATEGORY_LOG, message);
//
//            return;
//        } else {
//            message = "    Encontrados: ";
//            WriteLOG.getInstanceGeneratorLOG().writeLogSendReceived(message);
//            Log.d(CATEGORY_LOG, message);
//
//            for (WifiP2pDevice w : peers) {
//                message = "        DEVICE_ADDRESS -> " + w.deviceAddress.toUpperCase() +
//                        "\tDEVICE_NAME -> " + w.deviceName +
//                        "\tSTATUS -> " + getDeviceStatus(w.status) + " (" + w.status + ")";
//                WriteLOG.getInstanceGeneratorLOG().writeLogSendReceived(message);
////                Log.d(CATEGORY_LOG, message);
//            }
//        }
    }

    /* Atualizar os dados do dispositivo */
    public void updateThisDevice(WifiP2pDevice device) {
        this.device = device;

        TextView tv1 = (TextView) view.findViewById(R.id.my_name);
        TextView tv2 = (TextView) view.findViewById(R.id.my_address);
        TextView tv3 = (TextView) view.findViewById(R.id.my_status);

        tv1.setText(device.deviceName);
        tv2.setText(device.deviceAddress.toUpperCase());
        tv3.setText(Get.getDeviceStatus(device.status));
    }

    /* Limpar pontos */
    public void clearPeers() {
        peers.clear();
        ((PeerListAdapter) getListAdapter()).notifyDataSetChanged();
    }

    /* Buscando por pontos */
    public void onInitiateDiscovery() {
        WriteLOG.getInstanceGeneratorLOG().writeLogSearch(ACTION_REQUEST_SEARCH, 0, CONNECTION_TYPE_D2D);

        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
        pd = ProgressDialog.show(getActivity(), TITLE_SEARCH_PEERS, DIALOG_SEARCH_PEERS, true, true,
                new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                });
    }
}
