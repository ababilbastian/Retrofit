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
import id.putraprima.retrofit.api.models.EditProfileReq;
import id.putraprima.retrofit.api.models.EditProfileRes;
import id.putraprima.retrofit.api.models.Session;
import id.putraprima.retrofit.api.services.ApiInterface;
import id.putraprima.retrofit.ui.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {
    private Session session;
    private EditText emailInput,nameInput;
    private String email,name,token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        session = new Session(this);
        emailInput = findViewById(R.id.newEmailInput);
        nameInput = findViewById(R.id.newNameInput);
        token = session.getTokenType() + " " + session.getToken();
    }

    public void handleSubmitProf(View view){
        email = emailInput.getText().toString();
        name = nameInput.getText().toString();
        boolean status = email.equals("") && name.equals("");
        if(status)
            Toast.makeText(this, "Lengkapi Data Terlebih Dahulu!!!", Toast.LENGTH_SHORT).show();
        else
            editprofile();
    }

    public void editprofile(){

            ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
            System.out.println("email : "+email);
            System.out.println("name : "+name);
            Call<EditProfileRes> call = service.editProfile(token,new EditProfileReq(email,name));
            call.enqueue(new Callback<EditProfileRes>() {
                @Override
                public void onResponse(Call<EditProfileRes> call, Response<EditProfileRes> response) {
                    if(response.body() != null){
                        final AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                        builder.setMessage("Edit Profile Berhasil !!");
                        builder.setCancelable(true);
                        builder.setPositiveButton("Kembali ke halaman profile!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                                startActivity(intent);
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                }

                @Override
                public void onFailure(Call<EditProfileRes> call, Throwable t) {
                    Toast.makeText(EditProfileActivity.this, "Data gagal", Toast.LENGTH_SHORT).show();
                }
            });

    }
}
