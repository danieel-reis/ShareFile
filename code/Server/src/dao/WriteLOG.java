/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import br.com.ufop.daniel.d2dwifidirect.entities.DataSendReceived;
import static connect.Constants.DIRECTORY_SAVE_FILE_LOG;
import static connect.Constants.EXTENSION_FILE_LOG;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 *
 * @author daniel
 */
public class WriteLOG {

    private Writer writer;
    private static WriteLOG writeLOG;

    public static WriteLOG getInstanceGeneratorLOG() {
        if (writeLOG == null) {
            writeLOG = new WriteLOG();
        }
        return writeLOG;
    }

    private WriteLOG() {
        initWriteFile();
    }

    /* Inicia a gravação no arquivo */
    private void initWriteFile() {
        if (writer == null) {
            /* Se ainda não tiver iniciado */
            File dir = new File(DIRECTORY_SAVE_FILE_LOG);
            dir.mkdirs();
            File file = new File(DIRECTORY_SAVE_FILE_LOG + "log" + System.currentTimeMillis() + EXTENSION_FILE_LOG);

            try {
                writer = new BufferedWriter(new FileWriter(file, true));
//                System.err.println("Arquivo criado e aberto para escrita...");
            } catch (Exception e) {
                System.err.println("Erro ao manipular o arquivo! - " + e.getMessage());
                System.exit(1);
            }
        }
    }

    public void exit() {
        if (writer != null) {
            try {
                writer.close();
                writer = null;
                System.err.println("Fechando o buffer do arquivo");
            } catch (IOException e) {
                System.err.println("Erro ao fechar arquivo!");
            }
        }
    }

    public void writeLog(DataSendReceived data) {

        try {
            initWriteFile();
            /* Se não tiver inicializado, tenta inicializar a escrita no arquivo */

            if (writer != null && data != null) {
                writer.append(data + "\n");
                writer.flush();
            }
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo");
        }
    }

}
