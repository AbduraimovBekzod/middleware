package uz.nbu.middleware.util.jasper;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.query.JsonQueryExecuterFactory;
import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class PaymentSchedule {
    ByteArrayOutputStream schedule (JSONObject requestObj, Integer lang) throws JRException, IOException {
        String dir = "/home/";
		JasperReport jasperReport = lang == 0 ?
                JasperCompileManager.compileReport(dir + "reports/payment_schedule/rus/payment_schedule.jrxml"):
                JasperCompileManager.compileReport(dir + "reports/payment_schedule/uzb/payment_schedule.jrxml");

        String str = requestObj.toString();

        try (ByteArrayInputStream is = new ByteArrayInputStream(str.getBytes())) {
            Map<String,Object> parametersMap = new HashMap<>();

            parametersMap.put(JsonQueryExecuterFactory.JSON_INPUT_STREAM, is);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametersMap);
            JasperExportManager.exportReportToPdfFile(jasperPrint, "output/payment_schedule.pdf");
        }

        File file = new File("output/payment_schedule.pdf");

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
