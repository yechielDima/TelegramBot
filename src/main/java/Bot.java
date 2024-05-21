import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Bot extends TelegramLongPollingBot {

    public static final String[] ADMIN_THINGS = new String[]{"USER DATA" , "ALL USERS" , "ALL DATA" , "STOP BOT"};
    public static final String RESTART_BOT_WORD = "RESTART";
    public static final String BOT_HOME_PAGE = "HOME";
    public static HashMap<Long , MyUser> users;
    public static ArrayList<String> lastUsers;
    public static ArrayList<String> lastMassages;
    public static ArrayList<String> lastDates;
    public static int[] usersAndMassagesInLastMinute;
    private final String[] choiceOptions;
    private String[] selectedChoices;
    public static int[] selectedChoicesUses;
    InlineKeyboardButton[] buttons;
    DateTimeFormatter formatter;
    ObjectMapper objectMapper;
    private final String botName = "bot's name"; // Enter your bot's name
    private final String botToken = "bot's token"; //Enter your bot's token

    public Bot() throws FileNotFoundException, UnirestException, URISyntaxException {
        objectMapper = new ObjectMapper();

        users = new HashMap<>();
        lastUsers = new ArrayList<>(10);
        lastMassages = new ArrayList<>(10);
        lastDates = new ArrayList<>(10);
        usersAndMassagesInLastMinute = new int[2];

        this.formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yy");
        this.buttons = new InlineKeyboardButton[ChoicesPanel.getChoiceOptions().length];
        this.choiceOptions = ChoicesPanel.getChoiceOptions();
        this.selectedChoices = ChoicesPanel.getSelectedChoices();
        selectedChoicesUses = new int[3];
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {

        long chatId;
        String textMessageData = "";
        String callBackData = "";

        List<InlineKeyboardButton> topRow = new ArrayList<>();
        List<List<InlineKeyboardButton>> keyboard = List.of(topRow);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(keyboard);


        if (update.hasCallbackQuery()){
            chatId = update.getCallbackQuery().getMessage().getChatId();
        }
        else {
            chatId = update.getMessage().getChatId();
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        usersAndMassagesInLastMinute[0] = users.size();
        usersAndMassagesInLastMinute[1]++;

        if (update.hasMessage()) {
            if (update.getMessage().getText().equalsIgnoreCase(RESTART_BOT_WORD)){
                sendMessage.setText("The bot has been reset!\n all your messages have been deleted!\n have a nice day.");
                sendMessageWithExceptions(sendMessage);
                users.remove(chatId);
            }
        }

        if (!users.containsKey(chatId)) {
            sendMessage.setText(
                    "hi this is "+botName+"\n"
                     + "type anything to start"
            );

            if (chatId == Main.ADMIN_ID){
                sendMessage.setText("welcome back my lord"); // XD
            }

            users.put(chatId, new MyUser(update.getMessage().getFrom().getUserName(), chatId , update.getMessage().getFrom().getFirstName() , update.getMessage().getFrom().getLastName()));
            users.get(chatId).setPhase(1);

        }

        else {

            if (update.hasMessage()) {
                textMessageData = update.getMessage().getText();

                if (textMessageData.equals(BOT_HOME_PAGE)){
                    users.get(chatId).setPhase(3);
                }

                if (chatId == Main.ADMIN_ID){
                    if (textMessageData.toLowerCase().contains(ADMIN_THINGS[0])){
                        long cidTs = Long.parseLong(textMessageData.substring(10));
                        if (users.containsKey(cidTs)){
                            sendMessage.setText(users.get(cidTs).getUserFullInfoForAdmin());
                            sendMessageWithExceptions(sendMessage);
                        }
                    }
                    if (textMessageData.equalsIgnoreCase(ADMIN_THINGS[1])){
                        if (users.isEmpty()){
                            sendMessage.setText("users.isEmpty");
                            sendMessageWithExceptions(sendMessage);
                        }
                        else {
                            sendMessage.setText("users: " + users.size() + "\n\n"
                                    + users.values()
                                            .stream()
                                            .map(MyUser::getAbsoluteNameForAdmin)
                                            .toList()
                                    );
                            sendMessageWithExceptions(sendMessage);
                        }
                    }
                    if (textMessageData.equalsIgnoreCase(ADMIN_THINGS[2])){
                        if (users.isEmpty()){
                            sendMessage.setText("users.isEmpty");
                            sendMessageWithExceptions(sendMessage);
                        }
                        else {
                            sendMessage.setText("users: " + users.size() + "\n\n"
                                    + users.values()
                                    .stream()
                                    .map(MyUser::getUserFullInfoForAdmin)
                                    .toList()
                            );
                            sendMessageWithExceptions(sendMessage);
                        }
                    }
                }
            }



            String[] firstButtons = new String[]{"about" , "start"};
            if (users.get(chatId).getPhase() == 1) {

                sendMessage.setText(
                        "welcome to "+botName+"\n" +
                        "what would you like to do"
                );

                setButtonsText(topRow, firstButtons);

                users.get(chatId).setPhase(11);
            }


            if (users.get(chatId).getPhase() == 11){
                callBackData = "";
                textMessageData = "";

                if (update.hasCallbackQuery()){
                    callBackData = update.getCallbackQuery().getData();
                }
                if (update.hasMessage()) {
                    textMessageData = update.getMessage().getText();
                }

                if (callBackData.equalsIgnoreCase(firstButtons[0].toUpperCase()) || textMessageData.equalsIgnoreCase(firstButtons[0])){
                    users.get(chatId).setPhase(12);
                }
                else if (callBackData.equalsIgnoreCase(firstButtons[1].toUpperCase()) || textMessageData.equalsIgnoreCase(firstButtons[1])) {
                    users.get(chatId).setPhase(3);
                }
                else {
                    users.get(chatId).setPhase(1);
                }
            }



            if (users.get(chatId).getPhase() == 12){
                sendMessage.setReplyMarkup(null);
                sendMessage.setText(
                        "welcome to "+botName+" tutorial."
                        + "\n\nThroughout your interaction with the bot, options will appear based on what your provider has selected."
                        + "\nYou can select any of the options by clicking the buttons or typing the desired action."
                        + "\n\nDuring your activity with the bot, you can always return to the main page by typing: " + BOT_HOME_PAGE
                        + "\n\nIn case you will ever need it, you can reset the bot by typing: " + RESTART_BOT_WORD
                        + "\nthe bot will delete all information about you and you will be set as a new user!"
                        + "\n\nHope you enjoy your time with my bot"
                        + "\n\nbot name: "+botName
                        + "\nbot version: " + Main.BOT_VERSION
                        + "\ncrated by: daniel sasi & yechiel dimenshtein"

                );

                sendMessageWithExceptions(sendMessage);

                users.get(chatId).setPhase(13);

            }

            if (users.get(chatId).getPhase() == 13) {
                topRow = new ArrayList<>();

                this.buttons[1] = new InlineKeyboardButton();
                this.buttons[1].setText(firstButtons[1]);
                this.buttons[1].setCallbackData(firstButtons[1].toUpperCase());
                topRow.add(this.buttons[1]);

                keyboard = List.of(topRow);
                inlineKeyboardMarkup = new InlineKeyboardMarkup();
                inlineKeyboardMarkup.setKeyboard(keyboard);
                sendMessage.setReplyMarkup(inlineKeyboardMarkup);

                sendMessage.setText("to start - press or type start");

                users.get(chatId).setPhase(14);
            }

            if (users.get(chatId).getPhase() == 14){
                callBackData = "";
                textMessageData = "";

                if (update.hasCallbackQuery()){
                    callBackData = update.getCallbackQuery().getData();
                }
                if (update.hasMessage()) {
                    textMessageData = update.getMessage().getText();
                }

                if (callBackData.equals(firstButtons[1].toUpperCase()) || textMessageData.equalsIgnoreCase(firstButtons[1])){
                    users.get(chatId).setPhase(3);
                }
                else {
                    users.get(chatId).setPhase(13);
                }
            }


            if (users.get(chatId).getPhase() == 2){
                callBackData = "";
                textMessageData = "";
                boolean needElse = true;

                if (update.hasCallbackQuery()){
                    callBackData = update.getCallbackQuery().getData();
                }

                if (update.hasMessage()) {
                    textMessageData = update.getMessage().getText();

                }


                if (callBackData.equals(this.selectedChoices[0].toUpperCase()) || textMessageData.equalsIgnoreCase(this.selectedChoices[0])){
                    selectedChoicesUses[0]++;
                    users.get(chatId).setPhase(4);
                    needElse = false;
                }
                if (callBackData.equals(this.selectedChoices[1].toUpperCase())|| textMessageData.equalsIgnoreCase(this.selectedChoices[1])) {
                    selectedChoicesUses[1]++;
                    users.get(chatId).setPhase(4);
                    needElse = false;
                }
                if (callBackData.equals(this.selectedChoices[2].toUpperCase()) || textMessageData.equalsIgnoreCase(this.selectedChoices[2])) {
                    selectedChoicesUses[2]++;
                    users.get(chatId).setPhase(4);
                    needElse = false;
                }
                if (Arrays.toString(this.choiceOptions).contains(textMessageData.toLowerCase()) && textMessageData.length() > 3 && !Arrays.toString(this.selectedChoices).contains(textMessageData.toLowerCase())) {
                    sendMessage.setText("This option isn't available for you, speak to your provider for further details");
                    sendMessageWithExceptions(sendMessage);
                    users.get(chatId).setPhase(3);

                }
                else if (needElse && !callBackData.equals(firstButtons[1].toUpperCase()) && !textMessageData.equalsIgnoreCase(firstButtons[1])) {
                    sendMessage.setText("This option does not exist (yet)");
                    sendMessageWithExceptions(sendMessage);
                    users.get(chatId).setPhase(3);
                }

            }

            if (users.get(chatId).getPhase() == 3){
                topRow = new ArrayList<>();

                int c = 0;
                for (InlineKeyboardButton button : this.buttons) {
                    button = new InlineKeyboardButton();
                    button.setText(this.choiceOptions[c]);
                    button.setCallbackData(this.choiceOptions[c].toUpperCase());
                    this.selectedChoices = ChoicesPanel.getSelectedChoices();
                    if (Arrays.toString(this.selectedChoices).contains(button.getText())) {
                        topRow.add(button);
                    }
                    c++;
                }

                keyboard = List.of(topRow);
                inlineKeyboardMarkup = new InlineKeyboardMarkup();
                inlineKeyboardMarkup.setKeyboard(keyboard);

                sendMessage.setText("To select an option just type or click the button");

                sendMessage.setReplyMarkup(inlineKeyboardMarkup);

                users.get(chatId).setPhase(2);

            }


            if (users.get(chatId).getPhase() == 4){

                if (callBackData.equals(this.choiceOptions[0].toUpperCase()) || textMessageData.equalsIgnoreCase(this.choiceOptions[0])){ // jocks
                    users.get(chatId).setPhase(50);
                }
                if (callBackData.equals(this.choiceOptions[1].toUpperCase()) || textMessageData.equalsIgnoreCase(this.choiceOptions[1])){ // numbers
                    users.get(chatId).setPhase(51);
                }
                if (callBackData.equals(this.choiceOptions[2].toUpperCase()) || textMessageData.equalsIgnoreCase(this.choiceOptions[2])){ // HPC
                    users.get(chatId).setPhase(52);
                }
                if (callBackData.equals(this.choiceOptions[3].toUpperCase()) || textMessageData.equalsIgnoreCase(this.choiceOptions[3])){ // country
                    users.get(chatId).setPhase(53);
                }
                if (callBackData.equals(this.choiceOptions[4].toUpperCase()) || textMessageData.equalsIgnoreCase(this.choiceOptions[4])){ // I'm bored
                    users.get(chatId).setPhase(54);
                }

            }



            String[] jocksAPIButtons = new String[]{"random" , "chuck norris"};
            String[] cNJocksHEOrENButtons = new String[]{"עברית" , "English"};
            String[] cNJocksENButtons = new String[]{"random" , "by category"};
            if (users.get(chatId).getPhase() == 501){
                callBackData = "";
                textMessageData = "";

                if (update.hasCallbackQuery()){
                    callBackData = update.getCallbackQuery().getData();
                }
                if (update.hasMessage()) {
                    textMessageData = update.getMessage().getText();
                }

                if (callBackData.equals(jocksAPIButtons[0].toUpperCase()) || textMessageData.equalsIgnoreCase(jocksAPIButtons[0])){
                    users.get(chatId).setPhase(5011);
                }
                else if (callBackData.equalsIgnoreCase(jocksAPIButtons[1]) || textMessageData.equalsIgnoreCase(jocksAPIButtons[1]) || textMessageData.toLowerCase().contains("chuck") || textMessageData.toLowerCase().contains("norris")) {
                    users.get(chatId).setPhase(5012);
                }
                else {
                    users.get(chatId).setPhase(50);
                }
            }

            if (users.get(chatId).getPhase() == 50){
                sendMessage.setText("choose an option:");
                setButtonsText(topRow, jocksAPIButtons);

                users.get(chatId).setPhase(501);
            }

            if (users.get(chatId).getPhase() == 5011){
                try {
                    sendMessage.setText(objectMapper.getJockReadyToSend());
                } catch (UnirestException | JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                users.get(chatId).setPhase(3);
            }

            if (users.get(chatId).getPhase() == 5013){
                callBackData = "";
                textMessageData = "";

                if (update.hasCallbackQuery()){
                    callBackData = update.getCallbackQuery().getData();
                }
                if (update.hasMessage()) {
                    textMessageData = update.getMessage().getText();
                }

                if (callBackData.equals(cNJocksHEOrENButtons[0].toUpperCase()) || textMessageData.equalsIgnoreCase(cNJocksHEOrENButtons[0]) || textMessageData.equalsIgnoreCase("Hebrew")){
                    users.get(chatId).setPhase(50131);
                }
                else if (callBackData.equals(cNJocksHEOrENButtons[1].toUpperCase()) || textMessageData.equalsIgnoreCase(cNJocksHEOrENButtons[1])) {
                    users.get(chatId).setPhase(50132);
                }
                else {
                    users.get(chatId).setPhase(5012);
                }
            }

            if (users.get(chatId).getPhase() == 5012){
                sendMessage.setText("choose an option:");
                setButtonsText(topRow, cNJocksHEOrENButtons);

                users.get(chatId).setPhase(5013);
            }

            if (users.get(chatId).getPhase() == 50131){

                sendMessage.setText(objectMapper.getRandomHebrewChuckNorrisJock());

                users.get(chatId).setPhase(3);
            }

            if (users.get(chatId).getPhase() == 50133){
                callBackData = "";
                textMessageData = "";

                if (update.hasCallbackQuery()){
                    callBackData = update.getCallbackQuery().getData();
                }
                if (update.hasMessage()) {
                    textMessageData = update.getMessage().getText();
                }

                if (callBackData.equals(cNJocksENButtons[0].toUpperCase()) || textMessageData.equalsIgnoreCase(cNJocksENButtons[0])){
                    users.get(chatId).setPhase(501331);
                }
                else if (callBackData.equals(cNJocksENButtons[1].toUpperCase()) || textMessageData.equalsIgnoreCase(cNJocksENButtons[1]) || textMessageData.toLowerCase().contains("category")) {
                    users.get(chatId).setPhase(501332);
                }
                else {
                    users.get(chatId).setPhase(50132);
                }
            }

            if (users.get(chatId).getPhase() == 50132){
                sendMessage.setText("choose an option:");
                setButtonsText(topRow, cNJocksENButtons);

                users.get(chatId).setPhase(50133);
            }

            if (users.get(chatId).getPhase() == 501331){
                try {
                    sendMessage.setText(objectMapper.getChuckNorrisEnglishJock("random"));
                } catch (UnirestException | JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                users.get(chatId).setPhase(3);
            }

            if (users.get(chatId).getPhase() == 501333){
                if (update.hasMessage()) {
                    if (!update.getMessage().getText().equalsIgnoreCase(cNJocksENButtons[1])){
                        textMessageData = update.getMessage().getText();
                    }
                    else {
                        textMessageData = "";
                    }
                }

                if (!textMessageData.equals("")){
                    try {
                        sendMessage.setText(objectMapper.getChuckNorrisEnglishJock(textMessageData));
                    } catch (UnirestException | JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                    if (sendMessage.getText().equals("null")){
                        sendMessage.setText("category not found, please try again \nYou can always return to the home page by typing: " + BOT_HOME_PAGE);
                        sendMessageWithExceptions(sendMessage);
                        users.get(chatId).setPhase(501332);
                    }
                    else {
                        users.get(chatId).setPhase(3);
                    }
                }
            }

            if (users.get(chatId).getPhase() == 501332){
                sendMessage.setText("""
                                Type the desired category from the following list:\s
                                1. animal\s
                                2. career\s
                                3. celebrity\s
                                4. dev\s
                                5. explicit\s
                                6. fashion\s
                                7. food\s
                                8. history\s
                                9. money\s
                                10. movie\s
                                11. music\s
                                12. political\s
                                13. religion\s
                                14. science\s
                                15. sport\s
                                16. travel
                                """);
                users.get(chatId).setPhase(501333);
            }






            String[] numberApiButtons = new String[]{"random" , "pick a number"};
            if (users.get(chatId).getPhase() == 511){
                callBackData = "";
                textMessageData = "";

                if (update.hasCallbackQuery()){
                    callBackData = update.getCallbackQuery().getData();
                }
                if (update.hasMessage()) {
                    textMessageData = update.getMessage().getText();
                }

                if (callBackData.equals(numberApiButtons[0].toUpperCase()) || textMessageData.equalsIgnoreCase(numberApiButtons[0])){
                    users.get(chatId).setPhase(5111);
                }
                else if (callBackData.equals(numberApiButtons[1].toUpperCase()) || textMessageData.equalsIgnoreCase(numberApiButtons[1]) || textMessageData.toLowerCase().contains("pick") || textMessageData.toLowerCase().contains("number")) {
                    users.get(chatId).setPhase(5112);
                }
                else {
                    users.get(chatId).setPhase(51);
                }
            }

            if (users.get(chatId).getPhase() == 51){

                sendMessage.setText("choose an option:");
                setButtonsText(topRow, numberApiButtons);

                users.get(chatId).setPhase(511);
            }

            if (users.get(chatId).getPhase() == 5111){
                try {
                    sendMessage.setText(objectMapper.getFactsAboutNumber(numberApiButtons[0]));
                } catch (UnirestException e) {
                    throw new RuntimeException(e);
                }
                users.get(chatId).setPhase(3);
            }

            if (users.get(chatId).getPhase() == 5113){

                if (update.hasMessage()) {
                    if (!update.getMessage().getText().equalsIgnoreCase(numberApiButtons[1])){
                        textMessageData = update.getMessage().getText();
                    }
                    else {
                        textMessageData = "";
                    }
                }

                if (!textMessageData.equals("")){
                    try {
                        sendMessage.setText(objectMapper.getFactsAboutNumber(textMessageData));
                    } catch (UnirestException e) {
                        throw new RuntimeException(e);
                    }
                    if (sendMessage.getText().equals("null")){
                        sendMessage.setText("invalid number, please try again\nYou can always return to the home page by typing: " + BOT_HOME_PAGE);
                        sendMessageWithExceptions(sendMessage);
                        users.get(chatId).setPhase(5112);
                    }
                    else {
                        users.get(chatId).setPhase(3);
                    }
                }
            }

            if (users.get(chatId).getPhase() == 5112){

                sendMessage.setText("type any number:");

                users.get(chatId).setPhase(5113);
            }




            if (users.get(chatId).getPhase() == 521){

                if (update.hasMessage()) {
                    if (!update.getMessage().getText().equalsIgnoreCase(this.choiceOptions[2])){
                        textMessageData = update.getMessage().getText();
                    }
                    else {
                        textMessageData = "";
                    }
                }

                if (!textMessageData.equals("")){
                    try {
                        sendMessage.setText(objectMapper.getHPCharacterFullInformation(textMessageData));
                    } catch (UnirestException e) {
                        throw new RuntimeException(e);
                    }
                    if (sendMessage.getText().equals("null")){
                        sendMessage.setText("character not found, please try again\nYou can always return to the home page by typing: " + BOT_HOME_PAGE);
                        sendMessageWithExceptions(sendMessage);
                        users.get(chatId).setPhase(52);
                    }
                    else {
                        users.get(chatId).setPhase(3);
                    }
                }
            }

            if (users.get(chatId).getPhase() == 52){
                sendMessage.setText("character name:"
                        + "\nnote: write full name for maximum accuracy");
                users.get(chatId).setPhase(521);
            }



            String[] countriesApiButtons = new String[]{"by name" , "by alpha code"};
            if (users.get(chatId).getPhase() == 531){

                callBackData = "";
                textMessageData = "";

                if (update.hasCallbackQuery()){
                    callBackData = update.getCallbackQuery().getData();
                }
                if (update.hasMessage()) {
                    textMessageData = update.getMessage().getText();
                }

                if (callBackData.equals(countriesApiButtons[0].toUpperCase()) || textMessageData.equalsIgnoreCase(countriesApiButtons[0]) || textMessageData.contains("name")){
                    users.get(chatId).setPhase(5311);
                }
                else if (callBackData.equals(countriesApiButtons[1].toUpperCase()) || textMessageData.toLowerCase().equalsIgnoreCase(countriesApiButtons[1]) || textMessageData.contains("code")) {
                    users.get(chatId).setPhase(5312);
                }
                else {
                    users.get(chatId).setPhase(53);
                }
            }

            if (users.get(chatId).getPhase() == 53){

                sendMessage.setText("choose an option:");
                setButtonsText(topRow, countriesApiButtons);

                users.get(chatId).setPhase(531);
            }

            if (users.get(chatId).getPhase() == 53111){

                if (update.hasMessage()) {
                    if (!update.getMessage().getText().equalsIgnoreCase(countriesApiButtons[0]) && !countriesApiButtons[0].contains(update.getMessage().getText())){
                        textMessageData = update.getMessage().getText();
                    }
                    else {
                        textMessageData = "";
                    }
                }
                if (!textMessageData.equals("")){
                    try {
                        sendMessage.setText(objectMapper.getInformationAboutCountries("name" , textMessageData));
                    } catch (JsonProcessingException | UnirestException e) {
                        throw new RuntimeException(e);
                    }
                    if (sendMessage.getText().equals("null")){
                        sendMessage.setText("Country name not found, please try again \nYou can always return to the home page by typing: " + BOT_HOME_PAGE);
                        sendMessageWithExceptions(sendMessage);
                        users.get(chatId).setPhase(5311);
                    }
                    else {
                        users.get(chatId).setPhase(3);
                    }
                }
            }

            if (users.get(chatId).getPhase() == 5311){
                sendMessage.setText("type your country name:"
                        + "\nnote: write full name for maximum accuracy");
                users.get(chatId).setPhase(53111);
            }


            if (users.get(chatId).getPhase() == 53121){

                if (update.hasMessage()) {
                    if (!update.getMessage().getText().equalsIgnoreCase(countriesApiButtons[1]) && !countriesApiButtons[1].contains(update.getMessage().getText())){
                        textMessageData = update.getMessage().getText();
                    }
                    else {
                        textMessageData = "";
                    }
                }
                if (!textMessageData.equals("")){
                    try {
                        sendMessage.setText(objectMapper.getInformationAboutCountries("code" , textMessageData));
                    } catch (JsonProcessingException | UnirestException e) {
                        throw new RuntimeException(e);
                    }
                    if (sendMessage.getText().equals("null")){
                        sendMessage.setText("Country code not found, please try again \nYou can always return to the home page by typing: " + BOT_HOME_PAGE);
                        sendMessageWithExceptions(sendMessage);
                        users.get(chatId).setPhase(5312);
                    }
                    else {
                        users.get(chatId).setPhase(3);
                    }

                }
            }

            if (users.get(chatId).getPhase() == 5312){
                sendMessage.setText("type your country code:");

                users.get(chatId).setPhase(53121);
            }





            String[] imBoredAPIButtons = new String[]{"random" , "by category"};
            if (users.get(chatId).getPhase() == 54){

                sendMessage.setText("choose an option:");
                setButtonsText(topRow, imBoredAPIButtons);

                users.get(chatId).setPhase(541);
            }

            if (users.get(chatId).getPhase() == 541){
                callBackData = "";
                textMessageData = "";

                if (update.hasCallbackQuery()){
                    callBackData = update.getCallbackQuery().getData();
                }
                if (update.hasMessage()) {
                    textMessageData = update.getMessage().getText();
                }

                if (callBackData.equalsIgnoreCase(imBoredAPIButtons[0].toUpperCase()) || textMessageData.equalsIgnoreCase(imBoredAPIButtons[0])){
                    users.get(chatId).setPhase(5411);
                }
                if (callBackData.equalsIgnoreCase(imBoredAPIButtons[1].toUpperCase()) || textMessageData.toLowerCase().equalsIgnoreCase(imBoredAPIButtons[1]) || textMessageData.toLowerCase().contains("category")){
                    users.get(chatId).setPhase(5412);
                }
            }

            if (users.get(chatId).getPhase() == 5411){
                try {
                    sendMessage.setText(objectMapper.getActivityForBored(imBoredAPIButtons[0]));
                } catch (UnirestException | JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                users.get(chatId).setPhase(3);
            }

            if (users.get(chatId).getPhase() == 5413){

                if (update.hasMessage()) {
                    if (!update.getMessage().getText().equalsIgnoreCase(imBoredAPIButtons[1])){
                        textMessageData = update.getMessage().getText();
                    }
                    else {
                        textMessageData = "";
                    }
                }

                if (!textMessageData.equals("")){
                    try {
                        sendMessage.setText(objectMapper.getActivityForBored(textMessageData));
                    } catch (UnirestException | JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                    if (sendMessage.getText().equals("null")){
                        sendMessage.setText("category not found, please try again \nYou can always return to the home page by typing: " + BOT_HOME_PAGE);
                        sendMessageWithExceptions(sendMessage);
                        users.get(chatId).setPhase(5412);
                    }
                    else {
                        users.get(chatId).setPhase(3);
                    }
                }
            }


            if (users.get(chatId).getPhase() == 5412){
                sendMessage.setText("""
                                Type the desired category from the following list:\s
                                1. education\s
                                2. recreational\s
                                3. social\s
                                4. diy\s
                                5. charity\s
                                6. cooking\s
                                7. relaxation\s
                                8. music\s
                                9. busywork
                                """);
                users.get(chatId).setPhase(5413);
            }




        }




        if (!textMessageData.equals("")) {
            users.get(chatId).addMessages(LocalDateTime.now(), " " + textMessageData);
        }
        if (!callBackData.equals("")){
            users.get(chatId).addMessages(LocalDateTime.now() , " " + callBackData.toLowerCase() + " (as a button)");
        }


        if (lastUsers.size() >= 10){
            lastUsers.remove(0);
            lastMassages.remove(0);
            lastDates.remove(0);
        }
//        need to add anyway, using 'else' won't work
        if (lastUsers.size() < 10) {


            if (users.get(chatId).getUserName() != null){
                lastUsers.add(users.get(chatId).getUserName());
            }else {
                String name = "";
                if (users.get(chatId).getFirstName() != null){
                    name = users.get(chatId).getFirstName();
                }
                if (users.get(chatId).getLastName() != null){
                    name += " " + users.get(chatId).getLastName();
                }
                lastUsers.add(name);
            }


            if (update.hasCallbackQuery()){
                lastMassages.add(update.getCallbackQuery().getData());
            }
            if (update.hasMessage()) {
                lastMassages.add(update.getMessage().getText());
            }

            lastDates.add(LocalDateTime.now().format(formatter));
        }



        sendMessageWithExceptions(sendMessage);
    }

    private void setButtonsText(List<InlineKeyboardButton> topRow, String[] putInButtons) {
        this.buttons[0] = new InlineKeyboardButton();
        this.buttons[0].setText(putInButtons[0]);
        this.buttons[0].setCallbackData(putInButtons[0].toUpperCase());
        topRow.add(this.buttons[0]);

        this.buttons[1] = new InlineKeyboardButton();
        this.buttons[1].setText(putInButtons[1]);
        this.buttons[1].setCallbackData(putInButtons[1].toUpperCase());
        topRow.add(this.buttons[1]);
    }


    public void sendMessageWithExceptions(SendMessage sendMessage){
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }



}