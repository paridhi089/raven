package dev.paridhi.raven.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.List;

public class ChannelModel {
    List<String> members;
    String lastmessage;

    @ServerTimestamp
    Timestamp time;

    public ChannelModel(List<String> members, String lastmessage, Timestamp time) {
        this.members = members;
        this.lastmessage = lastmessage;
        this.time = time;
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public void setLastmessage(String lastmessage) {
        this.lastmessage = lastmessage;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public ChannelModel() {
    }

    public ChannelModel(List<String> members) {
        this.members = members;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }


    public String getOtherUser(String CurrentUID)
    {
        String User1=members.get(0).toString();
        String User2=members.get(1).toString();

        if(User1.equals(CurrentUID))
            return User2;
        else
            return User1;

    }
}


