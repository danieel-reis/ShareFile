package br.com.ufop.daniel.d2dwifidirect.util;

import java.util.Locale;

import static br.com.ufop.daniel.d2dwifidirect.util.Constants.DIRECTORY_SAVE_FILE;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.DIRECTORY_SEND_FILE_AUTOMATIC;

/**
 * Created by daniel on 05/12/17.
 */

public class Dialog {

    private static Boolean isPT() {
        return Locale.getDefault().getLanguage().equals("pt");
    }

    public static String TEST_PING = (isPT() ? "Aguarde um momento! Testando a conexão de rede..." : "Wait a moment! Testing the network connection...");

    public static String CLASSIFICATION = (isPT() ? "Classificação: " : "Classification");
    public static String GO = (isPT() ? "GO" : "GO");
    public static String CLIENT = (isPT() ? "Client" : "Client");

    public static String RETURN = (isPT() ? "Voltar" : "Back");
    public static String RETURN_DESCRIPTION = (isPT() ? "Retornar" : "Return");

    public static String IMPORT = (isPT() ? "Importar" : "Import");
    public static String IMPORT_DESCRIPTION = (isPT() ? "Baixar do Computador" : "Download from Computer");

    public static String OPEN_GOOGLE_DRIVE = (isPT() ? "Google Drive" : "Google Drive");
    public static String OPEN_GOOGLE_DRIVE_DESCRIPTION = (isPT() ? "Mostrar todos arquivos" : "Show all files");

    public static String WAIT = (isPT() ? "Aguarde um instante..." : "Wait a minute");

    public static String FILES_NOT_RECEIVED = (isPT() ? "Nenhum arquivo recebido..." : "No files received...");
    public static String FILE_RECEIVED = (isPT() ? "Arquivo recebido: " : "File received: ");
    public static String FILE_SENDING = (isPT() ? "Arquivo enviado: " : "File sent: ");

    public static String STATUS_GO = (isPT() ? "Este dispositivo irá apenas receber arquivos..." : "This device will only receive files...");
    public static String STATUS_CLIENT = (isPT() ? "Selecione um arquivo local (armazenado) para enviar..." : "Select a local (stored) file to send...");

    public static String YES = (isPT() ? "Sim" : "Yes");
    public static String NO = (isPT() ? "Não" : "No");

    public static String TITLE_SEARCH_D2D = (isPT() ? "Conexão" : "Connection");
    public static String DIALOG_SEARCH_D2D = (isPT() ? "Deseja criar um grupo ou procurar e se conectar a um grupo existente?\n" +
            "\nLembre-se que o criador do grupo apenas recebe arquivos, e os demais que se conectarem ao grupo irão " +
            "apenas enviar arquivos para o criador." : "Do you want to create a group or browse and connect to an existing group?\n" +
            "\nRemember that the creator of the group only receives files, and the others who connect to the group will only send files to the creator.");
    public static String BTN_POSITIVE_SEARCH_D2D = (isPT() ? "Criar" : "Create");
    public static String BTN_NEGATIVE_SEARCH_D2D = (isPT() ? "Procurar" : "Search");

    public static String TITLE_SEND_D2D = (isPT() ? "D2D" : "D2D");
    public static String DIALOG_SEND_D2D = (isPT() ? "Deseja receber/enviar arquivos para outro dispositivo usando o WiFi Direct?" : "Do you want to receive/send files to another device using WiFi Direct?");

    public static String TITLE_SEND_SERVER_LOCAL = (isPT() ? "Computador" : "Computer");
    public static String DIALOG_SEND_SERVER_LOCAL = (isPT() ?
            "Deseja se conectar ao seu computador e baixar/enviar arquivos pra ele? " +
                    "Para isso, baixe no seu computador o programa disponível em: goo.gl/XJRyJd, execute, e conecte este dispositivo e o computador na mesma rede WiFi." :
            "Want to connect to your computer and download/send files to it? "+
                    "To do this, download the program available at: goo.gl/XJRyJd, run, and connect this device and the computer to the same WiFi network.");

    public static String TITLE_DISCONNECT_D2D = (isPT() ? "Desconexão" : "Disconnect");
    public static String DIALOG_DISCONNECT_D2D = (isPT() ? "Deseja se desconectar do grupo?" : "Want to disconnect from the group?");
    public static String BTN_POSITIVE_DISCONNECT_D2D = (isPT() ? "Desconectar" : "Disconnect");
    public static String BTN_NEGATIVE_DISCONNECT_D2D = (isPT() ? "Cancelar" : "Cancel");

    public static String BTN_DISCONNECT = (isPT() ? "Desconectar" : "Disconnect");
    public static String BTN_CONNECT = (isPT() ? "Conectar" : "Connect");
    public static String BTN_SEND = (isPT() ? "Enviar" : "Send");

    public static String TITLE_SEND_SERVER_EXTERNAL = (isPT() ? "Google Drive" : "Google Drive");
    public static String DIALOG_SEND_SERVER_EXTERNAL = (isPT() ? "Deseja receber/enviar arquivos para o Google Drive?" : "Want to receive/send files to Google Drive");

    public static String TITLE_SEND_FOLDER = (isPT() ? "Envio" : "Send");
    public static String DIALOG_SEND_FOLDER = (isPT() ? "Deseja enviar todos arquivos da pasta: " + DIRECTORY_SEND_FILE_AUTOMATIC + "?" :
            "Do you want to send all files in the folder: " + DIRECTORY_SEND_FILE_AUTOMATIC + "?");

    public static String TITLE_PING = (isPT() ? "PINGs" : "PINGs");
    public static String DIALOG_PING = (isPT() ? "Deseja testar a rede por 30 segundos?" : "Do you want to test the network for 30 seconds?");

    public static String TITLE_IMPORT_FOLDER = (isPT() ? "Importação" : "Import");
    public static String DIALOG_IMPORT_FOLDER = (isPT() ? "Deseja receber arquivos do seu computador (Pasta ShareFiles/sendAutomatic/)?" :
            "Want to receive files from your computer (Folder ShareFiles/sendAutomatic/)?");

    public static String TITLE_DELETE_FILE = (isPT() ? "Exclusão" : "Exclusion");
    public static String NAME_FILE = (isPT() ? "Nome: " : "Name: ");
    public static String SIZE_FILE = (isPT() ? "Tamanho: " : "Size: ");
    public static String DATE_FILE = (isPT() ? "Data: " : "Data: ");
    public static String DIALOG_DELETE_FILE = (isPT() ? "Deseja excluir o arquivo?" : "Do you want to delete the file?");

    public static String TITLE_CONNECT_SERVER_LOCAL = (isPT() ? "Conexão" : "Connect");
    public static String DIALOG_CONNECT_SERVER_LOCAL = (isPT() ? "Deseja se conectar com " : "Do you want to connect with ");
    public static String BTN_POSITIVE_CONNECT_SERVER_LOCAL = (isPT() ? "Conectar" : "Connect");
    public static String BTN_NEGATIVE_CONNECT_SERVER_LOCAL = (isPT() ? "Recusar" : "Refuse");

    public static String TITLE_SETTINGS_WRITE_LOG = (isPT() ? "Geração de LOGs" : "Generation of LOGs");
    public static String DIALOG_SETTINGS_WRITE_LOG_ENABLE = (isPT() ? "Deseja habilitar a geração de LOGs?" : "Do you want to enable LOG generation?");
    public static String DIALOG_SETTINGS_WRITE_LOG_DISABLE = (isPT() ? "Deseja desabilitar a geração de LOGs?" : "Do you want to disable LOG generation?");

    public static String TITLE_SETTINGS_OPEN_FILE_RECEIVED = (isPT() ? "Abertura automática do arquivo" : "Automatic file opening");
    public static String DIALOG_SETTINGS_OPEN_FILE_RECEIVED_ENABLE = (isPT() ? "Deseja habilitar a abertura automática do arquivo após recebê-lo?" :
            "Do you want to enable automatic file opening after receiving it?");
    public static String DIALOG_SETTINGS_OPEN_FILE_RECEIVED_DISABLE = (isPT() ? "Deseja desabilitar a abertura automática do arquivo após recebê-lo?" :
            "Do you want to disable automatic file opening after receiving it?");

    public static String TITLE_SEARCH_PEERS = (isPT() ? "Pressione voltar para cancelar" : "Press back to cancel");
    public static String DIALOG_SEARCH_PEERS = (isPT() ? "Procurando pontos" : "Searching for points");

    public static String PERMISSION_DENIED = (isPT() ? "Permissão negada!" : "Permission denied!");

    public static String EXCEPTION_OPEN_FOLDER = (isPT() ? "Acesse: " + DIRECTORY_SAVE_FILE : "Go to: " + DIRECTORY_SAVE_FILE);
    public static String EXCEPTION_NETWORK_WIFI_UNAVAILABLE = (isPT() ? "Conexão WiFi indisponível..." : "WiFi connection unavailable...");
    public static String EXCEPTION_DEVICE_NOT_FOUND = (isPT() ? "Nenhum dispositivo encontrado!" : "No devices found!");
    public static String EXCEPTION_SERVER_LOCAL_NOT_FOUND = (isPT() ? "Nenhum computador encontrado!" : "No computer found!");
    public static String EXCEPTION_OPEN_FILE = (isPT() ? "Erro ao abrir o arquivo!" : "Error opening file!");
    public static String EXCEPTION_DELETE_FILE = (isPT() ? "Erro ao deletar arquivo!" : "Error deleting file!");
    public static String EXCEPTION_SAVE_FILE = (isPT() ? "Erro ao gravar o arquivo!" : "Error writing file!");
    public static String EXCEPTION_CLOSE_FILE = (isPT() ? "Erro ao fechar arquivo!" : "Error closing file!");
    public static String EXCEPTION_WRITE_FILE = (isPT() ? "Erro ao escrever no arquivo!" : "Error writing to file!");
    public static String EXCEPTION_READ_FILE = (isPT() ? "Erro ao ler arquivo!" : "Error reading file!");
    public static String EXCEPTION_HANDLE_FILE = (isPT() ? "Erro ao manipular o arquivo!" : "Error while handling the file!");
    public static String EXCEPTION_THREAD = (isPT() ? "Erro ao executar Thread!" : "Thread Error Running!");
    public static String EXCEPTION_SEND_FILE_DRIVE = (isPT() ? "Não é possível enviar arquivos do drive!" : "Can not send drive files!");
    public static String EXCEPTION_OPEN_DRIVE = (isPT() ? "Erro ao tentar abrir o drive!" : "Error trying to open the drive!");
    public static String EXCEPTION_CONNECTED_DRIVE = (isPT() ? "Erro ao tentar conectar ao drive!" : "Error trying to connect to the drive!");

    public static String EXCEPTION_GO_SOCKET = (isPT() ? "Go -> Erro ao criar socket ou enviar arquivo" : "Go -> Error creating socket or sending file");
    public static String EXCEPTION_CLIENT_SOCKET_OPEN = (isPT() ? "Client -> Erro ao abrir o socket" : "Client -> Error opening socket");
    public static String EXCEPTION_CLIENT_SOCKET_CLOSE = (isPT() ? "Client -> Erro ao fechar o socket" : "Client -> Error closing socket");
    public static String EXCEPTION_SEND_CLIENT_SOCKET = (isPT() ? "Erro ao enviar: " : "Error sending:");
    public static String EXCEPTION_RECEIVED_CLIENT_SOCKET = (isPT() ? "Erro ao receber: " : "Error receiving:");

    public static String FOLDER_EMPTY = (isPT() ? "Pasta vazia..." : "Empty folder...");
    public static String FOLDER_OPEN_GOOGLE_DRIVE = (isPT() ? "Abrindo o Google Drive..." : "Opening Google Drive...");

    public static String CLOSE_BUFFER_FILE = (isPT() ? "Fechando o buffer do arquivo" : "Closing the file buffer");

    public static String SEARCH_SERVER_LOCAL = (isPT() ? "Buscando servidor local..." : "Searching local server...");

    public static String ON_SUCCESS_DISCOVER_PEERS = (isPT() ? "Busca iniciada!" : "Search started!");

    public static String ON_FAILURE_DISCONNECT_GROUP = (isPT() ? "Falha ao desconectar!" : "Failed to disconnect!");
    public static String ON_FAILURE_CONNECT_GROUP = (isPT() ? "Falha de conexão. Tente novamente!" : "Connection failure. Try again!");
    public static String ON_FAILURE_CREATE_GROUP = (isPT() ? "Falha de conexão. Tente novamente! Se o erro persistir, reinicie o dispositivo!" :
            "Connection failure. Try again! If the error persists, restart the device!");
    public static String ON_FAILURE_DISCOVER_PEERS = (isPT() ? "Falha na busca!" : "Search failed!");

    public static String ON_CHANNEL_DISCONNECTED = (isPT() ? "Canal perdido. Tentando novamente." : "Lost Channel. Trying again.");
    public static String ON_CHANNEL_DISCONNECTED_NO_MANAGER = (isPT() ? "Grave! O canal provavelmente está perdido permanentemente. Experimente Desativar/Reativar o WiFi." : "Serious! The channel is probably permanently lost. Try Disabling/Reactivating WiFi.");

    public static String PROGRESS_DIALOG_CONNECT_GROUP_TITLE = (isPT() ? "Pressione voltar para cancelar" : "Press back to cancel");
    public static String PROGRESS_DIALOG_CONNECT_GROUP_MESSAGE = (isPT() ? "Conectando com " : "Connecting with ");

    public static String DEVICE_STATUS_AVAILABLE = (isPT() ? "DISPONÍVEL" : "AVAILABLE");
    public static String DEVICE_STATUS_INVITED = (isPT() ? "CONVIDANDO" : "INVITED");
    public static String DEVICE_STATUS_CONNECTED = (isPT() ? "CONECTADO" : "CONNECTED");
    public static String DEVICE_STATUS_FAILED = (isPT() ? "FALHOU" : "FAILED");
    public static String DEVICE_STATUS_UNAVAILABLE = (isPT() ? "INDISPONÍVEL" : "UNAVAILABLE");
    public static String DEVICE_STATUS_UNKNOWN = (isPT() ? "DESCONHECIDO" : "UNKNOWN");

}