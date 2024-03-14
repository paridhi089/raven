package dev.paridhi.raven.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
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
import dev.paridhi.raven.model.ApiMessageModel;
import dev.paridhi.raven.model.ApiResponseModel;
import dev.paridhi.raven.model.MessageModel;
import dev.paridhi.raven.others.Helper;
import dev.paridhi.raven.others.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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


    private void sendMessage() {
        String message=binding.chatMessage.getText().toString();
        if(!message.isEmpty())
        {

            ApiMessageModel apiMessageModel=new ApiMessageModel(message);

            Call<ApiResponseModel> call= RetrofitClient.getInstance().getApi().getMessageType(apiMessageModel);

            call.enqueue(new Callback<ApiResponseModel>() {
                @Override
                public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                    try {
                        String pred=response.body().getPrediction();
                        Toast.makeText(getApplicationContext(),pred,Toast.LENGTH_LONG).show();


                        if(pred=="ham")
                        {
                            DocumentReference documentReference=firestore.collection("channels").document(channelID).collection("messages").document();

                            MessageModel messageModel=new MessageModel(message,senderID);
                            documentReference.set(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    DocumentReference channels=firestore.collection("channels").document(channelID);
                                    Map<String,Object> LastMessage=new HashMap<>();
                                    LastMessage.put("lastmessage",message);
                                    LastMessage.put("time", FieldValue.serverTimestamp());
                                    channels.update(LastMessage).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                        else
                        {
                            DocumentReference documentReference=firestore.collection("channels").document(channelID).collection("spam").document();

                            MessageModel messageModel=new MessageModel(message,senderID);
                            documentReference.set(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    DocumentReference channels=firestore.collection("channels").document(channelID);
                                    Map<String,Object> LastSpamMessage=new HashMap<>();
                                    LastSpamMessage.put("lastSpamMessage",message);
                                    LastSpamMessage.put("time", FieldValue.serverTimestamp());
                                    channels.update(LastSpamMessage).addOnSuccessListener(new OnSuccessListener<Void>() {
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

                    }catch (Exception e)
                    {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();

                    }


                }

                @Override
                public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();

                }
            });




        }
        adapter.notifyDataSetChanged();

    }
}