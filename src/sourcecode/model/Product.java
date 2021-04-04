package sourcecode.model;

import javax.persistence.Column;

public class Product {
	 private int intProductId;
	 private String strSellerId;
	 private String strBuyerId;
	 private int intPrice;
	 
	 private String strName;
	 private String strInformation;
	 private String strStatus; // READY, ORDER
	 private String strCategoryName;
	 private String strShipmentCompanyName;
	 private String strImagePath; 

	 
	 public Product() {  
			this.strImagePath = "C:/";
			this.strInformation = "";
		}

		public String getBuyerId() {
			return strBuyerId;
		}

		public void setBuyerId(String id) {
			this.strBuyerId = id;
		}

		public int getProductId() {
			return intProductId;
		}

		public void setProductId(int id) {
			this.intProductId = id;
		}

		public String getSellerId() {
			return strSellerId;
		}

	 public void setSellerId(String id) {
	        this.strSellerId = id;
	    } 
	 
	 public int getPrice() {
	        return intPrice;
	    } 

	 public void setPrice(int price) {
	        this.intPrice = price;
	    }
	 
	 public String getProductName() {
		 return strName;
	 }
	 
	 public void setProductName(String name) {
		 this.strName = name;
	 }
	 
	 public String getInfo() {
		 return strInformation;
	 }
	 
	 public void setInfo(String information) {
		 this.strInformation = information;
	 }
	 
	 public String getStatus() {
		 return strStatus;
	 }
	 
	 public void setStatus(String status) {
		 this.strStatus = status;
	 }
	 
	 public String getCategoryName() {
		 return strCategoryName;
	 }
	 
	 public void setCategoryName(String category) {
		 this.strCategoryName = category;
	 }
	 
	 public String getShipmentCompanyName() {
		 return strShipmentCompanyName;
	 }
	 
	 public void setShipmentCompanyName(String shipment_company) {
		 this.strShipmentCompanyName = shipment_company;
	 }
	 
	 public String getImagePath() {
		 return strImagePath;
	 }
	 
	 public void setImagePath(String path) {
		 this.strImagePath = path;
	 }
	 
}