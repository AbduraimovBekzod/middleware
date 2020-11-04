package uz.nbu.middleware.util.jasper;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.query.JsonQueryExecuterFactory;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ProtocolForm {

    public ByteArrayOutputStream protocol_form(JSONObject requestObj, Integer lang) throws JRException, IOException {

        String dir = "/home/";

        JasperReport jasperReport = lang == 0 ?
                JasperCompileManager.compileReport(dir + "reports/protocol_form/rus/protocol_form.jrxml") :
                JasperCompileManager.compileReport(dir + "reports/protocol_form/uzb/protocol_form.jrxml");

        functions func = new functions();

        if (requestObj.has("protocol_form_element")) {
            JSONArray element = requestObj.getJSONArray("protocol_form_element");
            for (int i = 0; i < element.length(); i++) {
                JSONObject temp = element.getJSONObject(i);

                if (temp.has("loan_security")) {
                    JSONObject loanSecurity = temp.getJSONObject("loan_security");

                    if (loanSecurity.has("guarantors")) {
                        JSONArray guarantors = loanSecurity.getJSONArray("guarantors");
                        for (int g = 0; g < guarantors.length(); g++) {
                            JSONObject gTemp = guarantors.getJSONObject(g);
                            gTemp.put("protocol_form_guarantor_scope", func.indents(gTemp.getString("protocol_form_guarantor_scope")));
                            guarantors.put(g, gTemp);
                        }
                        loanSecurity.put("guarantors", guarantors);
                    }
                    if (loanSecurity.has("insurances")) {
                        JSONArray insurances = loanSecurity.getJSONArray("insurances");


                        for (int in = 0; in < insurances.length(); in++) {
                            JSONObject gTemp = insurances.getJSONObject(in);
                            gTemp.put("protocol_form_insurance_scope", func.indents(gTemp.getString("protocol_form_insurance_scope")));
                            insurances.put(in, gTemp);
                        }
                        loanSecurity.put("insurances", insurances);
                    }

                    temp.put("loan_security", loanSecurity);
                }
                temp.put("protocol_form_sum", func.indents(temp.getString("protocol_form_sum")));
                element.put(i, temp);
            }
            requestObj.put("protocol_form_element", element);
        }

        String str = requestObj.toString();

        try (ByteArrayInputStream is = new ByteArrayInputStream(str.getBytes())) {
            Map<String, Object> parametersMap = new HashMap<>();

            parametersMap.put(JsonQueryExecuterFactory.JSON_INPUT_STREAM, is);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametersMap);
            JasperExportManager.exportReportToPdfFile(jasperPrint, "output"+File.separator+"protocol_form.pdf");
        }

        File file = new File("output"+File.separator+"protocol_form.pdf");

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