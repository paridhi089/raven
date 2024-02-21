package dev.paridhi.raven.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import dev.paridhi.raven.R;
import dev.paridhi.raven.databinding.ActivityChatBinding;
import dev.paridhi.raven.others.Helper;

public class ChatActivity extends AppCompatActivity {
    ActivityChatBinding binding;
    Bundle bundle;
    String name, channelID,receiverID,senderID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);

        bundle=getIntent().getExtras();
        name=bundle.getString("Name");
        channelID=bundle.getString("channelID");
        receiverID=bundle.getString("receiverID");
        senderID=bundle.getString("senderID");

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.avatar.setAvatarInitials(new Helper().getInitals(name));
        binding.textName.setText(name);





    }
}