//Usually you will require both swing and awt packages
// even if you are working with just swings.
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

class main {
    // create a frame
    static JFrame frame;
    static JProgressBar progressbar1;
    static Options options;

    public static void main(String args[]) {
        if (System.getProperty("os.name").startsWith("Mac")) {
            //Creating the Frame
            frame = new JFrame("Usenet backup");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 400);

            options = new Options();

            //header
            JPanel header = new JPanel();
            JLabel headerlabel = new JLabel("Usenet backup");
            headerlabel.setFont(new Font("", Font.BOLD, 24));
            header.add(headerlabel);

            //Creating the panel at bottom and adding components
            JPanel panel = new JPanel(); // the panel is not visible in output
            JButton start = new JButton("Start");
            JButton optionsbutton = new JButton("Settings");
            JButton exit = new JButton("Exit");
            panel.add(start);
            panel.add(optionsbutton);
            panel.add(exit);

            // Text Area at the Center
            JPanel centerpanel = new JPanel();
            JLabel progresslabel1 = new JLabel("Progress");
            progressbar1 = new JProgressBar();
            progressbar1.setValue(0);
            progressbar1.setMaximum(100);
            progressbar1.setStringPainted(true);
            centerpanel.add(progresslabel1);
            centerpanel.add(progressbar1);


            //Adding Components to the frame.
            frame.getContentPane().add(BorderLayout.NORTH, header);
            frame.getContentPane().add(BorderLayout.SOUTH, panel);
            frame.getContentPane().add(BorderLayout.CENTER, centerpanel);
            frame.setVisible(true);

            optionsbutton.addActionListener(e -> OptionsDialog.createOptionsDialog(options));
            //start.addActionListener(e -> ThreadManager.startThreadManager(start,optionsbutton)); //execute the main method from CommandManager when pressing send button
            start.addActionListener(e -> {
//                    FileManager manager = new FileManager();
//                    manager.outputText(new File("/mnt/cifs_union/films"), options.getGBPARTSIZE());
//                    BackblazeManager.downloadFile("AllFiles.txt","AllFiles.txt",options.getB2TOKENID(),options.getB2TOKEN());
                ThreadManager.cloudDedupe(new String[]{"/Users/gijs/Desktop/Logo 4"});
                });
            exit.addActionListener(e -> confirmexit());

            progress();
        }else{
            frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            JOptionPane.showMessageDialog(null, "Cannot run on " + System.getProperty("os.name") + "\nPlease run this program on Linux. \nThe app is supported on Ubuntu 20.04 but may work on other linux flavors.", "Unsupported OS", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }

    private static void confirmexit(){
        int confirmed = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to exit?", "Confirm exit",
                JOptionPane.YES_NO_OPTION);
        if (confirmed == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public static void progress()
    {
        try {
            while (progressbar1.getValue() <= 100) {
                // fill the menu bar
                progressbar1.setValue(progressbar1.getValue() + 1);
                // delay the thread
                Thread.sleep(100);
            }
        }
        catch (Exception e) {
        }
    }
}