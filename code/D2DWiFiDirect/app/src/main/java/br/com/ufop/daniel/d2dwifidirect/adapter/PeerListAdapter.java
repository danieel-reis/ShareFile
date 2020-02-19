package br.com.ufop.daniel.d2dwifidirect.adapter;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.ufop.daniel.d2dwifidirect.R;
import br.com.ufop.daniel.d2dwifidirect.util.Get;

/**
 * Created by daniel on 19/11/17.
 */

public class PeerListAdapter extends ArrayAdapter<WifiP2pDevice> {

    private List<WifiP2pDevice> items;
    private Context context;

    /* Construtor */
    public PeerListAdapter(Context context, int textViewResourceId, List<WifiP2pDevice> objects) {
        super(context, textViewResourceId, objects);
        this.items = objects;
        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.device_in_list, null); /* Layout do meu dispositivo */
        }
        WifiP2pDevice device = items.get(position); /* Pega o dispositivo */
        if (device != null) {
            TextView tv1 = (TextView) view.findViewById(R.id.device_name);
            TextView tv2 = (TextView) view.findViewById(R.id.device_address);
            TextView tv3 = (TextView) view.findViewById(R.id.device_details);
            if (tv1 != null) {
                tv1.setText(device.deviceName); /* Seta o nome do dispositivo */
            }
            if (tv2 != null) {
                tv2.setText(device.deviceAddress.toUpperCase()); /* Seta o endereço do dispositivo */
            }
            if (tv3 != null) {
                tv3.setText(Get.getDeviceStatus(device.status)); /* Seta o status do dispositivo */
            }
        }
        return view;
    }
}