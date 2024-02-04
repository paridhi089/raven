package dev.paridhi.raven;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationBarView;

import dev.paridhi.raven.databinding.ActivityMainBinding;
import dev.paridhi.raven.fragments.mainactivity.ConnectFragment;
import dev.paridhi.raven.fragments.mainactivity.InboxFragment;
import dev.paridhi.raven.fragments.mainactivity.SpamFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,new InboxFragment()).commit();

        binding.mainBottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId()==R.id.inbox)
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,new InboxFragment()).commit();
                }
                else if(item.getItemId()==R.id.connect)
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,new ConnectFragment()).commit();
                }
                else if(item.getItemId()==R.id.spam)
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,new SpamFragment()).commit();
                }


                return true;
            }
        });
    }
}