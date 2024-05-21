import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;

public class ChoicesPanel extends JPanel {
    public static Font FOND1;
    private int counter;
    private static String[] choiceOptions;
    private static String[] selectedChoices;
    private final JButton setChoicesButton;
    private final List<JCheckBox> checkBoxes;
    private final JCheckBox jockSafeCheckBox;
    private static boolean jockSafe;

    public Color colorForButtons = new Color(23, 188, 250);
    public ChoicesPanel(){

        this.setLayout(null);
        this.setBackground(new Color(145, 220, 252));

        JLabel label = new JLabel(this.getClass().getName());
        FOND1 = new Font("Ariel", Font.PLAIN, 20);
        label.setFont(new Font("Ariel", Font.PLAIN, 40));
        label.setBounds(10,10,300,50);
        this.add(label);


        choiceOptions = new String[]{"jocks", "number fact", "HP characters", "countries info", "Im bored"};
        selectedChoices = new String[]{"jocks", "number fact", "HP characters"};

        this.checkBoxes = List.of(new JCheckBox() , new JCheckBox() , new JCheckBox() , new JCheckBox() , new JCheckBox());
        this.counter = 1;
        for (JCheckBox checkBox: this.checkBoxes){
            checkBox.setBounds(50, 130 * this.counter , 400 , 100);
            checkBox.setFont(new Font("Ariel", Font.PLAIN, 25));
            checkBox.setText(choiceOptions[counter - 1]);
            checkBox.setBackground(colorForButtons);
            this.counter ++;
            this.add(checkBox);
        }

        jockSafe = true;
        this.jockSafeCheckBox = new JCheckBox();
        this.jockSafeCheckBox.setBounds(470 , this.checkBoxes.get(0).getY() + (this.checkBoxes.get(0).getHeight() / 2) , 100,22);
        this.jockSafeCheckBox.setFont(new Font("Ariel", Font.PLAIN, 15));
        this.jockSafeCheckBox.setForeground(Color.BLACK);
        this.jockSafeCheckBox.setText("safe jocks");
        this.jockSafeCheckBox.setSelected(true);
        this.jockSafeCheckBox.setBackground(colorForButtons);
        this.add(this.jockSafeCheckBox);

        this.setChoicesButton = new JButton();
        this.setChoicesButton.setBounds(125, 800 , 500 , 120);
        this.setChoicesButton.setFont(new Font("Ariel", Font.PLAIN, 30));
        this.setChoicesButton.setBackground(colorForButtons);
        this.setChoicesButton.setText("submit");


        canShowButton();
    }

    public void canShowButton(){
        new Thread(() -> {
            boolean needPasswordCheck = true;
            while (true) {
                this.setChoicesButton.setVisible(checkChoices());

                if (this.jockSafeCheckBox.isSelected()){

                    jockSafe = true;
                    needPasswordCheck = true;
                    this.jockSafeCheckBox.setForeground(Color.BLACK);
                    this.jockSafeCheckBox.setBackground(colorForButtons);
                }else {
                    if (needPasswordCheck){
                        if (passwordCheck()){
                            jockSafe = false;
                            this.jockSafeCheckBox.setForeground(Color.RED);
                            this.jockSafeCheckBox.setBackground(Color.BLACK);
                            needPasswordCheck = false;
                            JOptionPane.showMessageDialog(null , "jocks 18+ hes opened successfully" , "success" , JOptionPane.INFORMATION_MESSAGE);
                        }else {
                            JOptionPane.showMessageDialog(null , "incorrect password!" , "error" , JOptionPane.ERROR_MESSAGE);
                            this.jockSafeCheckBox.setSelected(true);
                        }
                    }else {
                        jockSafe = false;
                        this.jockSafeCheckBox.setForeground(Color.RED);
                        this.jockSafeCheckBox.setBackground(Color.BLACK);
                    }

                }
                try {
                    Thread.sleep(100); // I believe it would be nicer without it, but it's 990 times less to check per second.
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        this.setChoicesButton.addActionListener(e -> {

            this.counter = 0;
            for (JCheckBox checkBox : this.checkBoxes) {
                if (checkBox.isSelected()) {
                    counter++;
                }
            }
            this.counter = 0;
            for (JCheckBox checkBox : this.checkBoxes) {
                if (checkBox.isSelected()) {
                    selectedChoices[this.counter] = checkBox.getText();
                    this.counter++;
                }
            }
            Bot.selectedChoicesUses[0] = 0;
            Bot.selectedChoicesUses[1] = 0;
            Bot.selectedChoicesUses[2] = 0;
        });
        this.add(setChoicesButton);
    }

    public boolean checkChoices(){
        int c = 0;
        for (JCheckBox checkBox : this.checkBoxes) {
            if (checkBox.isSelected()){
                c++;

            }
        }
        if (c == 3){
            for (JCheckBox checkBox : this.checkBoxes) {
                if (!checkBox.isSelected()){
                    checkBox.setEnabled(false);
                }
            }
            if (!this.checkBoxes.get(0).isEnabled()){
                jockSafeCheckBox.setEnabled(false);
            }
            return true;
        } else if (c < 3) {
            for (JCheckBox checkBox : this.checkBoxes) {
                checkBox.setEnabled(true);
            }
            if (this.checkBoxes.get(0).isEnabled()){
                jockSafeCheckBox.setEnabled(true);
            }
        }
        return false;
    }
    public static String[] getSelectedChoices() {
        return selectedChoices;
    }
    public static String[] getChoiceOptions() {
        return choiceOptions;
    }
    public static boolean isJocksSafe() {
        return jockSafe;
    }

    public boolean passwordCheck(){
        String password = JOptionPane.showInputDialog(null , "Enter password:","password" , JOptionPane.QUESTION_MESSAGE);
        return Objects.equals(password , JokeAPI.SAFE_JOCKS_PASSWORD);
    }
}
