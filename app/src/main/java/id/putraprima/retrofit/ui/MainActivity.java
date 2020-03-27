package id.putraprima.retrofit.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import id.putraprima.retrofit.ProfileActivity;
import id.putraprima.retrofit.R;
import id.putraprima.retrofit.RegisterActivity;
import id.putraprima.retrofit.SettingActivity;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.LoginRequest;
import id.putraprima.retrofit.api.models.LoginResponse;
import id.putraprima.retrofit.api.models.Session;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private EditText usernameInput,passwordInput;
    private TextView appTxt,versionTxt;
    private Session session;
    private String username,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameInput = findViewById(R.id.edtEmail);
        passwordInput = findViewById(R.id.edtPassword);
        appTxt = findViewById(R.id.mainTxtAppName);
        versionTxt = findViewById(R.id.mainTxtAppVersion);
        session = new Session(this);
        appTxt.setText(session.getDataApp());
        versionTxt.setText(session.getDataVersion());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.settingApp){
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void handleLogin(View view){
        username = usernameInput.getText().toString();
        password = passwordInput.getText().toString();
        login();
    }

   public void login(){
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<LoginResponse> call = service.loginRequest(new LoginRequest(username,password));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.body() != null){
                    Toast.makeText(MainActivity.this, "Koneksi Berhasil", Toast.LENGTH_SHORT).show();
                    session.setToken(response.body().token);
                    session.setTokenType(response.body().token_type);
                    if(response.body().token != null || response.body().token != ""){
                        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(MainActivity.this, "Token Kosong", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Gagal Konek", Toast.LENGTH_SHORT).show();
            }
        });
   }

   public void handleRegister(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
   }
    
}
