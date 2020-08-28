package uz.nbu.middleware.util.jasper;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.query.JsonQueryExecuterFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Fishka {
    public ByteArrayOutputStream fishka(JSONObject requestObj) throws JRException, IOException {

		String dir = "/home/";
        JasperReport jasperReport = JasperCompileManager.compileReport(dir + "reports/personal/fishka/fishka.jrxml");

        JSONArray performer = requestObj.getJSONArray("performer");
        JSONArray performer1 = new JSONArray();
        JSONArray performer2 = new JSONArray();

        for(int i = 0; i < performer.length(); i++) {
            JSONObject perf1 = performer.getJSONObject(i);
            performer1.put(i/2, perf1);

            i = i + 1;

            if (i < performer.length()) {
                JSONObject perf2 = performer.getJSONObject(i);
                performer2.put((i-1)/2, perf2);
            }
        }

        requestObj.remove("performer");
        requestObj.put("performer1", performer1);
        requestObj.put("performer2", performer2);

        String str = requestObj.toString();

        try (ByteArrayInputStream is = new ByteArrayInputStream(str.getBytes())) {
            Map<String, Object> parametersMap = new HashMap<>();

            parametersMap.put(JsonQueryExecuterFactory.JSON_INPUT_STREAM, is);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametersMap);
            JasperExportManager.exportReportToPdfFile(jasperPrint, "output/fishka.pdf");
        }

        File file = new File("output/fishka.pdf");

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
