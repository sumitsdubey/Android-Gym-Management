package com.sumit.gymmanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;
import com.sumit.gymmanagement.api.RetrofitClient;
import com.sumit.gymmanagement.model.OTPResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginEmailActivity extends AppCompatActivity {


    TextInputEditText et_email;
    AppCompatButton btn_getotp;
    String email;
    TextView tv_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);

        SharedPreferences sp = getSharedPreferences("user_data", MODE_PRIVATE);
        String token = sp.getString("token", "");

        if (!token.equals("")){
            Intent i = new Intent(LoginEmailActivity.this, HomeActivity.class);
            startActivity(i);
            finish();
        }
        et_email = findViewById(R.id.et_email);
        btn_getotp = findViewById(R.id.btn_getotp);
        tv_login = findViewById(R.id.tv_login);

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });

        ProgressDialog dialog = new ProgressDialog(LoginEmailActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle("Loading..");

        btn_getotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = et_email.getText().toString().trim();

                if (email.equals("")){
                    et_email.setError("Please Enter Email..");
                    et_email.requestFocus();
                    return;
                }

                dialog.show();
                Call<OTPResponseModel> call = RetrofitClient.getInstance().getApi().sendotp(email);
                call.enqueue(new Callback<OTPResponseModel>() {
                    @Override
                    public void onResponse(Call<OTPResponseModel> call, Response<OTPResponseModel> response) {
                        if (response.isSuccessful()){
                            OTPResponseModel responseModel = response.body();
                            if (responseModel.isStatus()){
                                dialog.dismiss();
                                Toast.makeText(LoginEmailActivity.this, ""+responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(LoginEmailActivity.this, OTPActivity.class);
                                i.putExtra("email", email);
                                startActivity(i);

                            }else{
                                dialog.dismiss();
                                Toast.makeText(LoginEmailActivity.this, ""+responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                                et_email.setError(responseModel.getMessage()+"  Please Login");
                                et_email.requestFocus();
                            }
                        }else{
                            dialog.dismiss();
                            Toast.makeText(LoginEmailActivity.this, "Server Down", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<OTPResponseModel> call, Throwable t) {
                        dialog.dismiss();
                        Intent i = new Intent(LoginEmailActivity.this, OTPActivity.class);
                        i.putExtra("email", email);
                        startActivity(i);
                        Toast.makeText(LoginEmailActivity.this, "Otp Send Successfully        "+t.getMessage() , Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

}