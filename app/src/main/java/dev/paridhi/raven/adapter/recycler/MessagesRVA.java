package dev.paridhi.raven.adapter.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import dev.paridhi.raven.R;
import dev.paridhi.raven.databinding.LayoutReceiverBinding;
import dev.paridhi.raven.databinding.LayoutSenderBinding;
import dev.paridhi.raven.model.MessageModel;
import org.ocpsoft.prettytime.PrettyTime;

public class MessagesRVA extends RecyclerView.Adapter {

    Context context;
    ArrayList<MessageModel> messages;
    final int ITEM_SENT=1;
    final int ITEM_RECEIVE=2;
    PrettyTime p = new PrettyTime();

    public MessagesRVA(Context context, ArrayList<MessageModel> messages)
    {
        this.context=context;
        this.messages=messages;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType==ITEM_SENT)
        {
            View view= LayoutInflater.from(context).inflate(R.layout.layout_sender,parent,false);
            return new SentViewHolder(view);
        }
        else
        {
            View view=LayoutInflater.from(context).inflate(R.layout.layout_receiver,parent,false);
            return  new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MessageModel message=messages.get(position);
        try {


            if (holder.getClass() == SentViewHolder.class) {
                SentViewHolder viewHolder = (SentViewHolder) holder;
                viewHolder.binding.seMessage.setText(message.getMessage());
                viewHolder.binding.seTimestamp.setText(p.format(message.getTimestamp().toDate()));
            } else {
                ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;
                viewHolder.binding.rcMessage.setText(message.getMessage());
                viewHolder.binding.rcTimestamp.setText(p.format(message.getTimestamp().toDate()));
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public int getItemViewType(int position) {
        MessageModel MM=messages.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(MM.getSenderId()))
        {
            return ITEM_SENT;
        }else
        {
            return  ITEM_RECEIVE;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    private class SentViewHolder extends RecyclerView.ViewHolder {
        LayoutSenderBinding binding;
        public SentViewHolder(View view) {
            super(view);
            binding=LayoutSenderBinding.bind(itemView);
        }
    }

    private class ReceiverViewHolder extends RecyclerView.ViewHolder {
        LayoutReceiverBinding binding;
        public ReceiverViewHolder(View view) {
            super(view);
            binding=LayoutReceiverBinding.bind(itemView);
        }
    }
}
