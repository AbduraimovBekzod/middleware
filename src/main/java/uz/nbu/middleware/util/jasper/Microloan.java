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
        for (int i = 1; i <= 3; i++) {//запись параметров в report

            JasperReport jasperReport = lang == 0 ?
                    JasperCompileManager.compileReport(dir + "reports/microloan/rus/microloan_" + i + ".jrxml") :
                    JasperCompileManager.compileReport(dir + "reports/microloan/uzb/microloan_" + i + ".jrxml");

            Map<String, Object> parameters = new HashMap<String, Object>();

            parameters.put("microloan_blanking_type", requestObj.getString("microloan_blanking_type"));
            parameters.put("microloan_guarantee_surety_sum", requestObj.getString("microloan_guarantee_surety_sum"));
            parameters.put("microloan_borrower_passport_number", requestObj.getString("microloan_borrower_passport_number"));
            parameters.put("microloan_loan_repayment_rate", requestObj.getString("microloan_loan_repayment_rate"));
            parameters.put("microloan_delay_percent_rate", requestObj.getString("microloan_delay_percent_rate"));
            parameters.put("microloan_loan_term", requestObj.getString("microloan_loan_term"));
            parameters.put("microloan_percent_rate", requestObj.getString("microloan_percent_rate"));
            parameters.put("microloan_borrower_passport_issue_by", requestObj.getString("microloan_borrower_passport_issue_by"));
            parameters.put("microloan_borrower_address", requestObj.getString("microloan_borrower_address"));
            parameters.put("microloan_doverennost_date", requestObj.getString("microloan_doverennost_date"));
            parameters.put("microloan_borrower_fio", requestObj.getString("microloan_borrower_fio"));
            parameters.put("PSK_words", requestObj.getString("PSK_words"));
            parameters.put("PSK_number", requestObj.getString("PSK_number"));
            parameters.put("microloan_filial_name", requestObj.getString("microloan_filial_name"));
            parameters.put("microloan_doverennost_number", requestObj.getString("microloan_doverennost_number"));
            parameters.put("microloan_loan_sum", requestObj.getString("microloan_loan_sum"));
            parameters.put("microloan_emp_fio", requestObj.getString("microloan_emp_fio"));
            parameters.put("microloan_number", requestObj.getString("microloan_number"));
            parameters.put("microloan_city", requestObj.getString("microloan_city"));
            parameters.put("microloan_date", requestObj.getString("microloan_date"));
            parameters.put("microloan_bank_name", requestObj.getString("microloan_bank_name"));
            parameters.put("microloan_borrower_passport_issue_date", requestObj.getString("microloan_borrower_passport_issue_date"));
            parameters.put("microloan_committee_decision_number", requestObj.getString("microloan_committee_decision_number"));
            parameters.put("microloan_borrower_phone", requestObj.getString("microloan_borrower_phone"));
            parameters.put("microloan_committee_decision_date", requestObj.getString("microloan_committee_decision_date"));
            parameters.put("microloan_filial_inn", requestObj.getString("microloan_filial_inn"));
            parameters.put("microloan_filial_phone", requestObj.getString("microloan_filial_phone"));
            parameters.put("microloan_filial_address", requestObj.getString("microloan_filial_address"));
            parameters.put("microloan_guarantee_surety", requestObj.getString("microloan_guarantee_surety"));
            parameters.put("microloan_month_day", requestObj.getString("microloan_month_day"));
            if (requestObj.getInt("microloan_loan_term") <= 12) parameters.put("microloan_account", "12501");
            else parameters.put("microloan_account", "14901");

            JRDataSource dataSource = new JREmptyDataSource();

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            JasperExportManager.exportReportToPdfFile(jasperPrint, dir + "output/microloan_" + i + ".pdf");//create pdf file
        }

        PDFMergerUtility ut = new PDFMergerUtility();
        for (int i = 1; i <= 3; i++) {
            ut.addSource(dir + "output/microloan_" + i + ".pdf");
        }
        ut.setDestinationFileName(dir + "output/microloan.pdf");//merge pdf name
        ut.mergeDocuments();//create merge pdf

        File file = new File(dir + "output/microloan.pdf");
        FileInputStream fis = new FileInputStream(file);

        ByteArrayOutputStream bos = new ByteArrayOutputStream(); //BLOB
        byte[] buf = new byte[1024];
        for (int readNum; (readNum = fis.read(buf)) != -1; ) {
            bos.write(buf, 0, readNum);
        }

        for (int i = 1; i <= 3; i++) { //delete parts
            new File(dir + "output/microloan_" + i + ".pdf").delete();
        }

        return bos;
    }
}
