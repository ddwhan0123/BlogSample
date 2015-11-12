package cn.eid.sample.idspsdk.common;

import java.util.List;
import java.util.Locale;

import cn.eid.sample.idspsdk.newapi.R;
import cn.eid.tools.bluetooth.ble.BleResult;
import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;




public final class MyListDlg extends Dialog {

	private static final String TAG = MyListDlg.class.getName();

	private Context context;
	private String myTitle;
	private static MyListAdapter myAdapter;
	private static TextView tvTitle;
	
	private static AnimationDrawable animDrawable = null;
	private Runnable executingRunable = null;
	private static ImageView progressAnim;
	
	
	private static TextView tvTip;
	private static LinearLayout llseperator;
	private static ListView myListView;

	public MyListDlg(Context context) {
		super(context);
		this.context = context;
	}

	public MyListDlg(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	public void setMyTitle(String myTitle) {
		this.myTitle = myTitle;

		if (null == tvTitle) {

			return;

		}

		tvTitle.setText(this.myTitle);
	}
	
	
	public void showTip(boolean show) {
		
		showTip(show, R.string.bluetooth_tip_discover_finished);
		
	}
	
	
	public void showProgress(boolean show) {
		
		if (show) {
			
			progressAnim.setVisibility(View.VISIBLE);
			startAnim();
			
		} else {
			
			stopAnim();
			progressAnim.setVisibility(View.INVISIBLE);
			
		}
		
	}
	
	
	public void startAnim() {

		if (null != animDrawable) {

			if (!animDrawable.isRunning()) {

				executingRunable = new Runnable() {

					@Override
					public void run() {

						animDrawable.start();

					}
				};

				progressAnim.post(executingRunable);

			}

		}

	}

	public void stopAnim() {

		if (null != animDrawable) {

			if (animDrawable.isRunning()) {

				animDrawable.stop();
				progressAnim.removeCallbacks(executingRunable);

			}

		}

	}
	
	
	public void showTip(boolean show, int tipResId) {
		
		if (show) {
			
			tvTip.setVisibility(View.VISIBLE);
			llseperator.setVisibility(View.VISIBLE);
			tvTip.setText(tipResId);
			
		} else {
			
			tvTip.setVisibility(View.GONE);
			llseperator.setVisibility(View.GONE);
			
		}
		
	}

	
	public void updateListItems(List<BleResult> items) {

		if (null == items) {

			return;

		}

		if (null == myListView) {

			return;

		}

		if (myAdapter != null)
			myAdapter = null;

		myAdapter = new MyListAdapter(context, items);
		myListView.setAdapter(myAdapter);

		myAdapter.notifyDataSetChanged();

	}
	
	
	public interface DiscoverFinishedTipClickListener {

		public void onClick(View view);

	}

	public interface MyListItemClickListener {

		public void itemClick(int pos);

	}

	public static class Builder {

		private Context context;
		private String title;
		private MyListItemClickListener itemClickLis = null;
		private DiscoverFinishedTipClickListener tipClickListener = null;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setTitle(int titleResId) {
			this.title = context.getText(titleResId).toString();
			return this;
		}

		public Builder setMyListItemClickListener(MyListItemClickListener lis) {

			this.itemClickLis = lis;
			return this;

		}
		
		public Builder setDiscoverFinishedTipClickListener(DiscoverFinishedTipClickListener tipClickListener) {
			
			this.tipClickListener = tipClickListener;
			return this;
			
		}

		public MyListDlg create() {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final MyListDlg dlg = new MyListDlg(context, R.style.PromptDialog);
			View layout = inflater.inflate(R.layout.my_list_dialog, null);
			
			progressAnim = (ImageView) layout.findViewById(R.id.progress_anim);
			progressAnim.setBackgroundResource(R.anim.logo_run);
			animDrawable = (AnimationDrawable) progressAnim.getBackground();
			
			tvTitle = (TextView) layout .findViewById(R.id.tv_title);
			tvTitle.setText(title);
			
			tvTip = (TextView) layout .findViewById(R.id.notify_discover_finished);
			llseperator = (LinearLayout) layout .findViewById(R.id.seperator);
			tvTip.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View view) {

					if (null != tipClickListener) {
						
						tipClickListener.onClick(view);
						
					}
					
				}
				
			});
			
			
			final ListView list_container = (ListView) layout
					.findViewById(R.id.list_container);
			list_container
					.setOnItemClickListener(new AdapterView.OnItemClickListener() {

						@Override
						public void onItemClick(
								AdapterView<?> arg0, 
								View arg1,
								int pos, 
								long arg3) {
							
							if (null != itemClickLis) {
								
								itemClickLis.itemClick(pos);
								
							}
							
						}
					});
			myListView = list_container;
			dlg.setContentView(layout);
			dlg.setCanceledOnTouchOutside(false);

			return dlg;

		}

	}

	private class MyListAdapter extends BaseAdapter {

		private List<BleResult> items;
		private Context context;

		public MyListAdapter(Context context, List<BleResult> items) {
			super();

			this.context = context;
			this.items = items;

		}
		

		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;

			if (null == convertView) {

				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.list_item, null);
				
				holder = new ViewHolder();
				
				holder.title = (TextView) convertView.findViewById(R.id.dev_name);
				holder.state = (TextView) convertView.findViewById(R.id.dev_mac);
				holder.rssi = (TextView) convertView.findViewById(R.id.dev_rssi);
				
				convertView.setTag(holder);

			} else {

				holder = (ViewHolder) convertView.getTag();

			}
			
			final BleResult info = items.get(position);
			final Resources res = context.getResources();
			
			holder.title.setText(info.device.getName());
			
			holder.state.setText(
					String.format(
							res.getString(R.string.bt_mac_format), 
							info.device.getAddress().toUpperCase(Locale.US)));
			
			String rssiFormat = String.format(res.getString(R.string.bt_rssi_format), info.rssi);
			holder.rssi.setText(rssiFormat);
			
			return convertView;
			
		}

	}

	static class ViewHolder {

		TextView title = null;
		TextView state = null;
		TextView rssi = null;

	}

}
