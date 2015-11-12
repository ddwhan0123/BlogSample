package cn.eid.sample.idspsdk.common;

import cn.eid.sample.idspsdk.newapi.R;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public final class MyPINDlg extends Dialog {

	private static final String TAG = MyPINDlg.class.getName();
		
	private static TextView tvTitle;
	private static EditText etPIN;
	private static Button etPINClear;
	private static TextView pinCount;
	private static TextView tvPINErrorTip;
	private static CharSequence curInput = "";
	

	public MyPINDlg(Context context) {
		super(context);
		
	}

	public MyPINDlg(Context context, int theme) {
		super(context, theme);
		
	}
    
    
    public void setTitle(String title) {
    	
		tvTitle.setText(title);
		
	}
    
    public void setPINErrorTip(String tip) {
    	
    	tvPINErrorTip.setVisibility(View.VISIBLE);
    	tvPINErrorTip.setText(tip);
    	
    }
    
    public String getPIN() {
    	
    	return etPIN.getEditableText().toString();
    	
    }
    
    public void clearPIN() {
    	
    	etPIN.setText("");
    	
    }
    
    
    public static class Builder {

        Context context;
        Resources res;
        private String positiveBtnText;
		private String negativeBtnText;

		private DialogInterface.OnClickListener positiveBtnOnClickListener;
		private DialogInterface.OnClickListener negativeBtnOnClickListener;
        
        public Builder(Context context) {
        	
            this.context = context;
            res = context.getResources();
            curInput = "";
            
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
		
		void updatePinCount(int count) {
		
			String formatText = String
					.format(res.getString(R.string.pin_count_format),
							count);
			pinCount.setText(formatText);
			
		}


        @SuppressLint("InflateParams")
        public MyPINDlg create() {

        	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final MyPINDlg dlg = new MyPINDlg(context, R.style.PromptDialog);
            
            View layout = inflater.inflate(R.layout.my_pin_dlg, null);
            
            tvTitle = (TextView) layout.findViewById(R.id.title);
            etPIN = (EditText) layout.findViewById(R.id.pin);
            etPIN.setText("");
            etPIN.requestFocus();
            etPINClear = (Button) layout.findViewById(R.id.clear_in_pin);
            etPINClear.setVisibility(View.GONE);
            etPINClear.setOnClickListener(new View.OnClickListener() {

    			@Override
    			public void onClick(View v) {

    				etPIN.setText("");
    				updatePinCount(0);
    				etPIN.requestFocus();

    			}

    		});
            etPIN.addTextChangedListener(new TextWatcher() {

    			@Override
    			public void afterTextChanged(Editable s) {

    				String remainString = etPIN.getEditableText().toString();
    				if (remainString.equals("")) {

    					etPINClear.setVisibility(View.GONE);

    				} else {

    					etPINClear.setVisibility(View.VISIBLE);

    				}
    				
    				updatePinCount(curInput.length());

    			}

    			@Override
    			public void beforeTextChanged(CharSequence s, int start, int count,
    					int after) {

    				curInput = s;

    			}

    			@Override
    			public void onTextChanged(CharSequence s, int start, int before,
    					int count) {
    				// TODO Auto-generated method stub

    			}

    		});
            
            
            pinCount = (TextView) layout.findViewById(R.id.text_count);
            updatePinCount(curInput.length());
            
            
            tvPINErrorTip = (TextView) layout.findViewById(R.id.tip);
            tvPINErrorTip.setVisibility(View.INVISIBLE);
            
            Button positiveBtn = (Button) layout.findViewById(R.id.positiveBtn);
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

			Button negativeBtn = (Button) layout.findViewById(R.id.negativeBtn);
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
