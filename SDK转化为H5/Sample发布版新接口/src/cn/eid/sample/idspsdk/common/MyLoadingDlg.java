package cn.eid.sample.idspsdk.common;


import cn.eid.sample.idspsdk.newapi.R;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;



public class MyLoadingDlg extends Dialog {

	private static AnimationDrawable animDrawable = null;
	private static ImageView animV = null;
	private Runnable executingRunable = null;
	private static TextView loadingTV = null;

	public MyLoadingDlg(Context context) {
		super(context);

	}

	public MyLoadingDlg(Context context, int theme) {
		super(context, theme);

	}
	
	public void setText(String str) {
		
		if(null != str && str.length() != 0 && null != loadingTV) {
			
			loadingTV.setText(str);
			
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

				animV.post(executingRunable);

			}

		}

	}

	public void stopAnim() {

		if (null != animDrawable) {

			if (animDrawable.isRunning()) {

				animDrawable.stop();

				animV.removeCallbacks(executingRunable);

			}

		}

	}

	public static class Builder {

		private Context context;

		public Builder(Context context) {

			this.context = context;

		}

		public MyLoadingDlg create() {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			final MyLoadingDlg dlg = new MyLoadingDlg(context,
					R.style.LoadingDialog);
			View layout = inflater.inflate(R.layout.my_loading_dialog, null);

			animV = (ImageView) layout.findViewById(R.id.my_loading_dlg_anim);
			animV.setBackgroundResource(R.anim.logo_run);
			animDrawable = (AnimationDrawable) animV.getBackground();

			TextView textTV = (TextView) layout
					.findViewById(R.id.my_loading_dlg_text);
			loadingTV = textTV;

			dlg.setContentView(layout);

			return dlg;

		}

	}

}
