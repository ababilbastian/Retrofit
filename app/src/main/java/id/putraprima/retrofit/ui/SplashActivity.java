package id.putraprima.retrofit.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.Timer;
import java.util.TimerTask;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.AppVersion;
import id.putraprima.retrofit.api.models.Session;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    TextView lblAppName, lblAppTittle, lblAppVersion;
    public Session data;
    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setupLayout();
        data = new Session(this);
        if (checkInternetConnection()) {
            checkAppVersion();
        }
        setAppInfo();
    }

    private void setupLayout() {
        lblAppName = findViewById(R.id.lblAppName);
        lblAppTittle = findViewById(R.id.lblAppTittle);
        lblAppVersion = findViewById(R.id.lblAppVersion);
        //Sembunyikan lblAppName dan lblAppVersion pada saat awal dibuka
        lblAppVersion.setVisibility(View.INVISIBLE);
        lblAppName.setVisibility(View.INVISIBLE);
    }

    private boolean checkInternetConnection() {
        boolean status = false;
        //TODO : 1. Implementasikan proses pengecekan koneksi internet, berikan informasi ke user jika tidak terdapat koneksi internet
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if(activeInfo != null && activeInfo.isConnected()){
            status = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE || activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            Toast.makeText(this, "Koneksi Berhasil", Toast.LENGTH_SHORT).show();
        }else{
            final AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
            builder.setMessage("Tidak ada internet");
            builder.setCancelable(true);
            builder.setPositiveButton("Keluar!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        return status;
    }


    private void setAppInfo() {
        //TODO : 5. Implementasikan proses setting app info, app info pada fungsi ini diambil dari shared preferences
        //lblAppVersion dan lblAppName dimunculkan kembali dengan data dari shared preferences
        //lblAppVersion.setVisibility(View.INVISIBLE);
        //lblAppName.setVisibility(View.INVISIBLE);
        if(data.isKeepData()){
            lblAppName.setVisibility(View.VISIBLE);
            lblAppVersion.setVisibility(View.VISIBLE);
            lblAppName.setText(data.getDataApp());
            lblAppVersion.setText(data.getDataVersion());
        }
    }

    private void checkAppVersion() {
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<AppVersion> call = service.getAppVersion();
        call.enqueue(new Callback<AppVersion>() {
            @Override
            public void onResponse(Call<AppVersion> call, Response<AppVersion> response) {
                //Todo : 2. Implementasikan Proses Simpan Data Yang didapat dari Server ke SharedPreference
                //Todo : 3. Implementasikan Proses Pindah Ke MainActivity Jika Proses getAppVersion() sukses
                if(response.body() != null){
                    data.setDataVersion(response.body().getVersion());
                    data.setDataApp(response.body().getApp());
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    },2000);
                }
            }

            @Override
            public void onFailure(Call<AppVersion> call, Throwable t) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                builder.setMessage("Koneksi ke Server Gagal !!");
                builder.setCancelable(true);
                builder.setPositiveButton("Keluar!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                //Todo : 4. Implementasikan Cara Notifikasi Ke user jika terjadi kegagalan koneksi ke server silahkan googling cara yang lain selain menggunakan TOAST
            }
        });
    }
}
