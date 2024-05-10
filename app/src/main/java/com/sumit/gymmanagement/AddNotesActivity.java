package com.sumit.gymmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.sumit.gymmanagement.api.RetrofitClient;
import com.sumit.gymmanagement.model.AddDataResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNotesActivity extends AppCompatActivity {

    private TextInputEditText et_title,et_notes;
    private String created_by, title, notes;

    private AppCompatButton btn_addNote;

    Integer REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        et_title = findViewById(R.id.et_title);
        et_notes = findViewById(R.id.et_notes);
        btn_addNote = findViewById(R.id.btn_addNote);


        SharedPreferences sp = getSharedPreferences("user_data", MODE_PRIVATE);
        created_by = sp.getString("id", "");

        ProgressDialog dialog = new ProgressDialog(AddNotesActivity.this);
        dialog.setCancelable(false);
        btn_addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = et_title.getText().toString().trim();
                notes = et_notes.getText().toString().trim();

                if (title.equals("")){
                    et_title.setError("Required");
                    et_title.requestFocus();
                    return;
                }
                if (notes.equals("")){
                    et_notes.setError("Required");
                    et_notes.requestFocus();
                    return;
                }
                dialog.show();
                Call<AddDataResponseModel> call = RetrofitClient.getInstance().getApi().addnotes(REQUEST_CODE, created_by, title, notes);
                call.enqueue(new Callback<AddDataResponseModel>() {
                    @Override
                    public void onResponse(Call<AddDataResponseModel> call, Response<AddDataResponseModel> response) {
                        if (response.isSuccessful()){
                            AddDataResponseModel addDataResponseModel = response.body();
                            Toast.makeText(AddNotesActivity.this, ""+ addDataResponseModel.message, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            if (addDataResponseModel.isStatus()){
                                Intent i = new Intent(AddNotesActivity.this, NotesActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }else{
                            Toast.makeText(AddNotesActivity.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                            dialog.hide();
                        }
                    }

                    @Override
                    public void onFailure(Call<AddDataResponseModel> call, Throwable t) {
                        Toast.makeText(AddNotesActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });
    }
}