/*
*  Jelly Bean Game
*  Copyright (C) 2013  George Piskas
*
*  This program is free software; you can redistribute it and/or modify
*  it under the terms of the GNU General Public License as published by
*  the Free Software Foundation; either version 2 of the License, or
*  (at your option) any later version.
*
*  This program is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*  GNU General Public License for more details.
*
*  You should have received a copy of the GNU General Public License along
*  with this program; if not, write to the Free Software Foundation, Inc.,
*  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*
*  Contact: geopiskas@gmail.com
*/

package areeb.xposed.eggster.jb;

import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import areeb.xposed.eggster.R;

public class BeanBag extends Activity {

	private Board board;

	@SuppressLint({ "NewApi", "InlinedApi" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		setContentView(R.layout.activity_bean_bag);		
		board = new Board(this, null);
		if (Build.VERSION.SDK_INT >= 19) {
	        board.setSystemUiVisibility(
	                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
	                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
	                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
	                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
	                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
	                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY); }
	        
		((FrameLayout) findViewById(R.id.gameview)).addView(board, 0);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (board != null) {
			board.startAnimation();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (board != null) {
			board.stopAnimation();
		}
	}

	
}
