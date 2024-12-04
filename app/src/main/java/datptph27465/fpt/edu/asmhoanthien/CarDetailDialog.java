package datptph27465.fpt.edu.asmhoanthien;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

public class CarDetailDialog extends Dialog {

    private Car car;

    public CarDetailDialog(Context context, Car car) {
        super(context);
        this.car = car;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_car_detail);

        // Ánh xạ các TextView trong dialog
        TextView tvName = findViewById(R.id.tvName);
        TextView tvManufacturer = findViewById(R.id.tvManufacturer);
        TextView tvYear = findViewById(R.id.tvYear);
        TextView tvPrice = findViewById(R.id.tvPrice);
        TextView tvDescription = findViewById(R.id.tvDescription);

        // Gán dữ liệu từ đối tượng car
        tvName.setText("Tên: "+car.getName());
        tvManufacturer.setText("Hãng: "+car.getManufacturer());
        tvYear.setText("Năm sản xuất: "+String.valueOf(car.getYear()));
        tvPrice.setText("Giá: "+String.valueOf(car.getPrice()));
        tvDescription.setText("Mô tả:"+car.getDescription());
    }
}
