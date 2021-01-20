import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private List<String> allFiles = new ArrayList<>();

    private List<List<String>> allFilesSplitList = new ArrayList<List<String>>();

    public void outputFilesInFolder(File folder, int GBsplitSize, String outputname) {
        listFiles(folder);
        groupFiles(GBsplitSize);
        textFile(outputname);
    }

    private void listFiles(File folder) {
        File[] fileList = folder.listFiles();
        String[] fileNames = new String[fileList.length];

        // Get pretty names for printing.
        for (int i = 0; i < fileList.length; i++) {
            fileNames[i] = fileList[i].getName();
            allFiles.add(folder.getPath() + "/" + fileNames[i]);
        }

        // Iterate through and call this function for any sub-directories.
        for (File f2 : fileList) {
            if (f2.isDirectory()) {
                listFiles(f2);
            }
        }
    }

    private String[] getAllFilesArray(){
        return allFiles.toArray(new String[allFiles.size()]);
    }

    private void groupFiles(int GBsplitSize){
        // gbsplitsize found in options
        int index = 0;
        long totalBytes = 0;
        allFilesSplitList.add(new ArrayList<String>());

        for (int i = 0; i < getAllFilesArray().length; i++) {
            Path path = Paths.get(getAllFilesArray()[i]);

            if (totalBytes > (long) GBsplitSize * 1073741274){
                index = index + 1;
                allFilesSplitList.add(new ArrayList<String>());
                totalBytes = 0;
            }

            try {
                // size of a file (in bytes)
                long bytes = Files.size(path);
                totalBytes = totalBytes + bytes;
                if (!Files.isDirectory(path)){ allFilesSplitList.get(index).add(path.toString()); }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void textFile(String outputname) {
        try {
            File myObj = new File(outputname);
                if (myObj.createNewFile()) {
                    for (int i = 0; i < allFilesSplitList.size(); i++) {
                        for (int j = 0; j < allFilesSplitList.get(i).size(); j++) {
                                File path = new File(allFilesSplitList.get(i).get(j));
                                FileWriter myWriter = new FileWriter(outputname, true);
                                myWriter.write(i + "," + allFilesSplitList.get(i).get(j) + "\n");
                                myWriter.close();
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
