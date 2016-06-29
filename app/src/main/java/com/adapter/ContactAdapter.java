package com.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bean.ProfileBean;
import com.cg.DB.DBAccess;
import com.cg.account.AMAVerification;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.snazmed.R;
import com.group.chat.GroupChatActivity;
import com.image.utils.ImageLoader;
import com.main.ContactsFragment;
import com.main.RequestFragment;
import com.util.SingleInstance;

import org.lib.model.BuddyInformationBean;

import java.util.Vector;

public class ContactAdapter extends ArrayAdapter<BuddyInformationBean> {

	/*********** Declare Used Variables *********/
	private CallDispatcher calldisp;
	private Context context;
	 private Vector<BuddyInformationBean> contactList;
    private Vector<BuddyInformationBean>originalList;
	private LayoutInflater inflater = null;
	private ProgressDialog pDialog;
    private  ContactsFilter filter;
	ImageLoader imageLoader;

	/************* CustomAdapter Constructor *****************/
	public ContactAdapter(Context context,
			Vector<BuddyInformationBean> contactList) {

		super(context, R.layout.contact_row, contactList);
		/********** Take passed values **********/

		if (WebServiceReferences.callDispatch.containsKey("calldisp"))
			this.calldisp = (CallDispatcher) WebServiceReferences.callDispatch
					.get("calldisp");
		else
			this.calldisp = new CallDispatcher(context);
		this.context = context;
		 this.contactList = contactList;
        this.originalList = new Vector<BuddyInformationBean>();
        this.originalList.addAll(contactList);
        imageLoader=new ImageLoader(context);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		try {

			final ViewHolder holder;

			if (convertView == null) {

				holder = new ViewHolder();

				inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				convertView = inflater.inflate(R.layout.contact_row, null);

				holder.main_content = (LinearLayout) convertView.findViewById(R.id.main_content);

                holder.tv_header_title = (TextView) convertView.findViewById(R.id.header_title);

                holder.buddy_icon = (ImageView) convertView.findViewById(R.id.buddyicon);

                holder.buddyName = (TextView) convertView.findViewById(R.id.buddyName);

                holder.occupation = (TextView) convertView.findViewById(R.id.occupation);

                holder.status = (ImageView) convertView.findViewById(R.id.imgstatus);

				holder.inreq = (LinearLayout) convertView.findViewById(R.id.inreq);

				holder.contact_history = (LinearLayout) convertView.findViewById(R.id.contact_history);

				holder.accept = (LinearLayout) convertView.findViewById(R.id.ll_accept);

				holder.reject = (LinearLayout) convertView.findViewById(R.id.ll_reject);

                holder.tv_reject = (TextView) convertView.findViewById(R.id.tv_reject);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

            holder.tv_header_title.setVisibility(View.GONE);

            if (contactList.size() > 0) {
            final BuddyInformationBean buddyInformationBean = (BuddyInformationBean) contactList.get(position);
                if (!buddyInformationBean.isTitle()) {
                    holder.tv_header_title.setText(String.valueOf(buddyInformationBean.getLastname().charAt(0)).toUpperCase());
                    holder.tv_header_title.setVisibility(View.VISIBLE);
                    if(position>0){
                        BuddyInformationBean bib=contactList.get(position-1);
                        if(String.valueOf(bib.getLastname().charAt(0)).equalsIgnoreCase(String.valueOf(buddyInformationBean.getLastname().charAt(0))))
                            holder.tv_header_title.setVisibility(View.GONE);
                        else
                            holder.tv_header_title.setVisibility(View.VISIBLE);
                    }
                    if(ContactsFragment.SortType.equalsIgnoreCase("ONLINE"))
                        holder.tv_header_title.setVisibility(View.GONE);
                    else if(buddyInformationBean.getStatus().equalsIgnoreCase("new")
                            || buddyInformationBean.getStatus().equalsIgnoreCase("pending")) {
                        if (buddyInformationBean.getHeader().equalsIgnoreCase("REQUEST"))
                            holder.tv_header_title.setText("REQUEST");
                        else
                            holder.tv_header_title.setVisibility(View.GONE);
                    }
                    if (buddyInformationBean.getStatus()
                            .equalsIgnoreCase("new")) {
                        holder.contact_history.setVisibility(View.GONE);
                        holder.occupation.setText("invite recieved");
                        holder.status.setBackgroundResource(R.drawable.checkbox_default);
                        holder.inreq.setVisibility(View.VISIBLE);
                        holder.accept.setVisibility(View.VISIBLE);

						holder.accept.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                // TODO Auto-generated method stub
                                if (SingleInstance.mainContext
                                        .isNetworkConnectionAvailable()) {
                                    String name = buddyInformationBean
                                            .getName();
                                    Toast.makeText(
                                            context,
                                            "Processing Request "
                                                    + name,
                                            Toast.LENGTH_SHORT).show();

                                    if (!WebServiceReferences.running) {
                                        calldisp.startWebService(
                                                context.getResources()
                                                        .getString(
                                                                R.string.service_url),
                                                "80");
                                    }

                                    WebServiceReferences.webServiceClient
                                            .acceptRejectPeople(
                                                    CallDispatcher.LoginUser,
                                                    name,
                                                    "1", "",
                                                    ContactsFragment
                                                            .getInstance(getContext()));
                                    showprogress();
                                } else {
                                    ContactsFragment
                                            .getInstance(context)
                                            .showAlert1("Info",
                                                    "Check Internet Connection,Unable to Connect Server");
                                }
                            }
                        });
                        holder.tv_reject.setText("REJECT");
                        holder.reject.setBackgroundColor(context.getResources().getColor(R.color.pink));
						holder.reject.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                // TODO Auto-generated method stub
                                if (SingleInstance.mainContext
                                        .isNetworkConnectionAvailable()) {
                                    RequestFragment requestFragment = RequestFragment.newInstance(SingleInstance.mainContext);
                                    requestFragment.reject(buddyInformationBean.getName());
                                } else {
                                    ContactsFragment
                                            .getInstance(context)
                                            .showAlert1("Info",
                                                    "Check Internet Connection,Unable to Connect Server");
                                }
                            }
                        });

                        holder.buddyName.setText(buddyInformationBean.getFirstname() + " " + buddyInformationBean.getLastname());

                        holder.main_content.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ProfileBean pBean=DBAccess.getdbHeler().getProfileDetails(buddyInformationBean.getName());
                                Log.i("AAAA", "Adapter ");
                                if(pBean!=null && pBean.getUsername()!=null) {
                                    Log.i("AAAA", "Adapter if");
                                    RequestFragment requestFragment = RequestFragment.newInstance(SingleInstance.mainContext);
                                    requestFragment.setBuddyName(buddyInformationBean.getName());
                                    requestFragment.setRequest("accept");
                                    FragmentManager fragmentManager = SingleInstance.mainContext
                                            .getSupportFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.activity_main_content_fragment, requestFragment)
                                            .commitAllowingStateLoss();
                                }else {
                                    Log.i("AAAA", "Adapter else");
                                    WebServiceReferences.webServiceClient.GetAllProfile(
                                            CallDispatcher.LoginUser, buddyInformationBean.getName(), ContactsFragment
                                                    .getInstance(SingleInstance.mainContext));
                                    RequestFragment requestFragment = RequestFragment.newInstance(SingleInstance.mainContext);
                                    requestFragment.setBuddyName(buddyInformationBean.getName());
                                    requestFragment.setRequest("accept");
                                }
                            }
                        });

                    } else if (buddyInformationBean.getStatus().equalsIgnoreCase("pending")) {
                        holder.occupation.setText("invite sent");
                        holder.contact_history.setVisibility(View.GONE);
                        holder.inreq.setVisibility(View.VISIBLE);
                        holder.accept.setVisibility(View.INVISIBLE);
                        holder.status.setBackgroundResource(R.drawable.checkbox_default);
                        holder.tv_reject.setText("CANCEL");
                        holder.reject.setBackgroundColor(context.getResources().getColor(R.color.grey2));
                        holder.reject.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    if (!WebServiceReferences.running) {
                                        calldisp.startWebService(
                                                context.getResources()
                                                        .getString(
                                                                R.string.service_url),
                                                "80");
                                    }
                                    WebServiceReferences.webServiceClient
                                            .deletePeople(
                                                    CallDispatcher.LoginUser,
                                                    buddyInformationBean.getName(),
                                                    ContactsFragment.getInstance(context));
                                    deleteUser(buddyInformationBean.getName());
                                    showprogress();
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        });

                        holder.buddyName.setText(buddyInformationBean.getFirstname() + " " + buddyInformationBean.getLastname());

                        holder.main_content.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ProfileBean pBean=DBAccess.getdbHeler().getProfileDetails(buddyInformationBean.getName());
                                if(pBean!=null && pBean.getUsername()!=null) {
                                    Log.i("AAAA", "Adapter if");
                                    RequestFragment requestFragment = RequestFragment.newInstance(SingleInstance.mainContext);
                                    requestFragment.setBuddyName(buddyInformationBean.getName());
                                    requestFragment.setRequest("cancel");
                                    FragmentManager fragmentManager = SingleInstance.mainContext
                                            .getSupportFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.activity_main_content_fragment, requestFragment)
                                            .commitAllowingStateLoss();
                                }else {
                                    Log.i("AAAA", "Adapter else");
                                    WebServiceReferences.webServiceClient.GetAllProfile(
                                            CallDispatcher.LoginUser, buddyInformationBean.getName(), ContactsFragment
                                                    .getInstance(SingleInstance.mainContext));
                                    RequestFragment requestFragment = RequestFragment.newInstance(SingleInstance.mainContext);
                                    requestFragment.setBuddyName(buddyInformationBean.getName());
                                    requestFragment.setRequest("cancel");
                                }
                            }
                        });

                    } else {
                        holder.inreq.setVisibility(View.GONE);
                        if (!buddyInformationBean.getName().equalsIgnoreCase(
                                CallDispatcher.LoginUser)) {
                            ProfileBean pbean= DBAccess.getdbHeler().getProfileDetails(buddyInformationBean.getName());
                                if(pbean!=null) {
                                    if(pbean.getPhoto()!=null)
                                    imageLoader.DisplayImage(
                                            Environment.getExternalStorageDirectory().getAbsolutePath()+"/COMMedia/"+
                                                    pbean.getPhoto(),
                                            holder.buddy_icon,
                                            R.drawable.icon_buddy_aoffline);
                                }

                            holder.buddyName.setText(buddyInformationBean.getFirstname() + " " + buddyInformationBean.getLastname());

                            holder.main_content.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									Intent intent = new Intent(SingleInstance.mainContext, GroupChatActivity.class);
									intent.putExtra("groupid", CallDispatcher.LoginUser
											+ buddyInformationBean.getName());
									intent.putExtra("isGroup", false);
                                    intent.putExtra("isReq", "C");
									intent.putExtra("buddy", buddyInformationBean.getName());
                                    intent.putExtra("buddystatus", buddyInformationBean.getStatus());
                                    intent.putExtra("nickname", buddyInformationBean.getFirstname()+" "+buddyInformationBean.getLastname());
									SingleInstance.mainContext.startActivity(intent);
								}
							});
                            holder.occupation.setText(buddyInformationBean.getOccupation());
                            if (buddyInformationBean.getStatus().equalsIgnoreCase("online")) {
                                holder.status.setBackgroundResource(R.drawable.online_icon);
                            } else if (buddyInformationBean.getStatus().equalsIgnoreCase("offline")) {
                                holder.status.setBackgroundResource(R.drawable.offline_icon);
                            } else if (buddyInformationBean.getStatus().equalsIgnoreCase("Away")) {
                                holder.status.setBackgroundResource(R.drawable.busy_icon);
                            } else if (buddyInformationBean.getStatus().equalsIgnoreCase("Stealth")) {
                                holder.status.setBackgroundResource(R.drawable.invisibleicon);
                            } else if (buddyInformationBean.getStatus().equalsIgnoreCase("Airport")) {
                                holder.status.setBackgroundResource(R.drawable.busy_icon);
                            } else {
                                holder.status.setBackgroundResource(R.drawable.offline_icon);
                            }

                            if (buddyInformationBean.getMessageCount() > 0) {
                                holder.contact_history.setVisibility(View.VISIBLE);
                            } else {
                                holder.contact_history.setVisibility(View.GONE);
                            }
                        }
                    }
                }else{
                    holder.tv_header_title.setText(buddyInformationBean.getName());
                    holder.tv_header_title.setVisibility(View.VISIBLE);
                    holder.main_content.setVisibility(View.GONE);
                }
                holder.main_content.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ContactsFragment.getInstance(context).doDeleteContact(buddyInformationBean.getName());
//                        Intent i = new Intent(SingleInstance.mainContext, AMAVerification.class);
//                        SingleInstance.mainContext.startActivity(i);
                        return true;
                    }
                });
            }
		} catch (Exception e) {
			e.printStackTrace();
		}

		return convertView;
	}

    public static class ViewHolder {
        TextView tv_header_title;
        LinearLayout main_content;
        ImageView buddy_icon;
        ImageView status;
        TextView buddyName;
        TextView occupation;
		LinearLayout inreq;
		LinearLayout contact_history;
		LinearLayout accept;
		LinearLayout reject;
        TextView tv_reject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */

	public void deleteUser(String name) {
		// TODO Auto-generated method stub
        for(BuddyInformationBean bib : contactList){
            if(bib.getName().equals(name)) {
                contactList.remove(bib);
                ContactsFragment.getBuddyList().remove(bib);
                return;
            }
        }
//		for (int i = 0; i < contactList.size(); i++) {
//			try {
//                BuddyInformationBean data = contactList
//						.get(i);
//				if (!data.isTitle()) {
//					if (data.getName().equalsIgnoreCase(name)) {
//						synchronized (contactList) {
//							contactList.remove(i);
//						}
//						break;
//					}
//				}
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}

	}

//	public Vector<BuddyInformationBean> getListofBuddy() {
//		// TODO Auto-generated method stub
//		return contactList;
//	}

	public String getUser(int pos) {
		// TODO Auto-generated method stub
		BuddyInformationBean data = contactList.get(pos);
		if (!data.isTitle()) {
			return data.getName().trim();
		} else {
			return "";
		}
	}

	private void showprogress() {
		try {
			pDialog = new ProgressDialog(SingleInstance.mainContext);
			pDialog.setCancelable(false);
			pDialog.setMessage("Progress ...");
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.setProgress(0);
			pDialog.setMax(100);
			pDialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void cancelDialog() {
		if (pDialog != null && pDialog.isShowing()) {
			pDialog.dismiss();
			pDialog = null;
		}
	}

    @Override
    public Filter getFilter() {
        if (filter == null){
            filter  = new ContactsFilter();
        }
        return filter;
    }

    private class ContactsFilter extends Filter
    {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            constraint = constraint.toString().toLowerCase();

            FilterResults result = new FilterResults();
            if (constraint != null && constraint.toString().length() > 0) {
                Vector<BuddyInformationBean> buddyInformationBeans = new Vector<BuddyInformationBean>();
                for(int i = 0, l = originalList.size(); i < l; i++)
                {
                    BuddyInformationBean buddyInformationBean = originalList.get(i);
                    if(buddyInformationBean.getName().toLowerCase().startsWith(String.valueOf(constraint)))
                        buddyInformationBeans.add(buddyInformationBean);
                }
                buddyInformationBeans = GroupChatActivity.getAdapterList(buddyInformationBeans);
                result.count = buddyInformationBeans.size();
                result.values = buddyInformationBeans;
            } else {
                synchronized (this) {
                    originalList = GroupChatActivity.getAdapterList(originalList);
                    result.values = originalList;
                    result.count = originalList.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            contactList= (Vector<BuddyInformationBean>)results.values;
            notifyDataSetChanged();
            clear();
            contactList = GroupChatActivity.getAdapterList(contactList);
            for(int i = 0, l = contactList.size(); i < l; i++)
                add(contactList.get(i));
            notifyDataSetInvalidated();

        }

    }
}
