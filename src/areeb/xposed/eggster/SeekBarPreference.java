/* The following code was written by Matthew Wiggins and iamareebjamal
 * and is released under the APACHE 2.0 license 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package areeb.xposed.eggster;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.os.Build;
import android.preference.DialogPreference;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.LinearLayout;


@SuppressLint("UseValueOf")
public class SeekBarPreference extends DialogPreference implements SeekBar.OnSeekBarChangeListener
{
  private static final String androidns="http://schemas.android.com/apk/res/android";

  private SeekBar mSeekBar;
  private TextView mSplashText,mValueText;
  private Context mContext;

  private String mDialogMessage, mSuffix;
  private int mDefault, mMax, mValue = 0;

  public SeekBarPreference(Context context, AttributeSet attrs) { 
    super(context,attrs); 
    mContext = context;

    mDialogMessage = attrs.getAttributeValue(androidns,"dialogMessage");
    mSuffix = attrs.getAttributeValue(androidns,"text");
    mDefault = attrs.getAttributeIntValue(androidns,"defaultValue", 0);
    mMax = attrs.getAttributeIntValue(androidns,"max", 100);
    
    

  }
  
 
  
  
  @SuppressWarnings("deprecation")
  @Override 
  protected View onCreateDialogView() {
    LinearLayout.LayoutParams params;
    LinearLayout layout = new LinearLayout(mContext);
    layout.setOrientation(LinearLayout.VERTICAL);
    layout.setPadding(6,6,6,6);
    

    mSplashText = new TextView(mContext);
    if (mDialogMessage != null)
      mSplashText.setText(mDialogMessage);
    mSplashText.setTextColor(Color.parseColor("#333333"));
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
    mSplashText.setTextColor(Color.parseColor("#ffffff"));
    mSplashText.setTextSize(17);
    mSplashText.setGravity(Gravity.CENTER_HORIZONTAL);
    mSplashText.setPadding(6, 6, 6, 16);
    layout.addView(mSplashText);
    
    Typeface bold = Typeface
			.createFromAsset(mContext.getAssets(), "Roboto-Bold.ttf");

    mValueText = new TextView(mContext);
    mValueText.setGravity(Gravity.CENTER_HORIZONTAL);
    mValueText.setTextSize(18);
    mValueText.setTypeface(bold);
    mValueText.setTextColor(Color.parseColor("#333333"));
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
    mValueText.setTextColor(Color.parseColor("#ffffff"));
    
    params = new LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.FILL_PARENT, 
        LinearLayout.LayoutParams.WRAP_CONTENT);
    layout.addView(mValueText, params);

    mSeekBar = new SeekBar(mContext);
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
    mSeekBar.setPadding(20, 0, 20, 0);
    mSeekBar.setOnSeekBarChangeListener(this);
    layout.addView(mSeekBar, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

    if (shouldPersist())
      mValue = getPersistedInt(mDefault);

    mSeekBar.setMax(mMax);
    mSeekBar.setProgress(mValue);
    return layout;
  }
  
  @Override
  protected void onPrepareDialogBuilder(Builder builder) {
      super.onPrepareDialogBuilder(builder);
      builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		
    	//Fix saving preferences in both "OK" and "Cancel" cases  
    	  
		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			// TODO Auto-generated method stub
			arg0.dismiss();
		}
	});
      builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			// TODO Auto-generated method stub
			callChangeListener(new Integer(mSeekBar.getProgress()));
			
			if (shouldPersist())
			      persistInt(mSeekBar.getProgress());
		}
	});
  }
  
  
  @Override 
  protected void onBindDialogView(View v) {
    super.onBindDialogView(v);
    mSeekBar.setMax(mMax);
    mSeekBar.setProgress(mValue);
  }
  @SuppressLint("UseValueOf")
@Override
  protected void onSetInitialValue(boolean restore, Object defaultValue)  
  {
    super.onSetInitialValue(restore, defaultValue);
    if (restore) 
      mValue = shouldPersist() ? getPersistedInt(mDefault) : 0;
    else {
      mValue = (Integer)defaultValue;
      callChangeListener(mDefault);
	
      if (shouldPersist())
	      persistInt(mDefault);
    }
  }

  public void onProgressChanged(SeekBar seek, int value, boolean fromTouch)
  {
    String t = String.valueOf(value);
    mValueText.setText(mSuffix == null ? t : t.concat(mSuffix));
  }
  public void onStartTrackingTouch(SeekBar seek) {}
  public void onStopTrackingTouch(SeekBar seek) {}

  public void setMax(int max) { mMax = max; }
  public int getMax() { return mMax; }

  public void setProgress(int progress) { 
    mValue = progress;
    if (mSeekBar != null)
      mSeekBar.setProgress(progress); 
  }
  public int getProgress() { return mValue; }
}