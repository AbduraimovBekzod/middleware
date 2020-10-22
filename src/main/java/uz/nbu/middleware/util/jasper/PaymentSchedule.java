package uz.nbu.middleware.util.jasper;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.query.JsonQueryExecuterFactory;
import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class PaymentSchedule {
    ByteArrayOutputStream schedule (JSONObject requestObj, Integer lang) throws JRException, IOException {
        String dir = "/home/";
		JasperReport jasperReport = lang == 0 ?
                JasperCompileManager.compileReport(dir + "reports/payment_schedule/rus/payment_schedule.jrxml"):
                JasperCompileManager.compileReport(dir + "reports/payment_schedule/uzb/payment_schedule.jrxml");

		JSONArray paymentSchedule = requestObj.getJSONArray("payment_schedule_month");

        for (int i = 0; i < paymentSchedule.length(); i++) {
            JSONObject tempJsonObject = paymentSchedule.getJSONObject(i);
            String tempMonthBegin = tempJsonObject.getString("month_begin");
            String tempPercent = tempJsonObject.getString("percent");
            String tempMainDebt = tempJsonObject.getString("main_debt");
            String tempTotal = tempJsonObject.getString("total");
            String tempMonthEnd = tempJsonObject.getString("month_end");

            tempMonthBegin = NumberFormat.getNumberInstance(Locale.US).format(Double.parseDouble(tempMonthBegin));
            tempPercent = NumberFormat.getNumberInstance(Locale.US).format(Double.parseDouble(tempPercent));
            tempMainDebt = NumberFormat.getNumberInstance(Locale.US).format(Double.parseDouble(tempMainDebt));
            tempTotal = NumberFormat.getNumberInstance(Locale.US).format(Double.parseDouble(tempTotal));
            tempMonthEnd = NumberFormat.getNumberInstance(Locale.US).format(Double.parseDouble(tempMonthEnd));

            tempMonthBegin = tempMonthBegin.replaceAll(",", " ");
            tempPercent = tempPercent.replaceAll(",", " ");
            tempMainDebt = tempMainDebt.replaceAll(",", " ");
            tempTotal = tempTotal.replaceAll(",", " ");
            tempMonthEnd = tempMonthEnd.replaceAll(",", " ");

            tempJsonObject.put("month_begin", tempMonthBegin);
            tempJsonObject.put("percent", tempPercent);
            tempJsonObject.put("main_debt", tempMainDebt);
            tempJsonObject.put("total", tempTotal);
            tempJsonObject.put("month_end", tempMonthEnd);

            paymentSchedule.put(i, tempJsonObject);
        }

        requestObj.put("payment_schedule_month", paymentSchedule);

		String temp = requestObj.getString("total_percent");
        temp = NumberFormat.getNumberInstance(Locale.US).format(Double.parseDouble(temp));
        temp = temp.replaceAll(",", " ");
        requestObj.put("total_percent", temp);

        temp = requestObj.getString("total_debt");
        temp = NumberFormat.getNumberInstance(Locale.US).format(Math.ceil(Double.parseDouble(temp)));
        temp = temp.replaceAll(",", " ");
        requestObj.put("total_debt", temp);

        String str = requestObj.toString();

        try (ByteArrayInputStream is = new ByteArrayInputStream(str.getBytes())) {
            Map<String,Object> parametersMap = new HashMap<>();

            parametersMap.put(JsonQueryExecuterFactory.JSON_INPUT_STREAM, is);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametersMap);
            JasperExportManager.exportReportToPdfFile(jasperPrint, "output/payment_schedule.pdf");
        }

        File file = new File("output/payment_schedule.pdf");

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
