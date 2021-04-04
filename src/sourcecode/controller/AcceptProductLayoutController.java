package sourcecode.controller;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import sourcecode.MainApp;
import sourcecode.model.DAOProduct;
import sourcecode.model.Product;

public class AcceptProductLayoutController implements Initializable{
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
	private void onBtnClickedDecideProduct(ActionEvent event) {
		//callable statement
		if(procProductAccept()) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("수령 확인");
			alert.setHeaderText("수령 확인되었습니다! 100마일리지 지급");
			alert.showAndWait();
			currentStage.close();
		}
		mainApp.procGetProductInfo();
	}
	
	private boolean procProductAccept() {
		
		String runP = "{ call receipt_click(?) }";
		System.out.println(product.getProductName()+"수령완료");
		try {
			Connection conn = DBConnection.getConnection();
			CallableStatement callableStatement = conn.prepareCall(runP.toString());
			callableStatement.setInt(1, product.getProductId());
			callableStatement.executeUpdate();
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	@FXML 
	private void onBtnClickedReturnProductBuy(ActionEvent event) {
		//callable statement
		System.out.println("반품 신청 (현재 이용할 수 없음)");
	}
	
	
	@FXML
	private void onBtnClickedBuyProductCancel(ActionEvent event) {
		currentStage.close();
	}
	
	public void setData(Product selectedProduct) {
		product = selectedProduct;
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
