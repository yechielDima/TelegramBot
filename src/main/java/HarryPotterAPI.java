import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class  HarryPotterAPI {
    private final String allData;
    private final ArrayList<HPCharacter> characters;


    public HarryPotterAPI() throws UnirestException {
        HttpResponse<String> response = Unirest.get("https://hp-api.onrender.com/api/characters").asString();
        this.allData = response.getBody();
        this.characters = new ArrayList<>();

        sortAllDataToSeparateCharacters();
    }

    public void sortAllDataToSeparateCharacters(){
        for (int i = 0; i < this.allData.length() - 88; i++) {
            if (this.allData.charAt(i) == '{'
                    && this.allData.charAt(i + 1) == '"'
                    && this.allData.charAt(i + 2) == 'i'
                    && this.allData.charAt(i + 3) == 'd'
                    && this.allData.charAt(i + 4) == '"'
                    && this.allData.charAt(i + 5) == ':'
                    && this.allData.charAt(i + 6) == '"'){
                this.characters.add(new HPCharacter(this.allData.substring(i + 7 , i + 43) , extractName(this.allData.substring(i + 53 , i + 83))));
            }
        }
    }

    public String extractName(String text){
        String cleanName = "";
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '"' && text.charAt(i + 1) == ','){
                cleanName = text.substring(0 , i);
                break;
            }
        }
        return cleanName;
    }

    public String findCharacterIdByName(String name){
        String id = null;
        for (HPCharacter character : this.characters) {
            if (character.getName().toLowerCase().contains(name.toLowerCase())){
                id = character.getId();
                break;
            }
        }
        return id;
    }

    public HPCharacter getAllInformation(String id) throws UnirestException {

        int indexOfDesiredCharacter = -1;
        for (int i = 0; i < this.characters.size(); i++) {
            if (this.characters.get(i).getId().equals(id)){
                indexOfDesiredCharacter = i;
                break;
            }
        }

        if (indexOfDesiredCharacter == -1){
            return null;
        }

        if (this.characters.get(indexOfDesiredCharacter).hasBeanInitialized()){
            System.out.println("less work!");
            return this.characters.get(indexOfDesiredCharacter);
        }

        HttpResponse<String> response = Unirest.get("https://hp-api.onrender.com/api/character/" + id).asString();
        String data = response.getBody();

        this.characters.get(indexOfDesiredCharacter).setSpecies(data.substring((data.indexOf("species\":") + 10) , data.indexOf(",\"gender")-1));
        this.characters.get(indexOfDesiredCharacter).setGender(data.substring((data.indexOf(",\"gender\"") + 11) , data.indexOf(",\"house")-1));
        this.characters.get(indexOfDesiredCharacter).setHouse(data.substring((data.indexOf(",\"house\"") + 10) , data.indexOf(",\"dateOf")-1));
        this.characters.get(indexOfDesiredCharacter).setDateOfBirth(data.substring((data.indexOf(",\"dateOfBirth\"") + 16) , data.indexOf(",\"yearOfBirth") - 1));
        this.characters.get(indexOfDesiredCharacter).setWizard(Boolean.parseBoolean(data.substring((data.indexOf(",\"wizard\"") + 10) , data.indexOf(",\"ancestry"))));
        this.characters.get(indexOfDesiredCharacter).setHogwartsStudent(Boolean.parseBoolean(data.substring((data.indexOf(",\"hogwartsStudent") + 19) , data.indexOf(",\"hogwartsStaff"))));
        this.characters.get(indexOfDesiredCharacter).setHogwartsStaff(Boolean.parseBoolean(data.substring((data.indexOf(",\"hogwartsStaff") + 17) , data.indexOf(",\"actor"))));
        this.characters.get(indexOfDesiredCharacter).setActor(data.substring((data.indexOf(",\"actor\"") + 10) , data.indexOf(",\"alternate_actors")-1));
        this.characters.get(indexOfDesiredCharacter).setAlive(Boolean.parseBoolean(data.substring((data.indexOf(",\"alive") + 9) , data.indexOf(",\"image"))));
        this.characters.get(indexOfDesiredCharacter).setHasBeanInitialized(true);

        return this.characters.get(indexOfDesiredCharacter);
    }

    public String[] extractTextFromHPCharacter(HPCharacter hpCharacter){
        return new String[]{
                "name: " + hpCharacter.getName() ,
                "species: " + hpCharacter.getSpecies() ,
                "gender: " + hpCharacter.getGender() ,
                "is wizard: " + hpCharacter.isWizard() ,
                "date of birth: " + hpCharacter.getDateOfBirth() ,
                "is Hogwarts student: " + hpCharacter.isHogwartsStudent() ,
                "Hogwarts house: " + hpCharacter.getHouse() ,
                "is Hogwarts staff: " + hpCharacter.isHogwartsStaff() ,
                "is alive: " + hpCharacter.isAlive() ,
                "real actor: " + hpCharacter.getActor()
        };
    }
// !!!!יאווו אלוהים יעיד כמה קשה זה היה
}
