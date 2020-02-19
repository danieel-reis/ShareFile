/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connect;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JOptionPane;
import static connect.Constants.LOCAL_SERVER_PORT_SOCKET;

/**
 *
 * @author daniel
 */
public class SocketServer implements Runnable {

    private boolean connected = false;
    private ServerSocket serverSocket = null;
    private Socket connection = null;
    private HandleConnection handle = null;

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(LOCAL_SERVER_PORT_SOCKET);

//            MainWindown.appendText("Servidor está executando...");
            connected = true;
            while (connected) {
//                MainWindown.appendText("Aguardando conexões...");
                connection = serverSocket.accept();
                
                /* Desativa a propriedade "keep-alive" que está ativada por padrão */
                System.setProperty("http.keepAlive", "false");
                connection.setKeepAlive(false);

                String deviceName = connection.getInetAddress().getHostName();
                String deviceAddress = connection.getInetAddress().getHostAddress();
                
                System.out.println("\n\nEndereço = " + deviceAddress + "\t\tNome = " + deviceName);

                handle = new HandleConnection(connection, deviceName, deviceAddress);
                Thread handleConnection = new Thread(handle);
                handleConnection.start();
//                MainWindown.appendText("Thread que lida com as conexões está executando...");
            }
        } catch (SocketException e) {
            System.out.println("\n\n\nServidor finalizado!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
                connected = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An exception occurred.");
        }
    }

}
