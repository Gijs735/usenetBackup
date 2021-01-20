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

    private static void startThread(){
        CommandManager object = new CommandManager("ls -la");
        object.start();
    }

    public static void cloudDedupe(String[] folders) {
        try {
            BackblazeManager.downloadFile("allFiles" + uniqueSN.getSerialNumber() + ".txt.cloud", "allFiles" + uniqueSN.getSerialNumber() + ".txt.cloud", main.options.getB2TOKENID(), main.options.getB2TOKEN());
        }catch (IOException e){
            System.out.println("cloudDedupe function IOException: creating empty file to make sure we can continue\n" +
                    "WARNING: If there is a file for this device on Backblaze STOP THE PROGRAM NOW and check your internet connection or the program's code.");
            File myObj = new File("allFiles"+ uniqueSN.getSerialNumber() +".txt.cloud");
            try {
                myObj.createNewFile();
            } catch (IOException ioException) {
                System.out.println("ERROR CREATING FILE MENTIONED ABOVE!");
                ioException.printStackTrace();
            }
        }finally {
            
        List<String> cloudlist = null;
        try (Stream<String> lines = Files.lines(Paths.get("allFiles"+ uniqueSN.getSerialNumber() +".txt.cloud"))) {
            cloudlist = lines.collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

            for (int i=0;i < folders.length;i++) {
                FileManager manager = new FileManager();
                manager.outputFilesInFolder(new File(folders[i]), main.options.getGBPARTSIZE(), i+".txt");

                List<String> locallist = null;
                try (Stream<String> lines = Files.lines(Paths.get(i+".txt"))) {
                    locallist = lines.collect(Collectors.toList());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                List<String> cloudandlocal = Stream.concat(cloudlist.stream(), locallist.stream())
                    .collect(Collectors.toList());

                cloudandlocal.stream()
                        .filter(j -> Collections.frequency(cloudandlocal, j) == 1)
                        .collect(Collectors.toList())
                        .forEach(System.out::println);
            }
        }
    }
}
