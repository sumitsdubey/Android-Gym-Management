package com.sumit.gymmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.sumit.gymmanagement.api.RetrofitClient;
import com.sumit.gymmanagement.model.RegisterResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    TextInputEditText et_gymname, et_address, et_mobile;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_gymname = findViewById(R.id.et_gymname);
        et_address = findViewById(R.id.et_address);
        et_mobile = findViewById(R.id.et_mobile);
        
        Intent i = getIntent();
        email = i.getStringExtra("email");


    }

    public void submitBasicdtls(View view) {
        ProgressDialog dialog = new ProgressDialog(RegisterActivity.this);
        dialog.setTitle("Verifying Data Please Wait");
        dialog.setCancelable(false);


        dialog.show();

        String gym_name = et_gymname.getText().toString().trim();
        String address = et_address.getText().toString().trim();
        String mobile = et_mobile.getText().toString().trim();

        if (gym_name.equals("")){
            et_gymname.setError("Required");
            et_gymname.requestFocus();
            return;
        }
        if (address.equals("")){
            et_address.setError("Required");
            et_address.requestFocus();
            return;
        }
        if (mobile.equals("")){
            et_mobile.setError("Required");
            et_mobile.requestFocus();
            return;
        }
        Call<RegisterResponseModel> call = RetrofitClient.getInstance().getApi().registergym(email, gym_name, address, mobile);
        call.enqueue(new Callback<RegisterResponseModel>() {
            @Override
            public void onResponse(Call<RegisterResponseModel> call, Response<RegisterResponseModel> response) {
                if (response.isSuccessful()){
                    RegisterResponseModel registerResponseModel = response.body();
                    if (registerResponseModel.isStatus()){
                        dialog.dismiss();
                        Intent i = new Intent(RegisterActivity.this, HomeActivity.class);
                        startActivity(i);
                        finish();
                    }else {
                        dialog.dismiss();
                        Toast.makeText(RegisterActivity.this, registerResponseModel.getMessage()+"  Please Login", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                    
                }else{
                    dialog.dismiss();
                    Toast.makeText(RegisterActivity.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponseModel> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(RegisterActivity.this, "Please Login", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}