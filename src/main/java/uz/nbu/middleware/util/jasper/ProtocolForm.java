package uz.nbu.middleware.util.jasper;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.query.JsonQueryExecuterFactory;
import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ProtocolForm {

    public ByteArrayOutputStream protocol_form(JSONObject requestObj, Integer lang) throws JRException, IOException {

        String dir = "/home/";

        JasperReport jasperReport = lang == 0 ?
                JasperCompileManager.compileReport(dir + "reports/protocol_form/rus/protocol_form.jrxml") :
                JasperCompileManager.compileReport(dir + "reports/protocol_form/uzb/protocol_form.jrxml");

        String str = requestObj.toString();

        try (ByteArrayInputStream is = new ByteArrayInputStream(str.getBytes())) {
            Map<String, Object> parametersMap = new HashMap<>();

            parametersMap.put(JsonQueryExecuterFactory.JSON_INPUT_STREAM, is);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametersMap);
            JasperExportManager.exportReportToPdfFile(jasperPrint, dir + "output/protocol_form.pdf");
        }

        File file = new File(dir + "output/protocol_form.pdf");

        FileInputStream fis = new FileInputStream(file);

        ByteArrayOutputStream bos = new ByteArrayOutputStream(); //BLOB
        byte[] buf = new byte[1024];
        for (int readNum; (readNum = fis.read(buf)) != -1; ) {
            bos.write(buf, 0, readNum);
        }

        return bos;
    }
}