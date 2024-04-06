package learn.fpoly.lab6_and103;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import learn.fpoly.lab6_and103.model.Response;
import learn.fpoly.lab6_and103.model.User;
import learn.fpoly.lab6_and103.server.APIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DangNhap extends AppCompatActivity {
    APIService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        EditText edUsername = findViewById(R.id.txtuser);
        EditText edPassword = findViewById(R.id.txtpass);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create((APIService.class));

        findViewById(R.id.btndangky).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DangNhap.this, DangKy.class));
            }
        });

        findViewById(R.id.btndangnhap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                String _username = edUsername.getText().toString().trim();
                String _password = edPassword.getText().toString().trim();
                user.setUsername(_username);
                user.setPassword(_password);

                Call<Response<User>> call = apiService.login(user);
                call.enqueue(new Callback<Response<User>>() {
                    @Override
                    public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(DangNhap.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(DangNhap.this, MainActivity.class));
                        }
                    }

                    @Override
                    public void onFailure(Call<Response<User>> call, Throwable t) {
                        Toast.makeText(DangNhap.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}