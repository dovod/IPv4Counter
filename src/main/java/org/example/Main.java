package org.example;

public class Main {
    private static final String ARG_NAME_PRECISION = "precision";
    private static final String ARG_NAME_FILE = "fileName";
    private static String fileName = "ip_addresses";
    private static int precision = 0;

    public static void main(String[] args) {
        CounterService counterService = new CounterService();
        getProperty(args);
        counterService.calcUniqueCount(fileName, precision);
    }

    private static void getProperty(String[] args) {
        String[] rawArg;
        String argValue;
        String argName;
        for (String s : args) {
            rawArg = s.split("=");
            argName = rawArg[0].trim();
            argValue = rawArg[1].trim();

            if (!argValue.isEmpty() && !argValue.isBlank()) {
                if (ARG_NAME_FILE.equalsIgnoreCase(argName)) {
                    fileName = argValue;
                } else if (ARG_NAME_PRECISION.equalsIgnoreCase(argName)) {
                    precision = Integer.parseInt(argValue);
                }
            }
        }
    }
}