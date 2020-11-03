package uz.nbu.middleware.util.jasper;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.query.JsonQueryExecuterFactory;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ConsumerGuarantorPhysical {
    public ByteArrayOutputStream consumer_guarantor_physical(JSONObject requestObj, Integer lang) throws JRException, IOException {

		String dir = "/home/";

        requestObj.put("loan_sum", String.format("%,.0f", Double.parseDouble(requestObj.getString("loan_sum"))).replaceAll(",", " "));
        requestObj.put("guarantor_sum", String.format("%,.0f", Double.parseDouble(requestObj.getString("guarantor_sum"))).replaceAll(",", " "));


        String str = requestObj.toString();

        for(int i = 1; i <= 3; i++) {
            JasperReport jasperReport = lang == 0 ?
                    JasperCompileManager.compileReport(dir + "reports/consumer_guarantor_physical/rus/consumer_guarantor_physical_" + i + ".jrxml"):
                    JasperCompileManager.compileReport(dir + "reports/consumer_guarantor_physical/uzb/consumer_guarantor_physical_" + i + ".jrxml");

            try (ByteArrayInputStream is = new ByteArrayInputStream(str.getBytes())) {
                Map<String, Object> parametersMap = new HashMap<>();

                parametersMap.put(JsonQueryExecuterFactory.JSON_INPUT_STREAM, is);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametersMap);
                JasperExportManager.exportReportToPdfFile(jasperPrint, "output/consumer_guarantor_physical_" + i + ".pdf");
            }
        }

        PDFMergerUtility ut = new PDFMergerUtility();
        for (int i = 1; i <= 3; i++) {
            ut.addSource("output/consumer_guarantor_physical_" + i + ".pdf");
        }
        ut.setDestinationFileName("output/consumer_guarantor_physical.pdf");//merge pdf name
        ut.mergeDocuments();//create merge pdf

        File file = new File("output/consumer_guarantor_physical.pdf");
        FileInputStream fis = new FileInputStream(file);

        ByteArrayOutputStream bos = new ByteArrayOutputStream(); //BLOB
        byte[] buf = new byte[1024];
        for(int readNum; (readNum = fis.read(buf)) != -1;) {
            bos.write(buf, 0, readNum);
        }

        for (int i = 1; i <= 3; i++) {
            new File("output/consumer_guarantor_physical_" + i + ".pdf").delete();
        }

        return bos;
    }
}
