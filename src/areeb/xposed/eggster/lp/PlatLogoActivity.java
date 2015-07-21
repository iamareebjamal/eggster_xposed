package areeb.xposed.eggster.lp;

import java.util.Random;

import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.*;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import areeb.xposed.eggster.Eggs;
import areeb.xposed.eggster.R;
import areeb.xposed.eggster.R.style;


@SuppressLint("ClickableViewAccessibility") 
public class PlatLogoActivity extends Activity {
	int tapatap;
	SharedPreferences.Editor edit;
    ImageView lightClickEffect;

    @SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		final ImageView lollipop = new ImageView(this);
		final ImageView logo 	 = new ImageView(this);
		final RelativeLayout pop = new RelativeLayout(this);
		final ImageView stick    = new ImageView(this);
		final ImageView lollipopLight 	 = new ImageView(this);
		final String[] materialPalette 	 = {"9C27B0", "BA68C8", "FF9800", "FFB74D", "F06292", "F8BBD0", "AFB42B", "CDDC39", "FFEB3B", "FFF176", "795548", "A1887F"};	//hex codes of the Material Design colors. You can find the palette at google.com/design
		lightClickEffect = new ImageView(this);

		edit = getSharedPreferences("preferenceggs", Context.MODE_PRIVATE).edit();
		
		RelativeLayout container = new RelativeLayout(this);

		lollipop.setImageResource(R.drawable.lollipop_circle);
		logo.setImageResource(R.drawable.lollipop_text);
		stick.setBackgroundResource(R.drawable.lollipop_stick);
		lollipopLight.setImageResource(R.drawable.lollipop_light);




		LayoutParams LollipopLP = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		LollipopLP.addRule(RelativeLayout.CENTER_IN_PARENT);
		lollipop.setLayoutParams(LollipopLP);

		LayoutParams LollipopLightLP = new LayoutParams(60,60);
		LollipopLightLP.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		LollipopLightLP.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		LollipopLightLP.setMargins(19, 17, 0, 0);
		lollipopLight.setLayoutParams(LollipopLightLP);

		LayoutParams LogoLP = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		LogoLP.addRule(RelativeLayout.CENTER_IN_PARENT);
		logo.setLayoutParams(LogoLP);

		int length = 5;
		if(getSharedPreferences("preferenceggs", Context.MODE_PRIVATE).getString("lp_sysui", getString(R.string.pref_none)).equals("Immersive Mode"))
			length = 50;
		@SuppressWarnings("deprecation")
        float scale = getResources().getDisplayMetrics().density;
        int width = (int) (35*scale + 0.5f);
		LayoutParams StickLP = new LayoutParams(width, (getWindowManager().getDefaultDisplay().getHeight() / 2) - lollipop.getBottom() + length);
		StickLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		StickLP.addRule(RelativeLayout.CENTER_HORIZONTAL);
		stick.setLayoutParams(StickLP);

		ViewHelper.setAlpha(stick, 0);
		ViewHelper.setAlpha(logo, 0);
		ViewHelper.setAlpha(lightClickEffect, 0.55F);
		pop.addView(lollipop);
		pop.addView(lollipopLight);
		pop.addView(logo);
		pop.addView(lightClickEffect, LollipopLP);
		LayoutParams popLP = new LayoutParams(140,140);
		popLP.addRule(RelativeLayout.CENTER_IN_PARENT);
		pop.setLayoutParams(popLP);

		container.addView(stick);
		container.addView(pop);

		ViewHelper.setScaleX(pop, 0);
		ViewHelper.setScaleY(pop, 0);

		setContentView(container);

		final Context ctx = this;
		//Decide if the color should be purely random or between the palette
		if (getSharedPreferences("preferenceggs", Context.MODE_PRIVATE).getBoolean("lp_purely_random", false))
			Eggs.setColorFilter(lollipop, new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));	//lollipop color is purely random
		else			
			colorizeHex(lollipop, materialPalette[new Random().nextInt(materialPalette.length)]);	//lollipop color is randomly picked from the material palette

		ViewPropertyAnimator.animate(pop).scaleX(1).scaleY(1).setDuration(500).setInterpolator(new DecelerateInterpolator()).setStartDelay(800).start();
		pop.setClickable(true);
		pop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub	
				if (tapatap==0) {
					ViewPropertyAnimator.animate(pop).scaleX(3.7F).scaleY(3.7F).setDuration(700).setInterpolator(new DecelerateInterpolator()).start();
					ViewPropertyAnimator.animate(logo).alpha(1).setDuration(700).setStartDelay(700).start();
					ViewPropertyAnimator.animate(stick).translationYBy(pop.getMeasuredHeight()*1.7F).alpha(1).setDuration(1000).setStartDelay(1000).start();
				} else {
                    int index = 2*(new Random().nextInt(materialPalette.length/2));

					lightning(materialPalette[index+1]).startTransition(500);				//illuminati weren't here
					if (getSharedPreferences("preferenceggs", Context.MODE_PRIVATE).getBoolean("lp_purely_random", false))
						Eggs.setColorFilter(lollipop, new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));	//lollipop color is purely random
					else {

                        colorizeHex(lollipop, materialPalette[index]);
                    }
				}
				tapatap++;
			}
		});
		pop.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				if(tapatap<5||Build.VERSION.SDK_INT<14){
					pop.callOnClick();
					return false;
				}
				try{
					Intent i = new Intent(Intent.ACTION_MAIN);
					i.setComponent(new ComponentName("com.jrummyapps.lollipopland", "com.jrummyapps.lollipopland.GameActivity"));
					startActivity(i);
					finish();
				} catch(ActivityNotFoundException e){
                    try{
                        Intent i = new Intent(Intent.ACTION_MAIN);
                        i.setComponent(new ComponentName("com.jrummyapps.lollipopland", "com.jrummyapps.lollipopland.activities.GameActivity"));
                        startActivity(i);
                        finish();
                    } catch (ActivityNotFoundException er) {
                        if (getApplicationContext().getSharedPreferences("preferenceggs", Context.MODE_PRIVATE).getBoolean("lp_game_install", true) == true) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
                            alert.setTitle("Pop missing");
                            alert.setMessage("To add the Lollipop Land game to the Easter Egg, you'll have to download a small app from play store. Would you like to do that?");
                            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    // TODO Auto-generated method stub
                                    Uri uri = Uri.parse("market://details?id=com.jrummyapps.lollipopland");
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);
                                }
                            });
                            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    // TODO Auto-generated method stub
                                    arg0.dismiss();
                                }
                            });
                            alert.setNeutralButton("Never", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    // TODO Auto-generated method stub
                                    edit.putBoolean("lp_game_install", false);
                                    edit.apply();
                                }
                            });
                            alert.show();

                        } else {
                            pop.callOnClick();
                        }
                    }
				}
				return true;
			}
		});
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
			String sysUIMode = (getSharedPreferences("preferenceggs", Context.MODE_PRIVATE).getString("lp_sysui", getString(R.string.pref_none)));
		
			if (Build.VERSION.SDK_INT >= 19 && sysUIMode.equals("Immersive Mode")) {
		        container.setSystemUiVisibility(
		                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
		                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
		                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
		                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION		// hide nav bar
		                | View.SYSTEM_UI_FLAG_FULLSCREEN			// hide status bar
		                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);	//Immersive
		        } else if (Build.VERSION.SDK_INT >= 19 && sysUIMode.equals("Translucent Mode")) 
		        	{
		                	//super.setTheme(style.Wallpaper_TranslucentDecor);
		        	}
		}
	
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		Boolean forcePort = getSharedPreferences("preferenceggs", Context.MODE_PRIVATE).getBoolean("lp_force_port", false);

		if (forcePort == true) {
				
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			
		}
				
	}

	public static void colorizeHex(ImageView iv, String hex) {
		Eggs.setColorFilter(iv,
				Integer.valueOf(hex.substring(0, 2), 16),
				Integer.valueOf(hex.substring(2, 4), 16),
				Integer.valueOf(hex.substring(4, 6), 16));
	}

    public TransitionDrawable lightning(String color){
        Drawable ripple = getResources().getDrawable(R.drawable.lollipop_white);
        ripple.setColorFilter(Color.parseColor("#"+color), PorterDuff.Mode.SRC_ATOP);
        final TransitionDrawable lightning = new TransitionDrawable(new Drawable[]{
                //new ColorDrawable(Color.parseColor("#"+color)),
                ripple,
                new ColorDrawable(0)
        });
        lightning.setCrossFadeEnabled(true);
        lightClickEffect.setImageDrawable(lightning);
        return  lightning;
    }

}

//TODO: add ripple effect