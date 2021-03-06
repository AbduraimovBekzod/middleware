package uz.nbu.middleware.util.jasper;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.query.JsonQueryExecuterFactory;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ConsumerGuarantorLegal {
    public ByteArrayOutputStream consumer_guarantor_legal(JSONObject requestObj, Integer lang) throws JRException, IOException {

		String dir = "/home/";
        
        Functions func = new Functions();

        requestObj.put("loan_sum", func.indents(requestObj.getString("loan_sum")));
        requestObj.put("guarantor_sum", func.indents(requestObj.getString("guarantor_sum")));
        requestObj.put("percent_rate", String.format("%,.1f", Double.parseDouble(requestObj.getString("percent_rate"))));

        String str = requestObj.toString();

        JasperReport jasperReport = lang == 0 ?
                JasperCompileManager.compileReport(dir+"reports/consumer_guarantor_legal/rus/consumer_guarantor_legal.jrxml"):
                JasperCompileManager.compileReport(dir+"reports/consumer_guarantor_legal/uzb/consumer_guarantor_legal.jrxml");

        try (ByteArrayInputStream is = new ByteArrayInputStream(str.getBytes())) {
            Map<String, Object> parametersMap = new HashMap<>();

            parametersMap.put(JsonQueryExecuterFactory.JSON_INPUT_STREAM, is);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametersMap);
            JasperExportManager.exportReportToPdfFile(jasperPrint, "output/consumer_guarantor_legal.pdf");
        }

        File file = new File("output/consumer_guarantor_legal.pdf");
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
