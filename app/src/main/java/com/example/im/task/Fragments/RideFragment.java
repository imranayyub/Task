package com.example.im.task.Fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.im.task.Activities.MainActivity;
import com.example.im.task.Interfaces.ApiInterface;
import com.example.im.task.Adapters.MyAdapter;
import com.example.im.task.R;
import com.example.im.task.Model.RideHistory;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RideFragment extends Fragment {

    //    private static OkHttpClient defaultHttpClient = new OkHttpClient();
    //used in Logs.
    private static final String TAG = MainActivity.class.getSimpleName();
    RecyclerView recyclerView;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ride, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);   //If the RecyclerView knows in advance that its size doesn't depend on the adapter content, then it will skip checking if its size should change every time an item is added or removed from the adapter.
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));  //Displays recycler view in fragment.
        //HttpCLient to Add Authorization Header.
        OkHttpClient defaultHttpClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public okhttp3.Response intercept(Chain chain) throws IOException {
                                Request request = chain.request().newBuilder()
                                        .addHeader("authorization", "bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoicnV0dXJhaiB0ZXN0MiIsImVtYWlsIjoiZGV2ZWxvcGVyQG1vb3ZsZWUuY29tIiwicGhvbmUiOiI5NjMyMDY1NjQ5IiwicGFzc3dvcmQiOiJ0ZXN0IiwidHlwZSI6ImN1c3RvbSIsInN1YnNjcmliZWRUbyI6W10sInJvbGUiOiJ1c2VyIiwiaXNfb3RwX3ZlcmlmaWVkIjp0cnVlLCJyYXRpbmciOjEsImNyZWF0ZWRBdCI6IjIwMTYtMTEtMjlUMDY6MTg6MTIuNTE2WiIsInVwZGF0ZWRBdCI6IjIwMTctMDgtMDJUMjE6MTI6MTYuOTUxWiIsIm90cCI6ODIzNTYsInJpZGVfY291bnQiOjE4MiwicmVmZXJyYWxfY29kZSI6IlJFRjk2MzIwNjU2NDkiLCJyZWZlcnJhbF9ib251cyI6MCwicmVmZXJyZWRfYnkiOm51bGwsImltYWdlX3VybCI6Imh0dHBzOi8vdHctdXNlci1pbWFnZXMuczMuYW1hem9uYXdzLmNvbS82ZWU4NWJlMC0wMGE0LTQyNDMtOGMwMS0xMzYzMGMyOTE1ZmQuanBnIiwicG9saWN5X2FjY2VwdGVkIjp0cnVlLCJpc19wYXl0bV9saW5rZWQiOnRydWUsInBheXRtIjp7InRva2VuIjoiSTR0THBIR3NRMVBjNXV5b0ZDVVFGZlI3dm9TaDA0RjdxNUFTTkdlVVRYdjJZSDBxV2YxRFZWcDFocVFJRmYyWiIsInBob25lIjoiODA4Nzg0NDM2NiIsImV4cGlyZXMiOiIxNTAyNDM4MzE1OTM4In0sImlkIjoiNTgzZDFkYTRmYWEwODhlZjFjYTdlM2JiIiwiaWF0IjoxNTAxNzU4OTU2LCJleHAiOjE1MzMyOTQ5NTZ9.i_D48-t_sOUBCoNNVXVrvgmQjasxA_kpF_P9dZIhQ74").build();
                                return chain.proceed(request);
                            }
                        }).build();
        //Retrofit to retrieve JSON data from server.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.BASE_URL)
                .client(defaultHttpClient)
                .addConverterFactory(GsonConverterFactory.create())     //Using GSON to Convert JSON into POJO.
                .build();
        progressDialog = ProgressDialog.show(getActivity(), "", "Please Wait...", true);

        ApiInterface apiService = retrofit.create(ApiInterface.class);

        Call<List<RideHistory>> call = apiService.getDetail();   //Making Api call using Call Retrofit method that sends a request to a webserver and returns a response.
        call.enqueue(new Callback<List<RideHistory>>() {  //enqueue  send request asynchronously and notify it response or any error occurs while talking to server.
            //In case of Success and server responds.
            @Override
            public void onResponse(Call<List<RideHistory>> call, Response<List<RideHistory>> response) {
                List<RideHistory> rideHistory = response.body(); //storing response body in rideHistory.
                //setting adapter to recyclerview
                MyAdapter adapter = new MyAdapter(getActivity(), rideHistory);
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();
//                for (RideHistory m : rideHistory) {
//                    Log.d("Booking_id : ", m.getBooking_id());
//                    Log.d("Image : ", m.getImage());
//                    Log.d("date : ", m.getTime());
//                }
                Toast.makeText(getActivity(), "Ride History", Toast.LENGTH_SHORT).show();
            }

            //In case of Failure i.e., couldnot connect to server because of some error.
            @Override
            public void onFailure(Call<List<RideHistory>> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

