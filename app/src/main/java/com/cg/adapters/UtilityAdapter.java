package com.cg.adapters;


//public class UtilityAdapter extends BaseAdapter {
//	// LayoutInflater mInflater;
//	Vector<UtilityBean> list;
//
//	Context context;
//
//	public UtilityAdapter(Context context, Vector<UtilityBean> list2) {
//		// TODO Auto-generated constructor stub
//		this.list = list2;
//		this.context = context;
//	}
//
//	@Override
//	public int getCount() {
//		// TODO Auto-generated method stub
//		return list.size();
//	}
//
//	@Override
//	public Object getItem(int arg0) {
//		// TODO Auto-generated method stub
//		return arg0;
//	}
//
//	@Override
//	public long getItemId(int arg0) {
//		// TODO Auto-generated method stub
//		return arg0;
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup arg2) {
//		try {
//			final ViewHolder holder;
//			convertView = null;
//			if (convertView == null) {
//				holder = new ViewHolder();
//				holder.bean = (UtilityBean) list.get(position);
//
//				holder.mInflater = (LayoutInflater) context
//						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//				convertView = holder.mInflater.inflate(
//						R.layout.utility_adapter, null);
//
//				holder.linearLayout = (LinearLayout) convertView
//						.findViewById(R.id.LinearLayout2);
//				holder.linearLayout.setTag(position);
//
//				holder.save = (TextView) convertView.findViewById(R.id.save);
//				holder.save.setTag(position);
//				holder.delete = (TextView) convertView
//						.findViewById(R.id.Delete);
//				holder.delete.setTag(position);
//
//				holder.camera = (ImageView) convertView
//						.findViewById(R.id.photo);
//				holder.video = (ImageView) convertView.findViewById(R.id.video);
//				holder.mic = (ImageView) convertView.findViewById(R.id.mic);
//
//				// buyer
//				holder.buyer = (EditText) convertView
//						.findViewById(R.id.BuyerNameET);
//				holder.buyer.setTag(position);
//				holder.buyer.setText(list.get(position).getUsername()
//						.toString());
//				// holder.buyer.setFocusable(true);
//				// UtilityActivity.list.add(saveBean.setBuyerName(list.get(position).getBuyerName().toString()));
//
//				// product
//				holder.product = (EditText) convertView
//						.findViewById(R.id.ProductNameET);
//				holder.product.setTag(position);
//				holder.product.setText(list.get(position).getProduct_name()
//						.toString());
//				// holder.product.setFocusable(true);
//
//				// quantity
//				holder.quantity = (EditText) convertView
//						.findViewById(R.id.QuantityET);
//				holder.quantity.setTag(position);
//				holder.quantity.setText(list.get(position).getQty().toString());
//
//				// price
//				holder.price = (EditText) convertView
//						.findViewById(R.id.productPrice);
//				holder.price.setTag(position);
//				holder.price.setText(list.get(position).getPrice().toString());
//
//				// location
//				holder.location = (EditText) convertView
//						.findViewById(R.id.location);
//				holder.location.setTag(position);
//				holder.location.setText(list.get(position).getLocation()
//						.toString());
//
//				// address
//				holder.address = (EditText) convertView
//						.findViewById(R.id.Address);
//				holder.address.setTag(position);
//				holder.address.setText(list.get(position).getAddress()
//						.toString());
//
//				// country
//				holder.country = (EditText) convertView
//						.findViewById(R.id.country);
//				holder.country.setTag(position);
//				holder.country.setText(list.get(position).getCountry()
//						.toString());
//
//				// state
//				holder.state = (EditText) convertView.findViewById(R.id.State);
//				holder.state.setTag(position);
//				holder.state.setText(list.get(position).getState().toString());
//
//				// city
//				holder.city = (EditText) convertView
//						.findViewById(R.id.District);
//				holder.city.setTag(position);
//				holder.city.setText(list.get(position).getCityordist()
//						.toString());
//
//				// pin
//				holder.pin = (EditText) convertView.findViewById(R.id.pin);
//				holder.pin.setTag(position);
//				holder.pin.setText(list.get(position).getPin().toString());
//
//				// email
//				holder.email = (EditText) convertView.findViewById(R.id.Email);
//				holder.email.setTag(position);
//				holder.email.setText(list.get(position).getEmail().toString());
//
//				// cnCode
//				holder.cnCode = (EditText) convertView.findViewById(R.id.code);
//				holder.cnCode.setTag(position);
//				holder.cnCode
//						.setText(list.get(position).getCnCode().toString());
//
//				// cnNumber
//				holder.cnNumber = (EditText) convertView
//						.findViewById(R.id.codaNumber);
//				holder.cnNumber.setTag(position);
//				holder.cnNumber
//						.setText(list.get(position).getC_no().toString());
//
//				convertView.setTag(holder);
//
//			} else {
//				holder = (ViewHolder) convertView.getTag();
//			}
//
//			int buyer_position = (Integer) holder.buyer.getTag();
//			holder.buyer.setId(buyer_position);
//
//			int product_position = (Integer) holder.product.getTag();
//			holder.product.setId(product_position);
//
//			int quantity_position = (Integer) holder.quantity.getTag();
//			holder.quantity.setId(quantity_position);
//
//			int price_position = (Integer) holder.price.getTag();
//			holder.price.setId(price_position);
//
//			int location_position = (Integer) holder.location.getTag();
//			holder.location.setId(location_position);
//
//			int address_position = (Integer) holder.address.getTag();
//			holder.address.setId(address_position);
//
//			int country_position = (Integer) holder.country.getTag();
//			holder.country.setId(country_position);
//
//			int state_position = (Integer) holder.state.getTag();
//			holder.state.setId(state_position);
//
//			int city_position = (Integer) holder.city.getTag();
//			holder.city.setId(city_position);
//
//			int pin_position = (Integer) holder.pin.getTag();
//			holder.pin.setId(pin_position);
//
//			int email_position = (Integer) holder.email.getTag();
//			holder.email.setId(email_position);
//
//			int cnCode_position = (Integer) holder.cnCode.getTag();
//			holder.cnCode.setId(cnCode_position);
//
//			int cnNumber_position = (Integer) holder.cnNumber.getTag();
//			holder.cnNumber.setId(cnNumber_position);
//
//			TextWatcher textWatcher = new TextWatcher() {
//
//				@Override
//				public void onTextChanged(CharSequence s, int start,
//						int before, int count) {
//					// TODO Auto-generated method stub
//					final int position2 = holder.buyer.getId();
//					final EditText buyer1 = (EditText) holder.buyer;
//					if (buyer1.getText().toString().length() > 0) {
//						holder.bean.setUsername(buyer1.getText().toString());
//						list.set(position2, holder.bean);
//					} else {
//						holder.bean.setUsername("");
//						list.set(position2, holder.bean);
//					}
//
//					final int position3 = holder.product.getId();
//					final EditText product = (EditText) holder.product;
//					if (product.getText().toString().length() > 0) {
//						holder.bean.setProduct_name(product.getText()
//								.toString());
//						list.set(position3, holder.bean);
//					} else {
//						holder.bean.setProduct_name("");
//						list.set(position3, holder.bean);
//					}
//
//					final int position4 = holder.quantity.getId();
//					final EditText quantity = (EditText) holder.quantity;
//					if (quantity.getText().toString().length() > 0) {
//						holder.bean.setQty(quantity.getText().toString());
//						list.set(position4, holder.bean);
//					} else {
//						holder.bean.setQty("");
//						list.set(position4, holder.bean);
//					}
//
//					final int position5 = holder.price.getId();
//					final EditText price = (EditText) holder.price;
//					if (price.getText().toString().length() > 0) {
//						holder.bean.setPrice(price.getText().toString());
//						list.set(position5, holder.bean);
//					} else {
//						holder.bean.setPrice("");
//						list.set(position5, holder.bean);
//					}
//
//					final int position6 = holder.location.getId();
//					final EditText location = (EditText) holder.location;
//					if (location.getText().toString().length() > 0) {
//						holder.bean.setLocation(location.getText().toString());
//						list.set(position6, holder.bean);
//					} else {
//						holder.bean.setLocation("");
//						list.set(position6, holder.bean);
//					}
//
//					final int position7 = holder.address.getId();
//					final EditText address = (EditText) holder.address;
//					if (address.getText().toString().length() > 0) {
//						holder.bean.setAddress(address.getText().toString());
//						list.set(position7, holder.bean);
//					} else {
//						holder.bean.setAddress("");
//						list.set(position7, holder.bean);
//					}
//
//					final int position8 = holder.country.getId();
//					final EditText country = (EditText) holder.country;
//					if (country.getText().toString().length() > 0) {
//						holder.bean.setCountry(country.getText().toString());
//						list.set(position8, holder.bean);
//					} else {
//						holder.bean.setCountry("");
//						list.set(position8, holder.bean);
//					}
//
//					final int position9 = holder.state.getId();
//					final EditText state = (EditText) holder.state;
//					if (state.getText().toString().length() > 0) {
//						holder.bean.setState(state.getText().toString());
//						list.set(position9, holder.bean);
//					} else {
//						holder.bean.setState("");
//						list.set(position9, holder.bean);
//					}
//
//					final int position10 = holder.city.getId();
//					final EditText city = (EditText) holder.city;
//					if (city.getText().toString().length() > 0) {
//						holder.bean.setCityordist(city.getText().toString());
//						list.set(position10, holder.bean);
//					} else {
//						holder.bean.setCityordist("");
//						list.set(position10, holder.bean);
//					}
//
//					final int position11 = holder.pin.getId();
//					final EditText pin = (EditText) holder.pin;
//					if (pin.getText().toString().length() > 0) {
//						holder.bean.setPin(pin.getText().toString());
//						list.set(position11, holder.bean);
//					} else {
//						holder.bean.setPin("");
//						list.set(position11, holder.bean);
//					}
//
//					final int position12 = holder.email.getId();
//					final EditText email = (EditText) holder.email;
//					if (email.getText().toString().length() > 0) {
//						holder.bean.setEmail(email.getText().toString());
//						list.set(position12, holder.bean);
//					} else {
//						holder.bean.setEmail("");
//						list.set(position12, holder.bean);
//					}
//
//					final int position13 = holder.cnCode.getId();
//					final EditText cnCode = (EditText) holder.cnCode;
//					if (cnCode.getText().toString().length() > 0) {
//						holder.bean.setCnCode(cnCode.getText().toString());
//						list.set(position13, holder.bean);
//					} else {
//						holder.bean.setCnCode("");
//						list.set(position13, holder.bean);
//					}
//
//					final int position14 = holder.cnNumber.getId();
//					final EditText cnNumber = (EditText) holder.cnNumber;
//					if (cnNumber.getText().toString().length() > 0) {
//						holder.bean.setC_no(cnNumber.getText().toString());
//						list.set(position14, holder.bean);
//					} else {
//						holder.bean.setC_no("");
//						list.set(position14, holder.bean);
//					}
//				}
//
//				@Override
//				public void beforeTextChanged(CharSequence s, int start,
//						int count, int after) {
//					// TODO Auto-generated method stub
//				}
//
//				@Override
//				public void afterTextChanged(Editable s) {
//					// TODO Auto-generated method stub
//				}
//			};
//			holder.buyer.addTextChangedListener(textWatcher);
//			holder.product.addTextChangedListener(textWatcher);
//			holder.quantity.addTextChangedListener(textWatcher);
//			holder.price.addTextChangedListener(textWatcher);
//			holder.location.addTextChangedListener(textWatcher);
//			holder.address.addTextChangedListener(textWatcher);
//			holder.country.addTextChangedListener(textWatcher);
//			holder.state.addTextChangedListener(textWatcher);
//			holder.city.addTextChangedListener(textWatcher);
//			holder.pin.addTextChangedListener(textWatcher);
//			holder.email.addTextChangedListener(textWatcher);
//			holder.cnCode.addTextChangedListener(textWatcher);
//			holder.cnNumber.addTextChangedListener(textWatcher);
//
//			holder.save.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					// holder.bean2 = new DataBean();
//					// holder.bean2 = holder.bean;
//
//					final int layoutPosition = holder.linearLayout.getId();
//					// for (int i = 0; i < MainActivity.staticlist.size(); i++)
//					// {
//					// if (MainActivity.staticlist.contains(null)) {
//					// MainActivity.staticlist.addAll(list);
//					if (!holder.bean.getListPosition().equals(0)
//							&& holder.bean.isAddNewList() == false) {
//
//						int pos = holder.bean.getListPosition();
//						UtilityBuyerNew.staticlist.set(pos, holder.bean);
//
//					} else if (!holder.bean.getListPosition().equals(0)
//							&& holder.bean.isAddNewList() == true) {
//
//						holder.bean.setAddNewList(false);
//						holder.bean.setListPosition(UtilityBuyerNew.staticlist
//								.size());
//						Log.i("log",
//								"adapterSide"
//										+ UtilityBuyerNew.staticlist.size());
//						UtilityBuyerNew.staticlist.add(holder.bean);
//
//					} else if (holder.bean.getListPosition().equals(0)
//							&& holder.bean.isAddNewList() == false) {
//
//						int pos = holder.bean.getListPosition();
//						UtilityBuyerNew.staticlist.set(pos, holder.bean);
//
//					} else if (holder.bean.getListPosition().equals(0)
//							&& holder.bean.isAddNewList() == true) {
//
//						UtilityBuyerNew.staticlist.add(holder.bean);
//						holder.bean.setAddNewList(false);
//					}
//					Toast.makeText(context, "Data's Saved Successfully",
//							Toast.LENGTH_SHORT).show();
//				}
//			});
//			holder.delete.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					// int index = Integer.parseInt(v.getTag().toString());
//				}
//			});
//			holder.camera.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View view) {
//					// TODO Auto-generated method stub
//					Toast.makeText(context, "Photo", Toast.LENGTH_SHORT).show();
//				}
//			});
//			holder.video.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					Toast.makeText(context, "Video", Toast.LENGTH_SHORT).show();
//				}
//			});
//			holder.mic.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					Toast.makeText(context, "Mic", Toast.LENGTH_SHORT).show();
//				}
//			});
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return convertView;
//	}
//
//	public class ViewHolder {
//		ImageView camera, video, mic;
//		LinearLayout linearLayout;
//		UtilityBean bean;
//		LayoutInflater mInflater;
//		EditText buyer, product, quantity, price, location, address, country,
//				state, city, pin, email, cnCode, cnNumber;
//		TextView save, delete;
//	}
//
//	
//
//}
