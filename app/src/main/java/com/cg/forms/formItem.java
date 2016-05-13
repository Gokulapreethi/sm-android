package com.cg.forms;

import android.graphics.Bitmap;


public class formItem {
        Bitmap image;
        String title;
        String action;
        String id;
        String owner;

      
        public Bitmap getImage() {
                return image;
        }
        public void setImage(Bitmap image) {
                this.image = image;
        }
        public String getTitle() {
                return title;
        }
        public void setTitle(String title) {
                this.title = title;
        }
		public String getAction() {
			return action;
		}
		public void setAction(String action) {
			this.action = action;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getOwner() {
			return owner;
		}
		public void setOwner(String owner) {
			this.owner = owner;
		}
        

}