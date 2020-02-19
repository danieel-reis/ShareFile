/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mergeFiles;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author daniel
 */
public class Main {

    private static String directory = "/home/daniel/Downloads/ShareFiles/";

    public static void main(String[] args) throws FileNotFoundException, IOException {
//        executaD2D("0cm");
//        executaD2D("1m");
//        executaD2D("3m");
//        executaD2D("5m");
//        executaD2D("10m");
//        executaD2D("15m");
//        executaProcessamentoD2D("0cm");
//        executaProcessamentoD2D("1m");
//        executaProcessamentoD2D("3m");
//        executaProcessamentoD2D("5m");
//        executaProcessamentoD2D("10m");
//        executaProcessamentoD2D("15m");
//        executaProcessamentoFinal("D2D");

//        executaProcessamentoConexaoD2D();
//        executaServer("SERVER_LOCAL", "IMPORT");
//        executaServer("SERVER_LOCAL", "EXPORT");
//        executaProcessamentoServer("SERVER_LOCAL", "IMPORT");
//        executaProcessamentoServer("SERVER_LOCAL", "EXPORT");
//        executaProcessamentoServerAll("SERVER_LOCAL", "IMPORT");
//        executaProcessamentoServerAll("SERVER_LOCAL", "EXPORT");
//        executaServer("SERVER_EXTERNAL", "IMPORT");
//        executaServer("SERVER_EXTERNAL", "EXPORT");
//        executaProcessamentoServer("SERVER_EXTERNAL", "IMPORT");
//        executaProcessamentoServer("SERVER_EXTERNAL", "EXPORT");
//        executaProcessamentoServerAll("SERVER_EXTERNAL", "IMPORT");
//        executaProcessamentoServerAll("SERVER_EXTERNAL", "EXPORT");
        mergeRTT();
    }

    private static void mergeRTT() throws IOException {
        String p = "/home/daniel/Documentos/A/";
        String f1 = p + "All-UPLOAD-SERVER_LOCAL.csv";
        String f2 = p + "All-UPLOAD-SERVER_EXTERNAL.csv";
        String filenameMerge = p + "CDF-RTT.csv";
        String d = ",";

        BufferedWriter bw = new BufferedWriter(new FileWriter(filenameMerge, true));
        // Adiciona cabecalho
        String c = "Local(ms), External(ms)";
        bw.append(c + "\n");
        bw.flush();
        ArrayList<String> linesA = getLines(f1);
        ArrayList<String> linesB = getLines(f2);
        ArrayList<Double> a = new ArrayList<>();
        ArrayList<Double> b = new ArrayList<>();

        for (int i = 0; i < linesA.size() && i < linesB.size(); i++) {
            String la[] = linesA.get(i).split(";");
            String lb[] = linesB.get(i).split(d);
            a.add(Double.parseDouble(la[5]));
            b.add(Double.parseDouble(lb[5]));
        }

        Collections.sort(a);
        Collections.sort(b);

        for (int i = 0; i < a.size(); i++) {
            bw.append(a.get(i) + d + b.get(i) + "\n");
            bw.flush();
        }
        bw.close();
    }

    // 20039 pra todos na execucao 1
    // 21368 pra 0cm e 1m na execucao 2 a 10
    // 21368 pra 3m na execucao 2 a 4
    // 
    private static void executaD2D(String di) throws IOException {
        String pc = directory + "D2D/" + di + "/Client/";
        String pg = directory + "D2D/" + di + "/Go/";
        String pp = directory + "D2D/" + di + "/Processamento/";
        String d = di + "-";
        String t[] = {"Client-", "Go-"};
        String s = "D2D.csv";
        String e = "Execucao";

        String n1;
        String n2;
        String n3;
        for (int i = 1; i <= 10; i++) {
            n1 = pc + e + i + "-" + t[0] + d + s;
            /* ExecucaoI-Client-0cm-D2D.csv */
            n2 = pg + e + i + "-" + t[1] + d + s;
            /* ExecucaoI-Go-0cm-D2D.csv */
            n3 = pp + e + i + "-" + "Processada-" + d + s;

            merge(n1, n2, n3, "D2D", ",", "");
        }
    }

    private static void executaProcessamentoConexaoD2D() throws IOException {
        ArrayList<String> filenameClient = new ArrayList<>();
        ArrayList<String> filenameGo = new ArrayList<>();

        for (int k = 0; k < 6; k++) {
            String di = "";
            switch (k) {
                case 0:
                    di = "0cm";
                    break;
                case 1:
                    di = "1m";
                    break;
                case 2:
                    di = "3m";
                    break;
                case 3:
                    di = "5m";
                    break;
                case 4:
                    di = "10m";
                    break;
                case 5:
                    di = "15m";
                    break;
            }

            String pc = directory + "D2D/" + di + "/Client/";
            String pg = directory + "D2D/" + di + "/Go/";
            String d = di + "-";
            String t[] = {"Client-", "Go-"};
            String s = "D2D.csv";
            String e = "Execucao";

            String n1;
            String n2;
            String n3;

            for (int i = 1; i <= 10; i++) {
                n1 = pc + e + i + "-" + t[0] + d + s;
                /* ExecucaoI-Client-0cm-D2D.csv */
                n2 = pg + e + i + "-" + t[1] + d + s;
                /* ExecucaoI-Go-0cm-D2D.csv */
                n3 = directory + "D2D/" + di + "/ConexaoProcessada-" + d + s;
                filenameClient.add(n1);
                filenameGo.add(n2);

//                mergeConexao(n1, n3, ",");
//                mergeConexao(n2, n3, ",");
            }
        }

        String filenameMergeBusca = directory + "D2D/Search.csv";
        String filenameMergeConexao = directory + "D2D/Connect.csv";
//        String filenameMergeDesconexao = directory + "D2D/Disconnect.csv";
        String cabecalho = "Tipo; Distancia(m); TimestampInicial(ms); TimestampFinal(ms); Tempo(ms)";

        BufferedWriter bwb = new BufferedWriter(new FileWriter(filenameMergeBusca, true));
        bwb.write(cabecalho + "\n");
        bwb.flush();

        BufferedWriter bwc = new BufferedWriter(new FileWriter(filenameMergeConexao, true));
        bwc.write(cabecalho + "\n");
        bwc.flush();

//        BufferedWriter bwd = new BufferedWriter(new FileWriter(filenameMergeDesconexao, true));
//        bwd.write(cabecalho + "\n");
//        bwd.flush();
        for (int u = 0; u < 2; u++) {
            ArrayList<String> files = new ArrayList<>();
            String tipo = "";
            if (u == 0) {
                tipo = "Client";
                files = filenameClient;
            } else if (u == 1) {
                tipo = "Go";
                files = filenameGo;
            }

            /* Para cada arquivo */
            for (int i = 0; i < files.size(); i++) {

                String di = "";
                switch (i / 10) {
                    case 0:
                        di = "0cm";
                        break;
                    case 1:
                        di = "1m";
                        break;
                    case 2:
                        di = "3m";
                        break;
                    case 3:
                        di = "5m";
                        break;
                    case 4:
                        di = "10m";
                        break;
                    case 5:
                        di = "15m";
                        break;
                }

                /* Pega todas linhas do arquivo */
                ArrayList<String> lines = getLines(files.get(i));

                for (int j = 0; j < lines.size(); j++) {

                    String c[] = lines.get(j).split(",");
                    /* Busca - Só considera client */
                    if (u == 0 && c[0].equals("REQUEST_SEARCH")) {

                        /* Testa o próximo */
                        if ((j + 1) < lines.size()) {
                            String cp[] = lines.get(j + 1).split(",");

                            Long ti = Long.parseLong(c[2]);
                            Long tf = Long.parseLong(cp[2]);
                            Long tt = tf - ti;
                            bwb.append(tipo + "," + di + ";" + ti + ";" + tf + ";" + tt + "\n");
                            bwb.flush();
                        }
                    } /* Conexão */ else if (c[0].equals("CONNECT")) {

                        /* Testa o próximo */
                        if (u == 1 && (j + 1) < lines.size()) {
                            String cp1[] = lines.get(j + 1).split(",");

                            if (cp1[5].equals("CONECTADO")) {
                                Long ti = Long.parseLong(c[2]);
                                Long tf = Long.parseLong(cp1[2]);
                                Long tt = tf - ti;
                                bwc.append(tipo + "," + di + ";" + ti + ";" + tf + ";" + tt + "\n");
                                bwc.flush();
                            }
                        } else if (u == 0 && (j + 3) < lines.size()) {
                            String cp3[] = lines.get(j + 3).split(",");

                            if (cp3[5].equals("CONECTADO")) {
                                Long ti = Long.parseLong(c[2]);
                                Long tf = Long.parseLong(cp3[2]);
                                Long tt = tf - ti;
                                bwc.append(tipo + "," + di + ";" + ti + ";" + tf + ";" + tt + "\n");
                                bwc.flush();
                            }
                        }
                    } /* Conexão */ else if (c[0].equals("DISCONNECT")) {

                    }
                }
            }

        }
        bwb.close();
        bwc.close();
//        bwd.close();

    }

    private static void executaServer(String type, String importOrExport) throws IOException {
        String pc = directory + type + "/Client/";
        String pg = directory + type + "/Server/";
        String pp = directory + type + "/Processamento/";
        String t[] = {"Client-", "Server-"};
        String s = "";
        if (type.equals("SERVER_LOCAL")) {
            s = "SERVERLOCAL";
        } else if (type.equals("SERVER_EXTERNAL")) {
            s = "SERVEREXTERNAL";
        }
        String e = "Execucao";

        String n1;
        String n2;
        String n3;
        for (int i = 1; i <= 10; i++) {
            n1 = pc + e + i + "-" + t[0] + s + ".csv";
            /* ExecucaoI-Client-Type.csv */
            n2 = pg + e + i + "-" + t[1] + s + ".csv";
            /* ExecucaoI-Go-Type.csv */
            n3 = pp + e + i + "-" + "Processada-" + s + ".csv";

            if (importOrExport.equals("IMPORT")) {
                merge(n1, n2, n3, type, ",", "IMPORT");
            } else {
                merge(n1, n2, n3, type, ",", "EXPORT");
            }
        }
    }

    private static void executaProcessamentoD2D(String di) throws IOException {
        String p = directory + "D2D/" + di + "/Processamento/";
        String pf = directory + "D2D/" + di + "/";
        String d = di + "-";
        String t[] = {"Client-", "Go-"};
        String s = "D2D.csv";
        String e = "Execucao";

        String n;
        ArrayList<String> names = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            n = p + e + i + "-Processada-" + d + s;
            /* ExecucaoI-Client-Processada-0cm-D2D.csv */
            names.add(n);
        }

        String nn1 = pf + "ProcessamentoFinal-Go-" + d + s;
        String nn2 = pf + "ProcessamentoFinal-Client-" + d + s;
        mergeProcessada(names, nn1, "GO", ";", "");
        mergeProcessada(names, nn2, "Client", ";", "");
    }

    private static void executaProcessamentoServerAll(String type, String importOrExport) throws IOException {
        String pc = directory + type + "/Client/";
        String pg = directory + type + "/Server/";
        String pp = directory + type + "/All-" + type + ".csv/";
        String t[] = {"Client-", "Server-"};
        String s = "";
        if (type.equals("SERVER_LOCAL")) {
            s = "SERVERLOCAL";
        } else if (type.equals("SERVER_EXTERNAL")) {
            s = "SERVEREXTERNAL";
        }
        String e = "Execucao";

        ArrayList<String> nomesC = new ArrayList<>();
        ArrayList<String> nomesS = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            String nc = pc + e + i + "-" + t[0] + s + ".csv";
            /* ExecucaoI-Client-Type.csv */
            String ns = pg + e + i + "-" + t[1] + s + ".csv";

            nomesC.add(nc);
            nomesS.add(ns);
        }

        mergeAllServer(nomesC, nomesS, pp, type, ",", importOrExport);
    }

    private static void executaProcessamentoServer(String type, String importOrExport) throws IOException {
        String p = directory + type + "/Processamento/";
        String pf = directory + type + "/";
        String s = "";
        String nn1 = "";

        if (type.equals("SERVER_LOCAL")) {
            s = "SERVERLOCAL.csv";
            nn1 = pf + "ProcessamentoFinal-ServerLocal-" + s;
        } else if (type.equals("SERVER_EXTERNAL")) {
            s = "SERVEREXTERNAL.csv";
            nn1 = pf + "ProcessamentoFinal-ServerExternal-" + s;
        }
        String e = "Execucao";

        String n;
        ArrayList<String> names = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            n = p + e + i + "-Processada-" + s;
            names.add(n);
        }

        String nn2 = pf + "ProcessamentoFinal-Client-" + s;

        if (type.equals("SERVER_LOCAL")) {
            mergeProcessada(names, nn1, "ServerLocal", ";", importOrExport);
        } else if (type.equals("SERVER_EXTERNAL")) {
            mergeProcessada(names, nn1, "ServerExternal", ";", importOrExport);
        }
        mergeProcessada(names, nn2, "Client", ";", importOrExport);
    }

    private static void executaProcessamentoFinal(String type) throws IOException {
        ArrayList<String> names1 = new ArrayList<>();
        ArrayList<String> names2 = new ArrayList<>();

        if (type.equals("D2D")) {
            names1.add(directory + type + "/" + "0cm/ProcessamentoFinal-Go-0cm-D2D.csv");
            names1.add(directory + type + "/" + "1m/ProcessamentoFinal-Go-1m-D2D.csv");
            names1.add(directory + type + "/" + "3m/ProcessamentoFinal-Go-3m-D2D.csv");
            names1.add(directory + type + "/" + "5m/ProcessamentoFinal-Go-5m-D2D.csv");
            names1.add(directory + type + "/" + "10m/ProcessamentoFinal-Go-10m-D2D.csv");
            names1.add(directory + type + "/" + "15m/ProcessamentoFinal-Go-15m-D2D.csv");
            names2.add(directory + type + "/" + "0cm/ProcessamentoFinal-Client-0cm-D2D.csv");
            names2.add(directory + type + "/" + "1m/ProcessamentoFinal-Client-1m-D2D.csv");
            names2.add(directory + type + "/" + "3m/ProcessamentoFinal-Client-3m-D2D.csv");
            names2.add(directory + type + "/" + "5m/ProcessamentoFinal-Client-5m-D2D.csv");
            names2.add(directory + type + "/" + "10m/ProcessamentoFinal-Client-10m-D2D.csv");
            names2.add(directory + type + "/" + "15m/ProcessamentoFinal-Client-15m-D2D.csv");

        } else if (type.equals("SERVER_LOCAL")) {
            names1.add(directory + type + "/" + "ProcessamentoFinal-Go-0cm-D2D.csv");
            names2.add(directory + type + "/" + "ProcessamentoFinal-Client-0cm-D2D.csv");
        }

        String nn1 = directory + type + "/" + "Resultado-Go-D2D.csv";
        String nn2 = directory + type + "/" + "Resultado-Client-D2D.csv";
        mergeTransferenciaProcessadaFinal(names1, nn1, "GO", ";");
        mergeTransferenciaProcessadaFinal(names2, nn2, "Client", ";");
    }

    private static ArrayList<String> getLinesRTT(String filename, String delimitador) throws IOException {
        ArrayList<String> lines = new ArrayList<>();

        FileReader arq = new FileReader(filename);
        BufferedReader br = new BufferedReader(arq);

        String line = br.readLine(); // Lê a primeira linha

        while (line != null) {

            String l[] = line.split(delimitador);

            try {
                Double.parseDouble(l[0]);
                lines.add(l[0]);
            } catch (Exception e) {

            }

            line = br.readLine(); // lê da segunda até a última linha

        }
        return lines;
    }

    private static ArrayList<String> getLines(String filename, String tag, String type, String delimitador) throws IOException {
        ArrayList<String> lines = new ArrayList<>();

        FileReader arq = new FileReader(filename);
        BufferedReader br = new BufferedReader(arq);

        String line = br.readLine(); // Lê a primeira linha

        while (line != null) {

            String l[] = line.split(delimitador);

            // Verifica se tem a tag
            if (l[0].equals(tag)) {

                if (l.length > 8) {
                    // Verifica o tipo da comunicação
                    if (l[8].equals(type)) {
                        lines.add(line);
                    }
                }
            }

            line = br.readLine(); // lê da segunda até a última linha

        }
        return lines;
    }

    private static void merge(String filename1, String filename2, String filenameMerge,
            String type, String delimitador, String action) throws IOException {

        BufferedWriter bw = new BufferedWriter(new FileWriter(filenameMerge, true));
        // Adiciona cabecalho
        String c = "TimestampInicialClient(ms); TimestampFinalClient(ms); TimestampInicialGo(ms); TimestampFinalGo(ms); Extensao; Tamanho(bytes); "
                + "TempoClient(ms); TempoGo(ms)";
        if ((type.equals("SERVER_LOCAL") || type.equals("SERVER_EXTERNAL")) && action.equals("EXPORT")) {
            c += "; RTT";
        }
        bw.append(c + "\n");
        bw.flush();
        ArrayList<String> linesA = new ArrayList<>();
        ArrayList<String> linesB = new ArrayList<>();
        ArrayList<String> rtts = new ArrayList<>();

        if (action.equals("IMPORT")) {
            linesA = getLines(filename2, "RECEIVED", "IMPORT_FROM_SERVER", delimitador); // Pega as linhas com aquela tag do primeiro arquivo
            linesB = getLines(filename1, "SEND", type, delimitador); // Pega as linhas com aquela tag do segundo arquivo

            if (linesA == null || linesA.size() == 0) {
                linesA = getLines(filename1, "RECEIVED", "IMPORT_FROM_SERVER", delimitador); // Pega as linhas com aquela tag do primeiro arquivo
            }
            if (linesB == null || linesB.size() == 0) {
                linesB = getLines(filename2, "SEND", type, delimitador); // Pega as linhas com aquela tag do segundo arquivo

            }

        } else if (action.equals("EXPORT")) {
            linesA = getLines(filename1, "SEND", type, delimitador); // Pega as linhas com aquela tag do primeiro arquivo
            linesB = getLines(filename2, "RECEIVED", type, delimitador); // Pega as linhas com aquela tag do segundo arquivo
            rtts = getLinesRTT(filename1, delimitador);
        }

        // Agrupa as que tiverem relacionando o mesmo arquivo
        int cont = 0;
        for (String la : linesA) {
            for (String lb : linesB) {
                String a[] = la.split(delimitador);
                String b[] = lb.split(delimitador);

                // Verifica se se trata do mesmo arquivo pelo tamanho
                if (a[7].equals(b[7])) {
                    // Captura: timestamp inicial e final de A e B + extensão + tamanho do arquivo
                    String tiD1 = a[4];
                    /* Timestamp inicial Dispositivo 1 */
                    String tfD1 = a[5];
                    /* Timestamp final Dispositivo 1 */
                    Long tD1 = Long.parseLong(tfD1) - Long.parseLong(tiD1);
                    /* Tempo gasto pelo Dispositivo 1 */

                    String tiD2 = b[4];
                    /* Timestamp inicial Dispositivo 2 */
                    String tfD2 = b[5];
                    /* Timestamp final Dispositivo 2 */
                    Long tD2 = Long.parseLong(tfD2) - Long.parseLong(tiD2);
                    /* Tempo gasto pelo Dispositivo 2 */

                    String t = b[7];
                    /* Tamanho */
                    String e = b[6];
                    /* Extensão */

                    String d = ";";
                    String text = tiD1 + d + tfD1 + d + tiD2 + d + tfD2 + d + e + d + t + d + tD1 + d + tD2;

                    if ((type.equals("SERVER_LOCAL") || type.equals("SERVER_EXTERNAL")) && rtts != null && rtts.size() > cont) {
                        text += d + rtts.get(cont);
                    }

                    // Adiciona no fim do arquivo o texto
                    bw.append(text + "\n");
                    bw.flush();
                }
            }
            cont++;
        }
    }

    private static ArrayList<String> getLines(String filename) throws IOException {
        ArrayList<String> lines = new ArrayList<>();

        FileReader arq = new FileReader(filename);
        BufferedReader br = new BufferedReader(arq);

        String line = br.readLine(); // Lê a primeira linha

        while (line != null) {
            line = br.readLine(); // lê da segunda até a última linha

            if (line != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    private static void mergeAllServer(ArrayList<String> filenameC, ArrayList<String> filenameS, String filenameMerge, String type, String delimitador, String importOrExport) throws IOException {
        BufferedWriter bw;
        if (type.equals("SERVER_LOCAL") || type.equals("SERVER_EXTERNAL")) {
            bw = new BufferedWriter(new FileWriter(filenameMerge, true));

            String c = "Tipo; Extensao; Tamanho(bytes); TempoClient(ms); TempoServer(ms)";

            if (!importOrExport.equals("IMPORT")) {
                c += "; RTT(ms)";
            }
            bw.append(c + "\n");
            bw.flush();

            String tag1 = "SEND";
            String tag2 = "RECEIVED";
            String t = type;
            String t2 = type;

            if (importOrExport.equals("IMPORT")) {
                tag1 = "RECEIVED";
                tag2 = "SEND";
                t = "IMPORT_FROM_SERVER";
            }

            ArrayList<String> lines1C = getLines(filenameC.get(0), tag1, t, delimitador); // Pega as linhas daquele arquivo
            ArrayList<String> lines2C = getLines(filenameC.get(1), tag1, t, delimitador); // Pega as linhas daquele arquivo
            ArrayList<String> lines3C = getLines(filenameC.get(2), tag1, t, delimitador); // Pega as linhas daquele arquivo
            ArrayList<String> lines4C = getLines(filenameC.get(3), tag1, t, delimitador); // Pega as linhas daquele arquivo
            ArrayList<String> lines5C = getLines(filenameC.get(4), tag1, t, delimitador); // Pega as linhas daquele arquivo
            ArrayList<String> lines6C = getLines(filenameC.get(5), tag1, t, delimitador); // Pega as linhas daquele arquivo
            ArrayList<String> lines7C = getLines(filenameC.get(6), tag1, t, delimitador); // Pega as linhas daquele arquivo
            ArrayList<String> lines8C = getLines(filenameC.get(7), tag1, t, delimitador); // Pega as linhas daquele arquivo
            ArrayList<String> lines9C = getLines(filenameC.get(8), tag1, t, delimitador); // Pega as linhas daquele arquivo
            ArrayList<String> lines10C = getLines(filenameC.get(9), tag1, t, delimitador); // Pega as daquele arquivo

            ArrayList<String> lines1S = getLines(filenameS.get(0), tag2, t2, delimitador); // Pega as linhas daquele arquivo
            ArrayList<String> lines2S = getLines(filenameS.get(1), tag2, t2, delimitador); // Pega as linhas daquele arquivo
            ArrayList<String> lines3S = getLines(filenameS.get(2), tag2, t2, delimitador); // Pega as linhas daquele arquivo
            ArrayList<String> lines4S = getLines(filenameS.get(3), tag2, t2, delimitador); // Pega as linhas daquele arquivo
            ArrayList<String> lines5S = getLines(filenameS.get(4), tag2, t2, delimitador); // Pega as linhas daquele arquivo
            ArrayList<String> lines6S = getLines(filenameS.get(5), tag2, t2, delimitador); // Pega as linhas daquele arquivo
            ArrayList<String> lines7S = getLines(filenameS.get(6), tag2, t2, delimitador); // Pega as linhas daquele arquivo
            ArrayList<String> lines8S = getLines(filenameS.get(7), tag2, t2, delimitador); // Pega as linhas daquele arquivo
            ArrayList<String> lines9S = getLines(filenameS.get(8), tag2, t2, delimitador); // Pega as linhas daquele arquivo
            ArrayList<String> lines10S = getLines(filenameS.get(9), tag2, t2, delimitador); // Pega as daquele arquivo

            ArrayList<String> rtts1 = new ArrayList<>();
            ArrayList<String> rtts2 = new ArrayList<>();
            ArrayList<String> rtts3 = new ArrayList<>();
            ArrayList<String> rtts4 = new ArrayList<>();
            ArrayList<String> rtts5 = new ArrayList<>();
            ArrayList<String> rtts6 = new ArrayList<>();
            ArrayList<String> rtts7 = new ArrayList<>();
            ArrayList<String> rtts8 = new ArrayList<>();
            ArrayList<String> rtts9 = new ArrayList<>();
            ArrayList<String> rtts10 = new ArrayList<>();

            if (!importOrExport.equals("IMPORT")) {

                rtts1 = getLinesRTT(filenameC.get(0), delimitador);
                rtts2 = getLinesRTT(filenameC.get(1), delimitador);
                rtts3 = getLinesRTT(filenameC.get(2), delimitador);
                rtts4 = getLinesRTT(filenameC.get(3), delimitador);
                rtts5 = getLinesRTT(filenameC.get(4), delimitador);
                rtts6 = getLinesRTT(filenameC.get(5), delimitador);
                rtts7 = getLinesRTT(filenameC.get(6), delimitador);
                rtts8 = getLinesRTT(filenameC.get(7), delimitador);
                rtts9 = getLinesRTT(filenameC.get(8), delimitador);
                rtts10 = getLinesRTT(filenameC.get(9), delimitador);
            }
            // Agrupa
            for (String l : getConcatLine(rtts1, lines1C, lines1S, delimitador, type)) {
                bw.append(l + "\n");
                bw.flush();
            }
            for (String l : getConcatLine(rtts2, lines2C, lines2S, delimitador, type)) {
                bw.append(l + "\n");
                bw.flush();
            }
            for (String l : getConcatLine(rtts3, lines3C, lines3S, delimitador, type)) {
                bw.append(l + "\n");
                bw.flush();
            }
            for (String l : getConcatLine(rtts4, lines4C, lines4S, delimitador, type)) {
                bw.append(l + "\n");
                bw.flush();
            }
            for (String l : getConcatLine(rtts5, lines5C, lines5S, delimitador, type)) {
                bw.append(l + "\n");
                bw.flush();
            }
            for (String l : getConcatLine(rtts6, lines6C, lines6S, delimitador, type)) {
                bw.append(l + "\n");
                bw.flush();
            }
            for (String l : getConcatLine(rtts7, lines7C, lines7S, delimitador, type)) {
                bw.append(l + "\n");
                bw.flush();
            }
            for (String l : getConcatLine(rtts8, lines8C, lines8S, delimitador, type)) {
                bw.append(l + "\n");
                bw.flush();
            }
            for (String l : getConcatLine(rtts9, lines9C, lines9S, delimitador, type)) {
                bw.append(l + "\n");
                bw.flush();
            }
            for (String l : getConcatLine(rtts10, lines10C, lines10S, delimitador, type)) {
                bw.append(l + "\n");
                bw.flush();
            }

            bw.close();
        }
    }

    private static ArrayList<String> getConcatLine(ArrayList<String> rtts, ArrayList<String> linesC, ArrayList<String> linesS, String delimitador, String type) throws IOException {
        ArrayList<String> text = new ArrayList<>();
        int cont = 0;

        for (int i = 0; i < linesC.size(); i++) {

            String lC[] = linesC.get(i).split(delimitador);

            for (int j = 0; j < linesS.size(); j++) {
                // Pega cada linha de cada arquivo
                String lS[] = linesS.get(j).split(delimitador);

                /* Mesmo arquivo */
                if (lC[7].equals(lS[7])) {
                    /* Calcula os tempos */
                    Long tc1 = Long.parseLong(lC[5]) - Long.parseLong(lC[4]);
                    Long ts1 = Long.parseLong(lS[5]) - Long.parseLong(lS[4]);

                    /* Monta os textos */
                    String d = ";";
                    String t = type + d + lC[6] + d + lC[7] + d + tc1 + d + ts1;

                    if (rtts != null && rtts.size() > 0) {
                        t += d + rtts.get(cont);
                    }
                    text.add(t);
                    cont++;

                }
            }
        }
        return text;
    }

    private static void mergeProcessada(ArrayList<String> filename, String filenameMerge, String type, String delimitador, String importOrExport) throws IOException {

        BufferedWriter bw = new BufferedWriter(new FileWriter(filenameMerge, true));
        // Adiciona cabecalho
        String c = "Tipo; Extensao; Tamanho(bytes); TT1(ms); TT2(ms); TT3(ms); TT4(ms); TT5(ms); "
                + "TT6(ms); TT7(ms); TT8(ms); TT9(ms); TT10(ms); TempoMedio(ms)";
        if (!importOrExport.equals("IMPORT") && (type.equals("ServerLocal") || type.equals("ServerExternal"))) {
            c += "; RTTMedio(ms)";
        }
        bw.append(c + "\n");
        bw.flush();

        ArrayList<String> lines1 = getLines(filename.get(0)); // Pega as linhas daquele arquivo
        ArrayList<String> lines2 = getLines(filename.get(1)); // Pega as linhas daquele arquivo
        ArrayList<String> lines3 = getLines(filename.get(2)); // Pega as linhas daquele arquivo
        ArrayList<String> lines4 = getLines(filename.get(3)); // Pega as linhas daquele arquivo
        ArrayList<String> lines5 = getLines(filename.get(4)); // Pega as linhas daquele arquivo
        ArrayList<String> lines6 = getLines(filename.get(5)); // Pega as linhas daquele arquivo
        ArrayList<String> lines7 = getLines(filename.get(6)); // Pega as linhas daquele arquivo
        ArrayList<String> lines8 = getLines(filename.get(7)); // Pega as linhas daquele arquivo
        ArrayList<String> lines9 = getLines(filename.get(8)); // Pega as linhas daquele arquivo
        ArrayList<String> lines10 = getLines(filename.get(9)); // Pega as daquele arquivo

        // Agrupa
        for (int i = 0; i < lines1.size(); i++) {

            // Pega cada linha de cada arquivo
            String l1[] = lines1.get(i).split(delimitador);
            String l2[] = lines2.get(i).split(delimitador);
            String l3[] = lines3.get(i).split(delimitador);
            String l4[] = lines4.get(i).split(delimitador);
            String l5[] = lines5.get(i).split(delimitador);
            String l6[] = lines6.get(i).split(delimitador);
            String l7[] = lines7.get(i).split(delimitador);
            String l8[] = lines8.get(i).split(delimitador);
            String l9[] = lines9.get(i).split(delimitador);
            String l10[] = lines10.get(i).split(delimitador);

            /* Extensão */
            String e = l1[4];

            /* Tamanho */
            String t = l1[5];

            /* Tempo total */
            int j = 0;
            if (type.equals("GO") || type.equals("ServerLocal") || type.equals("ServerExternal")) {
                j = 7;
            } else if (type.equals("Client")) {
                j = 6;
            }

            Double tt1 = Double.parseDouble(l1[j]);
            Double tt2 = Double.parseDouble(l2[j]);
            Double tt3 = Double.parseDouble(l3[j]);
            Double tt4 = Double.parseDouble(l4[j]);
            Double tt5 = Double.parseDouble(l5[j]);
            Double tt6 = Double.parseDouble(l6[j]);
            Double tt7 = Double.parseDouble(l7[j]);
            Double tt8 = Double.parseDouble(l8[j]);
            Double tt9 = Double.parseDouble(l9[j]);
            Double tt10 = Double.parseDouble(l10[j]);

            /* Media */
            Double media = (tt1 + tt2 + tt3 + tt4 + tt5 + tt6 + tt7 + tt8 + tt9 + tt10) / 10;

            String text = type + delimitador + e + delimitador + t + delimitador + tt1 + delimitador + tt2 + delimitador + tt3 + delimitador
                    + tt4 + delimitador + tt5 + delimitador + tt6 + delimitador + tt7 + delimitador + tt8 + delimitador
                    + tt9 + delimitador + tt10 + delimitador + media;

            if (!importOrExport.equals("IMPORT") && (type.equals("ServerLocal") || type.equals("ServerExternal"))) {
                Double rtt1 = Double.parseDouble(l1[8]);
                Double rtt2 = Double.parseDouble(l2[8]);
                Double rtt3 = Double.parseDouble(l3[8]);
                Double rtt4 = Double.parseDouble(l4[8]);
                Double rtt5 = Double.parseDouble(l5[8]);
                Double rtt6 = Double.parseDouble(l6[8]);
                Double rtt7 = Double.parseDouble(l7[8]);
                Double rtt8 = Double.parseDouble(l8[8]);
                Double rtt9 = Double.parseDouble(l9[8]);
                Double rtt10 = Double.parseDouble(l10[8]);

                Double RTTMedio = (rtt1 + rtt2 + rtt3 + rtt4 + rtt5 + rtt6 + rtt7 + rtt8 + rtt9 + rtt10) / 10;
                text += delimitador + RTTMedio;
            }

            // Adiciona no fim do arquivo o texto
            bw.append(text + "\n");
            bw.flush();
        }
    }

    private static void mergeTransferenciaProcessadaFinal(ArrayList<String> filename, String filenameMerge, String type, String delimitador) throws IOException {

        BufferedWriter bw = new BufferedWriter(new FileWriter(filenameMerge, true));
        // Adiciona cabecalho
        String c = "Tipo; Extensao; Tamanho(bytes); TempoMedio0cm(ms); TempoMedio1m(ms); TempoMedio3m(ms); TempoMedio5m(ms); TempoMedio10m(ms); TempoMedio15m(ms)";
        bw.append(c + "\n");
        bw.flush();

        ArrayList<String> lines1 = getLines(filename.get(0)); // Pega as linhas daquele arquivo
        ArrayList<String> lines2 = getLines(filename.get(1)); // Pega as linhas daquele arquivo
        ArrayList<String> lines3 = getLines(filename.get(2)); // Pega as linhas daquele arquivo
        ArrayList<String> lines4 = getLines(filename.get(3)); // Pega as linhas daquele arquivo
        ArrayList<String> lines5 = getLines(filename.get(4)); // Pega as linhas daquele arquivo
        ArrayList<String> lines6 = getLines(filename.get(5)); // Pega as linhas daquele arquivo

        // Agrupa
        for (int i = 0; i < lines1.size(); i++) {

            // Pega cada linha de cada arquivo
            String l1[] = lines1.get(i).split(delimitador);
            String l2[] = lines2.get(i).split(delimitador);
            String l3[] = lines3.get(i).split(delimitador);
            String l4[] = lines4.get(i).split(delimitador);
            String l5[] = lines5.get(i).split(delimitador);
            String l6[] = lines6.get(i).split(delimitador);

            /* Extensão */
            String e = l1[1];

            /* Tamanho */
            String t = l1[2];

            String tm1 = l1[13];
            String tm2 = l2[13];
            String tm3 = l3[13];
            String tm4 = l4[13];
            String tm5 = l5[13];
            String tm6 = l6[13];

            String text = type + delimitador + e + delimitador + t + delimitador + tm1 + delimitador + tm2 + delimitador + tm3 + delimitador
                    + tm4 + delimitador + tm5 + delimitador + tm6;

            // Adiciona no fim do arquivo o texto
            bw.append(text + "\n");
            bw.flush();
        }
    }

    private static void mergeConexao(String filename, String filenameMerge, String delimitador) throws IOException {

        BufferedWriter bw = new BufferedWriter(new FileWriter(filenameMerge, true));
        ArrayList<String> lines = getLines(filename); // Pega as linhas daquele arquivo

        // Agrupa
        for (int i = 0; i < lines.size(); i++) {

            // Pega cada linha de cada arquivo
            String l[] = lines.get(i).split(delimitador);

            /* Ação */
            String a = l[0];

            /* Filtra */
            if (!a.equals("SEND") && !a.equals("RECEIVED")) {
                /* Adiciona no fim do arquivo o texto */
                bw.append(lines.get(i) + "\n");
                bw.flush();

            }
        }
    }

}
