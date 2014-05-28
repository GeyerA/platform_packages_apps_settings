/*
 * Copyright (C) 2013-2014 Dokdo Project - Gwon Hyeok
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.dokdo;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.settings.dokdo.util.CMDProcessor;
import com.android.settings.dokdo.util.Helpers;

import com.android.settings.R;

public class DPI_Settings extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dpi_settings);
		Button btn = (Button)findViewById(R.id.button1);
		int CurrentDensity = getResources().getDisplayMetrics().densityDpi;
		TextView tv = (TextView)findViewById(R.id.textView1);
		TextView tv2 = (TextView)findViewById(R.id.textView2);
		tv.setText(getString(R.string.current_dpi)+":"+CurrentDensity);
		if(CurrentDensity <120) {
			tv2.setText(getString(R.string.dpi_ldpi));
		} else {
			if(CurrentDensity <160){
				tv2.setText(getString(R.string.dpi_mdpi));
			} else {
				if(CurrentDensity <240) {
					tv2.setText(getString(R.string.dpi_hdpi));
				} else {
					if(CurrentDensity <320) {
						tv2.setText(getString(R.string.dpi_xhdpi));
					} else {
						if(CurrentDensity <640) {
							tv2.setText(getString(R.string.dpi_xxhdpi));
						}
					}
				}
			}
		}
		
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				int CurrentDensity = getResources().getDisplayMetrics().densityDpi;
				EditText et = (EditText)findViewById(R.id.editText1);
				String NewDensity = et.getText().toString();				
				if(NewDensity.equals("")||NewDensity.equals(null)){
					Helpers.msgShort(getApplicationContext(), getString(R.string.dpi_null));
				}else{
				SetDPI(NewDensity);
			}
		}});
	}
		
	public void SetDPI(String newdpi) {
		int CurrentDensity = getResources().getDisplayMetrics().densityDpi;
		apply(CurrentDensity, newdpi);
		Helpers.msgShort(getApplicationContext(), getString(R.string.dpi_msg1)+CurrentDensity+ getString(R.string.dpi_msg2)+newdpi+getString(R.string.dpi_msg3));
		Helpers.msgShort(getApplicationContext(), getString(R.string.dpi_need_reboot));
		TextView tv = (TextView)findViewById(R.id.textView1);
		tv.setText(getString(R.string.current_dpi)+newdpi); 
		TextView tv3 = (TextView)findViewById(R.id.textView3);
		tv3.setText(getString(R.string.dpi_sucess));
		CheckBox ck = (CheckBox)findViewById(R.id.checkBox1);
		Helpers.getMount("ro");
		if(ck.isChecked()==true){
		    CMDProcessor.runSuCommand("reboot");
		}
	}

	public  void apply(int CurrentDensity, String newdpi) {
		SETDPI thread = new SETDPI(CurrentDensity, newdpi);
		thread.start();
		try {
		thread.join();
		} catch (InterruptedException e1) {
		e1.printStackTrace();
		}
        }

	class SETDPI extends Thread {
		private int CurrentDensity;
		private String newdpi;
	public SETDPI(int CurrentDensity, String newdpi) {
		this.CurrentDensity =CurrentDensity ;
		this.newdpi = newdpi ;
        }

		public void  run() {
	        Helpers.getMount("rw");
                String cmd = "sed -i 's/ro.sf.lcd_density="+CurrentDensity+"/ro.sf.lcd_density="+newdpi+"/g' /system/build.prop";
                CMDProcessor.startSuCommand(cmd);
		}
	}
}
