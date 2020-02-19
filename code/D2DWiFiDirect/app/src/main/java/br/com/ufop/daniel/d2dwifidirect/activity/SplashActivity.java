package br.com.ufop.daniel.d2dwifidirect.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import br.com.ufop.daniel.d2dwifidirect.R;

import static br.com.ufop.daniel.d2dwifidirect.util.Constants.CATEGORY_LOG;

public class SplashActivity extends Activity {

    private ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mProgress = (ProgressBar) findViewById(R.id.barrinha);

        // Inicia longa operação em um segmento de plano de fundo
        new Thread(new Runnable() {
            public void run() {
                // Remove o nome da aplicação
                progress();
                initApp();
                finish();
            }
        }).start();
    }

    private void progress() {
        for (int progress = 0; progress < 100; progress++) {
            try {
                Thread.sleep(30);
                mProgress.setProgress(progress);
            } catch (InterruptedException e) {
                Log.e(CATEGORY_LOG, e.getMessage());
            }
        }
    }

    private void initApp() {
        Intent intent = new Intent(SplashActivity.this, MenuActivity.class);
        startActivity(intent);
    }
}
