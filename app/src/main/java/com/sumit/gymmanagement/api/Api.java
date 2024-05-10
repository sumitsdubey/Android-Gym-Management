package com.sumit.gymmanagement.api;

import com.sumit.gymmanagement.model.FeesDataResponseModel;
import com.sumit.gymmanagement.model.GetNotesResponseModel;
import com.sumit.gymmanagement.model.LogoutResponseModel;
import com.sumit.gymmanagement.model.AddDataResponseModel;
import com.sumit.gymmanagement.model.GetMembersModelResponse;
import com.sumit.gymmanagement.model.OTPResponseModel;
import com.sumit.gymmanagement.model.RegisterResponseModel;
import com.sumit.gymmanagement.model.UpdateProfileModel;
import com.sumit.gymmanagement.model.UserLoginResponseModel;
import com.sumit.gymmanagement.model.VerifyOtpModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {
    @FormUrlEncoded
    @POST("sendotp.php")
    Call<OTPResponseModel> sendotp(
        @Field("email") String email
    );

    @FormUrlEncoded
    @POST("verifyotp.php")
    Call<VerifyOtpModel> verifyotp(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("registergym.php")
    Call<RegisterResponseModel> registergym(
      @Field("email") String email,
      @Field("gym_name") String name,
      @Field("address") String address,
      @Field("mobile") String mobile
    );

    @FormUrlEncoded
    @POST("logingymuser.php")
    Call<UserLoginResponseModel> userlogin(
      @Field("email") String email,
      @Field("password") String password
    );

    @FormUrlEncoded
    @POST("updateprofile.php")
    Call<UpdateProfileModel> updateprofile(
      @Field("gym_name") String gym_name,
      @Field("owner_name") String owner_name,
      @Field("email") String email,
      @Field("mobile") String mobile,
      @Field("address") String address
    );

    @FormUrlEncoded
    @POST("logoutuser.php")
    Call<LogoutResponseModel> logoutuser(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("addmember.php")
    Call<AddDataResponseModel> addmember(
            @Field("member_name") String member_name,
            @Field("member_mobile") String member_mobile,
            @Field("member_address") String member_address,
            @Field("member_joindate") String member_joindate,
            @Field("member_createdby") String member_createdby,
            @Field("created_gymname") String gym_name,
            @Field("joining_fee") String joinig_fee
    );

    @FormUrlEncoded
    @POST("getdata.php")
    Call<GetMembersModelResponse> getmember(
         @Field("request_code") Integer request_code,
         @Field("id") String created_by

    );

    @FormUrlEncoded
    @POST("getdata.php")
    Call<GetMembersModelResponse> getmemberforfee(
            @Field("request_code") Integer request_code,
            @Field("id") String id,
            @Field("created_by") String created_by
    );

    @FormUrlEncoded
    @POST("feesdeposit.php")
    Call<AddDataResponseModel> feesdeposit(
      @Field("member_id") String id,
      @Field("gym_userid") String created_by,
      @Field("member_name") String member_name,
      @Field("member_mobile") String member_mobile,
      @Field("month") String month,
      @Field("amount") String amount,
      @Field("payment_type") String txn_type
    );

    @FormUrlEncoded
    @POST("getdata.php")
    Call<FeesDataResponseModel> getFeesData(
            @Field("request_code") String request_code,
            @Field("created_by") String created_by,
            @Field("month") String month
    );

    @FormUrlEncoded
    @POST("adddata.php")
    Call<AddDataResponseModel> addnotes(
            @Field("request_code") Integer request_code,
            @Field("created_by") String created_by,
            @Field("title") String title,
            @Field("notes") String notes
    );

    @FormUrlEncoded
    @POST("getdata.php")
    Call<GetNotesResponseModel> getnotes(
      @Field("request_code") Integer request_code,
      @Field("id") String user_id
    );
}
