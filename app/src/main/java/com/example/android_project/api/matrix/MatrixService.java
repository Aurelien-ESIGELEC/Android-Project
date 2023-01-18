package com.example.android_project.api.matrix;

import com.example.android_project.api.matrix.pojo.request.MatrixRequestBody;
import com.example.android_project.api.matrix.pojo.response.MatrixInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MatrixService {

    @Headers({"Authorization: 5b3ce3597851110001cf62486ade3f0dee2b45fcb2c4d6400ee020b9"})
    @POST("/v2/matrix/driving-car")
    Call<MatrixInfo> getDistanceBetweenPoint(@Body MatrixRequestBody matrixRequestBody);

}
