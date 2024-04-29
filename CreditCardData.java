/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package creditcardprocessing;

/**
 *
 * @author maria
 */
public class CreditCardData {
    private Integer accountNumber;
    private String cardType;
    private String cardHolderName;
    private Integer phoneNumber;
    private String emailId;
    
    public CreditCardData(Integer accountNumber,String cardType,String cardHolderName,Integer phoneNumber,String emailId){
        this.accountNumber=accountNumber;
        this.cardType = cardType;
        this.cardHolderName = cardHolderName;
        this.phoneNumber = phoneNumber;
        this.emailId = emailId;
    }
    public Integer getAccountNumber(){
        return accountNumber;
    }
    public String getCardType(){
        return cardType;
    }
    public String getCardHolderName(){
        return cardHolderName;
    }
    public Integer getPhoneNumber(){
        return phoneNumber;
    }
    public String getEmailId(){
        return emailId;
    }
}
