import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ThreadManager {

    public static void startThreadManager(JButton startButton, JButton optionsbutton) {
        startButton.setEnabled(false);
        optionsbutton.setEnabled(false);
        startThread();
    }

    private static void startThread() {
        CommandManager object = new CommandManager("ls -la");
        object.start();
    }
}
