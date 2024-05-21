import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Random;

// להפוך למחלקה גנרית שהרי יש המון כפילויות
public class ObjectMapper {

    HarryPotterAPI harryPotterAPI;
    HebrewJocksAPI hebrewJocksAPI;
    public ObjectMapper() throws UnirestException, FileNotFoundException, URISyntaxException {
        this.harryPotterAPI = new HarryPotterAPI();
        this.hebrewJocksAPI = new HebrewJocksAPI();
    }



    public String getJockReadyToSend() throws UnirestException, JsonProcessingException {
        String responseBody;
        HttpResponse<String> response;

        if (ChoicesPanel.isJocksSafe()){
            response = Unirest.get("https://v2.jokeapi.dev/joke/Any?safe-mode").asString();
        }
        else {
            response = Unirest.get("https://v2.jokeapi.dev/joke/Any").asString();
        }
        responseBody = response.getBody();


        com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
        JokeAPI jokeAPI = objectMapper.readValue(responseBody , JokeAPI.class);


        if (jokeAPI.getJokeText() != null || jokeAPI.getSetup() != null){
            if (jokeAPI.getJokeText() != null){
                return jokeAPI.getJokeText();

            }
            else {
                return jokeAPI.getSetup() + "\n" + jokeAPI.getDelivery();
            }
        }
        return "error: Something went wrong, contact your provider";

    }


    public String getRandomHebrewChuckNorrisJock(){
        Random random = new Random();
        if (!this.hebrewJocksAPI.isCrashed()){
            return this.hebrewJocksAPI.getChuckNorrisHebrewJocks().get(random.nextInt(this.hebrewJocksAPI.getChuckNorrisHebrewJocks().size() - 1));
        }
        return "error: Something went wrong, contact your provider";
    }


    public String getChuckNorrisEnglishJock(String requestedCategory) throws UnirestException, JsonProcessingException {
        List<String> categories = List.of("animal","career","celebrity","dev","explicit","fashion","food","history","money","movie","music","political","religion","science","sport","travel");
        String responseBody;

        String numberCheck = extractType(requestedCategory , categories);
        if (numberCheck != null){
            requestedCategory = numberCheck;
        }

        if (requestedCategory.equalsIgnoreCase("random")){
            HttpResponse<String> response = Unirest.get("https://api.chucknorris.io/jokes/random").asString();
            responseBody = response.getBody();
        }
        else if (categories.contains(requestedCategory.toLowerCase())) {
            HttpResponse<String> response = Unirest.get("https://api.chucknorris.io/jokes/random?category=" + requestedCategory.toLowerCase()).asString();
            responseBody = response.getBody();
        }
        else{
            return "null";
        }

        com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
        ChuckNorrisEnglishJocksAPI chuckNorrisEnglishJocksAPI = objectMapper.readValue(responseBody , ChuckNorrisEnglishJocksAPI.class);

        if (chuckNorrisEnglishJocksAPI.getValue() != null){
            return chuckNorrisEnglishJocksAPI.getValue();
        }

        return "error: Something went wrong, contact your provider";
    }




    public String getFactsAboutNumber(String StrNumber) throws UnirestException {

        if (StrNumber.equalsIgnoreCase("random")){
            HttpResponse<String> response = Unirest.get("http://numbersapi.com/random").asString();
            return response.getBody();

        }
        else {
            int number;
            try {
                number = Integer.parseInt(StrNumber);
            } catch (NumberFormatException nfe) {
                return "null";
            }

            HttpResponse<String> response = Unirest.get("http://numbersapi.com/" + number).asString();
            return response.getBody();
        }

    }



    public String getHPCharacterFullInformation(String characterName) throws UnirestException {
        StringBuilder HPCharacterToReturn = new StringBuilder();

        String idForThatCharacter = this.harryPotterAPI.findCharacterIdByName(characterName);

        if (idForThatCharacter != null){
            HPCharacter hpCharacter = this.harryPotterAPI.getAllInformation(idForThatCharacter);

            for (String str : this.harryPotterAPI.extractTextFromHPCharacter(hpCharacter)) {
                HPCharacterToReturn.append(str).append("\n");
            }

            return String.valueOf(HPCharacterToReturn);
        }

        return "null";
    }




    public String getInformationAboutCountries(String choice ,String nameOrCodeToSend) throws JsonProcessingException, UnirestException {

        StringBuilder toReturn = new StringBuilder();
        com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
        String jsonToCountry = "";


        if (choice.equalsIgnoreCase("name")) {
            HttpResponse<String> response = Unirest.get("https://restcountries.com/v2/name/" + nameOrCodeToSend).asString();
            jsonToCountry = response.getBody().substring(1, response.getBody().length() - 1);
        }
        else if (choice.equalsIgnoreCase("code")) {
            HttpResponse<String> response = Unirest.get("https://restcountries.com/v2/alpha/" + nameOrCodeToSend).asString();
            jsonToCountry = response.getBody();

        }

        CountryModel countryModel = objectMapper.readValue(jsonToCountry, CountryModel.class);

        if (countryModel.getName() == null) {
            return "null";
        }
        else {
            for (String str : countryModel.getFullInformationAboutCountry()) {
                toReturn.append(str).append("\n");
            }

            return String.valueOf(toReturn);
        }

    }




    public String getActivityForBored(String requestedType) throws UnirestException, JsonProcessingException {
        List<String> types = List.of("education", "recreational", "social", "diy", "charity", "cooking", "relaxation", "music", "busywork");
        String responseBody;

        String numberCheck = extractType(requestedType , types);
        if (numberCheck != null){
            requestedType = numberCheck;
        }

        if (requestedType.equalsIgnoreCase("random")){
            HttpResponse<String> response = Unirest.get("https://www.boredapi.com/api/activity/").asString();
            responseBody = response.getBody();
        }
        else if (types.contains(requestedType.toLowerCase())) {
            HttpResponse<String> response = Unirest.get("https://www.boredapi.com/api/activity?type=" + requestedType.toLowerCase()).asString();
            responseBody = response.getBody();
        }
        else {
            return "null";
        }

        com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
        IamBoredAPI iamBoredAPI = objectMapper.readValue(responseBody , IamBoredAPI.class);

        if (iamBoredAPI.getActivity() != null){
            return "activity: " + iamBoredAPI.getActivity()
                    +"\ntype: " + iamBoredAPI.getType()
                    +"\nparticipants: " + iamBoredAPI.getParticipants()
                    +"\nprice: " + iamBoredAPI.getPrice()
                    +"\naccessibility: " + iamBoredAPI.getAccessibility()
                    +"\nlink: " + iamBoredAPI.getLink()
                    +"\n\nThe accessibility and price values are on a scale from 1 to 10.";

        }
        return "error: Something went wrong, contact your provider";
    }


    public String extractType(String input , List<String> list){
        Integer number = null;
        try {
            number = Integer.parseInt(input) -1;
        } catch (NumberFormatException ignored) {
        }

        if (number != null){
            if (number >= 0 && number < list.size()){
                return  list.get(number);
            }
        }
        return null;
    }

}