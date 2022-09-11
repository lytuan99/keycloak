package org.keycloak.testsuite.vbee.utils;

import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserProvider;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserUtils {

    private static UserModel singleUser(Stream<UserModel> users){
        List<UserModel> userList = users.collect(Collectors.toList());
        if (userList.size() == 0) {
            return null;
        }else {
            if (userList.size() > 1){
                return users
                        .filter(u -> u.getAttributeStream("phoneNumberVerified")
                                .anyMatch("true"::equals))
                        .findFirst().orElse(null);
            }else
                return userList.get(0);
        }
    }

    public static UserModel findUserByPhone(UserProvider userProvider, RealmModel realm, String phoneNumber){


        Stream<UserModel> users = userProvider.searchForUserByUserAttributeStream(realm,
                "phoneNumber", phoneNumber);
        return singleUser(users);
    }

    public static String randomVbeeEmail() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 8;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString + "@private.vbee.vn";
    }

}
