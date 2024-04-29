/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package creditcardprocessing;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author maria
 */
public class CRUDController implements Initializable {

    @FXML
    private TextField accno;

    @FXML
    private Button addbtn;

    @FXML
    private TextField cardholder;

    @FXML
    private TextField cardtype;

    @FXML
    private Button clearbtn;

    @FXML
    private TableColumn<CreditCardData, String> col_accno;

    @FXML
    private TableColumn<CreditCardData, String> col_cardtype;

    @FXML
    private TableColumn<CreditCardData, String> col_email;

    @FXML
    private TableColumn<CreditCardData, String> col_holdername;

    @FXML
    private TableColumn<CreditCardData, String> col_phno;

    @FXML
    private Button delbtn;

    @FXML
    private TextField email;

    @FXML
    private TextField phno;

    @FXML
    private TableView<CreditCardData> table_view;

    @FXML
    private Button updatebtn;

    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    private Alert alert;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.matches();
}
    
    public void creditCardAddBtn(){
        
        connect = database.connectDB();
        try{
            if(accno.getText().isEmpty()
                    || cardtype.getText().isEmpty()
                    || cardholder.getText().isEmpty()
                    || phno.getText().isEmpty()
                    || email.getText().isEmpty()){
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Messasge");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
              
            }
            else if(accno.getText().length()!=10){
                 alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Messasge");
                alert.setHeaderText(null);
                alert.setContentText("Enter valid account number of length 10");
                alert.showAndWait();
            }
            else if(phno.getText().length()!=10){
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Messasge");
                alert.setHeaderText(null);
                alert.setContentText("Enter valid phone number of length 10");
                alert.showAndWait();
            }
             else if (!validate(email.getText())){
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Messasge");
                alert.setHeaderText(null);
                alert.setContentText("Enter valid Email id");
                alert.showAndWait();
             }
            else{
                String checkData = "SELECT accno FROM carddetails WHERE accno = "
                    + accno.getText();
                prepare = connect.prepareStatement(checkData);
                result = prepare.executeQuery();
        
                if(result.next()){
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Account number: "+accno.getText()+"is already present");
                    alert.showAndWait();
                }else{
                    String insertData = "INSERT INTO carddetails(accno,cardtype,name,phno,email)"
                            + "VALUES(?,?,?,?,?)";
                    prepare = connect.prepareStatement(insertData);
                    prepare.setString(1,accno.getText());
                    prepare.setString(2,cardtype.getText());
                    prepare.setString(3,cardholder.getText());
                    prepare.setString(4,phno.getText());
                    prepare.setString(5,email.getText());
                    
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully added!");
                    alert.showAndWait();
                    
                    prepare.executeUpdate();
                    
                    creditShowData();  // To update the table view
                    creditCardClearBtn(); // To clear all fields
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void creditCardUpdateBtn(){
        
        connect = database.connectDB();
        
        try{
            if(accno.getText().isEmpty()
                    || cardtype.getText().isEmpty()
                    || cardholder.getText().isEmpty()
                    || phno.getText().isEmpty()
                    || email.getText().isEmpty()){
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Messasge");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }
            else{
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Messasge");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to update account number"+accno.getText()+"?");
                Optional<ButtonType>option = alert.showAndWait();
                
                if(option.get().equals(ButtonType.OK)){
                    String updateData = "UPDATE carddetails SET "
                        + "cardtype = '"+cardtype.getText()
                        + "',name = '"+cardholder.getText()
                        + "',phno = '"+phno.getText()
                        + "',email = '"+email.getText()
                        + "'WHERE accno = "+accno.getText();
                    prepare = connect.prepareStatement(updateData);
                    prepare.executeUpdate();
                    creditShowData();  // To update the table view
                    creditCardClearBtn(); // To clear all fields
                }
                else{
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Information Messasge");
                    alert.setHeaderText(null);
                    alert.setContentText("Cancelled...");
                    alert.showAndWait();
                }
                
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void creditCardDeleteBtn(){
        connect = database.connectDB();
        
        try{
            if(accno.getText().isEmpty()
                    || cardtype.getText().isEmpty()
                    || cardholder.getText().isEmpty()
                    || phno.getText().isEmpty()
                    || email.getText().isEmpty()){
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Messasge");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }
            else if(phno.getText().length()!=10){
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Messasge");
                alert.setHeaderText(null);
                alert.setContentText("Enter valid phone number of length 10");
                alert.showAndWait();
            }
             else if (!validate(email.getText())){
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Messasge");
                alert.setHeaderText(null);
                alert.setContentText("Enter valid Email id");
                alert.showAndWait();
             }
            else{
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Messasge");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete "+accno.getText()+"?");
                Optional<ButtonType>option = alert.showAndWait();
                
                if(option.get().equals(ButtonType.OK)){
                    String updateData = "DELETE FROM carddetails WHERE accno = "+accno.getText();
                    prepare = connect.prepareStatement(updateData);
                    prepare.executeUpdate();
                    creditShowData();  // To update the table view
                    creditCardClearBtn(); // To clear all fields
                }
                else{
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Information Messasge");
                    alert.setHeaderText(null);
                    alert.setContentText("Cancelled...");
                    alert.showAndWait();
                }
                
            }
        }
        catch(SQLException e){
        }
    }
    
    public void creditCardClearBtn(){
        accno.setText("");
        cardtype.setText("");
        cardholder.setText("");
        phno.setText("");
        email.setText("");
    }

    public ObservableList<CreditCardData> CreditCardListData() {
        ObservableList<CreditCardData> listData = FXCollections.observableArrayList();
        String selectData = "SELECT * FROM carddetails";
        connect = database.connectDB();
        try {
            prepare = connect.prepareStatement(selectData);
            result = prepare.executeQuery();

            CreditCardData cData;
            while (result.next()) {
                cData = new CreditCardData(result.getInt("accno"), result.getString("cardtype"), result.getString("name"), result.getInt("phno"), result.getString("email"));
                listData.add(cData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<CreditCardData> CreditCardData;
    public void creditShowData(){
        CreditCardData = CreditCardListData();
        col_accno.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        col_cardtype.setCellValueFactory(new PropertyValueFactory<>("cardType"));
        col_holdername.setCellValueFactory(new PropertyValueFactory<>("cardHolderName"));
        col_phno.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("emailId"));
    
        table_view.setItems(CreditCardData);
    }
    @SuppressWarnings("empty-statement")
    public void creditSelectData(){
        CreditCardData cData = table_view.getSelectionModel().getSelectedItem();
        int num = table_view.getSelectionModel().getSelectedIndex();
        //if((num<-1) < -1){};
        accno.setText(String.valueOf(cData.getAccountNumber()));
        cardtype.setText(String.valueOf(cData.getCardType()));
        cardholder.setText(String.valueOf(cData.getCardHolderName()));
        phno.setText(String.valueOf(cData.getPhoneNumber()));
        email.setText(String.valueOf(cData.getEmailId()));
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        creditShowData();
    }

}
