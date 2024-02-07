package dev.paridhi.raven.fragments.mainactivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.paridhi.raven.R;
import dev.paridhi.raven.adapter.recycler.ConnectRVA;
import dev.paridhi.raven.databinding.FragmentConnectBinding;
import dev.paridhi.raven.model.ChannelModel;
import dev.paridhi.raven.model.UserModel;

public class ConnectFragment extends Fragment {

    private FragmentConnectBinding binding;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    DocumentReference channelReference;

    ConnectRVA adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentConnectBinding.inflate(getLayoutInflater(),container,false);
        View view=binding.getRoot();
        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

        //For Testing
        setUpRecyclerView("");

        binding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String searchKey=binding.connectSearch.getText().toString();

                setUpRecyclerView(searchKey);
            }
        });
        return view;
    }

    private void setUpRecyclerView(String searchKey) {
        Query query=fStore.collection("users").orderBy("displayName");
        FirestoreRecyclerOptions<UserModel> options=new FirestoreRecyclerOptions.Builder<UserModel>().setQuery(query,UserModel.class).build();
        adapter=new ConnectRVA(options);
        RecyclerView recyclerView=binding.connectFragmentRecycler;
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lm=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();

        adapter.setOnItemClickListener(new ConnectRVA.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                String UUID=documentSnapshot.getId();
                UserModel userModel=documentSnapshot.toObject(UserModel.class);
                String[] memberArray=new String[]{UUID,fAuth.getCurrentUser().getUid()};
                List<String> members= Arrays.asList(memberArray);
                Collections.sort(members);

                CollectionReference channelCollection=fStore.collection("channels");

                Query checkChannelQuery=channelCollection.whereEqualTo("members",members);
                checkChannelQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && task.getResult().isEmpty())
                        {

                            channelReference=channelCollection.document();
                            ChannelModel channelModel =new ChannelModel();
                            channelModel.setMembers(members);
                            channelModel.setLastmessage("No Messages!");

                            channelReference.set(channelModel);
                            String channelID=channelReference.getId();


                            Map<String, Object> timestamp=new HashMap<>();
                            timestamp.put("timestamp", FieldValue.serverTimestamp());
                            fStore.collection("users").document(fAuth.getCurrentUser().getUid()).collection("myChannels").document(channelID).set(timestamp);
                            fStore.collection("users").document(UUID).collection("myChannels").document(channelID).set(timestamp);
                            Toast.makeText(getContext(), "Channel Created", Toast.LENGTH_SHORT).show();

                        }
                        else if(task.isSuccessful() && !task.getResult().isEmpty())
                        {

                            Toast.makeText(getContext(), "Channel already exists", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }
        });


    }
}