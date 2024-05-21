import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class HebrewJocksAPI {
    private boolean crashed;
    private final ArrayList<String> chuckNorrisHebrewJocks;

    public HebrewJocksAPI() throws URISyntaxException, FileNotFoundException {
        this.crashed = true;
        this.chuckNorrisHebrewJocks = new ArrayList<>();
        URL url = getClass().getClassLoader().getResource("chuckNorrisHebrewText.txt");
        if (url != null){
            File file = new File(url.toURI());
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                this.chuckNorrisHebrewJocks.add(scanner.nextLine());
            }
            this.crashed = false;
        }
    }

    public ArrayList<String> getChuckNorrisHebrewJocks() {
        return chuckNorrisHebrewJocks;
    }

    public boolean isCrashed() {
        return crashed;
    }
}