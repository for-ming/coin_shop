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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
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
	private static final String[] arrColumnNaming = {"image", "productName", "price", "sellerName", "category", "status"}; 
    private static enum columnNamingIdx{image, productName, price, sellerName, category, status};
	@FXML private JFXComboBox<String> cbCategoryList;
    private DAOCategory categoryList;
    private CustomerMySelf myInfo;
    @FXML private TextField tfSearch;
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, String> columnImage;
    @FXML private TableColumn<Product, String> columnProductName;
    @FXML private TableColumn<Product, String> columnPrice;
    @FXML private TableColumn<Product, String> columnSellerName;
    @FXML private TableColumn<Product, String> columnCategory; 
    @FXML private TableColumn<Product, String> columnProductStatus; 
    @FXML private Label lblNote;  
    @FXML private Label lblError;
    
    private List<Product> defaultProductList = new ArrayList();
    private List<Product> currentProductList = new ArrayList();
    private String strCurrentCategory = "All";
    private ObservableList<Product> observableListProduct;
    
    MainApp mainApp;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
    	loadMyInfo();
    	loadProduct(false);
        loadCombobox();
        
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
    void actionRegisterProduct(ActionEvent event) {
    	mainApp.showRegisterProductDialog();
    	loadProduct(currentProductList);
    }

    @FXML
    private void onClickedTable(MouseEvent event) {
    	mainApp.showBuyProductDialog();
    }
    @FXML
    void actionSearch(ActionEvent event) {
    	String newSearchTest = tfSearch.getText();
    	if(strCurrentCategory.equals(newSearchTest)) {
    		return;
    	} else
    		strCurrentCategory = newSearchTest;
    	currentProductList.clear();
    	
    	try{
            if (strCurrentCategory.equals("All")){
            	loadProduct(true);
            }else{
            	for(Product p : defaultProductList) {
                	if(p.getCategoryName().equals(strCurrentCategory))
                		currentProductList.add(p);
                }
                loadProduct(currentProductList);
            }
        }catch(NumberFormatException ime){
            lblError.setText("Enter the valid value type");
        }catch(NullPointerException npe){
            lblError.setText("Enter some value");
        }
    	
    }
    
    @FXML
    void keyPressed(KeyEvent event) {
    	lblError.setText("");
            
    }
    
    @FXML
    void actionCombobox(ActionEvent event) {
    	String newCategoryText = cbCategoryList.getValue();
    	if(strCurrentCategory.equals(newCategoryText)) {
    		return;
    	}
    	strCurrentCategory = newCategoryText;
    	currentProductList.clear();
    	try{
            if (strCurrentCategory.equals("All")){
            	loadProduct(true);
            }else{
                for(Product p : defaultProductList) {
                	if(p.getCategoryName().equals(strCurrentCategory))
                		currentProductList.add(p);
                }
                loadProduct(currentProductList);
            }
        }catch(NumberFormatException ime){
            lblError.setText("Enter the valid value type");
        }catch(NullPointerException npe){
            lblError.setText("Enter some value");
        }
    }
    
   
    public boolean loadMyInfo() {
    	myInfo = CustomerMySelf.getInstance();
    	if(myInfo.getInstance() != null)
    		return true;
    	else return false;
    }
    
    public boolean loadProduct(boolean cleanTable){
        
        try {
            if(cleanTable) {
                cleanTable();
            }
            
            definingColumn();
            
            String strMyName = myInfo.getCustomer().getName();
            defaultProductList = DAOProduct.getInstance().findByName(strMyName);
            

            observableListProduct = FXCollections.observableArrayList(defaultProductList);
            productTable.setItems(observableListProduct);
            
        }catch(Exception e) {
            alert("Error", null, "An error occurred while retrieving data", Alert.AlertType.ERROR);
            return false;
        }
        
        return true;
    }
    
    
    public void loadProduct(List<Product> arrayListProduct) {
         try {
            cleanTable();
            observableListProduct = FXCollections.observableArrayList(arrayListProduct);
            productTable.setItems(observableListProduct);
        }catch(Exception e) {
            alert("Error", null, "An error occurred while retrieving data", Alert.AlertType.ERROR);
        }
    }
    
    public void definingColumn() {
        columnImage.setCellValueFactory(new PropertyValueFactory<>(arrColumnNaming[0]));
        columnProductName.setCellValueFactory(new PropertyValueFactory<>(arrColumnNaming[1]));
        columnPrice.setCellValueFactory(new PropertyValueFactory<>(arrColumnNaming[2]));
        columnSellerName.setCellValueFactory(new PropertyValueFactory<>(arrColumnNaming[3]));
        columnCategory.setCellValueFactory(new PropertyValueFactory<>(arrColumnNaming[4]));
        columnProductStatus.setCellValueFactory(new PropertyValueFactory<>(arrColumnNaming[5]));
    }

    private void cleanTable() {
        productTable.getItems().clear();
    }
    
    private void alert(String titulo, String headerText, String contentText, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setTitle(titulo);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
    
    public void loadCombobox() {
    	categoryList = DAOCategory.getInstance();
        List<String> values = new ArrayList<String>();
        int categorySize = categoryList.getCategorySize();
        
        values.add("All");
        for(int idx=0; idx<categorySize; idx++) {
        	values.add(categoryList.getCategory(idx).getCategoryName());
        }
        
        ObservableList<String> obsValues = FXCollections.observableArrayList(values);
        cbCategoryList.setItems(obsValues);
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
    
    public List<Product> getListMyProduct() {
        return defaultProductList;
    }

    public void setListPerson(List<Product> productList) {
        this.defaultProductList = productList;
    }
    
    
    public MainApp getMainApp() {
        return mainApp;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
   
}
