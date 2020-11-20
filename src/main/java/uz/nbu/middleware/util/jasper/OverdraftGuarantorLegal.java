package uz.nbu.middleware.util.jasper;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.query.JsonQueryExecuterFactory;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class OverdraftGuarantorLegal {
    public ByteArrayOutputStream overdraft_guarantor_legal(JSONObject requestObj, Integer lang) throws JRException, IOException {

        String str = requestObj.toString();
        String dir = "/home/";
        
        Functions func = new Functions();

        requestObj.put("loan_sum", func.indents(requestObj.getString("loan_sum")));
        requestObj.put("guarantor_sum", func.indents(requestObj.getString("guarantor_sum")));

        for(int i = 1; i <= 3; i++) {
            JasperReport jasperReport = lang == 0 ?
                    JasperCompileManager.compileReport(dir + "reports/overdraft_guarantor_legal/rus/overdraft_guarantor_legal_" + i + ".jrxml"):
                    JasperCompileManager.compileReport(dir + "reports/overdraft_guarantor_legal/uzb/overdraft_guarantor_legal_" + i + ".jrxml");

            try (ByteArrayInputStream is = new ByteArrayInputStream(str.getBytes())) {
                Map<String, Object> parametersMap = new HashMap<>();

                parametersMap.put(JsonQueryExecuterFactory.JSON_INPUT_STREAM, is);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametersMap);
                JasperExportManager.exportReportToPdfFile(jasperPrint, "output/overdraft_guarantor_legal_" + i + ".pdf");
            }
        }

        PDFMergerUtility ut = new PDFMergerUtility();
        for (int i = 1; i <= 3; i++) {
            ut.addSource("output/overdraft_guarantor_legal_" + i + ".pdf");
        }
        ut.setDestinationFileName("output/overdraft_guarantor_legal.pdf");//merge pdf name
        ut.mergeDocuments();//create merge pdf

        File file = new File("output/overdraft_guarantor_legal.pdf");
        FileInputStream fis = new FileInputStream(file);

        ByteArrayOutputStream bos = new ByteArrayOutputStream(); //BLOB
        byte[] buf = new byte[1024];
        for(int readNum; (readNum = fis.read(buf)) != -1;) {
            bos.write(buf, 0, readNum);
        }

        for (int i = 1; i <= 3; i++) {
            new File("output/overdraft_guarantor_legal_" + i + ".pdf").delete();
        }

        return bos;
    }
}
