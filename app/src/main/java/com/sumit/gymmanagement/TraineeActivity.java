package com.sumit.gymmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.sumit.gymmanagement.adapter.MemberAdapter;
import com.sumit.gymmanagement.api.RetrofitClient;
import com.sumit.gymmanagement.model.MembersModel;
import com.sumit.gymmanagement.model.GetMembersModelResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TraineeActivity extends AppCompatActivity {
    ArrayList<MembersModel> members;

    RecyclerView recycler_member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee);

        recycler_member = findViewById(R.id.recycler_member);

        SharedPreferences sp = getSharedPreferences("user_data", MODE_PRIVATE);
        String created_by = sp.getString("id", "");
        Integer request_code = 0;

        ProgressDialog dialog = new ProgressDialog(TraineeActivity.this);
        dialog.setTitle("Loading Data");
        dialog.setCancelable(false);
        dialog.show();
        Call<GetMembersModelResponse> call = RetrofitClient.getInstance().getApi().getmember(request_code,created_by);
        call.enqueue(new Callback<GetMembersModelResponse>() {
            @Override
            public void onResponse(Call<GetMembersModelResponse> call, Response<GetMembersModelResponse> response) {
                if (response.isSuccessful()){
                    GetMembersModelResponse membersModelResponse = response.body();
                    try {
                        ArrayList<MembersModel> arrayList = membersModelResponse.getData();
                        int size = arrayList.size();
                        if (size == 0){
                            Toast.makeText(TraineeActivity.this, "No Members Found", Toast.LENGTH_SHORT).show();
                        }
                        MemberAdapter memberAdapter = new MemberAdapter(getApplicationContext(), arrayList);
                        recycler_member.setAdapter(memberAdapter);
                        recycler_member.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        dialog.dismiss();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        dialog.dismiss();
                    }
                }else{
                    Toast.makeText(TraineeActivity.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<GetMembersModelResponse> call, Throwable t) {
                Toast.makeText(TraineeActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });


    }
}