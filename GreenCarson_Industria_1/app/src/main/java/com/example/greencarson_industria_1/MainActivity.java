package com.example.greencarson_industria_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.greencarson_industria_1.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Inicializamos en Pedidos
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new PedidosFragment());

        // NAV BAR
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId(); // Almacena el ID del elemento seleccionado en una variable

            if (itemId == R.id.ayuda) {
                replaceFragment(new AyudaFragment());
            } else if (itemId == R.id.pedidos) {
                replaceFragment(new PedidosFragment());
            } else if (itemId == R.id.programados) {
                replaceFragment(new ProgramadosFragment());
            } else if (itemId == R.id.historial){
                replaceFragment(new HistorialFragment());
            } else if (itemId == R.id.perfil){
                replaceFragment(new PerfilFragment());
            }

            return true;
        });
    //fin navbar
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }

}