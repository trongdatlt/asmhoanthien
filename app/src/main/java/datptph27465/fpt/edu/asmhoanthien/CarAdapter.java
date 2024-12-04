package datptph27465.fpt.edu.asmhoanthien;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {

    private List<Car> cars;
    private Context context;

    public CarAdapter(Context context, List<Car> cars) {
        this.context = context;
        this.cars = cars;
    }

    public void setFilter(List<Car> filteredCars) {
        this.cars = new ArrayList<>(filteredCars);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.car_item, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = cars.get(position);
        holder.name.setText("Tên: "+car.getName());
        holder.manufacturer.setText("Hãng: "+car.getManufacturer());
        holder.price.setText("Giá: "+car.getPrice());
        holder.itemView.setOnClickListener(v -> {
            CarDetailDialog dialog = new CarDetailDialog(context, car);
            dialog.show();
        });
        holder.img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditCarDialog(car);
            }
        });
    }
    private void showEditCarDialog(Car car) {
        // Tạo dialog
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_add_car);

        EditText etName = dialog.findViewById(R.id.etName);
        EditText etManufacturer = dialog.findViewById(R.id.etManufacturer);
        EditText etYear = dialog.findViewById(R.id.etYear);
        EditText etPrice = dialog.findViewById(R.id.etPrice);
        EditText etDescription = dialog.findViewById(R.id.etDescription);
        Button btnSubmitCar = dialog.findViewById(R.id.btnSubmitCar);
        etName.setText(car.getName());
        etManufacturer.setText(car.getManufacturer());
        etYear.setText(String.valueOf(car.getYear()));
        etPrice.setText(String.valueOf(car.getPrice()));
        etDescription.setText(car.getDescription());
        CarApi carApi = RetrofitClient.getInstance().create(CarApi.class);

        // Xử lý khi nhấn Submit
        btnSubmitCar.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String manufacturer = etManufacturer.getText().toString().trim();
            int year = Integer.parseInt(etYear.getText().toString().trim());
            double price = Double.parseDouble(etPrice.getText().toString().trim());
            String description = etDescription.getText().toString().trim();

            Car newCar = new Car(car.getId(),name, manufacturer, year, price, description);
            carApi.updateCar(car.getId(),newCar).enqueue(new Callback<List<Car>>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                    cars.clear();
                    cars.addAll(response.body());
                    dialog.dismiss();
                    notifyDataSetChanged();
                    Toast.makeText(context, "Sửa thành công", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<List<Car>> call, Throwable t) {

                }
            });


        });

        dialog.show();
    }
    @Override
    public int getItemCount() {
        return cars != null ? cars.size() : 0;
    }

    public static class CarViewHolder extends RecyclerView.ViewHolder {
        TextView price,name, manufacturer;
        ImageView img_edit;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            manufacturer = itemView.findViewById(R.id.manufacturer);
            price = itemView.findViewById(R.id.price);
            img_edit = itemView.findViewById(R.id.img_edit);

        }
    }
}
