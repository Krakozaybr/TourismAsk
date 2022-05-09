package com.krak.tourismask.main_activity;

import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.krak.tourismask.R;
import com.krak.tourismask.databinding.MainActivityBinding;
import com.krak.tourismask.utils.PreferenceManager;
import com.krak.tourismask.view_models.ListViewModel;
import com.krak.tourismask.view_models.SightsViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainActivityBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    private ViewModelProvider provider;
    public static final String GOTO_FRAGMENT = "GOTO_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        provider = new ViewModelProvider(this);
        binding = MainActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadData();
        setSupportActionBar(binding.appBarMain.toolbar);
        initNavigationView();
        checkIntents();
    }

    private void initNavigationView(){
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_sights, R.id.nav_map, R.id.nav_settings)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Устанавливаем имя в NavView
        PreferenceManager preferenceManager = new PreferenceManager(this);
        TextView name = binding.navView.getHeaderView(0).findViewById(R.id.usernameNav);
        name.setText(preferenceManager.getName());
    }

    // Проверяем Intent
    private void checkIntents() {
        // Переходим к необходимому фрагменту
        if (getIntent().hasExtra(GOTO_FRAGMENT)){
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            navController.navigate(getIntent().getIntExtra(GOTO_FRAGMENT, R.id.nav_map));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    // Нажимаем на три горизонтальные палочки в Toolbar - шторка показывается
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    // Костыль, чтобы не было трёх точек справа в Toolbar
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.setGroupVisible(R.id.first_group, false);
        return super.onPrepareOptionsMenu(menu);
    }

    // Загружаем данные из всех ListViewModel (пока только один - SightsViewModel)
    // Но только если у нас нет информации от них.
    private void loadData(){
        ListViewModel[] viewModels = {
            provider.get(SightsViewModel.class)
        };
        for (ListViewModel viewModel : viewModels){
            List list = (List) viewModel.getData().getValue();
            if (list == null || list.isEmpty()){
                viewModel.loadData();
            }
        }
    }
}
