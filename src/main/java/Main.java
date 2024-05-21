import com.mashape.unirest.http.exceptions.UnirestException;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static final String BOT_VERSION = "2.6.6";
    public static final long ADMIN_ID = 123456789;

    public static void main(String[] args) throws IOException, UnirestException, URISyntaxException {

        Window window = new Window();
        window.showWindow();

        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiException | UnirestException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            while (! Thread.currentThread().isInterrupted()) {
                Runtime runtime = Runtime.getRuntime();
                long usedMemory = runtime.totalMemory() - runtime.freeMemory();
                System.out.println("Used Memory: " + usedMemory / 1024 / 1024 + " MB");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();

    }
}