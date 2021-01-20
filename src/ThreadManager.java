import javax.swing.*;

public class ThreadManager {

    public static void startThreadManager(JButton startButton, JButton optionsbutton) {
        startButton.setEnabled(false);
        optionsbutton.setEnabled(false);
        startThread();
    }

    private static void startThread(){
        CommandManager object = new CommandManager("ls -la");
        object.start();
    }
}
