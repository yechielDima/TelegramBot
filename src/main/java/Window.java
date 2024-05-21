import com.mashape.unirest.http.exceptions.UnirestException;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

public class Window extends JFrame {

    public static final int WINDOW_WIDTH = 1900;
    public static final int WINDOW_HEIGHT = 1000;
    public Window() throws FileNotFoundException, UnirestException, URISyntaxException {
        this.setLayout(null);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setSize(WINDOW_WIDTH , WINDOW_HEIGHT);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setBounds(0,0,WINDOW_WIDTH,WINDOW_HEIGHT);

        ChoicesPanel choicesPanel = new ChoicesPanel();
        choicesPanel.setBounds(0,0,WINDOW_WIDTH/2 - 200 , WINDOW_HEIGHT);
        this.add(choicesPanel);

        UserStatisticsPanel userStatisticsPanel = new UserStatisticsPanel();
        userStatisticsPanel.setBounds(WINDOW_WIDTH / 2 - 200, 0 , (WINDOW_WIDTH / 4) + 75, WINDOW_HEIGHT / 2);
        this.add(userStatisticsPanel);

        InteractionHistoryPanel interactionHistoryPanel = new InteractionHistoryPanel();
        interactionHistoryPanel.setBounds(3 * (WINDOW_WIDTH / 4) - 125 , 0 , WINDOW_WIDTH / 4 + 125 ,WINDOW_HEIGHT / 2);
        this.add(interactionHistoryPanel);

        GraphsPanel graphsPanel = new GraphsPanel();
        graphsPanel.setBounds(WINDOW_WIDTH / 2 -200,WINDOW_HEIGHT / 2 , WINDOW_WIDTH / 2 + 200 , WINDOW_HEIGHT / 2);
        this.add(graphsPanel);
    }

    public void showWindow(){
        this.setVisible(true);
    }
}
