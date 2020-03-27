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
import id.putraprima.retrofit.api.models.EditPassReq;
import id.putraprima.retrofit.api.models.EditPassRess;
import id.putraprima.retrofit.api.models.Session;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPassActivity extends AppCompatActivity {
    private EditText passInput,cpassInput;
    private Session session;
    private String pass,cpass,token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pass);
        passInput = findViewById(R.id.newpasswordInput);
        cpassInput = findViewById(R.id.newcpasswordInput);
        session = new Session(this);
        token = session.getTokenType() + " " + session.getToken();
    }

    public void handleSubmitPass(View view){
        pass = passInput.getText().toString();
        cpass = cpassInput.getText().toString();
        boolean status = pass.equals("") && cpass.equals("");
        boolean confirm = pass.equals(cpass);
        boolean dataLength = pass.length() < 8 && cpass.length() < 8;
        if (status){
            Toast.makeText(this, "Lengkapi data terlebih dahulu!!", Toast.LENGTH_SHORT).show();
            if (confirm) Toast.makeText(this, "Password tidak sama!!", Toast.LENGTH_SHORT).show();
            if (dataLength) Toast.makeText(this, "Jumlah character minimal 8", Toast.LENGTH_SHORT).show();
        }else{
            editPass();
        }
    }

    public void editPass(){
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<EditPassRess> call = service.editPassword(token,new EditPassReq(pass,cpass));
        call.enqueue(new Callback<EditPassRess>() {
            @Override
            public void onResponse(Call<EditPassRess> call, Response<EditPassRess> response) {
                if(response.body() != null){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(EditPassActivity.this);
                    builder.setMessage("Edit Password Berhasil !!");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Kembali ke halaman profile!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(EditPassActivity.this, ProfileActivity.class);
                            startActivity(intent);
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(Call<EditPassRess> call, Throwable t) {
                Toast.makeText(EditPassActivity.this, "Data gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
