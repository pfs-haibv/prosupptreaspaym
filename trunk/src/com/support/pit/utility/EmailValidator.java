/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.support.pit.utility;

import com.support.pit.system.Constants;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Administrator
 */
public class EmailValidator {

    private static Pattern pattern;
    private static Matcher matcher;

    public EmailValidator() {
        pattern = Pattern.compile(Constants.EMAIL_PATTERN);
    }

    /**
     * Validate hex with regular expression
     * @param hex hex for validation
     * @return true valid hex, false invalid hex
     */
    public static boolean validate(final String hex) {
        pattern = Pattern.compile(Constants.EMAIL_PATTERN);
        matcher = pattern.matcher(hex);
        return matcher.matches();
    }
}
