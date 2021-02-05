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

public class Microloan {
    public ByteArrayOutputStream microloan(JSONObject requestObj, Integer lang) throws JRException, IOException {

        String dir = "/home/";

        Functions func = new Functions();

        requestObj.put("microloan_loan_sum", func.indents(requestObj.getString("microloan_loan_sum")));
        requestObj.put("microloan_delay_percent_rate", String.format("%,.1f", Double.parseDouble(requestObj.getString("microloan_delay_percent_rate"))));
        requestObj.put("microloan_percent_rate", String.format("%,.1f", Double.parseDouble(requestObj.getString("microloan_percent_rate"))));
        requestObj.put("PSK_number", String.format("%,.1f", Double.parseDouble(requestObj.getString("PSK_number"))));

        if (requestObj.getInt("microloan_loan_term") <= 12) {
            requestObj.put("microloan_account", "12501");
        } else {
            requestObj.put("microloan_account", "14901");
        }

        JasperReport jasperReport = lang == 0 ?
                JasperCompileManager.compileReport(dir + "reports/microloan/rus/microloan.jrxml"):
                JasperCompileManager.compileReport(dir + "reports/microloan/uzb/microloan.jrxml");

        String str = requestObj.toString();

        try (ByteArrayInputStream is = new ByteArrayInputStream(str.getBytes())) {
            Map<String, Object> parametersMap = new HashMap<>();

            parametersMap.put(JsonQueryExecuterFactory.JSON_INPUT_STREAM, is);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametersMap);
            JasperExportManager.exportReportToPdfFile(jasperPrint, "output" + File.separator + "microloan.pdf");
        }

        File file = new File("output" + File.separator + "microloan.pdf");

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
