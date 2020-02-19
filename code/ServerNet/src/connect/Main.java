/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connect;

/**
 *
 * @author daniel
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        connectServerSocket();
    }

    private static SocketServer mySocket;

    public static void connectServerSocket() {
        mySocket = new SocketServer();
        Thread thread = new Thread(mySocket);
        thread.start();
    }

    public void disconnectServerSocket() {
        mySocket.disconnect();
    }

}
