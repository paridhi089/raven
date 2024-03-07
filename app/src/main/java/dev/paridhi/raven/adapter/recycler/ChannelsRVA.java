package dev.paridhi.raven.adapter.recycler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import dev.paridhi.raven.R;
import dev.paridhi.raven.model.ChannelModel;
import dev.paridhi.raven.others.Helper;
import io.getstream.avatarview.AvatarView;

public class ChannelsRVA extends FirestoreRecyclerAdapter<ChannelModel,ChannelsRVA.ChannelHolder > {

    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    FirebaseFirestore fStore=FirebaseFirestore.getInstance();

    public OnItemClickListener listener;
    Helper helper=new Helper();
    public ChannelsRVA(@NonNull FirestoreRecyclerOptions<ChannelModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ChannelsRVA.ChannelHolder holder, int position, @NonNull ChannelModel model) {


        try {
            holder.mLastMessage.setText(model.getLastmessage());
            String otheruserid,currentuser;
            currentuser=firebaseAuth.getCurrentUser().getUid();
            //List<String> member=model.getMembers();
            if(firebaseAuth.getCurrentUser()!=null) {
                fStore.collection("users").document(model.getOtherUser(currentuser)).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error == null) {
                            if (value.exists()) {
                                String displayName=value.getString("displayName");
                               holder.mImageview.setAvatarInitials(helper.getInitals(displayName));
                                holder.mImageURL.setText(value.getString("photoURL"));
                                holder.mdisplayName.setText(displayName);

                            } else {
                                error.printStackTrace();
                            }

                        }
                    }
                });
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public ChannelsRVA.ChannelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_channels,parent,false);

        return new ChannelHolder(view);
    }

    public class ChannelHolder extends RecyclerView.ViewHolder {
        TextView mdisplayName,mLastMessage,mImageURL;
        AvatarView mImageview;

        public ChannelHolder(@NonNull View itemView) {
            super(itemView);

            mdisplayName=itemView.findViewById(R.id.channel_user_displayname);
            mLastMessage=itemView.findViewById(R.id.channel_lastmessage);
            mImageview=itemView.findViewById(R.id.channel_rv_profile_image);
            mImageURL=itemView.findViewById(R.id.channels_short_imageurl);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && listener!=null)
                    {
                        listener.onItemClick(getSnapshots().getSnapshot(position),position,mdisplayName,mImageURL);
                    }

                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position, TextView FullName, TextView ImageURL);
    }
    public void setOnItemClickListener(ChannelsRVA.OnItemClickListener listener){
        this.listener=listener;

    }
}
