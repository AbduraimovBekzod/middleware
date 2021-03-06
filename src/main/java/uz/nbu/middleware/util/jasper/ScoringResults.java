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

        Functions func = new Functions();
        requestObj.put("result_sum", func.indents(requestObj.getString("result_sum")));

        String str = requestObj.toString();

        try (ByteArrayInputStream is = new ByteArrayInputStream(str.getBytes())) {
            Map<String, Object> parametersMap = new HashMap<>();

            parametersMap.put(JsonQueryExecuterFactory.JSON_INPUT_STREAM, is);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametersMap);
            JasperExportManager.exportReportToPdfFile(jasperPrint, "output" + File.separator + "scoring_results.pdf");
        }

        File file = new File("output" + File.separator + "scoring_results.pdf");

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
