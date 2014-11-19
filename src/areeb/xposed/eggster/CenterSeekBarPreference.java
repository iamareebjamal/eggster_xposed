package areeb.xposed.eggster;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.preference.DialogPreference;
import android.widget.*;
import areeb.xposed.eggster.CenterBar;


@SuppressLint("UseValueOf")
public class CenterSeekBarPreference extends DialogPreference implements SeekBar.OnSeekBarChangeListener
{

  private static final String androidns="http://schemas.android.com/apk/res/android";

  private CenterBar mSeekBar;
  private TextView mSplashText,mValueText;
  private Context mContext;

  private String mDialogMessage, mSuffix;
  private int mDefault, mMax, mValue = 0;

  public CenterSeekBarPreference(Context context, AttributeSet attrs) { 
    super(context,attrs); 
    mContext = context;

    mDialogMessage = attrs.getAttributeValue(androidns,"dialogMessage");
    mSuffix = attrs.getAttributeValue(androidns,"text");
    mDefault = attrs.getAttributeIntValue(androidns,"defaultValue", 0);
    mMax = attrs.getAttributeIntValue(androidns,"max", 100);
  } 
      
  public int getDefaultValue(){
	  return mDefault;
}
    
  @SuppressWarnings("deprecation")
  @Override 
  protected View onCreateDialogView() {
    LinearLayout.LayoutParams params;
    View layout = View.inflate(mContext, R.layout.centerbar_dialog, null);
    layout.setPadding(6,6,6,6);
    
    if (mDialogMessage != null) {
    mSplashText = new TextView(mContext);
      mSplashText.setText(mDialogMessage);
    mSplashText.setTextColor(Color.parseColor("#333333"));
    if (Build.VERSION.SDK_INT < 11)
    mSplashText.setTextColor(Color.parseColor("#ffffff"));
    mSplashText.setTextSize(17);
    mSplashText.setGravity(Gravity.CENTER_HORIZONTAL);
    mSplashText.setPadding(6, 6, 6, 16); 
    ((ViewGroup) layout).addView(mSplashText);
    }
    
    Typeface bold = Typeface.createFromAsset(mContext.getAssets(), "Roboto-Bold.ttf");

    mValueText = new TextView(mContext);
    mValueText.setGravity(Gravity.CENTER_HORIZONTAL);
    mValueText.setTextSize(18);
    mValueText.setTypeface(bold);
    mValueText.setTextColor(Color.parseColor("#333333"));
    if (Build.VERSION.SDK_INT < 11)
    mValueText.setTextColor(Color.parseColor("#ffffff"));
    mValueText.setPadding(6, 6, 6, 16); 
    mValueText.setText("0");
    params = new LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.FILL_PARENT, 
        LinearLayout.LayoutParams.WRAP_CONTENT);
    ((ViewGroup) layout).addView(mValueText, params);

    mSeekBar = (CenterBar) layout.findViewById(R.id.CenterBarID);
    mSeekBar.setOnSeekBarChangeListener(this);
    
    if (shouldPersist())
      mValue = getPersistedInt(mDefault);

    mSeekBar.setMax(mMax);
    mSeekBar.setProgress(mValue);
    return layout;
  }
  
  @Override
  protected void onPrepareDialogBuilder(Builder builder) {
      super.onPrepareDialogBuilder(builder);
      builder.setNegativeButton("Cancel", new OnClickListener() {
		
    	//Fix saving preferences in both "OK" and "Cancel" cases  
    	  
		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			// TODO Auto-generated method stub
			arg0.dismiss();
		}
	});
      builder.setNeutralButton("Reset", new OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			callChangeListener(mDefault);
			if (shouldPersist())
			      persistInt(mDefault);
		}
	});
      builder.setPositiveButton("OK", new OnClickListener() {
		
		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			Integer i = mSeekBar.getProgress();
			callChangeListener(i);
			if (shouldPersist())
			persistInt(i);
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