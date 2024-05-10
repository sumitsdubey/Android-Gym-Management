package com.sumit.gymmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.sumit.gymmanagement.api.RetrofitClient;
import com.sumit.gymmanagement.model.OTPResponseModel;
import com.sumit.gymmanagement.model.Otpdata;
import com.sumit.gymmanagement.model.VerifyOtpModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPActivity extends AppCompatActivity {
    TextView tv_error, tv_resend;
    String email;
    ProgressDialog dialog;
    TextInputEditText et_otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpactivity);

        et_otp = findViewById(R.id.et_otp);
        tv_error = findViewById(R.id.tv_error);
        tv_resend = findViewById(R.id.tv_resend);

        Intent data = getIntent();
        email = data.getStringExtra("email");

        dialog = new ProgressDialog(OTPActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle("Verifying OTP..");
    }

    public void submitOtp(View view) {
        dialog.show();

        String otp = et_otp.getText().toString().trim();

        Call<VerifyOtpModel> call = RetrofitClient.getInstance().getApi().verifyotp(email);
        call.enqueue(new Callback<VerifyOtpModel>() {
            @Override
            public void onResponse(Call<VerifyOtpModel> call, Response<VerifyOtpModel> response) {
                if (response.isSuccessful()){
                    VerifyOtpModel otpModel = response.body();
                    if (otpModel.isStatus()){
                        ArrayList<Otpdata> otpdata = otpModel.getData();
                        String rotp = otpdata.get(0).getOtp();
                        if (otp.equals(rotp)){
                            dialog.dismiss();
                            Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                            i.putExtra("email", email);
                            startActivity(i);
                            finish();
                        }else{
                            dialog.dismiss();
                            et_otp.setError("Incorrect OTP");
                            et_otp.requestFocus();
                            Toast.makeText(OTPActivity.this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        dialog.dismiss();
                        Toast.makeText(OTPActivity.this, "Otp Verification Failed", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    dialog.dismiss();
                    Toast.makeText(OTPActivity.this, "Otp Verification Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<VerifyOtpModel> call, Throwable t) {
                dialog.dismiss();
                tv_error.setText(t.getMessage());
                Toast.makeText(OTPActivity.this, "Internet Connection Error.... "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void resendEmail(View view) {
        dialog.show();
        Call<OTPResponseModel> call = RetrofitClient.getInstance().getApi().sendotp(email);
        call.enqueue(new Callback<OTPResponseModel>() {
            @Override
            public void onResponse(Call<OTPResponseModel> call, Response<OTPResponseModel> response) {
                if (response.isSuccessful()){
                    OTPResponseModel responseModel = response.body();
                    if (responseModel.isStatus()){
                        dialog.dismiss();
                        Toast.makeText(OTPActivity.this, ""+responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(OTPActivity.this, OTPActivity.class);
                        i.putExtra("email", email);
                        startActivity(i);

                    }else{
                        dialog.dismiss();
                        Toast.makeText(OTPActivity.this, ""+responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    dialog.dismiss();
                    Toast.makeText(OTPActivity.this, "Server Down", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<OTPResponseModel> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(OTPActivity.this, "Otp Send Successfully" , Toast.LENGTH_SHORT).show();
            }
        });
    }
}