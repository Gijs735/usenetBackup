import javax.swing.*;
import java.awt.*;

public class OptionsDialog {

    public static void createOptionsDialog(Options options)
    {
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                JFrame frame = new JFrame("Settings");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setSize(600, 400);
                try
                {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JPanel header = new JPanel();
                JLabel headerlabel = new JLabel("Settings");
                headerlabel.setFont(new Font("", Font.BOLD, 24));
                header.add(headerlabel);

                JPanel panel = new JPanel();
                panel.setLayout(new GridLayout(7,1));

                JLabel B2tokenlabel = new JLabel(" Backblaze B2 Application Key: ");
                JPanel b2textpanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                JTextField B2tokentext = new JTextField("", 10);
                B2tokentext.setText(options.getB2TOKEN());
                b2textpanel.add(B2tokentext);

                JLabel B2tokenIDlabel = new JLabel(" Backblaze B2 Application Key ID: ");
                JPanel b2textIDpanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                JTextField B2tokenIDtext = new JTextField("", 10);
                B2tokenIDtext.setText(options.getB2TOKENID());
                b2textIDpanel.add(B2tokenIDtext);

                JLabel B2bucketidlabel = new JLabel(" Backblaze B2 Bucket ID: ");
                JPanel b2textbucketIDpanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                JTextField B2bucketidtext = new JTextField("", 10);
                B2bucketidtext.setText(options.getB2BUCKETID());
                b2textbucketIDpanel.add(B2bucketidtext);

                JLabel newsgroupUserLabel = new JLabel(" Newsgroup User: ");
                JPanel newsuserpanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                JTextField newsgroupUserText = new JTextField("", 10);
                newsgroupUserText.setText(options.getNEWSUSER());
                newsuserpanel.add(newsgroupUserText);

                JLabel newsgroupPasswordLabel = new JLabel(" Newsgroup password: ");
                JPanel newsgrouppasspanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                JTextField newsgroupPasswordText = new JTextField("", 10);
                newsgroupPasswordText.setText(options.getNEWSPASSWORD());
                newsgrouppasspanel.add(newsgroupPasswordText);

                JLabel gblabel = new JLabel(" GB's per upload: ");
                JPanel gbpanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                JTextField gbText = new JTextField("", 3);
                gbText.setText(Integer.toString(options.getGBPARTSIZE()));
                gbpanel.add(gbText);

                JLabel SSLoptionlabel = new JLabel(" Enable SSL: ");
                JCheckBox SSLoption = new JCheckBox();
                SSLoption.setSelected(options.getSSL_NEWS());

                panel.add(B2tokenlabel);
                panel.add(b2textpanel);
                panel.add(B2tokenIDlabel);
                panel.add(b2textIDpanel);
                panel.add(B2bucketidlabel);
                panel.add(b2textbucketIDpanel);
                panel.add(newsgroupUserLabel);
                panel.add(newsuserpanel);
                panel.add(newsgroupPasswordLabel);
                panel.add(newsgrouppasspanel);
                panel.add(gblabel);
                panel.add(gbpanel);
                panel.add(SSLoptionlabel);
                panel.add(SSLoption);

                JPanel menu = new JPanel();
                JButton save = new JButton("Save");
                JButton exit = new JButton("Exit");
                menu.add(save);
                menu.add(exit);

                frame.getContentPane().add(BorderLayout.NORTH, header);
                frame.getContentPane().add(BorderLayout.CENTER, panel);
                frame.getContentPane().add(BorderLayout.SOUTH, menu);
                frame.setLocationByPlatform(true);
                frame.setVisible(true);
                save.requestFocus();
                frame.setResizable(false);

                exit.addActionListener(e -> frame.dispose());
                save.addActionListener(e -> {
                    options.setB2TOKEN(B2tokentext.getText());
                    options.setB2TOKENID(B2tokenIDtext.getText());
                    options.setB2BUCKETID(B2bucketidtext.getText());
                    options.setNEWSPASSWORD(newsgroupPasswordText.getText());
                    options.setNEWSUSER(newsgroupUserText.getText());
                    options.setGBPARTSIZE(Integer.parseInt(gbText.getText()));
                    options.setSSL_NEWS(SSLoption.isSelected());
                });
            }
        });
    }
}
