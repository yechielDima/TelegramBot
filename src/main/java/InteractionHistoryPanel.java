import com.mashape.unirest.http.exceptions.UnirestException;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

public class InteractionHistoryPanel extends JPanel {

    Bot bot = new Bot();
    private JLabel[] labels;
    private JLabel[] labels2;
    Font font1 = new Font("Ariel", Font.PLAIN, 19);

    public InteractionHistoryPanel() throws FileNotFoundException, UnirestException, URISyntaxException {
        this.setLayout(null);
        this.setBackground(new Color(192, 255, 165));

        JLabel label = new JLabel(this.getClass().getName());
        Font font2 = new Font("Ariel", Font.BOLD, 25);
        label.setFont(font2);
        label.setBounds(5,5,300,27);
        this.add(label);

        this.labels = new JLabel[10];
        this.labels2 = new JLabel[10];
        for (int i = 0; i < labels2.length; i++) {
            this.labels2[i] = new JLabel();
            this.add(labels2[i]);
        }


        updateText();
    }



    public void updateText(){
        new Thread(() -> {
            boolean lab1orLab2 = true;
            while (true){

                if (lab1orLab2) {

                    int s1 = 0;
                    for (JLabel label : this.labels2) {
                        if (label != null){
                            s1++;
                        }
                    }
                    for (int i = 0; i < s1; i++) {
                        this.remove(this.labels2[i]);
                    }

                    lab1orLab2 = false;

                    for (int i = 0; i < Bot.lastUsers.size(); i++) {
                        this.labels[i] = new JLabel();
                        labels[i].setFont(font1);
                        labels[i].setBounds(10, (i+1) * 45, 600, 20);
                        this.labels[i].setText(
                                "user: " + Bot.lastUsers.get(Bot.lastUsers.size() - 1 - i) +
                                        ", activity: " + Bot.lastMassages.get(Bot.lastMassages.size() - 1 - i) +
                                        ", date: " + Bot.lastDates.get(Bot.lastDates.size() - 1 - i));
                        this.add(this.labels[i]);
                    }
                }
                else {

                    int s2 = 0;
                    for (JLabel label : this.labels) {
                        if (label != null){
                            s2++;
                        }
                    }
                    for (int i = 0; i < s2; i++) {
                        this.remove(this.labels[i]);
                    }

                    lab1orLab2 = true;

                    for (int i = 0; i < Bot.lastUsers.size(); i++) {
                        this.labels2[i] = new JLabel();
                        labels2[i].setFont(font1);
                        labels2[i].setBounds(10, (i + 1) * 45, 600, 20);
                        this.labels2[i].setText(
                                "user: " + Bot.lastUsers.get(Bot.lastUsers.size() - 1 - i) +
                                        ", activity: " + Bot.lastMassages.get(Bot.lastMassages.size() - 1 - i) +
                                        ", date: " + Bot.lastDates.get(Bot.lastDates.size() - 1 - i));
                        this.add(this.labels2[i]);
                    }

                }
                repaint();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }



}
