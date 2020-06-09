package uz.nbu.middleware.util.jasper;
import net.sf.jasperreports.engine.*;
import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Protocol {
    public ByteArrayOutputStream protocol(JSONObject requestObj, Integer lang) throws JRException, IOException {

        JasperReport jasperReport = lang == 0 ?
            JasperCompileManager.compileReport("reports"+File.separator+"protocol"+File.separator+"rus"+File.separator+"protocol.jrxml"):
            JasperCompileManager.compileReport("reports"+File.separator+"protocol"+File.separator+"uzb"+File.separator+"protocol.jrxml");

        Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put("protocol_initiative_unit", requestObj.getString("protocol_initiative_unit"));
        parameters.put("protocol_client_inn", requestObj.getString("protocol_client_inn"));
        parameters.put("protocol_lending_currency", requestObj.getString("protocol_lending_currency"));
        parameters.put("protocol_loan_amount", requestObj.getString("protocol_loan_amount"));
        parameters.put("protocol_repayment_type", requestObj.getString("protocol_repayment_type"));
        parameters.put("protocol_customer_name", requestObj.getString("protocol_customer_name"));
        parameters.put("protocol_term", requestObj.getString("protocol_term"));
        parameters.put("protocol_grace_period", requestObj.getString("protocol_grace_period"));
        parameters.put("protocol_finance_source", requestObj.getString("protocol_finance_source"));
        parameters.put("protocol_loan_product", requestObj.getString("protocol_loan_product"));
        parameters.put("protocol_loan_type", requestObj.getString("protocol_loan_type"));
        parameters.put("protocol_percent_rate", requestObj.getString("protocol_percent_rate"));
        parameters.put("protocol_credit_rating", requestObj.getString("protocol_credit_rating"));
        parameters.put("protocol_request_number", requestObj.getString("protocol_request_number"));
        parameters.put("protocol_loan_specialist_position", requestObj.getString("protocol_loan_specialist_position"));
        parameters.put("protocol_loan_specialist_fio", requestObj.getString("protocol_loan_specialist_fio"));
        parameters.put("protocol_number", requestObj.getString("protocol_number"));
        parameters.put("protocol_filial", requestObj.getString("protocol_filial"));
        parameters.put("protocol_committee_decision_number", requestObj.getString("protocol_committee_decision_number"));
        parameters.put("protocol_committee_decision_date", requestObj.getString("protocol_committee_decision_date"));
        parameters.put("protocol_guarantor_name", requestObj.getString("protocol_guarantor_name"));
        parameters.put("protocol_guarantor_value", requestObj.getString("protocol_guarantor_value"));
        parameters.put("protocol_insurance_name", requestObj.getString("protocol_insurance_name"));
        parameters.put("protocol_insurance_value", requestObj.getString("protocol_insurance_value"));
        parameters.put("protocol_additional_name", requestObj.getString("protocol_additional_name"));
        parameters.put("protocol_additional_value", requestObj.getString("protocol_additional_value"));
        parameters.put("protocol_special_name", requestObj.getString("protocol_special_name"));
        parameters.put("protocol_special_value", requestObj.getString("protocol_special_value"));
        parameters.put("protocol_secretary_fio", requestObj.getString("protocol_secretary_fio"));

        JRDataSource dataSource = new JREmptyDataSource();

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        JasperExportManager.exportReportToPdfFile(jasperPrint, "output"+File.separator+"protocol.pdf");//create pdf file

        File file = new File("output"+File.separator+"protocol.pdf");
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
