package id.putraprima.retrofit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.EmailValidator;
import id.putraprima.retrofit.api.models.RegisterRequest;
import id.putraprima.retrofit.api.models.RegisterResponse;
import id.putraprima.retrofit.api.services.ApiInterface;
import id.putraprima.retrofit.ui.MainActivity;
import id.putraprima.retrofit.ui.SplashActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText usernameInput,emailInput,passwordInput,cPasswordInput;
    private String name,email,password,password_confirmation;
    private RegisterRequest regReq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        usernameInput = findViewById(R.id.usernameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        cPasswordInput = findViewById(R.id.cPasswordInput);
    }

    public void handleRegisterData(View view) {
        name = usernameInput.getText().toString();
        email = emailInput.getText().toString();
        password = passwordInput.getText().toString();
        password_confirmation = cPasswordInput.getText().toString();
        regReq = new RegisterRequest(name,email,password,password_confirmation);
        if (!name.equals("") && !email.equals("") && !password.equals("") && !password_confirmation.equals("")) {
            if (password.equals(password_confirmation)) {
                if(!EmailValidator.validate(email)){
                    Toast.makeText(this, "Masukkan Email dengan benar", Toast.LENGTH_SHORT).show();
                }
                 else   register();
            } else Toast.makeText(this, "Password tidak sama", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, "Lengkapi data Terlebih dahulu!!", Toast.LENGTH_SHORT).show();
    }

    public void register(){
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<RegisterResponse> call = service.tryRegister(regReq);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if(response.body() != null){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("Register Berhasil !!");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Kembali ke login page!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Gagal konek", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
}
    
    
    
    

