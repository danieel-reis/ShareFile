/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connect;

import static connect.Constants.LOCAL_SERVER_PORT_SOCKET_UDP;
import static connect.Constants.MESSAGE_DEFAULT_SEARCH_SERVER_UDP_RECEIVED;
import static connect.Constants.MESSAGE_DEFAULT_SEARCH_SERVER_UDP_SEND;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Adaptado de: http://android-er.blogspot.com.br/2016/05/android-datagramudp-client-example.html
 * http://helloraspberrypi.blogspot.com.br/2016/05/java-datagramudp-server-and-client-run.html
 * @author daniel
 */
public class UDPServerThread extends Thread {

    protected DatagramSocket socket = null;
    private String myName;
    private String myAddress;

    public UDPServerThread() {
        try {
            socket = new DatagramSocket(LOCAL_SERVER_PORT_SOCKET_UDP);
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                byte[] buf = new byte[1024];

                System.out.println("Server UDP...");

                /* Recebe requisição */
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                String message = new String(packet.getData()).trim();
                System.out.println("Received: " + message);

                String me[] = message.split("\\+");
                String message_default = me[0];
                
                if (message_default.equals(MESSAGE_DEFAULT_SEARCH_SERVER_UDP_SEND)) {
                    /* Responde */
                    InetAddress address = packet.getAddress();
                    int port = packet.getPort();

                    /* Pega o endereço do servidor */
                    searchMyAddressAndMyName();

                    /* Seta a mensagem padrão */
                    String send = myName + "+" + myAddress + "+" + MESSAGE_DEFAULT_SEARCH_SERVER_UDP_RECEIVED;
                    System.out.println("Send: " + send);
                    byte[] msg = send.getBytes();

                    /* Monta o pacote a ser enviado */
                    packet = new DatagramPacket(msg, msg.length, address, port);

                    /* Envia a mensagem */
                    socket.send(packet);
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }

        }

    }

    private void searchMyAddressAndMyName() {
        /* Adaptado de: http://mariojp.com.br/2012/03/11/obtendo-o-endereco-ip-real-da-maquina-na-rede/ */
        Enumeration e;
        try {
            e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface i = (NetworkInterface) e.nextElement();
                Enumeration ds = i.getInetAddresses();
                while (ds.hasMoreElements()) {
                    InetAddress myself = (InetAddress) ds.nextElement();
                    if (!myself.getHostName().equals("localhost") && myself.getHostAddress().contains(".")) {
                        myAddress = myself.getHostAddress();
                        myName = myself.getHostName();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
    }

}
