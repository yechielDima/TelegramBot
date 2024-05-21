import java.time.LocalDateTime;
import java.util.*;

public class MyUser {
    private final String userName;
    private final String firstName;
    private final String lastName;
    private final long userChatID;
    private Integer phase;
    private HashMap<LocalDateTime , String> messages;



    public MyUser(String userName , long userChatID , String firstName , String lastName){
        this.messages = new HashMap<>();
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userChatID = userChatID;
        this.phase = null;
    }

    public void setPhase(Integer phase) {
        this.phase = phase;
    }
    public void addMessages(LocalDateTime date, String message) {
        this.messages.put(date,message);
    }
    public Integer getPhase() {
        return phase;
    }
    public long getUserChatID() {
        return userChatID;
    }
    public String getUserName() {
        return userName;
    }
    public int getNumberOfMessages(){
        return this.messages.size();
    }
    public HashMap<LocalDateTime, String> getMessages() {
        return messages;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        if (this.userName != null){
            return "name: " + this.userName + " ,ID: " + this.userChatID;
        }
        return "name: " + this.firstName + " " + this.lastName + ", id: " + this.userChatID;
    }

    public String getMassagesForAdmin(){
        StringBuilder stringBuilder = new StringBuilder();
        Object[] keys = this.messages.keySet().toArray();
        Object[] values = this.messages.values().toArray();
        for (int i = 0; i < this.messages.size(); i++) {
            stringBuilder.append(keys[i].toString()).append(values[i].toString()).append("\n");
        }
        return String.valueOf(stringBuilder);
    }
    public String getUserFullInfoForAdmin(){
        return "user name: " + this.userName
                + "\nfirst name: " + this.firstName
                + "\nlast name: " + this.lastName
                + "\nchat ID: " + this.userChatID
                + "\nphase: " + this.phase
                + "\nmassages: " + getMassagesForAdmin()
                + "\n\n\n";
    }
    public String getAbsoluteNameForAdmin(){
        return "user name: " + this.userName
                + "\nfirst name: " + this.firstName
                + "\nlast name: " + this.lastName
                + "\nID: " + this.userChatID
                +"\n\n";
    }

}
