package com.sumit.gymmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.sumit.gymmanagement.api.RetrofitClient;
import com.sumit.gymmanagement.model.AddDataResponseModel;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMemberActivity extends AppCompatActivity {

    AppCompatButton submitMember;
    TextInputEditText et_reg_name, et_reg_mobile, et_reg_address, et_reg_date, et_reg_fee;

    int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        submitMember = findViewById(R.id.submitMember);

        et_reg_name = findViewById(R.id.et_reg_name);
        et_reg_mobile = findViewById(R.id.et_reg_mobile);
        et_reg_address = findViewById(R.id.et_reg_address);
        et_reg_date = findViewById(R.id.et_reg_date);
        et_reg_fee = findViewById(R.id.et_reg_fee);

        SharedPreferences sp = getSharedPreferences("user_data", MODE_PRIVATE);
        String createdby = sp.getString("id","");
        String Gym_name = sp.getString("gym_name", "");

        Calendar calendar = Calendar.getInstance();

        et_reg_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddMemberActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        et_reg_date.setText(i2+"-"+ i1+"-"+i);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        submitMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String member_name = et_reg_name.getText().toString().trim();
                String member_mobile = et_reg_mobile.getText().toString().trim();
                String member_address = et_reg_address.getText().toString().trim();
                String member_joindate = et_reg_date.getText().toString().trim();
                String member_createdby = createdby;
                String gym_name = Gym_name;
                String joining_fee = et_reg_fee.getText().toString().trim();

                if (member_name.equals("")){
                    et_reg_name.setError("Required");
                    et_reg_name.requestFocus();
                    return;
                }
                if (member_name.equals("")){
                    et_reg_mobile.setError("Required");
                    et_reg_mobile.requestFocus();
                    return;
                }
                if (member_name.equals("")){
                    et_reg_address.setError("Required");
                    et_reg_address.requestFocus();
                    return;
                }
                if (member_name.equals("")){
                    et_reg_date.setError("Required");
                    et_reg_date.requestFocus();
                    return;
                }
                if (member_name.equals("")){
                    et_reg_fee.setError("Required");
                    et_reg_fee.requestFocus();
                    return;
                }

                ProgressDialog dialog = new ProgressDialog(AddMemberActivity.this);
                dialog.setTitle("Submiting Member Data");
                dialog.setCancelable(false);
                dialog.show();

                Call<AddDataResponseModel> call = RetrofitClient.getInstance().getApi().addmember(member_name, member_mobile, member_address, member_joindate, member_createdby, gym_name, joining_fee);
                call.enqueue(new Callback<AddDataResponseModel>() {
                    @Override
                    public void onResponse(Call<AddDataResponseModel> call, Response<AddDataResponseModel> response) {
                        if (response.isSuccessful()){
                            AddDataResponseModel addDataResponseModel = response.body();
                            if (addDataResponseModel.isStatus()){
                                Toast.makeText(AddMemberActivity.this, "Member Registered Successfully", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(i);
                                finish();
                            }else{
                                Toast.makeText(AddMemberActivity.this, ""+addDataResponseModel.message, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }else{
                            Toast.makeText(AddMemberActivity.this, "Server Not Found      "+response.message(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<AddDataResponseModel> call, Throwable t) {
                        Toast.makeText(AddMemberActivity.this, "Network Connection Error     "+t.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });
    }
}