package dev.paridhi.raven.adapter.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import dev.paridhi.raven.R;
import dev.paridhi.raven.model.UserModel;
import io.getstream.avatarview.AvatarView;

public class ConnectRVA extends FirestoreRecyclerAdapter<UserModel,ConnectRVA.UsersHolder> {
    public OnItemClickListener listener;

    public ConnectRVA(@NonNull FirestoreRecyclerOptions<UserModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ConnectRVA.UsersHolder holder, int position, @NonNull UserModel model) {

        try{
            holder.mDisplayName.setText(model.getDisplayName());
            holder.memailid.setText(model.getEmail());
            holder.mImageview.setAvatarInitials(model.getInitials());


        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @NonNull
    @Override
    public ConnectRVA.UsersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_search,parent,false);
        return new UsersHolder(view);
    }

    public class UsersHolder extends RecyclerView.ViewHolder {

        TextView mDisplayName, memailid;
        AvatarView mImageview;

        public UsersHolder(@NonNull View itemView) {
            super(itemView);
            mDisplayName=itemView.findViewById(R.id.user_search_displayname);
            memailid=itemView.findViewById(R.id.user_search_useremail);
            mImageview=itemView.findViewById(R.id.user_search_profileimage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && listener!=null)
                    {
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });



        }
    }
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;

    }

}
