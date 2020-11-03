package uz.nbu.middleware.util.jasper;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.query.JsonQueryExecuterFactory;
import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Protocol {
    public ByteArrayOutputStream protocol(JSONObject requestObj, Integer lang) throws JRException, IOException {
        String dir = "/home/";
        JasperReport jasperReport = lang == 0 ?
                JasperCompileManager.compileReport(dir + "reports/protocol/rus/protocol.jrxml") :
                JasperCompileManager.compileReport(dir + "reports/protocol/uzb/protocol.jrxml");

        requestObj.put("protocol_loan_amount", String.format("%,.0f", Double.parseDouble(requestObj.getString("protocol_loan_amount"))).replaceAll(",", " "));

        JSONArray protocolGuarantors = requestObj.getJSONArray("protocol_guarantors");
        for (int i = 0; i < protocolGuarantors.length();i++){
            JSONObject tempJsonObject = protocolGuarantors.getJSONObject(i);
            tempJsonObject.put("protocol_guarantor_value", String.format("%,.0f", Double.parseDouble(tempJsonObject.getString("protocol_guarantor_value"))).replaceAll(",", " "));
            protocolGuarantors.put(i, tempJsonObject);
        }
        requestObj.put("protocol_guarantors", protocolGuarantors);

        JSONArray protocolInsurances = requestObj.getJSONArray("protocol_insurances");
        for (int i = 0; i < protocolInsurances.length();i++){
            JSONObject tempJsonObject = protocolInsurances.getJSONObject(i);
            tempJsonObject.put("protocol_insurance_value", String.format("%,.0f", Double.parseDouble(tempJsonObject.getString("protocol_insurance_value"))).replaceAll(",", " "));
            protocolInsurances.put(i, tempJsonObject);
        }
        requestObj.put("protocol_insurances", protocolInsurances);

        JSONArray protocolAddTerms = requestObj.getJSONArray("protocol_add_terms");
        for (int i = 0; i < protocolAddTerms.length();i++){
            JSONObject tempJsonObject = protocolAddTerms.getJSONObject(i);
            tempJsonObject.put("protocol_additional_value", String.format("%,.0f", Double.parseDouble(tempJsonObject.getString("protocol_additional_value"))).replaceAll(",", " "));
            protocolAddTerms.put(i, tempJsonObject);
        }
        requestObj.put("protocol_add_terms", protocolAddTerms);

        JSONArray protocolSpecTerms = requestObj.getJSONArray("protocol_spec_terms");
        for (int i = 0; i < protocolSpecTerms.length();i++){
            JSONObject tempJsonObject = protocolSpecTerms.getJSONObject(i);
            tempJsonObject.put("protocol_special_value", String.format("%,.0f", Double.parseDouble(tempJsonObject.getString("protocol_special_value"))).replaceAll(",", " "));
            protocolSpecTerms.put(i, tempJsonObject);
        }
        requestObj.put("protocol_spec_terms", protocolSpecTerms);

        String str = requestObj.toString();

        try (ByteArrayInputStream is = new ByteArrayInputStream(str.getBytes())) {
            Map<String, Object> parametersMap = new HashMap<>();

            parametersMap.put(JsonQueryExecuterFactory.JSON_INPUT_STREAM, is);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametersMap);
            JasperExportManager.exportReportToPdfFile(jasperPrint, "output/protocol.pdf");
        }

        File file = new File("output/protocol.pdf");

        FileInputStream fis = new FileInputStream(file);

        ByteArrayOutputStream bos = new ByteArrayOutputStream(); //BLOB
        byte[] buf = new byte[1024];
        for(int readNum; (readNum = fis.read(buf)) != -1;) {
            bos.write(buf, 0, readNum);
        }

        return bos;
    }
}
