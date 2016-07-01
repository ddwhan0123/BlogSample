package com.wjj.demo;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;
import com.wjj.demo.Adapter.MainAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jiajiewang on 16/6/30.
 */
public class SettingActivity extends BaseActivity {
    @BindView(R.id.input)
    EditText input;
    @BindView(R.id.add)
    ButtonRectangle add;
    @BindView(R.id.jump)
    ButtonRectangle jump;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private MainAdapter mAdapter;

    private RecyclerTouchListener onTouchListener;
    private List<String> list;

    @Override
    int getLayout() {
        return R.layout.activity_setting;
    }

    @Override
    void init() {
        ButterKnife.bind(this);
        list = new ArrayList<>();
        mAdapter = new MainAdapter(this, list);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        onTouchListener = new RecyclerTouchListener(this, recyclerView);
    }

    @Override
    void logic() {
        onTouchListener.setSwipeOptionViews(R.id.edit)
                .setSwipeable(R.id.rowFG, R.id.rowBG, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        if (viewID == R.id.edit) {
                            list.remove(position);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
        recyclerView.addOnItemTouchListener(onTouchListener);
    }

    @Override
    void onResumeInit() {
        if (list.size() > 0) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    void onResumeLogic() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        recyclerView.removeOnItemTouchListener(onTouchListener);
    }

    @OnClick({R.id.add, R.id.jump})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add:
                String value = input.getText().toString().trim();
                if (!value.isEmpty()) {
                    list.add(value);
                    mAdapter.notifyDataSetChanged();
                    input.setText("");
                }
                break;
            case R.id.jump:

                if (list.size() > 0) {
                    Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                    intent.putStringArrayListExtra("dataList", (ArrayList<String>) list);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "请添加关注内容", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
