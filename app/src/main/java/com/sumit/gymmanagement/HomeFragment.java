package com.sumit.gymmanagement;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;


public class HomeFragment extends Fragment {

    private TextView tv_name;



    public HomeFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        LinearLayout ll_fees = view.findViewById(R.id.ll_fees);
        LinearLayout ll_addMember = view.findViewById(R.id.ll_addMember);
        LinearLayout ll_notes = view.findViewById(R.id.ll_addnotes);
        LinearLayout ll_tdetails = view.findViewById(R.id.ll_tdetails);
        LinearLayout ll_fees_history = view.findViewById(R.id.ll_fees_history);
        LinearLayout ll_sellHistory= view.findViewById(R.id.ll_sellHistory);
        LinearLayout ll_image_icon = view.findViewById(R.id.ll_image_icon);
        tv_name = view.findViewById(R.id.tv_name);

        SharedPreferences sp = getContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);

        String gym_name = sp.getString("gym_name", "").toLowerCase();
        String new_name = null;

        if (gym_name.indexOf("gym")>=0){
            new_name = gym_name.replaceAll("gym","");
            tv_name.setText(new_name);
        }else{
            tv_name.setText(gym_name);
        }






        ll_fees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), FeesActivity.class);
                startActivity(i);
            }
        });

        ll_addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), AddMemberActivity.class);
                startActivity(i);
            }
        });

        ll_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), NotesActivity.class);
                startActivity(i);
            }
        });
        ll_tdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), TraineeActivity.class);
                startActivity(i);
            }
        });
        ll_fees_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), FeeHistoryActivity.class);
                startActivity(i);
            }
        });

        ll_sellHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getContext(), SellHistoryActivity.class);
                startActivity(i);
            }
        });

        ll_image_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog imageDialog =new Dialog(getContext());
                imageDialog.setContentView(R.layout.dialog_image);
                ShapeableImageView image = imageDialog.findViewById(R.id.dialog_image);
                image.setImageResource(R.drawable.reg_bg);
                imageDialog.show();
            }
        });
        return view;
    }

}