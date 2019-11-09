package com.cfpl.superedittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * Created by lWX490070 on 2018/3/8.
 */

public class SuperEdittext extends RelativeLayout implements TextWatcher, View.OnClickListener {
    private final static int SUPER_ACCOUNT = 0;
    private final static int SUPER_PASS_WORD = 1;
    private final static int SUPER_PHONE = 2;
    private final static int SUPER_VERIFICATION = 3;


    private ImageView clean;
    private ImageView hideOrShow;
    //用来标记当前密码显示状态
    private boolean pwdIsSecret = false;
    public EditText inputEt;
    public String contant;
    private Context context;
    private Drawable iconDrawable;
    private TextView getVerificationCode;


    private CountDownTimer timer;
    private int superType;


    public SuperEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context, attrs);
    }


    public SuperEdittext(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    /**
     * 初始化布局
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {

        // 获取控件资源
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SuperEdittext);
        //用来确认是 密码还是手机号输入框
        superType = typedArray.getInteger(R.styleable.SuperEdittext_super_edit_type, 0);
        String hint = typedArray.getString(R.styleable.SuperEdittext_super_edit_text_hint);
        View.inflate(context, R.layout.view_super_edittext, SuperEdittext.this);
        inputEt = findViewById(R.id.super_edit_text_input_et);
        clean = findViewById(R.id.super_edit_text_clean);
        hideOrShow = findViewById(R.id.super_edit_text_status);
        getVerificationCode = findViewById(R.id.super_edit_get_verification_img);

        if (typedArray.hasValue(R.styleable.SuperEdittext_super_edit_text_icon)) {
            iconDrawable = typedArray.getDrawable(R.styleable.SuperEdittext_super_edit_text_icon);
            ((ImageView) findViewById(R.id.super_edit_text_icon)).setImageDrawable(iconDrawable);
        }

        if (superType == SUPER_VERIFICATION) {
            getVerificationCode.setVisibility(VISIBLE);
            if (timer == null) {
                initCountDownTimer();
            }
        } else {
            hideOrShow.setVisibility(GONE);
        }
        getVerificationCode.setOnClickListener(this);
        inputEt.addTextChangedListener(this);
        clean.setOnClickListener(this);
        hideOrShow.setOnClickListener(this);
        inputEt.setHint(hint);
        initEdittext();
        typedArray.recycle();
    }

    /**
     * 初始化 输入框
     */
    private void initEdittext() {
        hideOrShow.setVisibility(GONE);
        inputEt.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        switch (superType) {

            case SUPER_ACCOUNT:
                inputEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)});
                break;
            case SUPER_PASS_WORD:
                inputEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                hideOrShow.setVisibility(VISIBLE);
                break;
            case SUPER_PHONE:
                inputEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
                break;
            case SUPER_VERIFICATION:
                inputEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
                break;

            default:
                break;

        }

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //判断 是否  输入框是否有内容
        //有内容
        contant = s.toString();

        clean.setVisibility(TextUtils.isEmpty(contant) ? GONE : VISIBLE);
        boolean isStandard = false;
        if (null != superEditTextListener) {
            switch (superType) {
                case SUPER_PHONE:
                    isStandard = CheckUtil.isPhoneNumber(contant);
                    break;
                case SUPER_ACCOUNT:
                    isStandard = CheckUtil.accountStandard(contant);
                    break;
                case SUPER_VERIFICATION:
                    isStandard = CheckUtil.isVerificationCode(contant);
                    break;
                case SUPER_PASS_WORD:
                    isStandard = CheckUtil.passwordStandard(contant);
                    break;
                default:
                    break;

            }

            superEditTextListener.onChanged(SuperEdittext.this, isStandard, contant);
        }
    }


    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        int vID = v.getId();
        if (vID == R.id.super_edit_text_clean) {
            inputEt.setText("");
        } else if (vID == R.id.super_edit_text_status) {
            initPwd(pwdIsSecret);
            pwdIsSecret = !pwdIsSecret;
        } else if (vID == R.id.super_edit_get_verification_img) {
//            if (!isHaveGetVerificayionImg) {
//                timer.start();
//            isHaveGetVerificayionImg = true;
            superEditTextListener.getImgVerificationCode();
//            }
        }

    }

    public void startTimer() {
        if (timer != null) {
            timer.start();
        }
        getVerificationCode.setEnabled(false);

    }

    /**
     * 置换密码  密文或者明文
     *
     * @param pwdIsSecret
     */
    private void initPwd(Boolean pwdIsSecret) {
        //显示明文  密文
        inputEt.setTransformationMethod(pwdIsSecret ? PasswordTransformationMethod.getInstance() : HideReturnsTransformationMethod.getInstance());
        inputEt.setSelection(contant.length());
        if (pwdIsSecret) {
            hideOrShow.setImageDrawable(context.getResources().getDrawable(R.drawable.visibility_off));
        } else {
            hideOrShow.setImageDrawable(context.getResources().getDrawable(R.drawable.visibility));
        }
    }

    InterfaceUtil.SuperEditTextListener superEditTextListener;

    public void setSuperEditTextListener(InterfaceUtil.SuperEditTextListener textOnchange) {

        this.superEditTextListener = textOnchange;

    }

    private boolean isHaveGetVerificayionImg;

    private void initCountDownTimer() {

        timer = new CountDownTimer(60000, 1000) {


            @Override
            public void onTick(long millisUntilFinished) {
                long temp = millisUntilFinished / 1000;
                if (temp != 0) {
                    getVerificationCode.setText(millisUntilFinished / 1000 + "s");
                }
            }

            @Override
            public void onFinish() {
                getVerificationCode.setEnabled(true);
                isHaveGetVerificayionImg = false;
                getVerificationCode.setText(context.getString(R.string.login_get_verification_code));
            }
        };

    }

    public void destory() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

    }

    public void exchangeVerificationStatus(boolean isPass) {
        getVerificationCode.setEnabled(isPass);
    }
}