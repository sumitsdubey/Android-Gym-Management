package com.sumit.gymmanagement;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.material.textfield.TextInputEditText;
import com.sumit.gymmanagement.api.RetrofitClient;
import com.sumit.gymmanagement.model.LogoutResponseModel;
import com.sumit.gymmanagement.model.UpdateProfileModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {
    private TextView profile_gym_name,profile_gym_address,profile_ownername,profile_gym_address1,profile_date,profile_email,profile_mobile;

    private ViewFlipper viewFlipper;
    private Dialog dl;

    private TextInputEditText et_reg_name,et_reg_owner_name,et_reg_address,et_reg_email,et_reg_mobile;
    private AppCompatButton dialog_update_btn,bt_update;
    private ImageView iv_edit;

    private ProgressDialog dialog;

    private String gym_name,owner_name,email,mobile,address;
    private AppCompatButton btn_logout;

    private CardView cv_member, cv_statement;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        dl = new Dialog(getContext());
        dl.setContentView(R.layout.update_profile_layout);

        LinearLayout ll_mynotes = view.findViewById(R.id.ll_mynotes);
        profile_gym_name = view.findViewById(R.id.profile_gym_name);
        profile_gym_address = view.findViewById(R.id.profile_gym_address);
        profile_ownername = view.findViewById(R.id.profile_ownername);
        profile_gym_address1 = view.findViewById(R.id.profile_gym_address1);
        profile_date = view.findViewById(R.id.profile_date);
        profile_email = view.findViewById(R.id.profile_email);
        profile_mobile = view.findViewById(R.id.profile_mobile);
        btn_logout = view.findViewById(R.id.btn_logout);

        bt_update = view.findViewById(R.id.bt_update);
        iv_edit = view.findViewById(R.id.iv_edit);
        cv_member = view.findViewById(R.id.cv_member);
        cv_statement = view.findViewById(R.id.cv_statement);

        //find id from dialog box

        et_reg_name = dl.findViewById(R.id.et_reg_name);
        et_reg_owner_name = dl.findViewById(R.id.et_reg_owner_name);
        et_reg_address= dl.findViewById(R.id.et_reg_address);
        et_reg_email = dl.findViewById(R.id.et_reg_email);
        et_reg_mobile = dl.findViewById(R.id.et_reg_mobile);
        dialog_update_btn = dl.findViewById(R.id.dialog_update_btn);


        SharedPreferences sp = getContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        profile_gym_name.setText(sp.getString("gym_name", ""));
        profile_gym_address.setText(sp.getString("address", ""));
        profile_ownername.setText(sp.getString("owner_name", ""));
        profile_gym_address1.setText(sp.getString("address", ""));
        profile_date.setText(sp.getString("date", ""));
        profile_email.setText(sp.getString("email", ""));
        profile_mobile.setText(sp.getString("mobile", ""));

        //set details for update panel

        et_reg_name.setText(sp.getString("gym_name", ""));
        et_reg_address.setText(sp.getString("address", ""));
        et_reg_email.setText(sp.getString("email", ""));
        et_reg_mobile.setText(sp.getString("mobile",""));

        //find value from dialog

        gym_name = et_reg_name.getText().toString().trim();
        owner_name = et_reg_owner_name.getText().toString().trim();
        address = et_reg_address.getText().toString().trim();
        mobile = et_reg_mobile.getText().toString().trim();
        email = et_reg_email.getText().toString().trim();



        dialog = new ProgressDialog(getContext());
        dialog.setTitle("Loading ");
        dialog.setCancelable(false);


        ll_mynotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), NotesActivity.class);
                startActivity(i);
            }
        });
        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dl.show();
            }
        });

        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dl.show();
            }
        });

        dialog_update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("name", gym_name);
                Log.d("owner_name", owner_name);
                dialog.show();
                Call<UpdateProfileModel> call = RetrofitClient.getInstance()
                        .getApi().updateprofile(gym_name, owner_name, email, mobile, address);
                call.enqueue(new Callback<UpdateProfileModel>() {
                    @Override
                    public void onResponse(Call<UpdateProfileModel> call, Response<UpdateProfileModel> response) {
                        if (response.isSuccessful()){
                            UpdateProfileModel profileModel = response.body();
                            if (profileModel.isStatus()){
                                Toast.makeText(getContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                                editor.putString("gym_name", gym_name);
                                editor.putString("owner_name", owner_name);
                                editor.putString("address", address);
                                editor.putString("mobile", mobile);
                                editor.commit();
                                dl.hide();
                                dialog.hide();
                            }else{
                                Toast.makeText(getContext(), "Error   "+profileModel.getMessage(), Toast.LENGTH_SHORT).show();
                                dialog.hide();
                            }
                        }else {
                            Toast.makeText(getContext(), "Error  "+response.message(), Toast.LENGTH_SHORT).show();
                            dialog.hide();
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateProfileModel> call, Throwable t) {
                        Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
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

        cv_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), TraineeActivity.class);
                startActivity(i);
            }
        });

        cv_statement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), FeeHistoryActivity.class);
                startActivity(i);
            }
        });
        viewFlipper = view.findViewById(R.id.flipper);

        int imageArray[] = {R.drawable.s1, R.drawable.s2, R.drawable.s3, R.drawable.s4, R.drawable.s5};


        for(int i=0; i<imageArray.length;i++){
            showImage(imageArray[i]);
        }

        return view;
    }
    public  void showImage(int img){
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(img);

        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);

        viewFlipper.setInAnimation(getContext(), android.R.anim.fade_in);
        viewFlipper.setOutAnimation(getContext(), android.R.anim.fade_out);

    }

}