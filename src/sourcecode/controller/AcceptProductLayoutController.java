package sourcecode.controller;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import sourcecode.MainApp;
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
	private void onBtnClickedDecideProduct(ActionEvent event) {
		//callable statement
	}
	
	@FXML 
	private void onBtnClickedReturnProductBuy(ActionEvent event) {
		//callable statement
		System.out.println("반품합니다");
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
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
