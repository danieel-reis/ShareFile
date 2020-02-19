package br.com.ufop.daniel.d2dwifidirect.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.ufop.daniel.d2dwifidirect.R;

import static br.com.ufop.daniel.d2dwifidirect.util.Constants.EXTENSION_FILE_IMAGE;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.EXTENSION_FILE_PDF;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.EXTENSION_FILE_VIDEO;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.TYPE_FILE_IMAGE_FULL;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.TYPE_FILE_PDF_FULL;
import static br.com.ufop.daniel.d2dwifidirect.util.Constants.TYPE_FILE_VIDEO_FULL;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.DATE_FILE;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.DIALOG_DELETE_FILE;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.EXCEPTION_DELETE_FILE;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.EXCEPTION_OPEN_FILE;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.NAME_FILE;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.NO;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.SIZE_FILE;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.TITLE_DELETE_FILE;
import static br.com.ufop.daniel.d2dwifidirect.util.Dialog.YES;

/**
 * Created by daniel on 15/11/17.
 */

public class FolderAdapter extends BaseAdapter {

    private File[] files;
    private Context context;

    public FolderAdapter(Context context, File[] files) {
        this.files = files;
        this.context = context;
    }

    @Override
    public int getCount() {
        return files.length;
    }

    @Override
    public Object getItem(int position) {
        return files[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(context);
        File imgFile = (File) getItem(position);

        if (convertView == null) {
            if (imgFile != null && imgFile.exists()) {
                /* Carrega e configura o Bitmap */
                Bitmap bitmap = null;
                try {
                    if (imgFile.getName().contains(EXTENSION_FILE_IMAGE)) {
                        bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    } else if (imgFile.getName().contains(EXTENSION_FILE_VIDEO)) {
                        bitmap = ThumbnailUtils.createVideoThumbnail(imgFile.getAbsolutePath(), 0);
                    } else if (imgFile.getName().contains(EXTENSION_FILE_PDF)) {
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_document_pdf);
                    }
                } catch (Exception e) {
                    if (imgFile.getName().contains(EXTENSION_FILE_IMAGE)) {
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_image);
                    } else if (imgFile.getName().contains(EXTENSION_FILE_VIDEO)) {
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_video);
                    }
                }
                imageView.setImageBitmap(getRoundedShape(bitmap));
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

                DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                imageView.setLayoutParams(new GridView.LayoutParams(displayMetrics.widthPixels / 3, displayMetrics.widthPixels / 3));
            }
        } else
            imageView = (ImageView) convertView;

        imageView.setAdjustViewBounds(true); /* Ajusta a imagem de acordo com o tamanho da coluna */

        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /* Pega o item clicado */
                final File itemValue = (File) getItem(position);

                /* Mostra o arquivo recebido */
                try {
                    String typeFile = "ERROR";
                    String nameFile = itemValue.getName();
                    if (nameFile.contains(EXTENSION_FILE_VIDEO)) {
                        typeFile = TYPE_FILE_VIDEO_FULL;
                    } else if (nameFile.contains(EXTENSION_FILE_IMAGE)) {
                        typeFile = TYPE_FILE_IMAGE_FULL;
                    } else if (nameFile.contains(EXTENSION_FILE_PDF)) {
                        typeFile = TYPE_FILE_PDF_FULL;
                    }
                    if (!typeFile.equals("ERROR")) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.parse("file://" + itemValue.getAbsolutePath()), typeFile);
                        context.startActivity(intent);

                    } else {
                        /* Cria alerta */
                        Toast.makeText(context, EXCEPTION_OPEN_FILE, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    /* Cria alerta */
                    Toast.makeText(context, EXCEPTION_OPEN_FILE, Toast.LENGTH_SHORT).show();
                }
            }
        });

        imageView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                /* Pega o item clicado */
                final File itemValue = (File) getItem(position);

                /* Cria alert dialog perguntando a forma como quer se conectar */
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(TITLE_DELETE_FILE);

                String message = NAME_FILE + itemValue.getName() + "\n";
                message += SIZE_FILE + (itemValue.length() / 1000) + " kb\n";
                message += DATE_FILE + getDate(itemValue.lastModified()) + "\n";
                message += "\n" + DIALOG_DELETE_FILE;
                builder.setMessage(message);

                builder.setPositiveButton(YES, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteFile(itemValue);
                    }
                });

                builder.setNegativeButton(NO, null);

                AlertDialog alerta = builder.create(); /* Cria o AlertDialog */
                alerta.show(); /* Exibe */
                return true;
            }
        });

        return imageView;
    }

    private String getDate(long timeStamp) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "";
        }
    }

    private void deleteFile(File file) {
        try {
            file.delete();
            ((Activity) context).finish();
        } catch (Exception e) {
            Toast.makeText(context, EXCEPTION_DELETE_FILE, Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        /* Fonte: https://stackoverflow.com/questions/3035692/how-to-convert-a-drawable-to-a-bitmap */
        int targetWidth = 100;
        int targetHeight = 100;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(targetBitmap);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), new Paint(Paint.FILTER_BITMAP_FLAG));
        return targetBitmap;
    }
}