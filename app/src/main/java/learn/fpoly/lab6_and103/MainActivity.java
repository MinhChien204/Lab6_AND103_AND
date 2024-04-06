package learn.fpoly.lab6_and103;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import learn.fpoly.lab6_and103.adapter.FruitAdapter;
import learn.fpoly.lab6_and103.model.Fruits;
import learn.fpoly.lab6_and103.model.Response;
import learn.fpoly.lab6_and103.server.APIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements FruitAdapter.OnItemLongClickListener {
    ListView listView;
    APIService apiService;
    ArrayList<Fruits> list;
    FruitAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(APIService.class);

        findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddFruits.class);
                startActivity(intent);
            }
        });

        loadData();
    }

    void loadData() {
        Call<ArrayList<Fruits>> call = apiService.getFruits();
        call.enqueue(new Callback<ArrayList<Fruits>>() {
            @Override
            public void onResponse(Call<ArrayList<Fruits>> call, retrofit2.Response<ArrayList<Fruits>> response) {
                if (response.isSuccessful()) {
                    list = response.body();

                    adapter = new FruitAdapter(list, getApplicationContext(), MainActivity.this, MainActivity.this);

                    listView.setAdapter(adapter);
                } else {
                    Log.e("MainActivity", "Error getting fruits: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Fruits>> call, Throwable t) {
                Log.e("MainActivity", "Failed to fetch fruits: " + t.getMessage());
            }
        });
    }

    public void xoa(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Delete");
        builder.setMessage("Bạn có chắc chắn muốn xóa?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Call<Fruits> call = apiService.deleteFruits(id);
                call.enqueue(new Callback<Fruits>() {
                    @Override
                    public void onResponse(Call<Fruits> call, retrofit2.Response<Fruits> response) {
                        if (response.isSuccessful()) {
                            loadData();
                            Toast.makeText(MainActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Fruits> call, Throwable t) {
                        Log.e("MainActivity", "Call failed: " + t.toString());
                        Toast.makeText(MainActivity.this, "Đã xảy ra lỗi khi xóa dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        builder.show();
    }

    @Override
    public void onItemLongClick(Fruits fruits) {
        Intent intent = new Intent(this, UpdateFruits.class);
        intent.putExtra("fruits", fruits);
        startActivity(intent);
    }
}
