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

public class InfoList {
    public ByteArrayOutputStream InfoList(JSONObject requestObj, Integer lang) throws JRException, IOException {

        String dir = "/home/";
        //first part
        JasperReport jasperReport = lang == 0 ?
                JasperCompileManager.compileReport(dir + "reports/info_list/rus/1-part.jrxml"):
                JasperCompileManager.compileReport(dir + "reports/info_list/uzb/1-part.jrxml");

        Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put("department_name", requestObj.getString("department_name"));
        parameters.put("fullname_and_position_emp", requestObj.getString("fullname_and_position_emp"));
        parameters.put("completion_date", requestObj.getString("completion_date"));
        parameters.put("loan_purpose", requestObj.getString("loan_purpose"));
        parameters.put("additional_costs", requestObj.getString("additional_costs"));
        parameters.put("one_time_amount", requestObj.getString("one_time_amount"));
        parameters.put("loan_rate_monetary_equivalent", requestObj.getString("loan_rate_monetary_equivalent"));
        parameters.put("loan_currency", requestObj.getString("loan_currency"));
        parameters.put("loan_term", requestObj.getString("loan_term"));
        parameters.put("loan_amount", requestObj.getString("loan_amount"));
        parameters.put("loan_rate_percent", requestObj.getString("loan_rate_percent"));
        parameters.put("additional_costs_third_party", requestObj.getString("additional_costs_third_party"));
        parameters.put("additional_costs_bank", requestObj.getString("additional_costs_bank"));
        parameters.put("loan_total_cost", requestObj.getString("loan_total_cost"));
        parameters.put("repayment_method", requestObj.getString("repayment_method"));
        parameters.put("payments_frequency", requestObj.getString("payments_frequency"));

        JRDataSource dataSource = new JREmptyDataSource();

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        JasperExportManager.exportReportToPdfFile(jasperPrint, dir + "output/info_list_1.pdf");//create pdf file
        //end first part
        //second part
        jasperReport = lang == 0 ?
                JasperCompileManager.compileReport(dir + "reports/info_list/rus/2-part.jrxml"):
                JasperCompileManager.compileReport(dir + "reports/info_list/uzb/2-part.jrxml");

        parameters = new HashMap<String, Object>();

        parameters.put("rate_for_late", requestObj.getString("rate_for_late"));
        parameters.put("collateral_loan", requestObj.getString("collateral_loan"));
        parameters.put("email", requestObj.getString("email"));
        parameters.put("phone_number", requestObj.getString("phone_number"));
        parameters.put("penalty_for_violation", requestObj.getString("penalty_for_violation"));
        parameters.put("mail", requestObj.getString("mail"));

        dataSource = new JREmptyDataSource();

        jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        JasperExportManager.exportReportToPdfFile(jasperPrint, dir + "output/info_list_2.pdf");//create pdf file
        //end second part

        PDFMergerUtility ut = new PDFMergerUtility();
        ut.addSource(dir + "output/info_list_1.pdf");
        ut.addSource(dir + "output/info_list_2.pdf");
        ut.setDestinationFileName(dir + "output/info_list.pdf");//merge pdf name
        ut.mergeDocuments();//create merge pdf

        File file = new File(dir + "output/info_list.pdf");
        FileInputStream fis = new FileInputStream(file);

        ByteArrayOutputStream bos = new ByteArrayOutputStream(); //BLOB
        byte[] buf = new byte[1024];
        for(int readNum; (readNum = fis.read(buf)) != -1;) {
            bos.write(buf, 0, readNum);
        }

        new File(dir + "output/info_list_1.pdf").delete();
        new File(dir + "output/info_list_2.pdf").delete();

        return bos;
    }
}
