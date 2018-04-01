package com.xiaoa.superedittext;

import android.content.Context;
import android.content.res.TypedArray;
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

import com.xiaoa.superedittext.R;


/**
 * Created by lWX490070 on 2018/3/8.
 */

public class SuperEdittext extends RelativeLayout implements TextWatcher, View.OnClickListener {

    private ImageView clean;
    private ImageView hideOrShow;
    //用来标记当前密码显示状态
    private Boolean pwdIsSecret = false;
    public EditText inputEt;
    public String contant;
    private Context context;

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
        //用来q确认是 密码还是手机号输入框
        Boolean isPwdEt = typedArray.getBoolean(R.styleable.SuperEdittext_is_pwd_et, true);
        String hint = typedArray.getString(R.styleable.SuperEdittext_et_hint);


        View.inflate(context, R.layout.super_edittext, SuperEdittext.this);
        inputEt = (EditText) findViewById(R.id.input_et);
        clean = (ImageView) findViewById(R.id.super_edittext_clean);
        hideOrShow = (ImageView) findViewById(R.id.hide_or_show);

        inputEt.addTextChangedListener(this);
        clean.setOnClickListener(this);
        hideOrShow.setOnClickListener(this);
        inputEt.setHint(hint);
        initEdittext(isPwdEt);
        typedArray.recycle();

    }

    /**
     * 初始化 输入框
     * 根据类型
     *
     * @param isPwdEt
     */
    private void initEdittext(Boolean isPwdEt) {

        //是密码输入框
        //密码 长度 限定6-12位
        //输入类型  password
        //  小眼睛  出于永久显示状态
        if (isPwdEt) {
            //是密码输入框
            inputEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            inputEt.setFilters(new InputFilter[]{

                    new InputFilter.LengthFilter(12)

            });
            hideOrShow.setVisibility(VISIBLE);
            //是手机号 输入框
            // 输入 类型 手机号
            //输入长度 限制11位
            // 小眼睛出于永久隐藏状态
        } else {
            inputEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
            inputEt.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
            hideOrShow.setVisibility(GONE);
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
        if (null != textOnchanged) {

            textOnchanged.onChanged();

        }
    }


    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {

        int vID=v.getId();
      if (vID==R.id.super_edittext_clean){
          inputEt.setText("");
      }else if (vID== R.id.hide_or_show){
          initPwd(pwdIsSecret);
          pwdIsSecret = !pwdIsSecret;

      }

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
            hideOrShow.setImageDrawable(context.getResources().getDrawable(R.drawable.hide_pwd));
        } else {
            hideOrShow.setImageDrawable(context.getResources().getDrawable(R.drawable.show_pwd));
        }
    }

    InterfaceUtil.TextOnchanged textOnchanged;

    public void setOnTextChanged(InterfaceUtil.TextOnchanged textOnchanged) {

        this.textOnchanged = textOnchanged;

    }


}
