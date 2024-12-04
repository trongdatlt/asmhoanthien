package datptph27465.fpt.edu.asmhoanthien;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    CarApi carApi = RetrofitClient.getInstance().create(CarApi.class);
    private RecyclerView recyclerView;
    private CarAdapter carAdapter;
    private EditText searchBar;
    private List<Car> carList = new ArrayList<>();
    private Button btnAddCar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        searchBar = findViewById(R.id.searchBar);
        btnAddCar = findViewById(R.id.btnAddCar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        carAdapter = new CarAdapter(this, carList);
        recyclerView.setAdapter(carAdapter);
        btnAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddCarDialog();
            }
        });
        fetchCars();
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterCars(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    private void showAddCarDialog() {
        // Tạo dialog
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_car);

        EditText etName = dialog.findViewById(R.id.etName);
        EditText etManufacturer = dialog.findViewById(R.id.etManufacturer);
        EditText etYear = dialog.findViewById(R.id.etYear);
        EditText etPrice = dialog.findViewById(R.id.etPrice);
        EditText etDescription = dialog.findViewById(R.id.etDescription);
        Button btnSubmitCar = dialog.findViewById(R.id.btnSubmitCar);

        // Xử lý khi nhấn Submit
        btnSubmitCar.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String manufacturer = etManufacturer.getText().toString().trim();
            int year = Integer.parseInt(etYear.getText().toString().trim());
            double price = Double.parseDouble(etPrice.getText().toString().trim());
            String description = etDescription.getText().toString().trim();

            Car newCar = new Car("",name, manufacturer, year, price, description);
            addCarToApi(newCar, dialog);
        });

        dialog.show();
    }


    private void addCarToApi(Car car, Dialog dialog) {
        carApi.addCar(car).enqueue(new Callback<List<Car>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                carList.clear();
                carList.addAll(response.body());
                carAdapter.setFilter(carList);
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {

            }
        });

    }
    private void filterCars(String query) {
        // Lọc danh sách theo tên hoặc hãng sản xuất
        List<Car> filteredCars = new ArrayList<>();
        for (Car car : carList) {
            if (car.getName().toLowerCase().contains(query.toLowerCase()) ||
                    car.getManufacturer().toLowerCase().contains(query.toLowerCase())) {
                filteredCars.add(car);
            }
        }
        carAdapter.setFilter(filteredCars);
    }


    private void fetchCars() {

        carApi.getAllCars().enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    carList = response.body();
                    Log.d("zzzzzzzzzzzzzzzz", "onResponse: "+carList.size());
                    carAdapter.setFilter(carList);
                }
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                Log.d("zzzzzzzzzzzzzzzz", "onResponse: loi call"+ t.getMessage());
            }
        });
    }
}
