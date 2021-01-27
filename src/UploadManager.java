import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UploadManager {

    //USE THIS METHOD ONLY WITH ONE FOLDER SO NOT MULTIPLE FOLDERS IN folders VARIABLE PLEASE
    public static List<String> cloudDedupe(String[] folders) {
        List<String> toUpload = new ArrayList<>();
        List<String> locallist = new ArrayList<>();
        try {
            BackblazeManager.downloadFile("allFiles" + uniqueSN.getSerialNumber() + ".txt.cloud", "allFiles" + uniqueSN.getSerialNumber() + ".txt.cloud", main.options.getB2TOKENID(), main.options.getB2TOKEN());
        } catch (IOException e) {
            System.out.println("cloudDedupe function IOException: creating empty file to make sure we can continue\n" +
                    "WARNING: If there is a file for this device on Backblaze STOP THE PROGRAM NOW and check your internet connection or the program's code.");
            File myObj = new File("allFiles" + uniqueSN.getSerialNumber() + ".txt.cloud");
            try {
                myObj.createNewFile();
            } catch (IOException ioException) {
                System.out.println("ERROR CREATING FILE MENTIONED ABOVE!");
                ioException.printStackTrace();
            }
        } finally {

            List<String> cloudlist = null;
            try (Stream<String> lines = Files.lines(Paths.get("allFiles" + uniqueSN.getSerialNumber() + ".txt.cloud"))) {
                cloudlist = lines.collect(Collectors.toList());
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < folders.length; i++) {
                FileManager manager = new FileManager();
                manager.outputFilesInFolder(new File(folders[i]), main.options.getGBPARTSIZE(), i + ".txt");

                List<String> locallisttemp = null;
                try (Stream<String> lines = Files.lines(Paths.get(i + ".txt"))) {
                    locallisttemp = lines.collect(Collectors.toList());
                    locallist = Stream.concat(locallisttemp.stream(), locallist.stream())
                            .collect(Collectors.toList());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            List<String> cloudandlocal = Stream.concat(cloudlist.stream(), locallist.stream())
                    .collect(Collectors.toList());

            List<String> tempList = cloudandlocal.stream()
                    .filter(j -> Collections.frequency(cloudandlocal, j) == 1)
                    .collect(Collectors.toList());

            toUpload = Stream.concat(tempList.stream(), toUpload.stream())
                    .collect(Collectors.toList());
        }
        return toUpload;
    }

    //return a packag
    public static List<String> preparePackage(List<String> listoffiles, int packagenumber) {
        List<String> firstPackage = new ArrayList<>();
        long totalBytes = 0;
        for (String item: listoffiles){
            String[] temp = item.split(",");
            long bytes = 0;
            try {
                bytes = Files.size(Paths.get(temp[1]));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (totalBytes < (long) main.options.getGBPARTSIZE() * 1073741274){
                firstPackage.add(temp[1]);
            }
            totalBytes = totalBytes + bytes;
        }
        for (String s : firstPackage){
            System.out.println("Generating MD5 for file: " + s + " in package: " + packagenumber);
            FileWriter myWriter = null;
            try {
                myWriter = new FileWriter("package" + packagenumber + "-" + uniqueSN.getSerialNumber() + ".md5", true);
                myWriter.write(s+","+MD5.getFileMD5String(new File(s)) + "\n");
                myWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return firstPackage;
    }

    public static Boolean checkIfPackageIsBiggerThen (int GBPartSize, List<String> Package){
        long totalBytes = 0;
        for (String s : Package){
            long bytes = 0;
            try {
                bytes = Files.size(Paths.get(s));
            } catch (IOException e) {
                e.printStackTrace();
            }
            totalBytes = totalBytes + bytes;
            if (totalBytes > (long) GBPartSize * 1073741274){
                return true;
            }
        }
        return false;
    }

    public static int checkHowManyPackagesInCloud () {
        List<String> cloudlist = null;
        try (Stream<String> lines = Files.lines(Paths.get("allFiles" + uniqueSN.getSerialNumber() + ".txt.cloud"))) {
            cloudlist = lines.collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> counter = new ArrayList<>();
        for (String item: cloudlist) {
            String[] temp = item.split(",");
            counter.add(temp[0]);
        }
        if ((int) counter.stream().distinct().count() - 1 == -1){
            return 0;
        }
        return (int) counter.stream().distinct().count();
    }

    public static void uploadChangedCloudFiles(List<String> uploaded, int packagenumber) {
        FileWriter myWriter = null;
        try {
            for(String s : uploaded) {
                myWriter = new FileWriter("allFiles" + uniqueSN.getSerialNumber() + ".txt.cloud", true);
                myWriter.write(packagenumber + "," + s);
                myWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BackblazeManager.uploadFile("allFiles" + uniqueSN.getSerialNumber() + ".txt.cloud", "allFiles" + uniqueSN.getSerialNumber() + ".txt.cloud", main.options.getB2TOKENID(), main.options.getB2TOKEN(), main.options.getB2BUCKETID());
            BackblazeManager.uploadFile("package" + packagenumber + "-" + uniqueSN.getSerialNumber() + ".md5", "package" + packagenumber + "-" + uniqueSN.getSerialNumber() + ".md5", main.options.getB2TOKENID(), main.options.getB2TOKEN(), main.options.getB2BUCKETID());
            BackblazeManager.uploadFile("package" + packagenumber + ".nzb", "package" + packagenumber + ".nzb", main.options.getB2TOKENID(), main.options.getB2TOKEN(), main.options.getB2BUCKETID());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
