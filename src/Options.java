import java.io.File;  // Import the File class
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors
import java.util.Scanner;
import java.io.FileNotFoundException;

public final class Options {
    /*
    adding an option:
     - Add below private string
     - add to options contructor myWriter (B2TOKEN=NEWSUSER=NEWSPASSWORD=SSL_NEWS=true ...)
     - add getter and setter
     - add to setoptions
     - add to optionsdialog
     */
    private String B2TOKEN;
    private String B2TOKENID;
    private String B2BUCKETID;
    private String NEWSUSER;
    private String NEWSPASSWORD;
    private Boolean SSL_NEWS;
    private int GBPARTSIZE;
    private String directories;

    public Options() {
        try {
            File myObj = new File("options.conf");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName() + " setting default Options...");
                FileWriter myWriter = new FileWriter("options.conf");
                myWriter.write("B2TOKEN=\nB2TOKENID=\nB2BUCKETID=\nNEWSUSER=\nNEWSPASSWORD=\nSSL_NEWS=true\nGBPARTSIZE=50\nDIRECTORIES=.");
                myWriter.close();
                setOptions();
            } else {
                System.out.println("Options already created, setting them now...");
                setOptions();
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private void setOptions() {
        try {
            File myObj = new File("options.conf");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if (data.startsWith("B2TOKEN=")) {
                    this.B2TOKEN = data.substring(8);
                }
                if (data.startsWith("NEWSUSER=")) {
                    this.NEWSUSER = data.substring(9);
                }
                if (data.startsWith("NEWSPASSWORD=")) {
                    this.NEWSPASSWORD = data.substring(13);
                }
                if (data.startsWith("SSL_NEWS=")) {
                    if(data.substring(9).equals("true")){
                        this.SSL_NEWS = true;
                    }else{
                        this.SSL_NEWS = false;
                    }
                }
                if (data.startsWith("GBPARTSIZE=")) {
                    this.GBPARTSIZE = Integer.parseInt(data.substring(11));
                }
                if (data.startsWith("B2TOKENID=")) {
                    this.B2TOKENID = data.substring(10);
                }
                if (data.startsWith("B2BUCKETID=")) {
                    this.B2BUCKETID = data.substring(11);
                }
                if (data.startsWith("DIRECTORIES=")) {
                    this.directories = data.substring(12);
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public String getB2TOKEN() {
        return B2TOKEN;
    }

    public void setB2TOKEN(String b2TOKEN) {
        try {
            FileWriter myWriter = new FileWriter("options.conf", false);
            myWriter.write("B2TOKEN=" + b2TOKEN + "\nB2TOKENID=" + this.B2TOKENID + "\nB2BUCKETID=" + this.B2BUCKETID + "\nNEWSUSER=" + this.NEWSUSER + "\nNEWSPASSWORD=" + this.NEWSPASSWORD + "\nSSL_NEWS=" + this.SSL_NEWS + "\nGBPARTSIZE=" + this.GBPARTSIZE + "\nDIRECTORIES=" + directories);
            myWriter.close();
            setOptions();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

        public String getNEWSUSER () {
            return NEWSUSER;
        }

        public void setNEWSUSER (String NEWSUSER){
            try {
                FileWriter myWriter = new FileWriter("options.conf", false);
                myWriter.write("B2TOKEN=" + this.B2TOKEN + "\nB2TOKENID=" + this.B2TOKENID + "\nB2BUCKETID=" + this.B2BUCKETID + "\nNEWSUSER=" + NEWSUSER + "\nNEWSPASSWORD=" + this.NEWSPASSWORD + "\nSSL_NEWS=" + this.SSL_NEWS + "\nGBPARTSIZE=" + this.GBPARTSIZE + "\nDIRECTORIES=" + directories);
                myWriter.close();
                setOptions();
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

        public String getNEWSPASSWORD () {
            return NEWSPASSWORD;
        }

        public void setNEWSPASSWORD (String NEWSPASSWORD){
            try {
                FileWriter myWriter = new FileWriter("options.conf", false);
                myWriter.write("B2TOKEN=" + this.B2TOKEN + "\nB2TOKENID=" + this.B2TOKENID + "\nB2BUCKETID=" + this.B2BUCKETID + "\nNEWSUSER=" + this.NEWSUSER + "\nNEWSPASSWORD=" + NEWSPASSWORD + "\nSSL_NEWS=" + this.SSL_NEWS + "\nGBPARTSIZE=" + this.GBPARTSIZE + "\nDIRECTORIES=" + directories);
                myWriter.close();
                setOptions();
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

        public Boolean getSSL_NEWS () {
            return SSL_NEWS;
        }

        public void setSSL_NEWS (Boolean SSL_NEWS){
            try {
                FileWriter myWriter = new FileWriter("options.conf", false);
                myWriter.write("B2TOKEN=" + this.B2TOKEN + "\nB2TOKENID=" + this.B2TOKENID + "\nB2BUCKETID=" + this.B2BUCKETID + "\nNEWSUSER=" + this.NEWSUSER + "\nNEWSPASSWORD=" + this.NEWSPASSWORD + "\nSSL_NEWS=" + SSL_NEWS + "\nGBPARTSIZE=" + this.GBPARTSIZE + "\nDIRECTORIES=" + directories);
                myWriter.close();
                setOptions();
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

    public int getGBPARTSIZE() {
        return GBPARTSIZE;
    }

    public void setGBPARTSIZE(int GBPARTSIZE) {
        try {
            FileWriter myWriter = new FileWriter("options.conf", false);
            myWriter.write("B2TOKEN=" + this.B2TOKEN + "\nB2TOKENID=" + this.B2TOKENID + "\nB2BUCKETID=" + this.B2BUCKETID + "\nNEWSUSER=" + this.NEWSUSER + "\nNEWSPASSWORD=" + NEWSPASSWORD + "\nSSL_NEWS=" + this.SSL_NEWS + "\nGBPARTSIZE=" + GBPARTSIZE + "\nDIRECTORIES=" + directories);
            myWriter.close();
            setOptions();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public String getB2TOKENID() {
        return B2TOKENID;
    }

    public void setB2TOKENID(String B2TOKENID) {
        try {
            FileWriter myWriter = new FileWriter("options.conf", false);
            myWriter.write("B2TOKEN=" + this.B2TOKEN + "\nB2TOKENID=" + B2TOKENID + "\nB2BUCKETID=" + this.B2BUCKETID + "\nNEWSUSER=" + this.NEWSUSER + "\nNEWSPASSWORD=" + NEWSPASSWORD + "\nSSL_NEWS=" + this.SSL_NEWS + "\nGBPARTSIZE=" + GBPARTSIZE + "\nDIRECTORIES=" + directories);
            myWriter.close();
            setOptions();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public String getB2BUCKETID() {
        return B2BUCKETID;
    }

    public void setB2BUCKETID(String B2BUCKETID) {
        try {
            FileWriter myWriter = new FileWriter("options.conf", false);
            myWriter.write("B2TOKEN=" + this.B2TOKEN + "\nB2TOKENID=" + B2TOKENID + "\nB2BUCKETID=" + B2BUCKETID + "\nNEWSUSER=" + this.NEWSUSER + "\nNEWSPASSWORD=" + NEWSPASSWORD + "\nSSL_NEWS=" + this.SSL_NEWS + "\nGBPARTSIZE=" + GBPARTSIZE + "\nDIRECTORIES=" + directories);
            myWriter.close();
            setOptions();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public String getDirectories() {
        return directories;
    }

    public void setDirectories(String Directories) {
        try {
            FileWriter myWriter = new FileWriter("options.conf", false);
            myWriter.write("B2TOKEN=" + this.B2TOKEN + "\nB2TOKENID=" + this.B2TOKENID + "\nB2BUCKETID=" + this.B2BUCKETID + "\nNEWSUSER=" + this.NEWSUSER + "\nNEWSPASSWORD=" + NEWSPASSWORD + "\nSSL_NEWS=" + this.SSL_NEWS + "\nGBPARTSIZE=" + this.GBPARTSIZE + "\nDIRECTORIES=" + Directories);
            myWriter.close();
            setOptions();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
