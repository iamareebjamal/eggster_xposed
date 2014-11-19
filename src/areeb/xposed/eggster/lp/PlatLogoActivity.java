package areeb.xposed.eggster.lp;

import java.util.Random;

import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
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


@SuppressLint("ClickableViewAccessibility") 
public class PlatLogoActivity extends Activity {

	boolean firstClickDone = false;

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
		final String[] materialPalette 	 = {"E51C23", "E91E63", "9C27B0", "673AB7", "3F51B5", "5677FC", "03A9F4", "00BCD4", "009688", "259B24", "8BC34A", "CDDC39", "FFEB3B", "FFC107", "FF9800", "FF5722", "795548", "607D8B"};	//hex codes of the Material Design colors. You can find the palette at google.com/design
		final ImageView lightClickEffect = new ImageView(this);

		RelativeLayout container = new RelativeLayout(this);

		lollipop.setImageResource(R.drawable.lollipop_circle);
		logo.setImageResource(R.drawable.lollipop_text);
		stick.setBackgroundResource(R.drawable.lollipop_stick);
		lollipopLight.setImageResource(R.drawable.lollipop_light);

		final TransitionDrawable lightning = new TransitionDrawable(new Drawable[]{
				getResources().getDrawable(R.drawable.lollipop_white),
				new ColorDrawable(0)
		});
		lightning.setCrossFadeEnabled(true);
		lightClickEffect.setImageDrawable(lightning);


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

		@SuppressWarnings("deprecation")
		LayoutParams StickLP = new LayoutParams(LayoutParams.WRAP_CONTENT, (getWindowManager().getDefaultDisplay().getHeight() / 2) - lollipop.getBottom() + 5);
		StickLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		StickLP.addRule(RelativeLayout.CENTER_HORIZONTAL);
		stick.setLayoutParams(StickLP);

		ViewHelper.setAlpha(stick, 0);
		ViewHelper.setAlpha(logo, 0);
		ViewHelper.setAlpha(lightClickEffect, 0.55F);
		logo.setVisibility(0);
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

		//Decide if the color should be purely random or between the palette
		if (getSharedPreferences("preferenceggs", Context.MODE_PRIVATE).getBoolean("purely_random", false))
			Eggs.setColorFilter(lollipop, new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));	//lollipop color is purely random
		else			
			colorizeHex(lollipop, materialPalette[new Random().nextInt(materialPalette.length)]);	//lollipop color is randomly picked from the material palette

		ViewPropertyAnimator.animate(pop).scaleX(1).scaleY(1).setDuration(1800).setInterpolator(new DecelerateInterpolator()).setStartDelay(1000).start();

		pop.setClickable(true);
		pop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub	
				if (!firstClickDone) {
					firstClickDone = true;
					ViewPropertyAnimator.animate(pop).scaleX(4.2F).scaleY(4.2F).setDuration(700).setInterpolator(new DecelerateInterpolator()).start();
					ViewPropertyAnimator.animate(logo).alpha(1).setDuration(700).setStartDelay(750).start();
					ViewPropertyAnimator.animate(stick).translationYBy(pop.getMeasuredHeight()*2.1F).alpha(1).setDuration(1700).setStartDelay(1000).start();
				} else {
					lightning.startTransition(200);				//illuminati weren't here
					if (getSharedPreferences("preferenceggs", Context.MODE_PRIVATE).getBoolean("purely_random", false))
						Eggs.setColorFilter(lollipop, new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));	//lollipop color is purely random
					else
						colorizeHex(lollipop, materialPalette[new Random().nextInt(materialPalette.length)]);
				}
			}
		});
		if(Build.VERSION.SDK_INT>10){
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
			String sysUIMode = (getSharedPreferences("preferenceggs", Context.MODE_PRIVATE).getString("lollipopSysUI", getString(R.string.pref_none)));
		
			if (sysUIMode.equals(getString(R.string.pref_immerge)))
				container.setSystemUiVisibility(
						View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
						| View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
						| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
						);
		
			if (sysUIMode.equals(getString(R.string.pref_translucent)))	{
				getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			}
		}
	}

	public static void colorizeHex(ImageView iv, String hex) {
		Eggs.setColorFilter(iv,
				Integer.valueOf(hex.substring(0, 2), 16),
				Integer.valueOf(hex.substring(2, 4), 16),
				Integer.valueOf(hex.substring(4, 6), 16));
	}

}

//TODO: add ripple effect