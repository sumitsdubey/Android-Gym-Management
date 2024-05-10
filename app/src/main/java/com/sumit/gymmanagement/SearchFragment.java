package com.sumit.gymmanagement;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sumit.gymmanagement.adapter.MemberAdapter;
import com.sumit.gymmanagement.api.RetrofitClient;
import com.sumit.gymmanagement.model.MembersModel;
import com.sumit.gymmanagement.model.GetMembersModelResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recycler_member;
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        recycler_member = view.findViewById(R.id.recycler_member);
        Integer request_code = 0;

        SharedPreferences sp = getContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String created_by = sp.getString("id", "");

        ProgressDialog dialog = new ProgressDialog(getContext());
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
                            Toast.makeText(getContext(), "No Members Found", Toast.LENGTH_SHORT).show();
                        }
                        MemberAdapter memberAdapter = new MemberAdapter(getContext(), arrayList);
                        recycler_member.setAdapter(memberAdapter);
                        recycler_member.setLayoutManager(new LinearLayoutManager(getContext()));
                        dialog.dismiss();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        dialog.dismiss();
                    }
                }else{
                    Toast.makeText(getContext(), ""+response.message(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<GetMembersModelResponse> call, Throwable t) {
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        return view;
    }
}