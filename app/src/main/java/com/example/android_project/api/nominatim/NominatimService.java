package com.example.android_project.api.nominatim;

import com.example.android_project.api.nominatim.pojo.NominatimAddress;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface NominatimService {

    @Headers({"User-Agent: Android-Project"})
    @GET("/search?format=jsonv2&addressdetails=1&namedetails=1&countrycodes=fr")
    Call<List<NominatimAddress>> getAddressBySearch(@Query("q") String search);

}
