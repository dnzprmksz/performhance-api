package com.monitise.performhance.helpers;

import java.util.ArrayList;

public class Util {

    public static String generateExistingUsersMessage(ArrayList<Integer> existingUserList) {
        String message = null;
        if (!existingUserList.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Completed successfully, however, the criteria was already assigned for following users:");
            for (int userId : existingUserList) {
                String appendUserId = " " + userId;
                stringBuilder.append(appendUserId);
            }
            message = stringBuilder.toString();
        }
        return message;
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().equals("");
    }

}
