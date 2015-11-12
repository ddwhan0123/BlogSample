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


public final class MyChangePINDlg extends Dialog {

	private static final String TAG = MyChangePINDlg.class.getName();
		
	private static TextView tvTitle;
	
	private static EditText etOldPIN;
	private static Button etOldPINClear;
	private static TextView pinCountOldPIN;
	
	private static EditText etNewPIN;
	private static Button etNewPINClear;
	private static TextView pinCountNewPIN;
	
	private static TextView tvPINErrorTip;
	
	private static CharSequence curInputOldPIN = "";
	private static CharSequence curInputNewPIN = "";
	

	public MyChangePINDlg(Context context) {
		super(context);
		
	}

	public MyChangePINDlg(Context context, int theme) {
		super(context, theme);
		
	}
    
    
    
    public void setTitle(String title) {
    	
		tvTitle.setText(title);
		
	}
    
    public void setPINErrorTip(String tip) {
    	
    	tvPINErrorTip.setVisibility(View.VISIBLE);
    	tvPINErrorTip.setText(tip);
    	
    }
    
    public String getOldPIN() {
    	
    	return etOldPIN.getEditableText().toString();
    	
    }
    
    public void clearOldPIN() {
    	
    	etOldPIN.setText("");
    	
    }
    
    public String getNewPIN() {
    	
    	return etNewPIN.getEditableText().toString();
    	
    }
    
    public void clearNewPIN() {
    	
    	etNewPIN.setText("");
    	
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
            curInputOldPIN = "";
            curInputNewPIN = "";
            
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
		
		void updateOldPinCount(int count) {
			
			String formatText = String
					.format(res.getString(R.string.pin_count_format),
							count);
			pinCountOldPIN.setText(formatText);
			
		}
		
		
		void updateNewPinCount(int count) {
			
			String formatText = String
					.format(res.getString(R.string.pin_count_format),
							count);
			pinCountNewPIN.setText(formatText);
			
		}


        @SuppressLint("InflateParams")
        public MyChangePINDlg create() {

        	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final MyChangePINDlg dlg = new MyChangePINDlg(context, R.style.PromptDialog);
            
            View layout = inflater.inflate(R.layout.my_change_pin_dlg, null);
            
            tvTitle = (TextView) layout.findViewById(R.id.title);
            etOldPIN = (EditText) layout.findViewById(R.id.oldPin);
            etOldPIN.setText("");
            etOldPIN.requestFocus();
            etOldPINClear = (Button) layout.findViewById(R.id.clear_in_old_pin);
            etOldPINClear.setVisibility(View.GONE);
            etOldPINClear.setOnClickListener(new View.OnClickListener() {

    			@Override
    			public void onClick(View v) {

    				etOldPIN.setText("");
    				updateOldPinCount(0);
    				etOldPIN.requestFocus();

    			}

    		});
            etOldPIN.addTextChangedListener(new TextWatcher() {

    			@Override
    			public void afterTextChanged(Editable s) {

    				String remainString = etOldPIN.getEditableText().toString();
    				if (remainString.equals("")) {

    					etOldPINClear.setVisibility(View.GONE);

    				} else {

    					etOldPINClear.setVisibility(View.VISIBLE);

    				}
    				
    				updateOldPinCount(curInputOldPIN.length());

    			}

    			@Override
    			public void beforeTextChanged(CharSequence s, int start, int count,
    					int after) {
    				// TODO Auto-generated method stub
    				
    				curInputOldPIN = s;

    			}

    			@Override
    			public void onTextChanged(CharSequence s, int start, int before,
    					int count) {
    				// TODO Auto-generated method stub

    			}

    		});
            pinCountOldPIN = (TextView) layout.findViewById(R.id.text_count_old_pin);
            
            
            etNewPIN = (EditText) layout.findViewById(R.id.newPin);
            etNewPIN.setText("");
            etNewPINClear = (Button) layout.findViewById(R.id.clear_in_new_pin);
            etNewPINClear.setVisibility(View.GONE);
            etNewPINClear.setOnClickListener(new View.OnClickListener() {

    			@Override
    			public void onClick(View v) {

    				etNewPIN.setText("");
    				updateNewPinCount(0);
    				etNewPIN.requestFocus();

    			}

    		});
            etNewPIN.addTextChangedListener(new TextWatcher() {

    			@Override
    			public void afterTextChanged(Editable s) {

    				String remainString = etNewPIN.getEditableText().toString();
    				if (remainString.equals("")) {

    					etNewPINClear.setVisibility(View.GONE);

    				} else {

    					etNewPINClear.setVisibility(View.VISIBLE);

    				}
    				
    				updateNewPinCount(curInputNewPIN.length());

    			}

    			@Override
    			public void beforeTextChanged(CharSequence s, int start, int count,
    					int after) {
    				// TODO Auto-generated method stub
    				
    				curInputNewPIN = s;

    			}

    			@Override
    			public void onTextChanged(CharSequence s, int start, int before,
    					int count) {
    				// TODO Auto-generated method stub

    			}

    		});
            pinCountNewPIN = (TextView) layout.findViewById(R.id.text_count_new_pin);
            
            updateOldPinCount(curInputOldPIN.length());
            updateNewPinCount(curInputNewPIN.length());
            
            
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
