package com.cg.quickaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.lib.model.FormsListBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.forms.FormQuery;
import com.cg.forms.QueryBuilder;
import com.cg.forms.QuerySelect;
import com.cg.forms.QuerySyntax.Column;
import com.cg.forms.Queryaggregate;
import com.main.AppMainActivity;
import com.util.SingleInstance;

public class TriggerMain extends Activity {

	List<String> childList;
	Map<String, List<String>> laptopCollection;
	ExpandableListView expListView;
	Parent parent;
	Context context = null;
	String selNonAggFld = null;
	Map<String, String> map = new HashMap<String, String>();
	String value = "";
	String isGroupby = null;
	ArrayList<String> columnName = null;
	ArrayList<String> columnType = null;
	ArrayList<String> nonAggFldList = null;
	ArrayList<String> aggFldList = null;
	Set<String> childName = new HashSet<String>();
	boolean setChecked = false;
	// String value = null;
	TextView title, subTitle;
	String type = null;
	private HashMap<String, String> selAggFldMap = new HashMap<String, String>();
	CharSequence items[] = null;
	String query = null;
	HashMap<String, Boolean> mCheckBoxData = new HashMap<String, Boolean>();
	Button next, cancel, btn_notification;
	public Button IMRequest;
	boolean childSelect = false;
	AlertDialog alert = null;
	String tableColumn = "";
	ArrayList<FormsListBean> dbNames = null;
	ArrayList<TableColumnsBean> cNames = null;
	private LinkedHashMap<Item, ArrayList<Item>> groupList;
	ArrayList<Item> parentCheckedItems = new ArrayList<Item>();
	private ExpandableListView expandableListView;
	LinearLayout linearLayout = null;
	ArrayList<Item> childCheckedItems = new ArrayList<Item>();
	ArrayList<String> columns = new ArrayList<String>();
	String TABLENAME, FIELDS;
	Set<String> tableNames = new HashSet<String>();
	public QuerySelect querySelect = null;
	public FormQuery obj = null;
	boolean isReport;
	CallDispatcher callDisp;
	private Handler viewHandler = new Handler();
	public String owner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.trig_query);
		title = (TextView) findViewById(R.id.textreport);
		subTitle = (TextView) findViewById(R.id.subtitle);
		linearLayout = (LinearLayout) findViewById(R.id.linear);
		nonAggFldList = new ArrayList<String>();
		aggFldList = new ArrayList<String>();
		columnName = new ArrayList<String>();
		columnType = new ArrayList<String>();
		context = this;
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int noScrHeight = displaymetrics.heightPixels;
		int noScrWidth = displaymetrics.widthPixels;

		if (WebServiceReferences.callDispatch.containsKey("calldisp"))
			callDisp = (CallDispatcher) WebServiceReferences.callDispatch
					.get("calldisp");
		else
			callDisp = new CallDispatcher(context);

		callDisp.setNoScrHeight(noScrHeight);
		callDisp.setNoScrWidth(noScrWidth);
		displaymetrics = null;
		TABLENAME = "";
		isReport = getIntent().getExtras().getBoolean("report");
		expListView = (ExpandableListView) findViewById(R.id.expandableListView);
		next = (Button) findViewById(R.id.btn_next);
		cancel = (Button) findViewById(R.id.btn_cancel);
		WebServiceReferences.contextTable.put("TriggerMain", this);
		// WebServiceReferences.contextTable.put("QueryActivity", this);
		HashMap<String, String> aggregateValues = new HashMap<String, String>();
		initViews();
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				String selected_fields = "";
				HashMap<String, String> fieldvalue = new HashMap<String, String>();

				for (Item item : childCheckedItems) {
					if (item.isChecked) {
						String fieldText = item.parentName + ".[" + item.name
								+ "]";
						Log.i("QAA", "Child Value :: " + item.parentName + ".["
								+ item.name + "]");
						tableNames.add(item.parentName);
						columns.add(item.parentName + ".[" + item.name + "]");

						if (selected_fields.trim().length() == 0) {
							selected_fields = fieldText.trim();
							if (item.isAggregate) {
								String aggreagtion = item.aggregation;

								aggreagtion = aggreagtion.replace("Aggregate:",
										"");

								fieldvalue.put(selected_fields, aggreagtion);

							}
						} else {
							selected_fields = selected_fields + "," + fieldText;

							selected_fields = fieldText.trim();
							if (item.isAggregate) {
								String aggreagtion = item.aggregation;

								aggreagtion = aggreagtion.replace("Aggregate:",
										"");

								fieldvalue.put(selected_fields, aggreagtion);

							}
						}
					}
				}

				populateresult(selected_fields, fieldvalue);

				StringBuilder sb = new StringBuilder();

				if (tableNames.size() > 0) {
					for (String table : tableNames) {

						if (sb.length() > 0)
							sb.append(",");
						sb.append(table);
					}
					TABLENAME = sb.toString();
				}

				StringBuilder fields = new StringBuilder();

				if (columns.size() > 0) {
					for (String table : columns) {

						if (fields.length() > 0)
							fields.append(",");
						fields.append(table);
					}
					FIELDS = fields.toString();
				}

				// TABLENAME=tableNames.toString();
				// FIELDS=columns.toString();

				Log.i("QAA", "Table Namessssss====>" + TABLENAME
						+ "fieldssss===>" + FIELDS);
				if (TABLENAME.length() > 0) {

					String[] forms = TABLENAME.split(",");
					QueryBuilder queryBuilder = new QueryBuilder();
					querySelect = queryBuilder.getQuerySelect(forms);
					String[] colnames = FIELDS.replace("[", "")
							.replace("]", "").split(",");
					String[] columnnames = FIELDS.split(",");

					for (int i = 0; i < colnames.length; i++) {
						Log.i("welcome", "------>" + colnames[i] + "<-----");
						String[] values = colnames[i].split("\\.");

						String tablename = values[0];
						String columnname = values[1];
						Log.i("QAA", "------>" + tablename + "<-----"
								+ columnname);

						obj = querySelect.getForm(tablename);

						obj.addColumnSelect(columnname);
						if (map.containsKey(columnnames[i])) {
							Column colObj = null;
							String aggregation = map.get(columnnames[i])
									.replace("(", "").replace(")", "");

							Log.i("welcome", "-------->aggregations---->"
									+ aggregation);
							colObj = obj.getColumnObj(columnname);
							colObj.setAggregateFunction(aggregation);

							Log.i("welcome",
									"--------------->"
											+ querySelect.getCompleteSQL()
											+ "<----------------");
							Log.i("Query", "Query :: " + query);

							Log.i("QB",
									"columnNames--->" + columnName.toString());

							Log.i("QB",
									"columTypes--->" + columnType.toString());

							new Queryaggregate();

						}

					}
					query = querySelect.getCompleteSQL();

					Intent intent = new Intent(context, TrigerActivity.class);
					intent.putExtra("report", isReport);
					intent.putExtra("tablename", tableNames.toString());
					intent.putStringArrayListExtra("columns", columns);
					startActivity(intent);

				} else {

					Toast.makeText(context,SingleInstance.mainContext.getResources().getString(R.string.select_table),
							Toast.LENGTH_LONG).show();

				}

			}

		});
		btn_notification = (Button) findViewById(R.id.notification);
		btn_notification.setVisibility(View.GONE);

		IMRequest = (Button) findViewById(R.id.im);

		IMRequest.setVisibility(View.INVISIBLE);
		IMRequest.setBackgroundResource(R.drawable.one);
	}

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }

    private void initViews() {

		initContactList();
		expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
		ExpandableAdapter adapter = new ExpandableAdapter(this,
				expandableListView, groupList);
		expandableListView.setAdapter(adapter);

	}

	public void populateresult(String value, HashMap<String, String> keyvalue) {
		selNonAggFld = value;

		Log.i("QB", "non agg  " + selNonAggFld);
		String[] temp = selNonAggFld.split(",");
		Log.i("QB", "length--->" + temp.length);
		Log.i("QB", "values of temp--->" + temp.toString());
		StringBuilder sbf = new StringBuilder();

		for (int i = 0; i < temp.length; i++) {
			Log.i("QB", "values of temp--->" + temp[i].toString());
			if (!keyvalue.containsKey(temp[i])) {
				String alias = temp[i].replace("[", "");
				alias = alias.replace("]", "");

				nonAggFldList.add(temp[i] + " as [" + alias.replace(".", "-")
						+ "]");
				sbf.append(temp[i]);

				columnName.add(alias.replace(".", "-"));
				columnType.add("nvarchar(20)");
				if (i != temp.length - 1) {
					sbf.append(",");
				}
			}

		}
		if (sbf.toString() != null) {
			Log.i("BL", "force close issue--->" + sbf.toString());
			if (sbf.toString().endsWith(",")) {
				isGroupby = sbf.toString().substring(0,
						sbf.toString().length() - 1);
			} else {
				isGroupby = sbf.toString();

			}
		}

		// int indx = sbf.toString().lastIndexOf(",");
		// if (indx == sbf.toString().length() - 1) {
		// isGroupby = sbf.toString().substring(0, sbf.toString().length() - 1);
		//
		// } else
		// {
		// isGroupby = sbf.toString();
		// }
		Log.i("QB", "is group by--->" + isGroupby);

		Iterator<Entry<String, String>> it = keyvalue.entrySet().iterator();
		String colName = null;
		String aggName = null;
		String aggFld = null;
		while (it.hasNext()) {
			Map.Entry<String, String> pairs = (Map.Entry<String, String>) it
					.next();
			colName = pairs.getKey();
			aggName = pairs.getValue();

			map.put(colName, aggName);

			Log.i("welcome", "--------" + aggName + "--------" + colName);

			aggFld = aggName.replace("()", "(" + colName + ")");
			String columnname = colName.replace("[", "").replace("]", "")
					.replace(".", "-");
			aggFld = aggFld + " as [" + columnname + "]";

			Log.i("BL", "------>AGGREGATE" + aggFld);
			aggFldList.add(aggFld);
			columnName.add(columnname);
			columnType.add("nvarchar(50)");
		}

		if (value != null) {

			// field.setText(value);
			Log.i("Query", "Query :: " + value);
		} else {
			// field.setText("");
			Log.i("Query", "Query :: " + value);
		}
		Log.i("QB", "values---->" + keyvalue.values());
		Log.i("QB", "values---->" + keyvalue.keySet().toString());
		Log.i("QB", "values---->" + nonAggFldList.toString());
		Log.i("QB", "values---->" + aggFldList.toString());

		selAggFldMap = keyvalue;

	}

	private void initContactList() {
		Log.i("Contact", "Inside initContactList");
		groupList = new LinkedHashMap<Item, ArrayList<Item>>();

		ArrayList<Item> groupsList = fetchGroups();
		Log.i("GroupsListSize", String.valueOf(groupsList.size()));
		// if(groupList.size()>0){
		for (Item item : groupsList) {
			Log.i("Item NAME", item.name);
			Log.i("Item Id", item.id);
			String[] names = item.name.split(",");
			String[] ids = item.id.split(",");
			// String[] columnType = item.columnType.split(",");
			ArrayList<Item> groupMembers = new ArrayList<Item>();
			for (int i = 0; i < ids.length; i++) {
				String groupName = names[i];
				String groupId = ids[i];
				// String colType = columnType[i];
				String formNames = groupName + "_" + groupId;
				Log.i("GroupId", groupName + "_" + groupId);
				groupMembers.addAll(fetchGroupMembers(formNames));
			}
			item.name = item.name + " (" + groupMembers.size() + ")";
			groupList.put(item, groupMembers);
		}
		// }

	}

	private ArrayList<Item> fetchGroups() {
		Log.i("Fetch Groups", "Inside Fetch Groups");
		ArrayList<String> groupTitle = new ArrayList<String>();
		ArrayList<FormsListBean> ownRecords = new ArrayList<FormsListBean>();
		ArrayList<FormsListBean> buddyRecords = new ArrayList<FormsListBean>();
		ownRecords = callDisp.getdbHeler(context).ownLookUpRecordss(
				CallDispatcher.LoginUser);
		buddyRecords = callDisp.getdbHeler(context).BuddiesLookUpRecords();
		String groupName = "";
		String groupId = "";
		for (FormsListBean fBean : ownRecords) {
			Item item = new Item();
			groupName = fBean.getForm_name();
			groupId = fBean.getFormId();
			groupTitle.add(groupName);
			item.name = groupName;
			item.id = groupId;
			Log.i("GroupName", groupName);
			parentCheckedItems.add(item);
		}
		for (FormsListBean fBean : buddyRecords) {
			Item item = new Item();
			groupName = fBean.getForm_name();
			groupId = fBean.getFormId();
			groupTitle.add(groupName);
			item.name = groupName;
			item.id = groupId;
			Log.i("GroupName", groupName);
			parentCheckedItems.add(item);
		}

		Collections.sort(parentCheckedItems, new Comparator<Item>() {

			public int compare(Item item1, Item item2) {
				// TODO Auto-generated method stub
				return item2.name.compareTo(item1.name) < 0 ? 0 : -1;
			}
		});

		return parentCheckedItems;
	}

	private ArrayList<Item> fetchGroupMembers(String groupId) {
		ArrayList<Item> groupMembers = new ArrayList<Item>();
		cNames = callDisp.getdbHeler(context).getColumnTypesTbl(groupId);
		for (TableColumnsBean tbBean : cNames) {
			Item item = new Item();
			item.parentName = groupId;
			item.name = tbBean.getColumnName();
			item.columnType = tbBean.getColumnType();

			groupMembers.add(item);
			childCheckedItems.add(item);
		}
		return groupMembers;
	}

	private class ExpandableAdapter extends BaseExpandableListAdapter {

		private LayoutInflater layoutInflater;
		private LinkedHashMap<Item, ArrayList<Item>> groupList;
		private ArrayList<Item> mainGroup;
		private int[] groupStatus;

		public ExpandableAdapter(final Context context,
				final ExpandableListView listView,
				LinkedHashMap<Item, ArrayList<Item>> groupsList) {
			layoutInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			this.groupList = groupsList;
			groupStatus = new int[groupsList.size()];

			listView.setOnGroupClickListener(new OnGroupClickListener() {

				@Override
				public boolean onGroupClick(ExpandableListView parent, View v,
						int groupPosition, long id) {

					Item group = mainGroup.get(groupPosition);
					String[] accessSetting = callDisp
							.getdbHeler(context)
							.getAccessRights(group.id, CallDispatcher.LoginUser);
					if (accessSetting != null) {
						if ((accessSetting[0].toString()
								.equalsIgnoreCase("A03"))
								|| (accessSetting[0].toString()
										.equalsIgnoreCase("A04"))
								|| (accessSetting[0].toString()
										.equalsIgnoreCase("A02"))
								|| (accessSetting[0].toString()
										.equalsIgnoreCase("A05"))) {
							if (groupList.get(group).size() > 0)
								groupStatus[groupPosition] = 1;
							return false;
						}

						else {

							Toast.makeText(
									context,
									SingleInstance.mainContext.getResources().getString(R.string.no_permission_to_view_the_records),
									Toast.LENGTH_SHORT).show();
							return true;
						}

					}
					return checkAll;

				}
			});

			//
			// listView.setOnGroupExpandListener(new OnGroupExpandListener() {
			// public void onGroupExpand(int groupPosition) {
			//
			// Item group = mainGroup.get(groupPosition);
			// String[] accessSetting = callDisp.getdbHeler(context)
			// .getAccessRights(group.id, CallDispatcher.LoginUser);
			// if (accessSetting != null) {
			// if ((accessSetting[0].toString().equalsIgnoreCase("A03"))
			// || (accessSetting[0].toString()
			// .equalsIgnoreCase("A04"))
			// || (accessSetting[0].toString()
			// .equalsIgnoreCase("A02"))
			// || (accessSetting[0].toString()
			// .equalsIgnoreCase("A05"))) {
			// if (groupList.get(group).size() > 0)
			// groupStatus[groupPosition] = 1;
			// }
			//
			// else {
			//
			//
			//
			// Toast.makeText(context,
			// "Sorry you have no permission to view the records",
			// Toast.LENGTH_SHORT).show();
			// }
			//
			// }
			// }
			// });

			listView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

				public void onGroupCollapse(int groupPosition) {
					Item group = mainGroup.get(groupPosition);
					if (groupList.get(group).size() > 0)
						groupStatus[groupPosition] = 0;

				}
			});

			mainGroup = new ArrayList<Item>();
			for (Map.Entry<Item, ArrayList<Item>> mapEntry : groupList
					.entrySet()) {
				mainGroup.add(mapEntry.getKey());
			}

		}

		public Item getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			Item item = mainGroup.get(groupPosition);
			return groupList.get(item).get(childPosition);

		}

		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return 0;
		}

		public View getChildView(final int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			final ChildHolder holder;
			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.group_item, null);
				holder = new ChildHolder();
				holder.cb = (CheckBox) convertView.findViewById(R.id.cb);
				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.aggregateType = (TextView) convertView
						.findViewById(R.id.aggregate);
				holder.aggregate = (LinearLayout) convertView
						.findViewById(R.id.Layformcheck);
				convertView.setTag(holder);
			} else {
				holder = (ChildHolder) convertView.getTag();
			}
			final Item child = getChild(groupPosition, childPosition);
			holder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					Item parentGroup = getGroup(groupPosition);
					child.isChecked = isChecked;
					if (isChecked) {
						// child.isChecked =true;
						ArrayList<Item> childList = getChild(parentGroup);
						int childIndex = childList.indexOf(child);
						boolean isAllChildClicked = true;
						for (int i = 0; i < childList.size(); i++) {
							if (i != childIndex) {
								Item siblings = childList.get(i);
								if (!siblings.isChecked) {
									isAllChildClicked = false;
									// if(DataHolder.checkedChilds.containsKey(child.name)==false){
									DataHolder.checkedChilds.put(child.name,
											parentGroup.name);
									// }
									break;
								}
							}
						}

						if (isAllChildClicked) {
							parentGroup.isChecked = true;
							if (!(DataHolder.checkedChilds
									.containsKey(child.name) == true)) {
								childName.add(parentGroup.name + "."
										+ child.name);
								DataHolder.checkedChilds.put(child.name,
										parentGroup.name);
							}
							checkAll = false;
						}

					} else {
						if (parentGroup.isChecked) {
							parentGroup.isChecked = false;
							checkAll = false;
							DataHolder.checkedChilds.remove(child.name);
						} else {
							checkAll = true;
							DataHolder.checkedChilds.remove(child.name);
						}
						// child.isChecked =false;
					}
					notifyDataSetChanged();

				}

			});

			holder.cb.setChecked(child.isChecked);
			holder.title.setText(child.name);
			holder.aggregateType.setContentDescription(child.columnType);
			holder.aggregate.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if (holder.cb.isChecked() == true) {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								context);
						builder.create();
						// builder.setTitle("Add");
						if (holder.aggregateType.getContentDescription()
								.toString().equalsIgnoreCase("INTEGER")) {
							final CharSequence[] choiceList = { "MIN", "MAX",
									"AVG", "SUM" };
							items = choiceList;
						} else if (holder.aggregateType.getContentDescription()
								.toString().equalsIgnoreCase("nvarchar(20)")) {
							final CharSequence[] choiceList = { "COUNT" };
							items = choiceList;
						} else {
							Toast.makeText(context, SingleInstance.mainContext.getResources().getString(R.string.select_columns),
									Toast.LENGTH_SHORT).show();
						}

						// System.out.println(choiceList);

						int selected = -1; // does not select anything
						builder.setSingleChoiceItems(items, selected,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										value = items[which].toString();
										holder.aggregateType.setText(value);
										alert.dismiss();
										child.isAggregate = true;
										String agg = holder.aggregateType
												.getText().toString().trim();
										child.aggregation = agg;
									}
								});
						alert = builder.create();
						if (items != null) {
							if (items.length != 0) {
								alert.show();
							}
						}
					} else {
						holder.aggregateType.setText(SingleInstance.mainContext.getResources().getString(R.string.aggregate));
					}
					return false;

				}
			});

			return convertView;
		}

		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			Item item = mainGroup.get(groupPosition);
			return groupList.get(item).size();
		}

		public Item getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return mainGroup.get(groupPosition);
		}

		public int getGroupCount() {
			// TODO Auto-generated method stub
			return mainGroup.size();
		}

		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return 0;
		}

		public View getGroupView(final int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {

			final GroupHolder holder;

			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.group_list, null);
				holder = new GroupHolder();
				holder.cb = (CheckBox) convertView.findViewById(R.id.cb);
				holder.title = (TextView) convertView.findViewById(R.id.title);
				convertView.setTag(holder);
			} else {
				holder = (GroupHolder) convertView.getTag();
			}

			final Item groupItem = getGroup(groupPosition);

			holder.title.setText(groupItem.name);
			boolean isAccessible = true;
			String[] accessSetting = callDisp.getdbHeler(context)
					.getAccessRights(groupItem.id, CallDispatcher.LoginUser);
			if (accessSetting != null) {
				if ((accessSetting[0].toString().equalsIgnoreCase("A03"))
						|| (accessSetting[0].toString().equalsIgnoreCase("A04"))
						|| (accessSetting[0].toString().equalsIgnoreCase("A02"))
						|| (accessSetting[0].toString().equalsIgnoreCase("A05"))) {
					isAccessible = true;
				} else {
					isAccessible = false;
				}
			}
			if (isAccessible) {
				holder.cb
						.setOnCheckedChangeListener(new OnCheckedChangeListener() {

							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {

								if (checkAll) {
									ArrayList<Item> childItem = getChild(groupItem);
									for (Item children : childItem)
										children.isChecked = isChecked;
								}
								groupItem.isChecked = isChecked;
								notifyDataSetChanged();
								new Handler().postDelayed(new Runnable() {

									public void run() {
										// TODO Auto-generated method stub
										if (!checkAll)
											checkAll = true;
									}
								}, 50);

							}

						});
				holder.cb.setChecked(groupItem.isChecked);

			} else {
				holder.cb
						.setOnCheckedChangeListener(new OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								// TODO Auto-generated method stub
								buttonView.setChecked(false);
								Toast.makeText(
										context,
										SingleInstance.mainContext.getResources().getString(R.string.no_permission_to_view_the_records),
										Toast.LENGTH_SHORT).show();
							}
						});
			}
			return convertView;
		}

		private boolean checkAll = true;

		private ArrayList<Item> getChild(Item group) {
			return groupList.get(group);
		}

		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return true;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return true;
		}

		private class GroupHolder {
			public CheckBox cb;
			public TextView title;

		}

		private class ChildHolder {
			public LinearLayout aggregate;
			public TextView title;
			public CheckBox cb;
			public TextView aggregateType;
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		WebServiceReferences.contextTable.remove("TriggerMain");
		super.onDestroy();
	}

}