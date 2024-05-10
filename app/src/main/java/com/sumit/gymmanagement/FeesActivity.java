package com.sumit.gymmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.sumit.gymmanagement.api.RetrofitClient;
import com.sumit.gymmanagement.model.AddDataResponseModel;
import com.sumit.gymmanagement.model.MembersModel;
import com.sumit.gymmanagement.model.GetMembersModelResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeesActivity extends AppCompatActivity {
    private TextInputEditText et_reg_number, et_reg_name, et_reg_mobile, et_reg_amount;
    private AppCompatButton submitbtn, get_dtl;

    private String created_by, member_name, member_mobile,month, txn_type, amount,id;
    private Integer request_code = 1;

    private Spinner spn_month, spn_type;

    private String[] arrmonth = {"January","February","March","April","May","June","July","August","September","October","November","December"};
    private String[] arrtype = {"Cash","Online"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fees);

        et_reg_number = findViewById(R.id.et_reg_number);
        et_reg_name = findViewById(R.id.et_reg_name);
        et_reg_mobile = findViewById(R.id.et_reg_mobile);
        et_reg_amount = findViewById(R.id.et_reg_amount);
        submitbtn = findViewById(R.id.submitbtn);
        get_dtl = findViewById(R.id.get_dtl);
        spn_month = findViewById(R.id.spn_month);
        spn_type = findViewById(R.id.spn_type);


        SharedPreferences sp = getSharedPreferences("user_data", MODE_PRIVATE);
        created_by = sp.getString("id", "");

        ProgressDialog dialog = new ProgressDialog(FeesActivity.this);
        dialog.setTitle("Loading Data");
        dialog.setCancelable(false);

        //get Data From Spinner


        ArrayAdapter month_adapter =new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arrmonth);
        ArrayAdapter type_adapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arrtype);

        month_adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spn_month.setAdapter(month_adapter);

        type_adapter.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item);
        spn_type.setAdapter(type_adapter);

        spn_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                month = arrmonth[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                month = "null";
            }
        });

        spn_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                txn_type = arrtype[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                txn_type = "null";
            }
        });

        get_dtl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id= et_reg_number.getText().toString().trim();
                if (id.equals("")){
                    et_reg_number.setError("Required");
                    et_reg_number.requestFocus();
                    return;
                }
                dialog.show();
                Call<GetMembersModelResponse> call = RetrofitClient.getInstance().getApi().getmemberforfee(request_code, id, created_by);
                call.enqueue(new Callback<GetMembersModelResponse>() {
                    @Override
                    public void onResponse(Call<GetMembersModelResponse> call, Response<GetMembersModelResponse> response) {
                        if (response.isSuccessful()){
                            GetMembersModelResponse membersModelResponse = response.body();
                            ArrayList<MembersModel> arrayList = membersModelResponse.getData();
                            if (arrayList.size()!=0){
                                member_name = arrayList.get(0).name;
                                member_mobile = arrayList.get(0).mobile;
                                et_reg_name.setText(member_name);
                                et_reg_mobile.setText(member_mobile);
                                dialog.dismiss();
                            }else {
                                Toast.makeText(FeesActivity.this, "Please Enter Valid Registration Number", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                           
                        }else{
                            Toast.makeText(FeesActivity.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetMembersModelResponse> call, Throwable t) {
                        Toast.makeText(FeesActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount = et_reg_amount.getText().toString().trim();
                dialog.show();
                Call<AddDataResponseModel> call = RetrofitClient.getInstance()
                        .getApi()
                        .feesdeposit(id, created_by, member_name, member_mobile, month, amount, txn_type);
                call.enqueue(new Callback<AddDataResponseModel>() {
                    @Override
                    public void onResponse(Call<AddDataResponseModel> call, Response<AddDataResponseModel> response) {
                        if (response.isSuccessful()){
                            AddDataResponseModel addDataResponseModel = response.body();
                            Toast.makeText(FeesActivity.this, ""+ addDataResponseModel.message, Toast.LENGTH_SHORT).show();
                            Intent i= new Intent(FeesActivity.this, HomeActivity.class);
                            startActivity(i);
                            finish();
                            dialog.dismiss();
                        }else{
                            Toast.makeText(FeesActivity.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<AddDataResponseModel> call, Throwable t) {
                        Toast.makeText(FeesActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });
    }
}