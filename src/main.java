//Usually you will require both swing and awt packages
// even if you are working with just swings.
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class main {
    // create a frame
    static JFrame frame;
    static JProgressBar progressbar1;
    static Options options;

    public static void main(String args[]) {
        if (System.getProperty("os.name").startsWith("Linux")) {
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
            start.addActionListener(main::actionPerformed);
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

    private static void actionPerformed(ActionEvent e) {
        List<String> toUpload = UploadManager.cloudDedupe(new String[]{options.getDirectories()});

        for (int i = UploadManager.checkHowManyPackagesInCloud(); i < 100; i++){ //upload a max of 100 packages (5TB) per start click (100 packages = 100 * 50 = 5TB)
            toUpload = UploadManager.preparePackage(toUpload, i);
            if(UploadManager.checkIfPackageIsBiggerThen(options.getGBPARTSIZE() - 1, toUpload)){
                try {
                    CommandManager rar = new CommandManager("rar a -m5 -v256000k -hp896912666 package" + i + ".rar \"" + toUpload.stream().collect(Collectors.joining("\" \"")) + "\"");
                    rar.start();
                    rar.join();
                    CommandManager par = new CommandManager("par2 create -s640000 -r10 package" + i + ".rar.par2 package" + i + ".part*.rar");
                    par.start();
                    par.join();
                    CommandManager sfv = new CommandManager("cksfv -b $(pwd)package" + i + ".part*.rar > $(pwd)package" + i + ".sfv");
                    sfv.start();
                    sfv.join();
                    CommandManager nyuu = new CommandManager("nyuu -h news.newshosting.com --ssl -u " + options.getNEWSUSER() + " -p " + options.getNEWSPASSWORD() + " --article-size 640000 --from cumspray@bigdick.cock --groups alt.binaries.backup -k1 --out $(pwd)package" + i + ".nzb *.sfv *.par2 package" + i + ".part*.rar");
                    nyuu.start();
                    nyuu.join();
                    CommandManager clean = new CommandManager("rm -rf *.sfv *.par2 package" + i + ".part*.rar *.txt"); //bad for multithread switch later pls
                    clean.start();
                    clean.join();
                    UploadManager.uploadChangedCloudFiles(toUpload, i);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
            toUpload = UploadManager.cloudDedupe(new String[]{options.getDirectories()});
        }
    }
}