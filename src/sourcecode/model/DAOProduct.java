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
   }
   
   public static DAOProduct getInstance() {
      if(instance != null)
         return instance;
      else {
         instance = new DAOProduct();
         return instance;
      }
   }
   
   public void setAllProduct() {
      
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
   
   public ArrayList<Product> findByName(String sellerName) throws NullPointerException {
      ArrayList<Product> productByName = new ArrayList<Product>();
      try {
         for(Product p : products) {
            if(p.getSellerId().equals(sellerName)) {
               productByName.add(p);
            }
         }
      } catch(NullPointerException e) {
         System.out.println();
      }
      return productByName;
   }
}