package com.cfpl.superedittext;

import android.view.View;

/**
 * Created by 40645 on 2018/4/2.
 */

public class InterfaceUtil {

    public interface SuperEditTextListener {
        /**
         * 输入框文本发生变化
         *
         * @param view       edittext
         * @param isStandard 输入问题是否符合规范
         * @param text       对应文本
         */
        void onChanged(View view, boolean isStandard, String text);

        /**
         * 显示 图形验证码 dialog
         */
        void getImgVerificationCode();

    }
}
