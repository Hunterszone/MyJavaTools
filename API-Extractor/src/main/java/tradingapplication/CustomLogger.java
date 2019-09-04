package tradingapplication;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;

class CustomLogger {
    
    private static StringBuilder sb = new StringBuilder();
    
    void addToLog(String parameter) {
        long currentDateTime = System.currentTimeMillis();
        Date currentDate = new Date(currentDateTime);
        sb
                .append(currentDate)
                .append("\n")
                .append(parameter)
                .append(System.getProperty("line.separator"));
    }
    
    void writeLogToDisk (String fileName){
        try {
            String desktopPath = FileSystemView.getFileSystemView().getHomeDirectory().toString();
            FileOutputStream out = new FileOutputStream(desktopPath + fileName);
            OutputStreamWriter w = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            w.append(sb);
            w.flush();
            w.close();
        } catch (IOException ex) {
            System.out.println("Caught" + ex);
        }
    }

    static boolean checkIfFileIsOpened(String fileName){
        File file = new File(fileName);

        // try to rename the file with the same name
        File sameFileName = new File(fileName);

        if(file.renameTo(sameFileName)){
            // if the file is renamed
            System.out.println("file is closed");
            return false;
        }else{
            // if the file didnt accept the renaming operation
            System.out.println("file is opened");
            return true;
        }
    }

    /*public static void clearTheFile(String fileName) {
        String desktopPath = FileSystemView.getFileSystemView().getHomeDirectory().toString();
        String fullPath = desktopPath + fileName;
        try {
            FileWriter fwOb = new FileWriter(fullPath, false);
            PrintWriter pwOb = new PrintWriter(fwOb, false);
            pwOb.flush();
            pwOb.close();
            fwOb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
