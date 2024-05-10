package com.sumit.gymmanagement;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sumit.gymmanagement.api.RetrofitClient;
import com.sumit.gymmanagement.model.LogoutResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingFragment extends Fragment {
    private CardView cv_logout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_setting, container, false);

        cv_logout = view.findViewById(R.id.cv_logout);
        SharedPreferences sp = getContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String email = sp.getString("email","");
        SharedPreferences.Editor editor = sp.edit();


        ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setTitle("Loading");
        dialog.setCancelable(false);
        cv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                Call<LogoutResponseModel> call = RetrofitClient.getInstance().getApi().logoutuser(email);
                call.enqueue(new Callback<LogoutResponseModel>() {
                    @Override
                    public void onResponse(Call<LogoutResponseModel> call, Response<LogoutResponseModel> response) {
                        if (response.isSuccessful()){
                            Intent i = new Intent(getContext(), LoginActivity.class);
                            startActivity(i);
                            getActivity().finish();
                            dialog.dismiss();
                            editor.clear().commit();
                        }else{
                            Toast.makeText(getContext(), "Server Connection Failed", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<LogoutResponseModel> call, Throwable t) {
                        Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                });
            }
        });


        return view;
    }
}