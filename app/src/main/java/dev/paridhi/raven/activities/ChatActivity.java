package dev.paridhi.raven.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dev.paridhi.raven.R;
import dev.paridhi.raven.adapter.recycler.MessagesRVA;
import dev.paridhi.raven.databinding.ActivityChatBinding;
import dev.paridhi.raven.model.MessageModel;
import dev.paridhi.raven.others.Helper;

public class ChatActivity extends AppCompatActivity {
    ActivityChatBinding binding;
    Bundle bundle;
    String name, channelID,receiverID,senderID;
    FirebaseFirestore firestore;

    MessagesRVA adapter;
    ArrayList<MessageModel> messages;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;


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

        setRecycler();

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

    private void setRecycler() {
        recyclerView=binding.messagesRV;
        messages=new ArrayList<>();
        adapter=new MessagesRVA(getApplicationContext(),messages);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.clearAnimation();
        recyclerView.scrollToPosition(adapter.getItemCount()-1);

        firestore.collection("channels").document(channelID).collection("messages").orderBy("timestamp").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

                } else
                {

                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            MessageModel mm = doc.getDocument().toObject(MessageModel.class);
                            messages.add(mm);
                            adapter.notifyDataSetChanged();

                        }
                    }
                    recyclerView.scrollToPosition(adapter.getItemCount()-1);

                }

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
                            adapter.notifyDataSetChanged();

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