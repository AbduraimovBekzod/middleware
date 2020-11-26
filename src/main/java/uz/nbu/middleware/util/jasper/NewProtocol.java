package uz.nbu.middleware.util.jasper;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.query.JsonQueryExecuterFactory;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class NewProtocol {
    public ByteArrayOutputStream newProtocol(JSONObject requestObj, Integer lang) throws JRException, IOException {

        JasperReport jasperReport = lang == 0 ?
                JasperCompileManager.compileReport("reports"+File.separator+"new_protocol"+File.separator+"rus"+File.separator+"protocol_form.jrxml"):
                JasperCompileManager.compileReport("reports"+File.separator+"new_protocol"+File.separator+"rus"+File.separator+"protocol_form.jrxml");

        Functions func = new Functions();

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

                temp.put("protocol_form_filial", requestObj.getString("protocol_form_filial"));
                temp.put("protocol_form_number", requestObj.getString("protocol_form_number"));
                temp.put("protocol_form_kvorum", requestObj.getString("protocol_form_kvorum"));
                temp.put("protocol_form_secret_fio", requestObj.getString("protocol_form_secret_fio"));
                temp.put("protocol_form_secret_post", requestObj.getString("protocol_form_secret_post"));
                temp.put("protocol_form_date", requestObj.getString("protocol_form_date"));
                temp.put("credit_committee_composition", requestObj.getJSONObject("credit_committee_composition"));

//                element.put(i, temp);

                String str = temp.toString();

                try (ByteArrayInputStream is = new ByteArrayInputStream(str.getBytes())) {
                    Map<String, Object> parametersMap = new HashMap<>();

                    parametersMap.put(JsonQueryExecuterFactory.JSON_INPUT_STREAM, is);
                    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametersMap);
                    JasperExportManager.exportReportToPdfFile(jasperPrint, "output"+File.separator+"protocol_form_"+i+".pdf");
                }


            }
//            requestObj.put("protocol_form_element", element);

            PDFMergerUtility ut = new PDFMergerUtility();
            for (int i = 0; i < element.length(); i++)
            {
                ut.addSource("output"+File.separator+"protocol_form_"+i+".pdf");
            }
            ut.setDestinationFileName("output"+File.separator+"protocol_form.pdf");//merge pdf name
            ut.mergeDocuments();//create merge pdf

            for (int i = 0; i < element.length(); i++) //delete parts
            {
                new File("output"+File.separator+"protocol_form_"+i+".pdf").delete();
            }
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
