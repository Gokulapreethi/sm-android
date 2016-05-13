package com.cg.timer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cg.DB.DBAccess;
import com.cg.files.CompleteListBean;
import com.main.FilesFragment;
import com.util.SingleInstance;

public class FileBroadCastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			// TODO Auto-generated method stub
			final CompleteListBean cBean = (CompleteListBean) intent
					.getSerializableExtra("cBean");
			String strDeleteQry = "delete from component where componentid="
					+ cBean.getComponentId();
			if (DBAccess.getdbHeler().ExecuteQuery(strDeleteQry)) {
				SingleInstance.mainContext.notifyUI();
				FilesFragment filesFragment = FilesFragment
						.newInstance(SingleInstance.mainContext);
				Log.i("delete file 123", "delete files...");
				filesFragment.filesListRefresh();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
