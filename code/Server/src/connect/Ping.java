/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connect;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author daniel
 */
public class Ping {

    public static void main(String args[]) {
        long init = System.currentTimeMillis();
//        System.out.println(ping("https://drive.google.com/drive/"));
        System.out.println(getAvgPing("192.168.1.6"));
        System.out.println("Tempo = " + (System.currentTimeMillis() - init));
    }

    public static String getAvgPing(String ip) {
        Double soma = 0.0;

        for (int i = 0; i < 5; i++) {
            soma += Double.parseDouble(ping(ip));
        }

        return String.valueOf(soma / 5);
    }

    private static String ping(String ip) {
        String comando = "ping " + ip;
        try {
            Scanner S = new Scanner(Runtime.getRuntime().exec(comando).getInputStream());
            S.nextLine();

            String reply[] = S.nextLine().split("=");
            String r = reply[reply.length - 1];
            return r.substring(0, r.length() - 3);

        } catch (Exception ex) {
            return "0";
        }
    }

}
