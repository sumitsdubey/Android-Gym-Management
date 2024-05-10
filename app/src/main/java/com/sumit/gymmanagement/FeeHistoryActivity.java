package com.sumit.gymmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sumit.gymmanagement.adapter.FeesAdapter;
import com.sumit.gymmanagement.api.RetrofitClient;
import com.sumit.gymmanagement.model.ExtraData;
import com.sumit.gymmanagement.model.FeesDataModel;
import com.sumit.gymmanagement.model.FeesDataResponseModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeeHistoryActivity extends AppCompatActivity {
    private String[] arrmonth = {"January","February","March","April","May","June","July","August","September","October","November","December"};
    private RecyclerView recycler_fees;
    private Spinner spn_fee_month;

    private String created_by, month;

    private String REQUEST_CODE = "2";

    private TextView tv_paid_member, tv_total_fees;

    private ProgressDialog dialog;

    private ArrayList<FeesDataModel> data;
    private ArrayList<com.sumit.gymmanagement.model.ExtraData> extraData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_history);

        recycler_fees = findViewById(R.id.recycler_fees);
        spn_fee_month = findViewById(R.id.spn_fee_month);
        tv_paid_member = findViewById(R.id.tv_paid_member);
        tv_total_fees = findViewById(R.id.tv_total_fees);

        //Setting Progress Dialog
        dialog = new ProgressDialog(FeeHistoryActivity.this);
        dialog.setCancelable(false);

        //Getting Data from Shared Preferences
        SharedPreferences sp = getSharedPreferences("user_data", MODE_PRIVATE);
        created_by = sp.getString("id", "");

        //gettng data from Spinner

        ArrayAdapter month_adapter = new ArrayAdapter(FeeHistoryActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arrmonth);
        month_adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spn_fee_month.setAdapter(month_adapter);

        spn_fee_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                month = arrmonth[i];
                getFeesData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                month = "";
            }
        });





    }

    //setup method for getting data.... ***********
    private void getFeesData() {
        try {
            dialog.show();
            Call<FeesDataResponseModel> call = RetrofitClient.getInstance().getApi().getFeesData(REQUEST_CODE, created_by, month);
            call.enqueue(new Callback<FeesDataResponseModel>() {
                @Override
                public void onResponse(Call<FeesDataResponseModel> call, Response<FeesDataResponseModel> response) {
                    if (response.isSuccessful()){
                        FeesDataResponseModel getMembersModelResponse = response.body();
                        if (getMembersModelResponse.isStatus()){
                            data = getMembersModelResponse.getData();
                            extraData = getMembersModelResponse.getExtra_data();
                            String total_amount = extraData.get(0).amount;
                            FeesAdapter adapter = new FeesAdapter(getApplicationContext(),data);
                            if (data.size()!=0) {
                                recycler_fees.setAdapter(adapter);
                                recycler_fees.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                String size = String.valueOf(data.size());
                                tv_paid_member.setText(size);
                                tv_total_fees.setText(total_amount+" ₹");
                                dialog.dismiss();
                            }else{
                                Toast.makeText(FeeHistoryActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }else{
                            Toast.makeText(FeeHistoryActivity.this, ""+getMembersModelResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }else{
                        Toast.makeText(FeeHistoryActivity.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<FeesDataResponseModel> call, Throwable t) {
                    Toast.makeText(FeeHistoryActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}