package sourcecode.controller;
import java.io.IOException;
import java.net.URL;

import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import sourcecode.MainApp;

import sourcecode.model.CustomerMySelf;
import sourcecode.model.DAOCategory;
import sourcecode.model.DAOProduct;
import sourcecode.model.Product;
import sourcecode.model.Customer;

public class MyProductLayoutController implements Initializable {
	MainApp mainApp;

    //tabPane
    @FXML private TabPane tabpaneMyProduct;
  
    //buyTab
    @FXML private Tab tabBuy;
    @FXML private TableView<Product> buyProductTable;
    @FXML private TableColumn<Product, String> buyColumnImage;
    @FXML private TableColumn<Product, String> buyColumnProductName;
    @FXML private TableColumn<Product, String> buyColumnPrice;
    @FXML private TableColumn<Product, String> buyColumnSellerName;
    @FXML private TableColumn<Product, String> buyColumnCategory; 
    @FXML private TableColumn<Product, String> buyColumnProductStatus; 
    //
    //selTab
    @FXML private Tab tabSel;
    @FXML private TableView<Product> selProductTable;
    @FXML private TableColumn<Product, String> selColumnImage;
    @FXML private TableColumn<Product, String> selColumnProductName;
    @FXML private TableColumn<Product, String> selColumnPrice;
    @FXML private TableColumn<Product, String> selColumnCategory; 
    @FXML private TableColumn<Product, String> selColumnProductStatus; 
    //
    private CustomerMySelf myInfo;
   
    private List<Product> currentProductList_sel = new ArrayList();
    private List<Product> currentProductList_buy = new ArrayList();
    private ObservableList<Product> observableListProduct_sel;
    private ObservableList<Product> observableListProduct_buy;
    
   
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {    	
    	tabSel.setOnSelectionChanged(event -> {
    		if(tabSel.isSelected()) {
    			definingColumn_sel();
    			loadProduct_sel(true);
    			System.out.println("tabSel selected");
    		}
    	});
    	
    	tabBuy.setOnSelectionChanged(event -> {
    		if(tabBuy.isSelected()) {
    			definingColumn_buy();
    			loadProduct_buy(true);
    			System.out.println("tabBuy selected");
    		}
    	});
    	
    	
    	//loadProduct(false);
        
		/*
		 * try{ productTable.getSelectionModel().selectedItemProperty().addListener(
		 * (observable, oldValue, newValue) -> showNote(newValue));
		 * 
		 * //loadAutoComplete();
		 * 
		 * }catch(NullPointerException npe){ lblNote.setText(""); }
		 */
    }    
    
    

    @FXML
    private void onClickedSelTable(MouseEvent event) {
    	Product selectedProduct = selProductTable.getSelectionModel().getSelectedItem();
    	if(selectedProduct == null) {
    		Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning !!");
            alert.setHeaderText("등록된 제품이 없습니다 !");
            alert.showAndWait();
    		return;
    	}
    	mainApp.showCheckMySellPoductDialog(selectedProduct);
    	loadProduct_sel(true);
    	System.out.println("clicked seltable");
    }
    
    @FXML
    private void onClickedBuyTable(MouseEvent event) {
    	Product selectedProduct = buyProductTable.getSelectionModel().getSelectedItem();
    	if(selectedProduct == null) {
    		Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning !!");
            alert.setHeaderText("등록된 제품이 없습니다 !");
            alert.showAndWait();
    		return;
    	}
    	mainApp.showAcceptProductDialog(selectedProduct);
    	loadProduct_buy(true);
    	System.out.println("clicked buytable");
    }
    
  
   
    @FXML
    void keyPressed(KeyEvent event) {
    	
            
    }
    
    public boolean loadMyInfo() {
    	myInfo = CustomerMySelf.getInstance();
    	if(myInfo.getInstance() != null)
    		return true;
    	else return false;
    }
    
    public boolean loadProduct_sel(boolean cleanTable){
        
        try {
            if(cleanTable) {
                cleanTable_sel();
            }
            
            definingColumn_sel();
            
            String strMyName = myInfo.getCustomer().getName();
            currentProductList_sel = DAOProduct.getInstance().findBySeller(strMyName);
            

            observableListProduct_sel = FXCollections.observableList(currentProductList_sel);
            selProductTable.setItems(observableListProduct_sel);
            
        }catch(Exception e) {
            alert("Error", null, "An error occurred while retrieving data", Alert.AlertType.ERROR);
            return false;
        }
        
        return true;
    }
    
    public boolean loadProduct_buy(boolean cleanTable){
        
        try {
            if(cleanTable) {
                cleanTable_buy();
            }
            
            definingColumn_buy();
            
            String strMyName = myInfo.getCustomer().getName();
            currentProductList_buy = DAOProduct.getInstance().findByBuyer(strMyName);
            

            observableListProduct_buy = FXCollections.observableList(currentProductList_buy);
            buyProductTable.setItems(observableListProduct_buy);
            
        }catch(Exception e) {
            alert("Error", null, "An error occurred while retrieving data", Alert.AlertType.ERROR);
            return false;
        }
        
        return true;
    }
    
    /*
    public void loadProduct_sel(List<Product> productList) {
         try {
            cleanTable_sel();
            observableListProduct_sel = FXCollections.observableArrayList(productList);
            selProductTable.setItems(observableListProduct_sel);
        }catch(Exception e) {
            alert("Error", null, "An error occurred while retrieving data", Alert.AlertType.ERROR);
        }
    }
    
    public void loadProduct_buy(List<Product> productList) {
        try {
           cleanTable_buy();
           observableListProduct_buy = FXCollections.observableArrayList(productList);
           buyProductTable.setItems(observableListProduct_buy);
       }catch(Exception e) {
           alert("Error", null, "An error occurred while retrieving data", Alert.AlertType.ERROR);
       }
   }
   */
    public void definingColumn_sel() {
    	selColumnImage.setCellValueFactory(new PropertyValueFactory<Product, String>("imagePath"));
        selColumnProductName.setCellValueFactory(new PropertyValueFactory<Product, String>("productName"));
        selColumnPrice.setCellValueFactory(new PropertyValueFactory<Product, String>("price"));
        selColumnCategory.setCellValueFactory(new PropertyValueFactory<Product, String>("categoryName"));
        selColumnProductStatus.setCellValueFactory(new PropertyValueFactory<Product, String>("status"));
    }
    
    public void definingColumn_buy() {
    	buyColumnImage.setCellValueFactory(new PropertyValueFactory<Product, String>("imagePath"));
    	buyColumnProductName.setCellValueFactory(new PropertyValueFactory<Product, String>("productName"));
    	buyColumnPrice.setCellValueFactory(new PropertyValueFactory<Product, String>("price"));
    	buyColumnSellerName.setCellValueFactory(new PropertyValueFactory<Product, String>("sellerId"));
    	buyColumnCategory.setCellValueFactory(new PropertyValueFactory<Product, String>("categoryName"));
    	buyColumnProductStatus.setCellValueFactory(new PropertyValueFactory<Product, String>("status"));
    }

    private void cleanTable_sel() {
        selProductTable.getItems().clear();
    }
    private void cleanTable_buy() {
        buyProductTable.getItems().clear();
    }
    
    private void alert(String titulo, String headerText, String contentText, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setTitle(titulo);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
    

    

    public void loadAutoComplete() {
        
        // Variables for autosuggestion :)
        AutoCompletionBinding<String> acb;
        Set<String> ps;
        
        ArrayList<String> values = new ArrayList<String>();
		/*
		 * for (int i = 0; i < listPerson.size(); i++){
		 * values.add(listPerson.get(i).getName());
		 * values.add(listPerson.get(i).getAddress());
		 * values.add(listPerson.get(i).getEmail());
		 * values.add(listPerson.get(i).getBirthday());
		 * values.add(listPerson.get(i).getNumber()); }
		 
        
        
        String[] _possibleSuggestions = values.toArray(new String[0]);
        ps = new HashSet<>(Arrays.asList(_possibleSuggestions));
        TextFields.bindAutoCompletion(txtSearch, _possibleSuggestions);
        */
    }
    
    public void showNote(Customer person) {
        //lblNote.setText(person.getNote());
    }
    

    
    
    public MainApp getMainApp() {
        return mainApp;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
   
}
