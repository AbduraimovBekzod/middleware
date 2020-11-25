package uz.nbu.middleware.util.jasper;

public class Functions {
    public String indents (String number) {

        number = number.replaceAll(" ", "").replaceAll(",", ".");
        return number.equals("") ? "":
                String.format("%,.2f", Double.parseDouble(number));
//        String.format("%,.2f", Double.parseDouble(number)).replaceAll(",", " ");
    }
}
