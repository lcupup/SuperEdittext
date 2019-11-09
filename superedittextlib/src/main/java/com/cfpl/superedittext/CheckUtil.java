package com.cfpl.superedittext;

import android.text.TextUtils;

/**
 * Date: 2019-11-09
 * User: chao
 */
public class CheckUtil {
    private static int PHONE_NUMBER_LENGTH = 11;
    private static int ID_NUMBER_LENGTH = 18;
    /**
     * 目前 要求 密码符合6位即可
     *
     * @param pwd 密码文本
     * @return is success
     */
    protected static boolean passwordStandard(String pwd) {
        return !TextUtils.isEmpty(pwd);
    }

    /**
     * 目前只要 校验 是否是 手机号 或者 是否是身份证号  11 或 18
     *
     * @param account
     * @return is success
     */
    protected static boolean accountStandard(String account) {


        return account.length() == PHONE_NUMBER_LENGTH || account.length() == ID_NUMBER_LENGTH;
    }

    protected static boolean isVerificationCode(String verification) {
        return verification.length() == 6;
    }

    protected static boolean isPhoneNumber(String phoneNumber) {
        String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";// "[1]"代表下一位为数字可以是几，"[0-9]"代表可以为0-9中的一个，"[5,7,9]"表示可以是5,7,9中的任意一位,[^4]表示除4以外的任何一个,\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(phoneNumber))
            return false;
        else
            return phoneNumber.matches(telRegex);

    }

}
