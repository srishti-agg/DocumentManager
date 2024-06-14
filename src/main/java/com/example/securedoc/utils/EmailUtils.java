package com.example.securedoc.utils;

public class EmailUtils {

    public static String getEmailMessage(String name, String host, String token) {

        return "Hello " + name + ",\n\nYour new account has been created. Please click on the link below" +
                " to verify your new account.\n\n"+ getVerificationUrl(host, token);
    }

    private static String getVerificationUrl(String host, String token) {

        return host+"/user/verify/account?token="+token;
    }

    public static String getResetPassword(String name, String host, String token) {
        return "Hello " + name + ",\n\nPlease click on the link below" +
                " to set new password.\n\n"+ getResetPasswordUrl(host, token);
    }

    private static String getResetPasswordUrl(String host, String token) {

        return host+"/user/verify/password?token="+token;
    }
}
