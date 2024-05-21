import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Arrays;

import com.mashape.unirest.http.exceptions.UnirestException;
import io.quickchart.QuickChart;

public class GraphsPanel extends JPanel {
    Bot bot = new Bot();
    private int[][] massagesAndUsersIn1234minutes;
    private final int SECONDS_IN_START_MOMENT = LocalDateTime.now().getSecond();
    ImageIcon graphImageIcon;
    JLabel graphLabel1;
    JLabel graphLabel2;
    ImageIcon doughnutImageIcon;
    JLabel doughnutLabel1;
    JLabel doughnutLabel2;

    public GraphsPanel() throws FileNotFoundException, UnirestException, URISyntaxException {
        this.setLayout(null);
        this.setBackground(new Color(233, 234, 255, 255));

        this.massagesAndUsersIn1234minutes = new int[4][2];

        this.graphImageIcon = new ImageIcon();
        this.graphLabel1 = new JLabel();
        this.graphLabel2 = new JLabel();
        this.graphLabel2.setBounds(500, 25, 600, 400);
        this.add(graphLabel2);

        this.doughnutImageIcon = new ImageIcon();
        this.doughnutLabel1 = new JLabel();
        this.doughnutLabel2 = new JLabel();
        this.doughnutLabel2.setBounds(0, 25, 500, 400);
        this.add(doughnutLabel2);

        updateLabel();
        updateImage();
        updateMassagesAndUsersIn1234minutes();

    }

    public synchronized ImageIcon getGraphImageIcon() {
        return this.graphImageIcon;
    }

    public synchronized ImageIcon getDoughnutImageIcon() {
        return doughnutImageIcon;
    }

    public synchronized int[][] getMassagesAndUsersIn1234minutes() {
        return massagesAndUsersIn1234minutes;
    }

    public void updateLabel() {
        new Thread(() -> {
            boolean lab1orLab2 = true;
            while (true) {

                if (lab1orLab2) {

                    this.remove(graphLabel2);
                    this.remove(doughnutLabel2);

                    this.graphLabel1 = new JLabel(getGraphImageIcon());
                    this.doughnutLabel1 = new JLabel(getDoughnutImageIcon());

                    this.graphLabel1.setBounds(500, 25, 600, 400);
                    this.doughnutLabel1.setBounds(0, 25, 500, 400);

                    this.add(graphLabel1);
                    this.add(doughnutLabel1);

                    lab1orLab2 = false;
                }
                else {
                    this.remove(graphLabel1);
                    this.remove(doughnutLabel1);

                    this.graphLabel2 = new JLabel(getGraphImageIcon());
                    this.doughnutLabel2 = new JLabel(getDoughnutImageIcon());

                    this.graphLabel2.setBounds(500, 25, 600, 400);
                    this.doughnutLabel2.setBounds(0, 25, 500, 400);

                    this.add(graphLabel2);
                    this.add(doughnutLabel2);

                    lab1orLab2 = true;
                }


                repaint();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public void updateImage() {
        new Thread(() -> {
            while (true) {

                try {
                   this.graphImageIcon = calcGraph();
                   this.doughnutImageIcon = calcDoughnut();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                try {
                    Thread.sleep(1800);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public ImageIcon calcGraph() throws IOException {
        QuickChart chart = new QuickChart();
        ImageIcon graphImage;
        chart.setWidth(600);
        chart.setHeight(400);
        chart.setVersion("2.9.4");
        chart.setConfig("{"
                + "    type: 'bar',"
                + "    data: {"
                + "        labels: ['now', 'minute ago', '2 minutes ago', '3 minutes ago'],"
                + "        datasets: [{"
                + "            label: 'Users',"
                + "            data: [" + getMassagesAndUsersIn1234minutes()[0][0] + "," + getMassagesAndUsersIn1234minutes()[1][0] + "," + getMassagesAndUsersIn1234minutes()[2][0] + "," + getMassagesAndUsersIn1234minutes()[3][0] + "]},"
                + "{           label: 'massages' ,"
                +"             data:[" + getMassagesAndUsersIn1234minutes()[0][1] + "," + getMassagesAndUsersIn1234minutes()[1][1] + "," + getMassagesAndUsersIn1234minutes()[2][1] + "," + getMassagesAndUsersIn1234minutes()[3][1] + "]}"
                + "        ]"
                + "    }"
                + "}"
        );


        graphImage = new ImageIcon(chart.toByteArray());
        return graphImage;
    }
    public ImageIcon calcDoughnut() throws IOException {
        QuickChart chart = new QuickChart();
        ImageIcon doughnutImage;
        String[] options = ChoicesPanel.getSelectedChoices();
        DecimalFormat decimalFormat = new DecimalFormat("#.00");

        String use = "0, 0, 0";
        if (Arrays.stream(Bot.selectedChoicesUses).sum() > 0){
            use = decimalFormat.format((((double) Bot.selectedChoicesUses[0]) / ((double) Arrays.stream(Bot.selectedChoicesUses).sum())) * 100)
                    + "," + decimalFormat.format((((double) Bot.selectedChoicesUses[1]) / ((double) Arrays.stream(Bot.selectedChoicesUses).sum())) * 100)
                    + "," + decimalFormat.format((((double) Bot.selectedChoicesUses[2]) / ((double) Arrays.stream(Bot.selectedChoicesUses).sum())) * 100);
        }
        chart.setWidth(500);
        chart.setHeight(400);
        chart.setVersion("2.9.4");
        chart.setConfig("{" +
                "  type: 'doughnut'," +
                "  data: {" +
                "    datasets: [" +
                "      {" +
                "        data: [" + use + "]," +
                "        backgroundColor: [" +
                "          'rgb(255, 120, 120)'," +
                "          'rgb(120, 255, 120)'," +
                "          'rgb(120, 120, 255)'," +
                "        ]," +
                "      }," +
                "    ]," +
                "    labels: ['" + options[0] + "', '" + options[1] + "', '"+ options[2] + "']," +
                "  }," +
                "  options: {" +
                "    plugins: {" +
                "      datalabels: {" +
                    "formatter: (value) => {" +
                "          return value + '%';" +
                "        }," +
                "      }," +
                "       doughnutlabel: {" +
                "        labels: [" +
                "          {" +
                "            text: '" + Arrays.stream(Bot.selectedChoicesUses).sum() + "'," +
                "            font: {" +
                "              size: 20," +
                "              weight: 'bold'," +
                "            }," +
                "          }," +
                "          {" +
                "            text: 'total'," +
                "          }," +
                "        ]," +
                "      },"+
                "    }," +
                "  }," +
                "}");

        doughnutImage = new ImageIcon(chart.toByteArray());
        return doughnutImage;
    }

    public void updateMassagesAndUsersIn1234minutes(){
        new Thread(()->{
            while (true){
                this.massagesAndUsersIn1234minutes[0][0] = Bot.usersAndMassagesInLastMinute[0];
                this.massagesAndUsersIn1234minutes[0][1] = Bot.usersAndMassagesInLastMinute[1];

                if (LocalDateTime.now().getSecond() == SECONDS_IN_START_MOMENT){
                    moveMassagesAndUsersIn1234minutes();
                    Bot.usersAndMassagesInLastMinute[0] = 0;
                    Bot.usersAndMassagesInLastMinute[1] = 0;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
    public void moveMassagesAndUsersIn1234minutes(){
        for (int i = 0; i < this.massagesAndUsersIn1234minutes.length -1; i++) {
            this.massagesAndUsersIn1234minutes[this.massagesAndUsersIn1234minutes.length -1 - i][0] = this.massagesAndUsersIn1234minutes[this.massagesAndUsersIn1234minutes.length -2 - i][0];
        }
        for (int i = 0; i < this.massagesAndUsersIn1234minutes.length -1; i++) {
            this.massagesAndUsersIn1234minutes[this.massagesAndUsersIn1234minutes.length -1 - i][1] = this.massagesAndUsersIn1234minutes[this.massagesAndUsersIn1234minutes.length -2 - i][1];
        }
    }

}