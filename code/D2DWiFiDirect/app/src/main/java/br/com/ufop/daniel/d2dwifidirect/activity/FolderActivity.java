package br.com.ufop.daniel.d2dwifidirect.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import java.io.File;

import br.com.ufop.daniel.d2dwifidirect.R;
import br.com.ufop.daniel.d2dwifidirect.adapter.FolderAdapter;
import br.com.ufop.daniel.d2dwifidirect.dao.FileOrder;

import static br.com.ufop.daniel.d2dwifidirect.util.Constants.DIRECTORY_SAVE_FILE;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.EXTENSION_FILE_IMAGE;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.EXTENSION_FILE_PDF;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.EXTENSION_FILE_VIDEO;

/**
 * Created by daniel on 10/11/17.
 */

public class FolderActivity extends Activity {

    GridView gridView;
    File[] listFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        /* Seta os arquivos da pasta na lista */
        File dir = new File(DIRECTORY_SAVE_FILE);
        dir.mkdirs();

        /* Seleciona apenas imagens, vídeos e documentos em formato PDF */
        listFiles = dir.listFiles();
        File[] newListFiles = new File[listFiles.length];
        int j = 0;
        for (int i = 0; i < listFiles.length; i++) {
            if (listFiles[i].getName().contains(EXTENSION_FILE_IMAGE) ||
                    listFiles[i].getName().contains(EXTENSION_FILE_VIDEO) ||
                    listFiles[i].getName().contains(EXTENSION_FILE_PDF)) {
                if (listFiles[i].length() > 0) {
                    newListFiles[j++] = listFiles[i];
                } else {
                    listFiles[i].delete();
                }
            }
        }
        listFiles = newListFiles;

        if (listFiles.length == 0) {
            TextView tv = (TextView) findViewById(R.id.folder_info);
            tv.setVisibility(View.VISIBLE);
        } else {
            TextView tv = (TextView) findViewById(R.id.folder_info);
            tv.setVisibility(View.GONE);

            /* Ordena por data - decrescente */
            FileOrder.quickSortByLastModified(listFiles, 0, listFiles.length - 1);

            /* Pega o gridview */
            gridView = (GridView) findViewById(R.id.gridView);

            /* Seta o adapter */
            gridView.setAdapter(new FolderAdapter(this, listFiles));
        }
    }
}
