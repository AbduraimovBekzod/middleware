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

        JSONArray element = requestObj.getJSONArray("protocol_form_element");
        for (int i = 0; i < element.length(); i++) {
            JSONObject temp = element.getJSONObject(i);

            JSONObject loanSecurity = temp.getJSONObject("loan_security");
            JSONArray guarantors = loanSecurity.getJSONArray("guarantors");
            JSONArray insurances = loanSecurity.getJSONArray("insurances");

            for (int g = 0; g < guarantors.length(); g++) {
                JSONObject gTemp = guarantors.getJSONObject(g);
                gTemp.put("protocol_form_guarantor_scope", String.format("%,.0f", Double.parseDouble(gTemp.getString("protocol_form_guarantor_scope"))).replaceAll(",", " "));
                guarantors.put(g, gTemp);
            }
            loanSecurity.put("guarantors", guarantors);

            for (int in = 0; in < insurances.length();in++) {
                JSONObject gTemp = insurances.getJSONObject(in);
                gTemp.put("protocol_form_insurance_scope", String.format("%,.0f", Double.parseDouble(gTemp.getString("protocol_form_insurance_scope"))).replaceAll(",", " "));
                insurances.put(in, gTemp);
            }
            loanSecurity.put("insurances", insurances);
            temp.put("loan_security", loanSecurity);

            temp.put("protocol_form_sum", String.format("%,.0f", Double.parseDouble(temp.getString("protocol_form_sum"))).replaceAll(",", " "));
            element.put(i, temp);
        }

        requestObj.put("protocol_form_element", element);
        
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