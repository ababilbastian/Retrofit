package id.putraprima.retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.Data;
import id.putraprima.retrofit.api.models.Profile;
import id.putraprima.retrofit.api.models.Session;
import id.putraprima.retrofit.api.services.ApiInterface;
import id.putraprima.retrofit.ui.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    private String tokenData,tokenTypeData,name,email;
    private TextView emailText,nameText;
    private Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        session = new Session(this);
        emailText = findViewById(R.id.myEmail);
        nameText = findViewById(R.id.myName);
        tokenData = session.getToken();
        tokenTypeData = session.getTokenType();
        showData();
    }
    
    public void showData(){
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<Data<Profile>> call = service.showProfile(tokenTypeData +" "+tokenData);
        call.enqueue(new Callback<Data<Profile>>() {
            @Override
            public void onResponse(Call<Data<Profile>> call, Response<Data<Profile>> response) {
                if(response.body() !=null){
                    Toast.makeText(ProfileActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                    name = response.body().data.name;
                    email = response.body().data.email;
                    emailText.setText(email);
                    nameText.setText(name);
                }
            }

            @Override
            public void onFailure(Call<Data<Profile>> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void handleEditPass(View view){
        Intent intent = new Intent(this,EditPassActivity.class);
        startActivity(intent);
    }

    public void handleEditProfile(View view){
        Intent intent = new Intent(this,EditProfileActivity.class);
        startActivity(intent);
    }

    public void handleLogout(View view){
        Intent intent = new Intent(this, MainActivity.class);
        session.setToken("");
        session.setTokenType("");
        startActivity(intent);
    }
}
