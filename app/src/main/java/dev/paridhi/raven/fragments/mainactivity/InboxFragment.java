package dev.paridhi.raven.fragments.mainactivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import dev.paridhi.raven.R;
import dev.paridhi.raven.activities.ChatActivity;
import dev.paridhi.raven.adapter.recycler.ChannelsRVA;
import dev.paridhi.raven.databinding.FragmentInboxBinding;
import dev.paridhi.raven.model.ChannelModel;

public class InboxFragment extends Fragment {

    private FragmentInboxBinding binding;
    FirebaseFirestore fStore;
    FirebaseAuth firebaseAuth;

    ChannelsRVA adapter;
    String displayName,photoURL,Key,senderID,ReceiverID;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentInboxBinding.inflate(getLayoutInflater(),container,false);
        View view=binding.getRoot();

        fStore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        setUpRecylerView(view);




        return view;
    }

    public void setUpRecylerView(View view) {
        Query query=fStore.collection("channels").whereArrayContains("members",firebaseAuth.getCurrentUser().getUid());
        FirestoreRecyclerOptions<ChannelModel> options=new FirestoreRecyclerOptions.Builder<ChannelModel>().setQuery(query,ChannelModel.class).build();
        adapter=new ChannelsRVA(options);
        RecyclerView recyclerView=binding.inboxRV;
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lm=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new ChannelsRVA.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position, TextView FullName, TextView ImageURL) {
                ChannelModel channelModel=documentSnapshot.toObject(ChannelModel.class);
                Intent intent=new Intent(getActivity(), ChatActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("Name",FullName.getText().toString());
                bundle.putString("channelID",documentSnapshot.getId().toString());
                bundle.putString("receiverID",channelModel.getOtherUser(firebaseAuth.getCurrentUser().getUid()));
                bundle.putString("senderID",firebaseAuth.getCurrentUser().toString());

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });




    }
}