package com.android.miniproject.teacherkit;

/**
 * Created by user on 1/3/2017.
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    private Pattern emailPattern;
    private Pattern passwordpattern;

    private Matcher emailMatcher;
    private Matcher passwordMatcher;
    private static final String PASSWORD_PATTERN="(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}";

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";




    private Pattern stringPattern;
    private Matcher stringMatcher;

    private static final String STRING_PATTERN =
            "^[a-zA-Z]+$";

    //String regex = "";


    public Validator() {
        emailPattern = Pattern.compile(EMAIL_PATTERN);
        stringPattern = Pattern.compile(STRING_PATTERN);
        passwordpattern = Pattern.compile(PASSWORD_PATTERN);

    }

    /**
     * Validate hex with regular expression
     *
     * @param hex
     *            hex for validation
     * @return true valid hex, false invalid hex
     */
    public boolean validateEmail(final String hex) {

        emailMatcher = emailPattern.matcher(hex);
        return emailMatcher.matches();

    }

    public boolean validatePass(final String hex) {

        passwordMatcher = passwordpattern.matcher(hex);
        return passwordMatcher.matches();

    }


    public boolean validateString(final String hex) {

        stringMatcher = stringPattern.matcher(hex);
        return stringMatcher.matches();

    }


    public static void main(String [] args){

        Validator validator=new Validator();

        String email="aaa@h.k.com.";
//		System.out.println("test mail:"+validator.validate(email));

        String string="AAArtrt";
        System.out.println("test str:"+validator.validateString(string));

    }
}