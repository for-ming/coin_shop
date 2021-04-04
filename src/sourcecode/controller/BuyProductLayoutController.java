package sourcecode.controller;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import org.apache.poi.ss.usermodel.DateUtil;

import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import sourcecode.model.CustomerMySelf;
import sourcecode.model.DAOProduct;
import sourcecode.model.Product;
import sourcecode.MainApp;
import sourcecode.model.Customer;

public class BuyProductLayoutController implements Initializable {
	private Stage currentStage;
	private MainApp mainApp;
	@FXML private Text txtProductName;
	@FXML private Text txtCategory;
	@FXML private Text txtProductPrice;
	@FXML private Text txtShipmentName;
	@FXML private TextFlow txtProductInformation;
	@FXML private Text txtContractDate;
	@FXML private Text txtDeliveryDate;
	@FXML private ImageView imgProductImage;
	
	private CustomerMySelf customerMyself = CustomerMySelf.getInstance();
	private Product product;
	
	
	/*
	 * public BuyProductLayoutController() {
	 * 
	 * }
	 * 
	 * public BuyProductLayoutController(Product product) {
	 * 
	 * }
	 */
	@Override
    public void initialize(URL url, ResourceBundle rb) {
		//get product info
		
		
		//Image productImage = new Image("URL");
		//imgProductImage.setImage(productImage);
	}
	
	@FXML 
	private void onBtnClickedBuyProductBuy(ActionEvent event) {
		if(product == null) return;
		
		Alert alert = new Alert(Alert.AlertType.ERROR);
		if(product.getPrice() > customerMyself.getCustomer().getCoin()) {
			alert.setTitle("Invalid Fields");
			alert.setHeaderText("your mileage is lower than product price");
			alert.showAndWait();
			currentStage.close();
		}
		if(procBuyProduct()){
			mainApp.procGetProductInfo();
			currentStage.close();
		} else {
			Alert alert1 = new Alert(Alert.AlertType.ERROR);
			alert1.setTitle("Invalid Fields");
			alert1.setHeaderText("product register do not work!");
			alert1.showAndWait();
		}
		
	}
	
	private boolean procBuyProduct() {
		  	
			Date date = new Date();
		    // 성공시 1 반환, 실패시 0 반환
			String runP = "{ call buy_item(?, ?, ?, ?, ?)}";
			System.out.println("구매 물품명 : "+ product.getProductName() + " " + customerMyself.getCustomer().getCoin());
		   try {
			   Connection conn = DBConnection.getConnection();
			   CallableStatement callableStatement = conn.prepareCall(runP.toString());
			   callableStatement.setInt(1, product.getProductId()); // 구매자 ID
			   callableStatement.setString(2, product.getSellerId()); // 판매자 ID
			   callableStatement.setInt(3, customerMyself.getCustomer().getId());
			   callableStatement.setDate(4, new java.sql.Date(date.getTime()));
			   callableStatement.registerOutParameter(5, java.sql.Types.INTEGER);
			   callableStatement.executeUpdate();	
				
				int check = callableStatement.getInt(5);
				if(check == 0) {
					System.out.println("구매 요청 실패");
					return false;	
				}
				else
					System.out.println(check+"구매 승인");
				
			   
		   } catch(Exception e) {
			   e.printStackTrace();
			   return false;
		   }
		   
		   int currentCustomerCoin = customerMyself.getCustomer().getCoin();
		   int productPrice = product.getPrice();
		   customerMyself.getCustomer().setCoin(currentCustomerCoin - productPrice);
		   mainApp.procGetProductInfo();
		   System.out.println("구매완료!");
		   return true;
	}
	@FXML
	private void onBtnClickedBuyProductCancel(ActionEvent event) {
		currentStage.close();
	}
	
	public void setData(Product selectedProduct) {
		product = selectedProduct;
		if(product == null) return;
		
		txtProductName.setText(selectedProduct.getProductName());
		txtCategory.setText(selectedProduct.getCategoryName());
		txtProductPrice.setText(Integer.toString(selectedProduct.getPrice()));
		txtShipmentName.setText(selectedProduct.getShipmentCompanyName());
		
		Text productInfo = new Text(selectedProduct.getInfo());
		txtProductInformation.getChildren().add(productInfo);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		String strDay = df.format(cal.getTime());
		cal.add(Calendar.DATE, +3);
		String strDelivery = df.format(cal.getTime());
		
		txtContractDate.setText(strDay);
		txtDeliveryDate.setText(strDelivery);
		
		
		try {
			Image productImage = new Image(selectedProduct.getImagePath());
			imgProductImage.setImage(productImage);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void setDialogStage(Stage dialogStage) {
		this.currentStage = dialogStage;
	}
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
