package org.keycloak.testsuite.vbee.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static String convertObjectToJson(Object obj) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = null;
        try {
            json = ow.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static String randomOtpCode(int length) {
        char[] chars = "0123456789".toCharArray();
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder((rnd.nextInt(9000000)));
        for (int i = 0; i < length; i++)
            sb.append(chars[rnd.nextInt(chars.length)]);

        return sb.toString();
    }

    public static Map<String, String> mapListMapToStringMap(Map<String, List<String>> map) {
        Map<String, String> result = new HashMap<String, String>();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            String key = stringToSnakeCase(entry.getKey());
            result.put(key, entry.getValue().get(0));
        }
        return result;
    }

    public static String stringToSnakeCase(String camelStr) {
        String result = "";
        char c = camelStr.charAt(0);
        result = result + Character.toLowerCase(c);
        for (int i = 1; i < camelStr.length(); i++) {
            char ch = camelStr.charAt(i);
            if (Character.isUpperCase(ch)) {
                result = result + '_';
                result = result + Character.toLowerCase(ch);
            }
            else {
                result = result + ch;
            }
        }
        return result;
    }

    public static boolean isPhoneNumberValid(String phoneNumber) {
        String regex = "^\\d{10}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}
