package com.example.android_project.api.nominatim;

import com.example.android_project.api.nominatim.pojo.NominatimSearch;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface NominatimService {

    @Headers({"User-Agent: Android-Project"})
    @GET("/search?format=jsonv2&addressdetails=1&namedetails=1&countrycodes=fr")
    Call<List<NominatimSearch>> getAddressBySearch(@Query("q") String search);

    @Headers({"User-Agent: Android-Project"})
    @GET("/reverse?zoom=10&format=jsonv2&polygon_geojson=1&countrycodes=fr")
    Call<List<NominatimSearch>> getCityByReverse(@Query("lat") String lat, @Query("lon") String lon);

    @Headers({"User-Agent: Android-Project"})
    @GET("/reverse?zoom=10&format=jsonv2&polygon_geojson=1&countrycodes=fr")
    Call<List<NominatimSearch>> getCountyByReverse(@Query("lat") String lat, @Query("lon") String lon);

}
