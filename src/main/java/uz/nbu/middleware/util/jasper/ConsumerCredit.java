package uz.nbu.middleware.util.jasper;
import net.sf.jasperreports.engine.*;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ConsumerCredit {
    public ByteArrayOutputStream consumer_credit(JSONObject requestObj, Integer lang) throws JRException, IOException {
        for(int i = 1; i <= 3; i++) //запись параметров в report
        {
            JasperReport jasperReport = lang == 0 ?
                    JasperCompileManager.compileReport("reports"+File.separator+"consumer_credit"+File.separator+"rus"+File.separator+"consumer_credit_"+i+".jrxml"):
                    JasperCompileManager.compileReport("reports"+File.separator+"consumer_credit"+File.separator+"uzb"+File.separator+"consumer_credit_"+i+".jrxml");

            Map<String, Object> parameters = new HashMap<String, Object>();

            parameters.put("consumer_blanking_type", requestObj.getString("consumer_blanking_type"));
            parameters.put("consumer_guarantee_surety_sum", requestObj.getString("consumer_guarantee_surety_sum"));
            parameters.put("consumer_grace_period", requestObj.getString("consumer_grace_period"));
            parameters.put("consumer_borrower_passport_number", requestObj.getString("consumer_borrower_passport_number"));
            parameters.put("consumer_loan_repayment_rate", requestObj.getString("consumer_loan_repayment_rate"));
            parameters.put("consumer_delay_percent_rate", requestObj.getString("consumer_delay_percent_rate"));
            parameters.put("consumer_loan_term", requestObj.getString("consumer_loan_term"));
            parameters.put("consumer_percent_rate", requestObj.getString("consumer_percent_rate"));
            parameters.put("consumer_borrower_passport_issue_by", requestObj.getString("consumer_borrower_passport_issue_by"));
            parameters.put("consumer_borrower_address", requestObj.getString("consumer_borrower_address"));
            parameters.put("PSK_number", requestObj.getString("PSK_number"));
            parameters.put("PSK_words", requestObj.getString("PSK_words"));
            parameters.put("consumer_doverennost_date", requestObj.getString("consumer_doverennost_date"));
            parameters.put("consumer_borrower_fio", requestObj.getString("consumer_borrower_fio"));
            parameters.put("consumer_filial_name", requestObj.getString("consumer_filial_name"));
            parameters.put("consumer_doverennost_number", requestObj.getString("consumer_doverennost_number"));
            parameters.put("consumer_loan_sum", requestObj.getString("consumer_loan_sum"));
            parameters.put("consumer_emp_fio", requestObj.getString("consumer_emp_fio"));
            parameters.put("consumer_number", requestObj.getString("consumer_number"));
            parameters.put("consumer_city", requestObj.getString("consumer_city"));
            parameters.put("consumer_date", requestObj.getString("consumer_date"));
            parameters.put("consumer_bank_name", requestObj.getString("consumer_bank_name"));
            parameters.put("consumer_committee_decision_number", requestObj.getString("consumer_committee_decision_number"));
            parameters.put("consumer_borrower_passport_issue_date", requestObj.getString("consumer_borrower_passport_issue_date"));
            parameters.put("consumer_filial_inn", requestObj.getString("consumer_filial_inn"));
            parameters.put("consumer_committee_decision_date", requestObj.getString("consumer_committee_decision_date"));
            parameters.put("consumer_filial_phone", requestObj.getString("consumer_filial_phone"));
            parameters.put("consumer_filial_address", requestObj.getString("consumer_filial_address"));
            parameters.put("consumer_loan_purpose", requestObj.getString("consumer_loan_purpose"));
            parameters.put("consumer_seller_bank_account", requestObj.getString("consumer_seller_bank_account"));
            parameters.put("consumer_seller_bank_name", requestObj.getString("consumer_seller_bank_name"));
            parameters.put("consumer_seller_contract_number", requestObj.getString("consumer_seller_contract_number"));
            parameters.put("consumer_seller_contract_date", requestObj.getString("consumer_seller_contract_date"));
            parameters.put("consumer_guarantee_surety", requestObj.getString("consumer_guarantee_surety"));
            parameters.put("consumer_borrower_phone", requestObj.getString("consumer_borrower_phone"));

            JRDataSource dataSource = new JREmptyDataSource();

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            JasperExportManager.exportReportToPdfFile(jasperPrint, "output"+File.separator+"consumer_credit_"+i+".pdf");//create pdf file
        }

        PDFMergerUtility ut = new PDFMergerUtility();
        for (int i = 1; i <= 3; i++)
        {
            ut.addSource("output"+File.separator+"consumer_credit_"+i+".pdf");
        }
        ut.setDestinationFileName("output"+File.separator+"consumer_credit.pdf");//merge pdf name
        ut.mergeDocuments();//create merge pdf

        File file = new File("output"+File.separator+"consumer_credit.pdf");
        FileInputStream fis = new FileInputStream(file);

        ByteArrayOutputStream bos = new ByteArrayOutputStream(); //BLOB
        byte[] buf = new byte[1024];
        for(int readNum; (readNum = fis.read(buf)) != -1;)
        {
            bos.write(buf, 0, readNum);
        }

        for (int i = 1; i <= 3; i++) //delete parts
        {
            new File("output"+File.separator+"consumer_credit_"+i+".pdf").delete();
        }

        return bos;
    }
}