package br.com.ufop.daniel.d2dwifidirect.connect;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import br.com.ufop.daniel.d2dwifidirect.activity.SendFileD2DAndServerLocalActivity;
import br.com.ufop.daniel.d2dwifidirect.dao.WriteLOG;

import static br.com.ufop.daniel.d2dwifidirect.util.Constants.ACTION_END_SEARCH;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.ACTION_REQUEST_SEARCH;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.CATEGORY_LOG;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.CONNECTION_TYPE_SERVER_LOCAL;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.LOCAL_SERVER_PORT_SOCKET_UDP;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.MESSAGE_DEFAULT_SEARCH_SERVER_UDP_RECEIVED;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.MESSAGE_DEFAULT_SEARCH_SERVER_UDP_SEND;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.BTN_NEGATIVE_CONNECT_SERVER_LOCAL;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.BTN_POSITIVE_CONNECT_SERVER_LOCAL;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.DIALOG_CONNECT_SERVER_LOCAL;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.TITLE_CONNECT_SERVER_LOCAL;

/**
 * Created by daniel on 01/12/17.
 */
public class ServerLocalUdpClientThread extends Thread {

    private DatagramSocket socket = null;
    private String myAddress;
    private Activity activity;

    private String name_server;
    private String address_server;

    public ServerLocalUdpClientThread(String myAddress, Activity activity) {
        this.myAddress = myAddress;
        this.activity = activity;
    }

    @Override
    public void run() {

        try {
            /* Quebra a string por pontos */
            String a[] = myAddress.split("\\.");

            /* Envia uma mensagem UDP pra cada host - Broadcast "X.X.X.255" */
            String addressBroadcast = a[0] + "." + a[1] + "." + a[2] + "." + String.valueOf(255);

            /* Gera o Log da Requisição de Busca */
            WriteLOG.getInstanceGeneratorLOG().writeLogSearch(ACTION_REQUEST_SEARCH, 0, CONNECTION_TYPE_SERVER_LOCAL);

            /* Cria o socket */
            socket = new DatagramSocket();

            /* Define o endereço */
            final InetAddress address = InetAddress.getByName(addressBroadcast);

            /* Seta a mensagem padrão + código aleatório */
            String message = MESSAGE_DEFAULT_SEARCH_SERVER_UDP_SEND;
            byte[] buf = message.getBytes();

            /* Envia mensagem */
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, LOCAL_SERVER_PORT_SOCKET_UDP);
            socket.send(packet);

            /* Espera resposta */
            byte[] bufresp = new byte[1024];
            packet = new DatagramPacket(bufresp, bufresp.length);
            socket.receive(packet);
            String message_received = new String(packet.getData(), 0, packet.getLength());

            /* Gera o Log da Finalização da Busca -> Recebe resposta */
            WriteLOG.getInstanceGeneratorLOG().writeLogSearch(ACTION_END_SEARCH, 0, CONNECTION_TYPE_SERVER_LOCAL);

            try {
                if (message_received != null && message_received.length() > MESSAGE_DEFAULT_SEARCH_SERVER_UDP_RECEIVED.length()) {
                    String me[] = message_received.split("\\+");
                    name_server = me[0];
                    address_server = me[1];
                    String message_default = me[2];

                    if (SendFileD2DAndServerLocalActivity.getAddressServerLocal() == null || SendFileD2DAndServerLocalActivity.getAddressServerLocal().equals("")) {
                        if (message_default.equals(MESSAGE_DEFAULT_SEARCH_SERVER_UDP_RECEIVED)) {
                        /* Pergunta se o usuário deseja se conectar com esse servidor */
                            Runnable runnable = new Runnable() {
                                public void run() {
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                        /* Cria alert dialog perguntando se deseja se conectar com o servidor */
                                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                            builder.setTitle(TITLE_CONNECT_SERVER_LOCAL);
                                            builder.setMessage(DIALOG_CONNECT_SERVER_LOCAL + name_server + "?");

                                            builder.setPositiveButton(BTN_POSITIVE_CONNECT_SERVER_LOCAL, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface arg0, int arg1) {
                                                /* Pega o endereço de quem enviou a mensagem */
                                                    SendFileD2DAndServerLocalActivity.setAddressServerLocal(address_server);
                                                }
                                            });

                                            builder.setNegativeButton(BTN_NEGATIVE_CONNECT_SERVER_LOCAL, null);

                                            AlertDialog alerta = builder.create(); /* Cria o AlertDialog */
                                            alerta.show(); /* Exibe */
                                        }
                                    });

                                }
                            };
                            runnable.run(); /* Executa */
                        }
                    }
                }
            } catch (Exception e) {

            }

        } catch (SocketException e) {
            Log.e(CATEGORY_LOG, e.getMessage());
        } catch (UnknownHostException e) {
            Log.e(CATEGORY_LOG, e.getMessage());
        } catch (IOException e) {
            Log.e(CATEGORY_LOG, e.getMessage());
        } finally {
            if (socket != null) {
                socket.close();
            }
        }

    }
}