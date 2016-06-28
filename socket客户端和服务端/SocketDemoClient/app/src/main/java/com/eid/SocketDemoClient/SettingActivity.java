package com.eid.SocketDemoClient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eid.SocketDemoClient.Adapter.SettingAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import github.nisrulz.recyclerviewhelper.RVHItemClickListener;
import github.nisrulz.recyclerviewhelper.RVHItemDividerDecoration;
import github.nisrulz.recyclerviewhelper.RVHItemTouchHelperCallback;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.ipTextView)
    EditText ipTextView;
    @BindView(R.id.okBtn)
    Button okBtn;
    @BindView(R.id.addItem)
    Button addItem;
    @BindView(R.id.setting_list)
    RecyclerView settingList;
    @BindView(R.id.filterET)
    EditText filterET;

    List<String> list;
    SettingAdapter settingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        list = new ArrayList<>();
        settingAdapter = new SettingAdapter(SettingActivity.this, list);
        settingList.hasFixedSize();
        settingList.setLayoutManager(new LinearLayoutManager(this));
        settingList.setAdapter(settingAdapter);

        // Setup onItemTouchHandler
        ItemTouchHelper.Callback callback = new RVHItemTouchHelperCallback(settingAdapter, true, true,
                true);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(settingList);

        // Set the divider
        settingList.addItemDecoration(new RVHItemDividerDecoration(this, LinearLayoutManager.VERTICAL));

        // Set On Click
        settingList.addOnItemTouchListener(new RVHItemClickListener(this, new RVHItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String value = "Clicked Item " + list.get(position) + " at " + position;

                Log.d("TAG", value);
                Toast.makeText(SettingActivity.this, value, Toast.LENGTH_SHORT).show();
            }
        }));
    }


    @OnClick(R.id.okBtn)
    public void onClick() {
        String value = ipTextView.getText().toString().trim();

        if (value.length() > 0 && list.size() > 0) {
            Intent intent = new Intent(SettingActivity.this, MainActivity.class);
            intent.putExtra("ipAddress", value);
            intent.putStringArrayListExtra("filterArray", (ArrayList<String>) list);
            startActivity(intent);
        } else {
            Toast.makeText(SettingActivity.this, "输入ip地址不符合逻辑/过滤字段过少", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.addItem)
    public void addItem() {
        if (filterET.getText().toString().trim().length() > 0) {
            list.add(filterET.getText().toString().trim());
            settingAdapter.notifyDataSetChanged();
            filterET.setText("");
        } else {
            Toast.makeText(SettingActivity.this, "请输入过滤文字", Toast.LENGTH_SHORT).show();
        }
    }


}
