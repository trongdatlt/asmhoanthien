package datptph27465.fpt.edu.asmhoanthien;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CarApi {
    @GET("/cars/api")
    Call<List<Car>> getAllCars();

    @POST("/cars/api")
    Call<List<Car>> addCar(@Body Car car);

    @PUT("/cars/api/{id}")
    Call<List<Car>> updateCar(@Path("id") String id, @Body Car car);

}
