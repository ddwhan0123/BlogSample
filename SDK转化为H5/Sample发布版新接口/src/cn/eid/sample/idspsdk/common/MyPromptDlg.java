package cn.eid.sample.idspsdk.common;



import cn.eid.sample.idspsdk.newapi.R;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class MyPromptDlg extends Dialog {

	private static final String TAG = MyPromptDlg.class.getName();
	
	private static TextView myTextView;

	public MyPromptDlg(Context context) {
		super(context);

	}

	public MyPromptDlg(Context context, int theme) {
		super(context, theme);

	}
    public void setMyText(String str){
    	
    	if(null == myTextView || null == str)
    		return;
    	
    	myTextView.setText(str);
    	
    }


	public static class Builder {

		private Context context;
		private String title;
		private String text;
		private String positiveBtnText;
		private String negativeBtnText;

		private DialogInterface.OnClickListener positiveBtnOnClickListener;
		private DialogInterface.OnClickListener negativeBtnOnClickListener;

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

		public Builder setText(String text) {

			this.text = text;
			return this;

		}

		public Builder setText(int textResId) {

			this.text = context.getText(textResId).toString();
			return this;

		}

		public Builder setPositiveButton(String positiveButtonText,
				DialogInterface.OnClickListener listener) {

			this.positiveBtnText = positiveButtonText;
			this.positiveBtnOnClickListener = listener;

			return this;

		}

		public Builder setPositiveButton(int positiveButtonTextResId,
				DialogInterface.OnClickListener listener) {

			this.positiveBtnText = context.getText(positiveButtonTextResId)
					.toString();
			this.positiveBtnOnClickListener = listener;

			return this;

		}

		public Builder setNegativeButton(String negativeButtonText,
				DialogInterface.OnClickListener listener) {

			this.negativeBtnText = negativeButtonText;
			this.negativeBtnOnClickListener = listener;

			return this;

		}

		public Builder setNegativeButton(int negativeButtonTextResId,
				DialogInterface.OnClickListener listener) {

			this.negativeBtnText = context.getText(negativeButtonTextResId)
					.toString();
			this.negativeBtnOnClickListener = listener;

			return this;

		}

		@SuppressLint("InflateParams")
		public MyPromptDlg create() {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			final MyPromptDlg dlg = new MyPromptDlg(context,
					R.style.PromptDialog);

			Window win = dlg.getWindow();
			win.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

			View layout = inflater.inflate(R.layout.my_prompt_dialog, null);

			final TextView titleTV = (TextView) layout.findViewById(R.id.my_prompt_dlg_title);
			titleTV.setText(title);
			
			myTextView = (TextView) layout.findViewById(R.id.my_prompt_dlg_text);
			if (null != text) {

				myTextView.setText(text);

			}

			Button positiveBtn = (Button) layout
					.findViewById(R.id.my_prompt_dlg_positiveButton);
			if (null != positiveBtnText) {

				positiveBtn.setText(positiveBtnText);

				if (null != positiveBtnOnClickListener) {

					positiveBtn.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {

							positiveBtnOnClickListener.onClick(dlg,
									DialogInterface.BUTTON_POSITIVE);

						}

					});

				}

			} else {

				positiveBtn.setVisibility(View.GONE);

			}

			Button negativeBtn = (Button) layout
					.findViewById(R.id.my_prompt_dlg_negativeButton);
			if (null != negativeBtnText) {

				negativeBtn.setText(negativeBtnText);

				if (null != negativeBtnOnClickListener) {

					negativeBtn.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {

							negativeBtnOnClickListener.onClick(dlg,
									DialogInterface.BUTTON_NEGATIVE);

						}

					});

				}

			} else {

				negativeBtn.setVisibility(View.GONE);

			}

			dlg.setContentView(layout);

			return dlg;

		}

	}

}
