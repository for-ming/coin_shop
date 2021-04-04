package sourcecode.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import sourcecode.model.CustomerMySelf;
import sourcecode.model.Product;
import sourcecode.model.Customer;

public class BuyProductLayoutController implements Initializable {
	private Stage currentStage;
	
	@FXML private Text txtProductName;
	@FXML private Text txtCategory;
	@FXML private Text txtProductPrice;
	@FXML private Text txtShipmentName;
	@FXML private TextFlow txtProductInformation;
	@FXML private Text txtContractDate;
	@FXML private Text txtDeliveryDate;
	@FXML private ImageView imgProductImage;
	
	//private Product product;
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
		//callable statement
	}
	
	@FXML
	private void onBtnClickedBuyProductCancel(ActionEvent event) {
		currentStage.close();
	}
	
	public void setData(Product selectedProduct) {
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
}
