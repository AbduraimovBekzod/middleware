package uz.nbu.middleware.util.jasper;

import net.sf.jasperreports.engine.*;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ScoringResults {

    public ByteArrayOutputStream scoring_results(JSONObject requestObj, Integer lang) throws JRException, IOException {
        String dir = "/home/";
        JasperReport jasperReport = lang == 0 ?
                JasperCompileManager.compileReport(dir + "reports/scoring_results/rus/scoring_results.jrxml") :
                JasperCompileManager.compileReport(dir + "reports/scoring_results/uzb/scoring_results.jrxml");

        functions func = new functions();

        Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put("result_emp_fio", requestObj.getString("result_emp_fio"));
        parameters.put("result_borrower_fio", requestObj.getString("result_borrower_fio"));
        parameters.put("result_borrower_phone", requestObj.getString("result_borrower_phone"));
        parameters.put("result_borrower_address", requestObj.getString("result_borrower_address"));
        parameters.put("result_borrower_date", requestObj.getString("result_borrower_date"));
        parameters.put("result_borrower_request_number", requestObj.getString("result_borrower_request_number"));
        parameters.put("result_product", requestObj.getString("result_product"));
        parameters.put("result_sum", func.indents(requestObj.getString("result_sum")));
        parameters.put("result_currency", requestObj.getString("result_currency"));
        parameters.put("result_filial", requestObj.getString("result_filial"));

        JRDataSource dataSource = new JREmptyDataSource();
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint, dir + "output/scoring_results.pdf");//create pdf file

        File file = new File(dir + "output/scoring_results.pdf");
        FileInputStream fis = new FileInputStream(file);

        ByteArrayOutputStream bos = new ByteArrayOutputStream(); //BLOB
        byte[] buf = new byte[1024];
        for (int readNum; (readNum = fis.read(buf)) != -1; ) {
            bos.write(buf, 0, readNum);
        }

        return bos;
    }
}
