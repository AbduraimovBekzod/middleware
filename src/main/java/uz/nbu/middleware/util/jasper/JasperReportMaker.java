package uz.nbu.middleware.util.jasper;import net.sf.jasperreports.engine.*;import org.json.JSONObject;import java.io.*;public class JasperReportMaker {    public ByteArrayOutputStream makePdfBlob(String content) throws IOException, JRException {		content = content.replace("\"null\"", "");        JSONObject requestObj = new JSONObject(content);        ByteArrayOutputStream baos = new ByteArrayOutputStream();        switch (requestObj.getString("type")) {            case "overdraft":                Overdraft od = new Overdraft();                baos = od.overdraft(requestObj.getJSONObject("data"), requestObj.getInt("lang"));                break;            case "consumer_credit":                ConsumerCredit cc = new ConsumerCredit();                baos = cc.consumer_credit(requestObj.getJSONObject("data"), requestObj.getInt("lang"));                break;            case "microloan":                Microloan ml = new Microloan();                baos = ml.microloan(requestObj.getJSONObject("data"), requestObj.getInt("lang"));                break;            case "protocol":                Protocol pc = new Protocol();                baos = pc.protocol(requestObj.getJSONObject("data"), requestObj.getInt("lang"));                break;            case "scoring_results":                ScoringResults sr = new ScoringResults();                baos = sr.scoring_results(requestObj.getJSONObject("data"), requestObj.getInt("lang"));                break;            case "protocol_form":                ProtocolForm pf = new ProtocolForm();                baos = pf.protocol_form(requestObj.getJSONObject("data"), requestObj.getInt("lang"));                break;            case "info_list":                InfoList il = new InfoList();                baos = il.InfoList(requestObj.getJSONObject("data"), requestObj.getInt("lang"));                break;            case "offer":                Offer off = new Offer();                baos = off.offer(requestObj.getJSONObject("data"), requestObj.getInt("lang"));                break;            case "overdraft_guarantor_physical":                OverdraftGuarantorPhysical ogp = new OverdraftGuarantorPhysical();                baos = ogp.overdraft_guarantor_physical(requestObj.getJSONObject("data"), requestObj.getInt("lang"));                break;            case "overdraft_guarantor_legal":                OverdraftGuarantorLegal ogl = new OverdraftGuarantorLegal();                baos = ogl.overdraft_guarantor_legal(requestObj.getJSONObject("data"), requestObj.getInt("lang"));                break;            case "payment_schedule":                PaymentSchedule ps = new PaymentSchedule();                baos = ps.schedule(requestObj.getJSONObject("data"), requestObj.getInt("lang"));                break;            case "microloan_guarantor_physical":                MicroloanGuarantorPhysical mgp = new MicroloanGuarantorPhysical();                baos = mgp.microloan_guarantor_physical(requestObj.getJSONObject("data"), requestObj.getInt("lang"));                break;            case "microloan_guarantor_legal":                MicroloanGuarantorLegal mgl = new MicroloanGuarantorLegal();                baos = mgl.microloan_guarantor_legal(requestObj.getJSONObject("data"), requestObj.getInt("lang"));                break;            case "consumer_guarantor_physical":                ConsumerGuarantorPhysical cgp = new ConsumerGuarantorPhysical();                baos = cgp.consumer_guarantor_physical(requestObj.getJSONObject("data"), requestObj.getInt("lang"));                break;            case "consumer_guarantor_legal":                ConsumerGuarantorLegal cgl = new ConsumerGuarantorLegal();                baos = cgl.consumer_guarantor_legal(requestObj.getJSONObject("data"), requestObj.getInt("lang"));                break;            case "personal":                switch (requestObj.getString("doc_type")) {                    case "fishka":                        Fishka fishka = new Fishka();                        baos = fishka.fishka(requestObj.getJSONObject("data"));                        break;                }        }        return baos;    }}