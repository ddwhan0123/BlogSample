package com.dialogdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button showMyLoadingDlg, showInfoDlgOne, showInfoDlgTwo, showInputDlg, showListDlg, showComplexInputDlg;
    DialogUtils dialogUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialogUtils = DialogUtils.getInstance();
        findId();
        init();
    }

    private void init() {
        showMyLoadingDlg.setOnClickListener(this);
        showInfoDlgTwo.setOnClickListener(this);
        showInfoDlgOne.setOnClickListener(this);
        showInputDlg.setOnClickListener(this);
        showComplexInputDlg.setOnClickListener(this);
        showListDlg.setOnClickListener(this);
    }

    private void findId() {
        showMyLoadingDlg = (Button) findViewById(R.id.showMyLoadingDlg);
        showInfoDlgTwo = (Button) findViewById(R.id.showInfoDlgTwo);
        showInfoDlgOne = (Button) findViewById(R.id.showInfoDlgOne);
        showInputDlg = (Button) findViewById(R.id.showInputDlg);
        showListDlg = (Button) findViewById(R.id.showListDlg);
        showComplexInputDlg = (Button) findViewById(R.id.showComplexInputDlg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showMyLoadingDlg:
                dialogUtils.showProgressDialogs(this, "这标题", "测试用的很长的测试文本内容...");
                break;
            case R.id.showInfoDlgTwo:
                dialogUtils.showInfoDlg(this, "这是标题", "这是提示的内容", "确认", "取消");
                break;
            case R.id.showInfoDlgOne:
                dialogUtils.showInfoDlg(this, "这是标题", "这是提示的内容", "确认");
                break;
            case R.id.showInputDlg:
                dialogUtils.showInputDlg(this, "标题", 6, 6, "在此输入pin");
                break;
            case R.id.showListDlg:
                dialogUtils.showListDlg(this, "选择蓝牙设备", makeData(), "确认");
                break;
            case R.id.showComplexInputDlg:
                dialogUtils.showComplexInputDlg(this, "标题", "确认", 6, 6);
                break;
        }

    }


    /**
     * 仿造List数据源
     **/
    private ArrayList<BleInformation> makeData() {
        ArrayList<BleInformation> list = new ArrayList<>();
        for (int k = 0; k < 10; k++) {
            BleInformation bleInformation = new BleInformation();
            bleInformation.setMacAddress("我是 " + k + "个地址");
            bleInformation.setRSSI("我是 " + k + "个RSSI");
            bleInformation.setUUID("我是 " + k + "个UUID");
            list.add(bleInformation);
        }
        return list;
    }
}
