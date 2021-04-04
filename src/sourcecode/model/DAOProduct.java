package sourcecode.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import sourcecode.controller.DBConnection;


public class DAOProduct {

   static DAOProduct instance;
   private ArrayList<Product> products;
   private Connection conn = DBConnection.getConnection();
   
   private DAOProduct() {
	   products = new ArrayList<Product>();
   }
   
   public static DAOProduct getInstance() {
      if(instance != null)
         return instance;
      else {
         instance = new DAOProduct();
         return instance;
      }
   }
   
   public void refreshProduct() {
	   this.products.clear();
   }
   public void addProduct(Product product) {
	   products.add(product);
   }
   public ArrayList<Product> getProduct() {
	   return products;
   }
   
   public ArrayList<Product> findByCategory(String categoryName) throws NullPointerException {
      ArrayList<Product> productByCategory = new ArrayList<Product>();
      try {
         for(Product p : products) {
            if(p.getCategoryName().equals(categoryName)) {
               productByCategory.add(p);
            }
         }         
      } catch(NullPointerException e) {
         Alert alert = new Alert(AlertType.WARNING);
         alert.setTitle("Warning !!");
         alert.setHeaderText("등록된 제품이 없습니다 !");
         alert.showAndWait();
      }finally {
         
      }
      return productByCategory;
   }
   
   public ArrayList<Product> findBySeller(String sellerName) throws NullPointerException {
      ArrayList<Product> productBySeller = new ArrayList<Product>();
      try {
         for(Product p : products) {
            if(p.getSellerId().equals(sellerName)) {
            	productBySeller.add(p);
            }
         }
      } catch(NullPointerException e) {
    	  System.out.println("해당 고객의 판매제품이 없습니다");
    	  //Alert alert = new Alert(AlertType.WARNING);
          //alert.setTitle("Warning !!");
          //alert.setHeaderText("등록된 제품이 없습니다 !");
          //alert.showAndWait();
      }
      return productBySeller;
   }
   
	public ArrayList<Product> findByBuyer(String buyerName) throws NullPointerException {
		ArrayList<Product> productByBuyer = new ArrayList<Product>();
		try {
			for (Product p : products) {
				if (p.getBuyerId() == null)
					continue;
				else {
					if (p.getBuyerId().equals(buyerName)) {
						productByBuyer.add(p);
					}
				}
			}
		} catch (NullPointerException e) {
			System.out.println("해당 고객의 구매제품이 없습니다");
			// Alert alert = new Alert(AlertType.WARNING);
			// alert.setTitle("Warning !!");
			// alert.setHeaderText("등록된 제품이 없습니다 !");
			// alert.showAndWait();
		}
		return productByBuyer;
	}
   
   public ArrayList<Product> findByProductName(String productName) throws NullPointerException {
	   ArrayList<Product> productByProductName = new ArrayList<Product>();
	   
	   return productByProductName;
   }
}