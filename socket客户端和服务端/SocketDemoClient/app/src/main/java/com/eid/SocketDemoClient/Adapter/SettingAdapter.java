package com.eid.SocketDemoClient.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eid.SocketDemoClient.R;

import java.util.Collections;
import java.util.List;

import github.nisrulz.recyclerviewhelper.RVHAdapter;
import github.nisrulz.recyclerviewhelper.RVHViewHolder;

/**
 * Created by jiajiewang on 16/6/28.
 */
public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.ItemViewHolder> implements RVHAdapter {

    List<String> list;
    Context context;

    public SettingAdapter(Context context, List<String> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.setting_item, parent,
                false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.itemTv.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        swap(fromPosition, toPosition);
        return false;
    }

    @Override
    public void onItemDismiss(int position, int direction) {
        remove(position);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements RVHViewHolder {
        TextView itemTv;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemTv = (TextView) itemView.findViewById(R.id.itemTv);
        }

        @Override
        public void onItemSelected(int actionstate) {
            System.out.println("Item is selected actionstate " + actionstate);
        }

        @Override
        public void onItemClear() {
            System.out.println("Item is selected");
        }
    }

    private void remove(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    private void swap(int firstPosition, int secondPosition) {
        Collections.swap(list, firstPosition, secondPosition);
        notifyItemMoved(firstPosition, secondPosition);
    }
}
