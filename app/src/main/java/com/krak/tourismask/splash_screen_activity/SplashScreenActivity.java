package com.krak.tourismask.splash_screen_activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.krak.tourismask.R;
import com.krak.tourismask.databinding.SplashScreenActivityBinding;
import com.krak.tourismask.main_activity.MainActivity;
import com.krak.tourismask.name_activity.NameActivity;
import com.krak.tourismask.utils.PreferenceManager;

import java.util.Random;

public class SplashScreenActivity extends AppCompatActivity {

    private SplashScreenActivityBinding binding;
    private static final int[] gifVariants = {
            R.drawable.tourist2,
            R.drawable.tourist3,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SplashScreenActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setGif();
        startWaiting();
    }

    private void setGif(){
        int rnd = new Random().nextInt(gifVariants.length);
        binding.gifImageView.setImageResource(gifVariants[rnd]);
    }

    // Время вышло, нужно идти дальше
    private void goNext(){
        // Проверяем сохранено ли имя пользователя
        PreferenceManager manager = new PreferenceManager(this);
        Intent intent;
        if (manager.getName().isEmpty()){
            intent = new Intent(SplashScreenActivity.this, NameActivity.class);
        } else {
            intent = new Intent(SplashScreenActivity.this, MainActivity.class);
        }
        startActivity(intent);
    }

    // Ждём 2.5 секунды заставки
    private void startWaiting() {
        addListener();
        // Чтобы экран не завис, ожидание выполняем в отдельном потоке
        new Thread(() -> {
            try{
                Thread.sleep(2500);
            } catch (InterruptedException ignored){
            } finally {
                listener.postValue(true);
            }
        }).start();
    }

    private MutableLiveData<Boolean> listener;

    // Добавляем слушатель к LiveData, чтобы по истечении времени вызвать goNext()
    private void addListener() {
        listener = new MutableLiveData<>();
        listener.observe(this, data -> goNext());
    }
}
