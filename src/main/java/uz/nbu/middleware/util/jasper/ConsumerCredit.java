package uz.nbu.middleware.util.jasper;

import net.sf.jasperreports.engine.*;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConsumerCredit {

    public ByteArrayOutputStream consumer_credit(JSONObject requestObj, Integer lang) throws JRException, IOException {
        String dir = "/home/";
        
        Functions func = new Functions();

        requestObj.put("consumer_loan_sum", func.indents(requestObj.getString("consumer_loan_sum")));
        requestObj.put("consumer_delay_percent_rate", String.format("%,.1f", Double.parseDouble(requestObj.getString("consumer_delay_percent_rate"))));
        requestObj.put("consumer_percent_rate", String.format("%,.1f", Double.parseDouble(requestObj.getString("consumer_percent_rate"))));
        requestObj.put("PSK_number", String.format("%,.1f", Double.parseDouble(requestObj.getString("PSK_number"))));

        if (requestObj.getInt("consumer_loan_term") <= 12) {
            requestObj.put("consumer_account", "12501");
        } else {
            requestObj.put("consumer_account", "14901");
        }

        JasperReport jasperReport = lang == 0 ?
                JasperCompileManager.compileReport(dir+"reports/consumer_credit/rus/consumer_credit.jrxml"):
                JasperCompileManager.compileReport(dir+"reports/consumer_credit/uzb/consumer_credit.jrxml");

        String str = requestObj.toString();

        try (ByteArrayInputStream is = new ByteArrayInputStream(str.getBytes())) {
            Map<String, Object> parametersMap = new HashMap<>();

            parametersMap.put(JsonQueryExecuterFactory.JSON_INPUT_STREAM, is);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametersMap);
            JasperExportManager.exportReportToPdfFile(jasperPrint, "output" + File.separator + "consumer_credit.pdf");
        }

        File file = new File("output" + File.separator + "consumer_credit.pdf");

        FileInputStream fis = new FileInputStream(file);

        ByteArrayOutputStream bos = new ByteArrayOutputStream(); //BLOB
        byte[] buf = new byte[1024];
        for(int readNum; (readNum = fis.read(buf)) != -1;)
        {
            bos.write(buf, 0, readNum);
        }

        return bos;
    }
}