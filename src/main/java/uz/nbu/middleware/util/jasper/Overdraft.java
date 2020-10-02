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

public class Overdraft {
    public ByteArrayOutputStream overdraft(JSONObject requestObj, Integer lang) throws JRException, IOException {
        String dir = "/home/";
        for (int i = 1; i <= 3; i++) { //запись параметров в report

            JasperReport jasperReport = lang == 0 ?
                    JasperCompileManager.compileReport(dir + "reports/overdraft/rus/overdraft_" + i + ".jrxml") :
                    JasperCompileManager.compileReport(dir + "reports/overdraft/uzb/overdraft_" + i + ".jrxml");

            Map<String, Object> parameters = new HashMap<String, Object>();

            parameters.put("overdraft_guarantee_surety_sum", requestObj.getString("overdraft_guarantee_surety_sum"));
            parameters.put("overdraft_borrower_passport_number", requestObj.getString("overdraft_borrower_passport_number"));
            parameters.put("overdraft_loan_repayment_rate", requestObj.getString("overdraft_loan_repayment_rate"));
            parameters.put("overdraft_delay_percent_rate", requestObj.getString("overdraft_delay_percent_rate"));
            parameters.put("overdraft_loan_term", requestObj.getString("overdraft_loan_term"));
            parameters.put("overdraft_percent_rate", requestObj.getString("overdraft_percent_rate"));
            parameters.put("overdraft_borrower_passport_issue_by", requestObj.getString("overdraft_borrower_passport_issue_by"));
            parameters.put("overdraft_borrower_address", requestObj.getString("overdraft_borrower_address"));
            parameters.put("overdraft_doverennost_date", requestObj.getString("overdraft_doverennost_date"));
            parameters.put("overdraft_borrower_fio", requestObj.getString("overdraft_borrower_fio"));
            parameters.put("PSK_number", requestObj.getString("PSK_number"));
            parameters.put("PSK_words", requestObj.getString("PSK_words"));
            parameters.put("overdraft_filial_name", requestObj.getString("overdraft_filial_name"));
            parameters.put("overdraft_doverennost_number", requestObj.getString("overdraft_doverennost_number"));
            parameters.put("overdraft_loan_sum", requestObj.getString("overdraft_loan_sum"));
            parameters.put("overdraft_emp_fio", requestObj.getString("overdraft_emp_fio"));
            parameters.put("overdraft_number", requestObj.getString("overdraft_number"));
            parameters.put("overdraft_grace_period", requestObj.getString("overdraft_grace_period"));
            parameters.put("overdraft_city", requestObj.getString("overdraft_city"));
            parameters.put("overdraft_date", requestObj.getString("overdraft_date"));
            parameters.put("overdraft_bank_name", requestObj.getString("overdraft_bank_name"));
            parameters.put("overdraft_committee_decision_number", requestObj.getString("overdraft_committee_decision_number"));
            parameters.put("overdraft_borrower_passport_issue_date", requestObj.getString("overdraft_borrower_passport_issue_date"));
            parameters.put("overdraft_borrower_phone", requestObj.getString("overdraft_borrower_phone"));
            parameters.put("overdraft_committee_decision_date", requestObj.getString("overdraft_committee_decision_date"));
            parameters.put("overdraft_filial_inn", requestObj.getString("overdraft_filial_inn"));
            parameters.put("overdraft_filial_phone", requestObj.getString("overdraft_filial_phone"));
            parameters.put("overdraft_filial_address", requestObj.getString("overdraft_filial_address"));
            parameters.put("overdraft_guarantee_surety", requestObj.getString("overdraft_guarantee_surety"));
            parameters.put("overdraft_filial_mfo", requestObj.getString("overdraft_filial_mfo"));
			parameters.put("overdraft_percent_rate_word", requestObj.getString("overdraft_percent_rate_word"));

            JRDataSource dataSource = new JREmptyDataSource();

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            JasperExportManager.exportReportToPdfFile(jasperPrint, dir + "output/overdraft_" + i + ".pdf");//create pdf file
        }

        PDFMergerUtility ut = new PDFMergerUtility();
        for (int i = 1; i <= 3; i++) {
            ut.addSource(dir + "output/overdraft_" + i + ".pdf");
        }
        ut.setDestinationFileName(dir + "output/overdraft.pdf");//merge pdf name
        ut.mergeDocuments();//create merge pdf

        File file = new File(dir + "output/overdraft.pdf");
        FileInputStream fis = new FileInputStream(file);

        ByteArrayOutputStream bos = new ByteArrayOutputStream(); //BLOB
        byte[] buf = new byte[1024];
        for (int readNum; (readNum = fis.read(buf)) != -1; ) {
            bos.write(buf, 0, readNum);
        }

        for (int i = 1; i <= 3; i++) {//delete parts
            new File(dir + "output/overdraft_" + i + ".pdf").delete();
        }

        return bos;
    }
}
