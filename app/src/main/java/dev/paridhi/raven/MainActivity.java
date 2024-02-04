package dev.paridhi.raven;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

import dev.paridhi.raven.databinding.ActivityMainBinding;
import dev.paridhi.raven.fragments.mainactivity.ConnectFragment;
import dev.paridhi.raven.fragments.mainactivity.InboxFragment;
import dev.paridhi.raven.fragments.mainactivity.SpamFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        firebaseAuth=FirebaseAuth.getInstance();

        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,new InboxFragment()).commit();





        binding.mainToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(item.getItemId()==R.id.logout_menu)
                {
                    firebaseAuth.signOut();
                    Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                return true;
            }
        });

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