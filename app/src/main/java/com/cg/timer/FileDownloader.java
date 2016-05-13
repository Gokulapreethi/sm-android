package com.cg.timer;

import java.io.File;
import java.util.TimerTask;
import java.util.Vector;

import org.lib.model.BuddyInformationBean;
import org.lib.model.FieldTemplateBean;

import android.os.Environment;
import android.util.Log;

import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.main.ContactsFragment;

public class FileDownloader extends TimerTask {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (WebServiceReferences.callDispatch.containsKey("calldisp")) {
			CallDispatcher callDispatcher = (CallDispatcher) WebServiceReferences.callDispatch
					.get("calldisp");
			Vector<FieldTemplateBean> file_list = callDispatcher.getdbHeler(
					callDispatcher.getContext()).getfieldstoDownload();
			if (file_list != null) {
				for (FieldTemplateBean fieldTemplateBean : file_list) {
					String profile_multimedia = Environment
							.getExternalStorageDirectory()
							+ "/COMMedia/"
							+ fieldTemplateBean.getFiledvalue();
					File msg_file = new File(profile_multimedia);
					if (!msg_file.exists()) {

						Log.d("profile",
								"Field value is :"
										+ fieldTemplateBean.getFiledvalue());
						if (fieldTemplateBean.getFieldId().equals("3")) {
							
							for (BuddyInformationBean bib  : ContactsFragment.getBuddyList()) {
									if(!bib.isTitle()){
								if(bib.getName().equalsIgnoreCase(fieldTemplateBean
											.getUsername())){
									bib.setProfile_picpath(fieldTemplateBean
											.getFiledvalue());
								}
							}
							}
							
							// if (WebServiceReferences.buddyList
							// .containsKey(fieldTemplateBean
							// .getUsername())) {
							// BuddyInformationBean bean =
							// WebServiceReferences.buddyList
							// .get(fieldTemplateBean.getUsername());
							//
							// if (bean != null)
							//
							// }
						}
						callDispatcher.downloadOfflineresponse(
								fieldTemplateBean.getFiledvalue(),
								fieldTemplateBean.getFieldId() + ","
										+ fieldTemplateBean.getUsername() + ","
										+ true, "profile field",null);
					}
					msg_file = null;
					profile_multimedia = null;

				}
			}
		}

	}

}
