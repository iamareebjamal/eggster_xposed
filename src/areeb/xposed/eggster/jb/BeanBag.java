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

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;
import areeb.xposed.eggster.R;

public class BeanBag extends Activity {

	private Board board;
	private SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		setContentView(R.layout.activity_bean_bag);		
		board = new Board(this, null);
		((FrameLayout) findViewById(R.id.gameview)).addView(board, 0);
		prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		prefs.edit().clear().commit();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.bean_bag, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		if (item.getItemId() == R.id.number_of_jb) {
			final EditText input = new EditText(this);
			input.setInputType(InputType.TYPE_CLASS_NUMBER);
			alertDialogBuilder
					.setView(input)
					.setTitle("Number of Beans")
					.setMessage(
							"Choose a rational number of beans!")
					.setCancelable(true)
					.setIcon(R.drawable.ic_launcher)
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
								}
							})
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									String newValue = input.getText()
											.toString();
									if (newValue != null
											&& newValue.length() > 0
											&& newValue.matches("\\d*")
											&& Integer.parseInt(newValue) > 0) {
										prefs.edit()
												.putInt("number_of_jb",
														Integer.parseInt(newValue))
												.commit();
										Intent i = getIntent();
										finish();
										startActivity(i);
									} else {
										Toast.makeText(BeanBag.this,
												"Invalid Input",
												Toast.LENGTH_SHORT).show();
									}
								}
							});

			alertDialogBuilder.show();
		} else {
			alertDialogBuilder
					.setTitle("About")
					.setMessage(
							"This is a port of the JellyBean-Settings Easter Egg to lower android versions, with a couple additional features.\n\nModified by George Piskas!\ngeopiskas@gmail.com")
					.setCancelable(true)
					.setIcon(R.drawable.ic_launcher)
					.setNeutralButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.dismiss();
								}
							});
			alertDialogBuilder.show();
		}
		return super.onOptionsItemSelected(item);
	}
}
