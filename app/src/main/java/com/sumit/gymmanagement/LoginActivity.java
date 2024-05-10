package com.sumit.gymmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.sumit.gymmanagement.api.RetrofitClient;
import com.sumit.gymmanagement.model.UserData;
import com.sumit.gymmanagement.model.UserLoginResponseModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText et_login_email, et_login_password;
    AppCompatButton bt_login;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_login_email = findViewById(R.id.et_login_email);
        et_login_password = findViewById(R.id.et_login_password);

        bt_login = findViewById(R.id.bt_login);

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
                dialog.setTitle("Verifying User Please Wait");
                dialog.setCancelable(false);


                String email = et_login_email.getText().toString().trim();
                String password = et_login_password.getText().toString().trim();

                if (email.equals("")){
                    et_login_email.setError("Required");
                    et_login_email.requestFocus();
                    return;
                }
                if (password.equals("")){
                    et_login_password.setError("Required");
                    et_login_password.requestFocus();
                    return;
                }
                dialog.show();
                Call<UserLoginResponseModel> call = RetrofitClient.getInstance().getApi().userlogin(email,password);
                call.enqueue(new Callback<UserLoginResponseModel>() {
                    @Override
                    public void onResponse(Call<UserLoginResponseModel> call, Response<UserLoginResponseModel> response) {
                        if (response.isSuccessful()){
                            UserLoginResponseModel responseModel = response.body();
                            if (responseModel.isStatus()){
                                ArrayList<UserData> users = responseModel.getData();
                                Toast.makeText(LoginActivity.this, ""+responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                                SharedPreferences sp = getSharedPreferences("user_data", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("id", users.get(0).getId());
                                editor.putString("gym_name", users.get(0).getGym_name());
                                editor.putString("owner_name", users.get(0).getOwner_name());
                                editor.putString("email", users.get(0).getEmail());
                                editor.putString("mobile", users.get(0).getMobile());
                                editor.putString("address", users.get(0).getAddress());
                                editor.putString("date", users.get(0).getDate());
                                editor.putString("profile_image", users.get(0).getProfile_image());
                                editor.putString("token", users.get(0).getToken());
                                editor.commit();
                                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(i);
                                finish();
                                dialog.dismiss();
                            }else{
                                Toast.makeText(LoginActivity.this, ""+responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }

                        }else{
                            Toast.makeText(LoginActivity.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserLoginResponseModel> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    public void RegisterActivity(View view) {
        Intent i = new Intent(getApplicationContext(), LoginEmailActivity.class);
        startActivity(i);
    }
}