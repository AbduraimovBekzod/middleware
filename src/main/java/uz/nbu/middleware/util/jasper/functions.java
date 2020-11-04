package uz.nbu.middleware.util.jasper;

public class functions {
    public String indents (String number) {

        number = number.replaceAll(" ", "");
        return number.equals("") ? "":
                String.format("%,.0f", Double.parseDouble(number)).replaceAll(",", " ");
    }
}
