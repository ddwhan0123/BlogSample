package com.wjj.demo.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wjj.demo.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jiajiewang on 16/6/30.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {
    private LayoutInflater inflater;
    private List<String> dataList;
    private Context context;

    public MainAdapter(Context context, List<String> dataList) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_item, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        holder.mainText.setText(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class MainViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.mainText)
        TextView mainText;

        public MainViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
