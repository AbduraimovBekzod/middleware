package uz.nbu.middleware.util.jasper;

import net.sf.jasperreports.engine.JRException;

import java.io.*;
import java.util.Scanner;

public class testBlob {
    public static void main(String argv[]) throws IOException, JRException {

        FileReader request = new FileReader("C:\\Users\\User\\IdeaProjects\\qwe\\array.json");
        Scanner scan = new Scanner(request);
        String content = "";//json string

        ////////////////////////////////////if content full, delete next 4 lines
        while (scan.hasNextLine()) {
            content = content + scan.nextLine();
        }
        scan.close();
        ////////////////////////////////////

        JasperReportMaker myObj = new JasperReportMaker();

        ByteArrayOutputStream temp =  myObj.makePdfBlob(content);

        try(OutputStream outputStream = new FileOutputStream("test.pdf"))
        {
            temp.writeTo(outputStream);
        }
    }
}