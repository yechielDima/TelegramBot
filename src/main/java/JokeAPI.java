import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JokeAPI {

    private String type;
    private String category;
    private String joke;
    private String setup;
    private String delivery;

    public void setCategory(String category) {
        this.category = category;
    }
    public String getCategory() {
        return this.category;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return this.type;
    }

    public void setJoke(String joke) {
        this.joke = joke;
    }
    public String getJokeText() {
        return this.joke;
    }

    public void setSetup(String setup) {
        this.setup = setup;
    }
    public String getSetup() {
        return this.setup;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }
    public String getDelivery() {
        return delivery;
    }
    public static final String SAFE_JOCKS_PASSWORD = "214774";


}
