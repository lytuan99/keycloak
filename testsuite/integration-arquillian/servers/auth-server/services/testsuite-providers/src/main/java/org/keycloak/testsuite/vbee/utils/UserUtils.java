package org.keycloak.testsuite.vbee.utils;

import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserProvider;

import java.util.List;
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

}
