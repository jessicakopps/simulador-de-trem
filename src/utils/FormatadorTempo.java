package utils;

public class FormatadorTempo {
    public static String formatarMinutos(int minutos) {
        int horas = minutos / 60;
        int mins = minutos % 60;
        return String.format("%02d:%02d", horas, mins);
    }
}