package dev.paridhi.raven.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import dev.paridhi.raven.R;
import dev.paridhi.raven.databinding.ActivityChatBinding;
import dev.paridhi.raven.model.MessageModel;
import dev.paridhi.raven.others.Helper;

public class ChatActivity extends AppCompatActivity {
    ActivityChatBinding binding;
    Bundle bundle;
    String name, channelID,receiverID,senderID;
    FirebaseFirestore firestore;

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

        firestore=FirebaseFirestore.getInstance();

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.avatar.setAvatarInitials(new Helper().getInitals(name));
        binding.textName.setText(name);

        binding.messageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

    }

    private void sendMessage() {
        String message=binding.chatMessage.getText().toString();
        if(!message.isEmpty())
        {
            DocumentReference documentReference=firestore.collection("channels").document(channelID).collection("messages").document();
            MessageModel messageModel=new MessageModel(message,senderID);
            documentReference.set(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    DocumentReference channels=firestore.collection("channels").document(channelID);
                    Map<String,Object> Lastmessage=new HashMap<>();
                    Lastmessage.put("lastmessage",message);
                    Lastmessage.put("time", FieldValue.serverTimestamp());
                    channels.update(Lastmessage).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            binding.chatMessage.setText("");

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });


        }

    }
}