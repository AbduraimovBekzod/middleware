package uz.nbu.middleware.util.jasper;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.query.JsonQueryExecuterFactory;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class MicroloanGuarantorLegal {
    public ByteArrayOutputStream microloan_guarantor_legal(JSONObject requestObj, Integer lang) throws JRException, IOException {

		String dir = "/home/";
        
        requestObj.put("loan_sum", String.format("%,.0f", Double.parseDouble(requestObj.getString("loan_sum"))).replaceAll(",", " "));
        requestObj.put("guarantor_sum", String.format("%,.0f", Double.parseDouble(requestObj.getString("guarantor_sum"))).replaceAll(",", " "));
        
        String str = requestObj.toString();

        for(int i = 1; i <= 3; i++) {
            JasperReport jasperReport = lang == 0 ?
                    JasperCompileManager.compileReport(dir + "reports/microloan_guarantor_legal/rus/microloan_guarantor_legal_" + i + ".jrxml"):
                    JasperCompileManager.compileReport(dir + "reports/microloan_guarantor_legal/uzb/microloan_guarantor_legal_" + i + ".jrxml");

            try (ByteArrayInputStream is = new ByteArrayInputStream(str.getBytes())) {
                Map<String, Object> parametersMap = new HashMap<>();

                parametersMap.put(JsonQueryExecuterFactory.JSON_INPUT_STREAM, is);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametersMap);
                JasperExportManager.exportReportToPdfFile(jasperPrint, "output/microloan_guarantor_legal_" + i + ".pdf");
            }
        }

        PDFMergerUtility ut = new PDFMergerUtility();
        for (int i = 1; i <= 3; i++) {
            ut.addSource("output/microloan_guarantor_legal_" + i + ".pdf");
        }
        ut.setDestinationFileName("output/microloan_guarantor_legal.pdf");//merge pdf name
        ut.mergeDocuments();//create merge pdf

        File file = new File("output/microloan_guarantor_legal.pdf");
        FileInputStream fis = new FileInputStream(file);

        ByteArrayOutputStream bos = new ByteArrayOutputStream(); //BLOB
        byte[] buf = new byte[1024];
        for(int readNum; (readNum = fis.read(buf)) != -1;) {
            bos.write(buf, 0, readNum);
        }

        for (int i = 1; i <= 3; i++) {
            new File("output/microloan_guarantor_legal_" + i + ".pdf").delete();
        }

        return bos;
    }
}
