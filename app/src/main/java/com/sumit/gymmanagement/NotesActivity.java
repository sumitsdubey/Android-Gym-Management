package com.sumit.gymmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sumit.gymmanagement.adapter.NotesAdapter;
import com.sumit.gymmanagement.api.RetrofitClient;
import com.sumit.gymmanagement.model.GetNotesModel;
import com.sumit.gymmanagement.model.GetNotesResponseModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotesActivity extends AppCompatActivity {
    RecyclerView recycler_notes;
    ArrayList<GetNotesModel> list;
    private Integer REQUEST_CODE = 3;
    private String user_id;
    private ProgressDialog dialog;
    TextView tv_notes_heading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        tv_notes_heading = findViewById(R.id.tv_notes_heading);
        recycler_notes = findViewById(R.id.recycler_notes);

        SharedPreferences sp = getSharedPreferences("user_data", MODE_PRIVATE);
        user_id = sp.getString("id","");

        dialog = new ProgressDialog(NotesActivity.this);

         try {
             getNotesData();
         }catch (Exception e){
               e.printStackTrace();
         }
    }

    private void getNotesData() {
        dialog.show();
        Call<GetNotesResponseModel> call = RetrofitClient.getInstance().getApi().getnotes(REQUEST_CODE, user_id);
        call.enqueue(new Callback<GetNotesResponseModel>() {
            @Override
            public void onResponse(Call<GetNotesResponseModel> call, Response<GetNotesResponseModel> response) {
                if (response.isSuccessful()){
                    GetNotesResponseModel getNotesResponseModel = response.body();
                    if (getNotesResponseModel.isStatus()){
                        list = getNotesResponseModel.getData();
                        NotesAdapter adapter = new NotesAdapter(NotesActivity.this, list);
                        recycler_notes.setAdapter(adapter);
                        recycler_notes.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        dialog.dismiss();
                    }else{
                        Toast.makeText(NotesActivity.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                        tv_notes_heading.setText("No Notes Found");
                        dialog.dismiss();
                    }
                }else{
                    Toast.makeText(NotesActivity.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                    tv_notes_heading.setText("No Notes Found");
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<GetNotesResponseModel> call, Throwable t) {
                Toast.makeText(NotesActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    public void AddNotes(View view) {
        Intent i = new Intent(getApplicationContext(), AddNotesActivity.class);
        startActivity(i);
    }


}