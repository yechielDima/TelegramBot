import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IamBoredAPI {
    private String activity;
    private String type;
    private Double participants;
    private Double price;
    private Double accessibility;
    private String link;

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getParticipants() {
        return (int) (participants * 1);
    }

    public void setParticipants(Double participants) {
        this.participants = participants;
    }

    public int getPrice() {
        return (int) (price * 10);
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getAccessibility() {
        return (int) (accessibility * 10);
    }

    public void setAccessibility(Double accessibility) {
        this.accessibility = accessibility;
    }

    public String getLink() {
        if (this.link.equals("")){
            return "Don't need one";
        }
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
