package org.lib.model;

import java.io.Serializable;

public class UtilityBean implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String nameororg;

	private String product_name;

	private String qty;

	private String price;

	private String imag_filename;

	private String videofilename;

	private String audiofilename;

	private String address;

	private String country;

	private String state;

	private String cityordist;

	private String pin;

	private String email;

	private String c_no;

	private int id;

	private int mode;

	private String posted_date;

	private String username;

	private String location;

	private String type;

	private String utility_name;

	private String result;

	private int slideposition = 1;

	private String distance;

	private String latlong;

	private boolean isDownloading;
	private boolean isSelect;

	private boolean isResultshowing = false;

	private int resultcount = 0;

	private boolean addNewList;

	private Integer listPosition;

	private String cnCode;
	
	private String existingPostedDate;
	
	private String ownerName;
	

	public String getExistingPostedDate() {
		return existingPostedDate;
	}

	public void setExistingPostedDate(String existingPostedDate) {
		this.existingPostedDate = existingPostedDate;
	}

	public String getNameororg() {
		return nameororg;
	}

	public void setNameororg(String nameororg) {
		this.nameororg = nameororg;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getImag_filename() {
		return imag_filename;
	}

	public void setImag_filename(String imag_filename) {
		this.imag_filename = imag_filename;
	}

	public String getVideofilename() {
		return videofilename;
	}

	public void setVideofilename(String videofilename) {
		this.videofilename = videofilename;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCityordist() {
		return cityordist;
	}

	public void setCityordist(String cityordist) {
		this.cityordist = cityordist;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getC_no() {
		return c_no;
	}

	public void setC_no(String c_no) {
		this.c_no = c_no;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isAddNewList() {
		return addNewList;
	}

	public String getCnCode() {
		return cnCode;
	}

	public void setCnCode(String cnCode) {
		this.cnCode = cnCode;
	}

	public void setAddNewList(boolean addNewList) {
		this.addNewList = addNewList;
	}

	public Integer getListPosition() {
		return listPosition;
	}

	public void setListPosition(Integer listPosition) {
		this.listPosition = listPosition;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public String getPosted_date() {
		return posted_date;
	}

	public void setPosted_date(String posted_date) {
		this.posted_date = posted_date;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAudiofilename() {
		return audiofilename;
	}

	public void setAudiofilename(String audiofilename) {
		this.audiofilename = audiofilename;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUtility_name() {
		return utility_name;
	}

	public void setUtility_name(String utility_name) {
		this.utility_name = utility_name;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public int getSlideposition() {
		return slideposition;
	}

	public void setSlideposition(int slideposition) {
		this.slideposition = slideposition;
	}

	public boolean isDownloading() {
		return isDownloading;
	}

	public void setDownloading(boolean isDownloading) {
		this.isDownloading = isDownloading;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getLatlong() {
		return latlong;
	}

	public void setLatlong(String latlong) {
		this.latlong = latlong;
	}

	/**
	 * @return the isResultshowing
	 */
	public boolean isResultshowing() {
		return isResultshowing;
	}

	/**
	 * @param isResultshowing
	 *            the isResultshowing to set
	 */
	public void setResultshowing(boolean isResultshowing) {
		this.isResultshowing = isResultshowing;
	}

	/**
	 * @return the resultcount
	 */
	public int getResultcount() {
		return resultcount;
	}

	/**
	 * @param resultcount
	 *            the resultcount to set
	 */
	public void setResultcount(int resultcount) {
		this.resultcount = resultcount;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		try {
			return (UtilityBean) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

}
