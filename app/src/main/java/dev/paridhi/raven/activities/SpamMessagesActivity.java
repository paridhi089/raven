package dev.paridhi.raven.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import dev.paridhi.raven.R;
import dev.paridhi.raven.adapter.recycler.MessagesRVA;
import dev.paridhi.raven.databinding.ActivitySpamMessagesBinding;
import dev.paridhi.raven.model.MessageModel;
import dev.paridhi.raven.others.Helper;

public class SpamMessagesActivity extends AppCompatActivity {

    ActivitySpamMessagesBinding binding;
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
        binding=ActivitySpamMessagesBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);

        bundle=getIntent().getExtras();
        name=bundle.getString("Name");
        channelID=bundle.getString("channelID");
        receiverID=bundle.getString("receiverID");
        senderID=bundle.getString("senderID");

        firestore=FirebaseFirestore.getInstance();
        setRecycler();

        binding.spamBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.spamAvatar.setAvatarInitials(new Helper().getInitals(name));
        binding.spamTextName.setText(name);



    }
    private void setRecycler() {
        recyclerView=binding.spammessagesRV;
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

        firestore.collection("channels").document(channelID).collection("spam").orderBy("timestamp").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d("Firebase",error.getMessage());

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

}