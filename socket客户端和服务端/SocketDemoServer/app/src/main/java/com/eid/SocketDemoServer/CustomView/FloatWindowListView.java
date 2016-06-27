package com.eid.SocketDemoServer.CustomView;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.eid.SocketDemoServer.Moeel.PhoneMessage;
import com.eid.SocketDemoServer.R;

import java.util.List;

/**
 * Created by jiajiewang on 16/6/27.
 */
public class FloatWindowListView extends LinearLayout {
    /**
     * 记录大悬浮窗的宽度
     */
    public static int viewWidth;
    /**
     * 记录大悬浮窗的高度
     */
    public static int viewHeight;

    private Button closetBtn;
    private RecyclerView list;
    private RecyclerView.LayoutManager mLayoutManager;
    private ListAdapter listAdapter;
    private static List<PhoneMessage> phoneMessageArrayList;

    //构造函数
    public FloatWindowListView(final Context context, final List<PhoneMessage> phoneMessageArrayList) {
        super(context);
        this.phoneMessageArrayList = phoneMessageArrayList;
        LayoutInflater.from(context).inflate(R.layout.float_window_list, this);
        //根布局
        View view = findViewById(R.id.rootLayout);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        //初始化
        init();
        closetBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击返回的时候，移除大悬浮窗，创建小悬浮窗
                MyWindowManager.removeListWindow(context);
                MyWindowManager.createSmallWindow(context, 0, phoneMessageArrayList);
            }
        });
    }

    private void init() {
        closetBtn = (Button) findViewById(R.id.closetBtn);
        list = (RecyclerView) findViewById(R.id.float_window_list);
        initRV();
    }

    private void initRV() {
        //创建默认的线性LayoutManager
        if (mLayoutManager == null) {
            mLayoutManager = new LinearLayoutManager(getContext());
        }

        list.setLayoutManager(mLayoutManager);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space);
        list.addItemDecoration(new SpaceItemDecoration(spacingInPixels));

        if (listAdapter == null) {
            listAdapter = new ListAdapter(getContext(), phoneMessageArrayList);
        }
        list.setAdapter(listAdapter);
    }

    //自定义适配器
    public static class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
        private boolean flag = true;
        private List<PhoneMessage> list;
        private Context context;
        private ViewHolder vh;

        public ListAdapter(Context context, List<PhoneMessage> list) {
            this.list = list;
            this.context = context;
            LogUtils.d("--->ListAdapter 里 list.size()" + list.size());
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            vh = new ViewHolder(view, context);
            return vh;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.phoneNumber.setText(list.get(position).getPhoneNumber());
            holder.msgContent.setText(list.get(position).getMsgContent());
            holder.msgTime.setText(list.get(position).getMsgTime());


            if (holder.msgContent.length() > 10) {
                holder.msgContent.setText(list.get(position).getMsgContent().substring(0, 9) + "..... 点击显示全部");
            } else {
                holder.msgContent.setText(list.get(position).getMsgContent());
            }

            holder.msgContent.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flag) {
                        holder.msgContent.setText(list.get(position).getMsgContent());
                        flag = false;
                    } else {
                        holder.msgContent.setText(list.get(position).getMsgContent().substring(0, 9) + "..... 点击显示全部");
                        flag = true;
                    }
                }
            });

            //将数据保存在itemView的Tag中，以便点击时进行获取
            holder.itemView.setTag(position);
        }


        @Override
        public int getItemCount() {
            return list.size();
        }

        //自定义的ViewHolder，持有每个Item的的所有界面元素
        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView msgContent;
            private TextView msgTime;
            private TextView phoneNumber;


            public ViewHolder(View view, Context context) {
                super(view);
                phoneNumber = (TextView) view.findViewById(R.id.phoneNumber);
                msgContent = (TextView) view.findViewById(R.id.msgContent);
                msgTime = (TextView) view.findViewById(R.id.msgTime);
            }


        }
    }

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            if (parent.getChildPosition(view) != 0)
                outRect.top = space;
        }
    }
}
