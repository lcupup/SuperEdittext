package com.cfpl.superedittextdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cfpl.superedittext.InterfaceUtil;
import com.cfpl.superedittext.SuperEdittext;

public class MainActivity extends AppCompatActivity implements InterfaceUtil.SuperEditTextListener {

    private SuperEdittext superEdittextVerification;
    private SuperEdittext superPassword;
    private SuperEdittext superPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        superPhone = findViewById(R.id.super_phone);
        superPassword = findViewById(R.id.super_pass_word);
        superEdittextVerification = findViewById(R.id.super_verification);
        superEdittextVerification.setSuperEditTextListener(this);
        superPassword.setSuperEditTextListener(this);
        superPhone.setSuperEditTextListener(this);
    }

    @Override
    public void onChanged(View view, boolean isStandard, String text) {

        switch (view.getId()) {
            case R.id.super_phone:
                Log.i("cc", "onChanged: super_phone::" + text);
                break;
            case R.id.super_pass_word:
                Log.i("cc", "onChanged: super_pass_word::" + text);
                break;
            case R.id.super_verification:
                Log.i("cc", "onChanged: super_verification::" + text);
                break;
            default:
                break;
        }
    }

    @Override
    public void getImgVerificationCode() {
        Toast.makeText(this, "获取验证码", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        superEdittextVerification.destory();
        superPassword.destory();
        superPhone.destory();
        super.onDestroy();

    }
}
