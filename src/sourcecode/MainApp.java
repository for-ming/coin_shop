package sourcecode;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import sourcecode.controller.AcceptProductLayoutController;
import sourcecode.controller.BuyProductLayoutController;
import sourcecode.controller.DBConnection;
import sourcecode.controller.LoginLayoutController;
import sourcecode.controller.RegisterMemberDialogController;
import sourcecode.controller.RegisterProductLayoutController;
import sourcecode.model.CustomerMySelf;
import sourcecode.model.DAOCategory;
import sourcecode.model.DAOCompany;
import sourcecode.model.DAOProduct;
import sourcecode.model.Product;
import sourcecode.model.Category;
import sourcecode.model.Company;
import sourcecode.model.Customer;


import sourcecode.controller.RootLayoutController;
import sourcecode.controller.UpdateProductLayoutController;


public class MainApp extends Application {

	private Stage primaryStage;
	private Stage loginPopup;
	private BorderPane rootLayout;
	
	private LoginLayoutController loginController;
	
	private DAOCategory daoCategory;
	private DAOCompany daoCompany;
	private DAOProduct daoProduct;
	
	@Override
	public void start(Stage primaryStage) throws Exception {

		this.primaryStage = primaryStage;

		this.primaryStage.setTitle("CRUD JavaFX");

		primaryStage.getIcons()
				.add(new Image(getClass().getResourceAsStream("/resources/images/informacao-pessoal.png")));

		if (showLoginLayout()) {
			if (initRootLayout()) {

				System.out.println("데이터불러오기 성공");
			} else {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Unexpected error");
				alert.setContentText("An error occurred while trying to start the program");
				alert.showAndWait();
			}
		}

		loginPopup.setOnCloseRequest(event -> {
			terminate();
		});

	}

	public static void terminate() {
		Platform.exit();
	}

	public boolean initRootLayout() {

		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(MainApp.class.getResource("view/fxml/RootLayout.fxml"));
			rootLayout = (BorderPane) fxmlLoader.load();

			RootLayoutController rootController = fxmlLoader.getController();

			rootController.setMainApp(this);
			rootController.setDialogStage(primaryStage);
			rootController.lazyInitialize();
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);

			rootController.createSegment();

			// rootLayout.setCenter(rootController.getAllProduct());
			// rootLayout.setCenter(rootController.getMyProduct());
			// rootLayout.setCenter(rootController.getRankChart());
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean showLoginLayout() {
		loginPopup = new Stage(StageStyle.UTILITY);
		loginPopup.initModality(Modality.WINDOW_MODAL);
		loginPopup.initOwner(primaryStage);

		try {
			FXMLLoader fxmlloader = new FXMLLoader();
			fxmlloader.setLocation(MainApp.class.getResource("view/fxml/LoginLayout.fxml"));

			Parent loginLayout = fxmlloader.load();

			loginPopup.setScene(new Scene(loginLayout));
			loginPopup.setResizable(false);

			loginController = fxmlloader.getController();
			loginController.setMainApp(this);

			loginPopup.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		if (loginController.isLoginSuccess())
			return true;

		return true;
	}

	public boolean showCheckMySellPoductDialog(Product selectedProduct) {
		try {
			FXMLLoader loader = new FXMLLoader();

			loader.setLocation(MainApp.class.getResource("view/fxml/UpdateProductLayout.fxml"));

			AnchorPane page = (AnchorPane) loader.load();
			Stage stageBuyProduct = new Stage();
			stageBuyProduct.initOwner(primaryStage.getScene().getWindow());

			Scene scene = new Scene(page);
			stageBuyProduct.setScene(scene);
			stageBuyProduct.getIcons()
					.add(new Image(getClass().getResourceAsStream("/resources/images/adicionar.png")));

			UpdateProductLayoutController controller = loader.getController();
			controller.setDialogStage(stageBuyProduct);
			controller.setData(selectedProduct);
			controller.setMainApp(this);
			stageBuyProduct.showAndWait();
			procGetProductInfo();
			return true;

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean showAcceptProductDialog(Product selectedProduct) {
		try {
			FXMLLoader loader = new FXMLLoader();

			loader.setLocation(MainApp.class.getResource("view/fxml/AcceptProductLayout.fxml"));

			AnchorPane page = (AnchorPane) loader.load();
			Stage stageBuyProduct = new Stage();
			stageBuyProduct.initOwner(primaryStage.getScene().getWindow());

			Scene scene = new Scene(page);
			stageBuyProduct.setScene(scene);
			stageBuyProduct.getIcons()
					.add(new Image(getClass().getResourceAsStream("/resources/images/adicionar.png")));

			AcceptProductLayoutController controller = loader.getController();
			controller.setDialogStage(stageBuyProduct);
			controller.setData(selectedProduct);
			controller.setMainApp(this);
			stageBuyProduct.showAndWait();
			return true;

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean showBuyProductDialog(Product selectedProduct) {
		try {
			FXMLLoader loader = new FXMLLoader();

			loader.setLocation(MainApp.class.getResource("view/fxml/BuyProductLayout.fxml"));

			AnchorPane page = (AnchorPane) loader.load();
			Stage stageBuyProduct = new Stage();
			stageBuyProduct.initOwner(primaryStage.getScene().getWindow());

			Scene scene = new Scene(page);
			stageBuyProduct.setScene(scene);
			stageBuyProduct.getIcons()
					.add(new Image(getClass().getResourceAsStream("/resources/images/adicionar.png")));

			BuyProductLayoutController controller = loader.getController();
			controller.setDialogStage(stageBuyProduct);
			controller.setData(selectedProduct);
			controller.setMainApp(this);
			stageBuyProduct.showAndWait();
			procGetProductInfo();
			return true;
			// return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean showRegisterProductDialog() {
		try {
			FXMLLoader loader = new FXMLLoader();

			loader.setLocation(MainApp.class.getResource("view/fxml/RegisterProductLayout.fxml"));

			AnchorPane page = (AnchorPane) loader.load();
			Stage stageRegProduct = new Stage();
			stageRegProduct.initOwner(primaryStage.getScene().getWindow());

			Scene scene = new Scene(page);
			stageRegProduct.setScene(scene);
			stageRegProduct.getIcons()
					.add(new Image(getClass().getResourceAsStream("/resources/images/adicionar.png")));

			RegisterProductLayoutController controller = loader.getController();
			controller.setDialogStage(stageRegProduct);
			controller.setMainApp(this);
			stageRegProduct.showAndWait();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean procCallCustomerInfo(String strID) {
		CustomerMySelf customerMyself = CustomerMySelf.getInstance();
		OracleCallableStatement ocstmt = null;

		String runP = "{ call sp_get_customer_info(?, ?) }";
		try {
			Connection conn = DBConnection.getConnection();
			Statement stmt = conn.createStatement();
			CallableStatement callableStatement = conn.prepareCall(runP.toString());
			callableStatement.setString(1, strID);
			callableStatement.registerOutParameter(2, OracleTypes.CURSOR);
			callableStatement.executeUpdate();
			ocstmt = (OracleCallableStatement) callableStatement;

			ResultSet rs = ocstmt.getCursor(2);
			while (rs.next()) {
				String field1 = rs.getString(1);
				customerMyself.getCustomer().setId(rs.getInt("id"));
				customerMyself.getCustomer().setName(rs.getString("name"));
				customerMyself.getCustomer().setPassword(rs.getString("password"));
				customerMyself.getCustomer().setPhone(rs.getString("phone_number"));
				customerMyself.getCustomer().setZipcode(rs.getString("zipcode"));
				customerMyself.getCustomer().setVolunteer_time(rs.getInt("volunteer_working_time"));
				customerMyself.getCustomer().setCoin(rs.getInt("coin"));
				System.out.println(customerMyself.getCustomer().getName() + " 로그인 정보 동기화 완료");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;

	}
	   
	
	public boolean procGetCategoryInfo() {
		OracleCallableStatement ocstmt = null;

		daoCategory = DAOCategory.getInstance();
		String runP = "{ call get_category_info(?)}";

		try {
			Connection conn = DBConnection.getConnection();
			Statement stmt = conn.createStatement();
			CallableStatement callableStatement = conn.prepareCall(runP.toString());
			callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
			callableStatement.executeUpdate();
			ocstmt = (OracleCallableStatement) callableStatement;

			ResultSet rs = ocstmt.getCursor(1);
			while (rs.next()) {
				Category<Integer, String> categorys = new Category<>();
				categorys.setCategory(rs.getInt("id"), rs.getString("name"));
				System.out.println(categorys.getCategoryID() + " " + categorys.getCategoryName());
				daoCategory.addCategory(categorys);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean procGetCompanyInfo() {
		OracleCallableStatement ocstmt = null;

		daoCompany = DAOCompany.getInstance();
		String runP = "{ call get_company_info(?)}";

		try {
			Connection conn = DBConnection.getConnection();
			Statement stmt = conn.createStatement();
			CallableStatement callableStatement = conn.prepareCall(runP.toString());
			callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
			callableStatement.executeUpdate();
			ocstmt = (OracleCallableStatement) callableStatement;

			ResultSet rs = ocstmt.getCursor(1);
			while (rs.next()) {
				Company<Integer, String> companys = new Company<>();
				companys.setCompany(rs.getInt("id"), rs.getString("name"));
				System.out.println(companys.getCompanyID() + " " + companys.getCompanyName());
				daoCompany.addCompany(companys);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

		public boolean procGetProductInfo() {
			 OracleCallableStatement ocstmt = null;
			   
			   daoProduct = DAOProduct.getInstance();
			   daoProduct.refreshProduct();
			  // String runP = "{ call ALL_product(?)}"; -- ver. procedure
			   String runP = "{ call all_product_view_select(?)}"; // ver.view
			   
			   try {
				   Connection conn = DBConnection.getConnection();
				   Statement stmt = conn.createStatement();
				   CallableStatement callableStatement = conn.prepareCall(runP.toString());
				   callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
				   callableStatement.executeUpdate();	
				   ocstmt = (OracleCallableStatement)callableStatement;

				   ResultSet rs =  ocstmt.getCursor(1);
				   while (rs.next()) {
					   Product products = new Product();
					   products.setProductId(rs.getInt("id"));
					   products.setProductName(rs.getString("name"));
					   products.setInfo(rs.getString("information"));
					   products.setPrice(rs.getInt("price"));
					   products.setSellerId(rs.getString(5));
					   products.setCategoryName(rs.getString("category_name"));
					   products.setStatus(rs.getString("product_status"));
					   products.setShipmentCompanyName(rs.getString(8));
					   products.setBuyerId(rs.getString(9));
					   daoProduct.addProduct(products);
				   }
				   
			   } catch(Exception e) {
				   e.printStackTrace();
				   return false;
			   }
			   System.out.println("상품 정보 불러오기 완료!");
			   return true;
		}
		
		
	
	public static void main(String[] args) {
		launch(args);
	}

}