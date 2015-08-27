package com.thickedge.issuer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.thickedge.issuer.core.Card;
import com.thickedge.issuer.core.Customer;
import com.thickedge.issuer.util.DBConnection;

public class CardDao {

    private static String SELECT_CARD_BALANCE = "select * from CARD where number = ?";

    private static String UPDTAE_CARD_BALANCE = "update CARD set balance = ? where number = ?";

    private static String UPDTAE_CARD_STATUS  = "update CARD set status = ? where number = ?";

    private static String SELECT_CUSTOMER     = "select * from CUSTOMER cust inner join CUSTOMER_CARD custc on cust.customer_id = custc.CUSTOMER_ID where custc.CARD_NUMBER = ?";

    public Card getCarddetail(String cardNumber) {
	Connection lConnection = null;
	PreparedStatement lPreparedStatement = null;
	ResultSet lResultSet = null;
	Card card = null;
	try {
	    lConnection = DBConnection.getTxnConnection();
	    lPreparedStatement = lConnection.prepareStatement(SELECT_CARD_BALANCE);
	    lPreparedStatement.setString(1, cardNumber);
	    lResultSet = lPreparedStatement.executeQuery();
	    if (lResultSet != null && lResultSet.next()) {
		card = new Card();
		card.setBalance(lResultSet.getDouble("balance"));
		card.setCardNumber(lResultSet.getString("number"));
		card.setStatus(lResultSet.getString("status"));
	    }
	} catch (SQLException _sqlException) {
	    _sqlException.printStackTrace();
	} catch (Exception _Exception) {
	    _Exception.printStackTrace();
	} finally {
	    DBConnection.closeAll(lResultSet, lPreparedStatement, lConnection);
	}
	return card;
    }

    public void updateCardBalance(String cardNumber, Double cardBalance) {
	Connection lConnection = null;
	PreparedStatement lPreparedStatement = null;
	ResultSet lResultSet = null;
	try {
	    lConnection = DBConnection.getTxnConnection();
	    lPreparedStatement = lConnection.prepareStatement(UPDTAE_CARD_BALANCE);
	    lPreparedStatement.setDouble(1, cardBalance);
	    lPreparedStatement.setString(2, cardNumber);
	    int count = lPreparedStatement.executeUpdate();
	} catch (SQLException _sqlException) {
	    _sqlException.printStackTrace();
	} catch (Exception _Exception) {
	    _Exception.printStackTrace();
	} finally {
	    DBConnection.closeAll(lResultSet, lPreparedStatement, lConnection);
	}
    }

    public void updateCardStatus(String cardNumber, String status) {
	Connection lConnection = null;
	PreparedStatement lPreparedStatement = null;
	ResultSet lResultSet = null;
	try {
	    lConnection = DBConnection.getTxnConnection();
	    lPreparedStatement = lConnection.prepareStatement(UPDTAE_CARD_STATUS);
	    lPreparedStatement.setString(1, status);
	    lPreparedStatement.setString(2, cardNumber);
	    int count = lPreparedStatement.executeUpdate();
	} catch (SQLException _sqlException) {
	    _sqlException.printStackTrace();
	} catch (Exception _Exception) {
	    _Exception.printStackTrace();
	} finally {
	    DBConnection.closeAll(lResultSet, lPreparedStatement, lConnection);
	}
    }

    public Customer getCustomer(String cardNumber) {
	Connection lConnection = null;
	PreparedStatement lPreparedStatement = null;
	ResultSet lResultSet = null;
	Customer customer = null;
	try {
	    lConnection = DBConnection.getTxnConnection();
	    lPreparedStatement = lConnection.prepareStatement(SELECT_CUSTOMER);
	    lPreparedStatement.setString(1, cardNumber);
	    lResultSet = lPreparedStatement.executeQuery();
	    if (lResultSet != null && lResultSet.next()) {
		customer = new Customer();
		customer.setCustId(lResultSet.getString("CUSTOMER_ID"));
		customer.setSalutation(lResultSet.getString("TITLE"));
		customer.setFirstName(lResultSet.getString("FIRST_NAME"));
		customer.setLastName(lResultSet.getString("LAST_NAME"));

	    }
	} catch (SQLException _sqlException) {
	    _sqlException.printStackTrace();
	} catch (Exception _Exception) {
	    _Exception.printStackTrace();
	} finally {
	    DBConnection.closeAll(lResultSet, lPreparedStatement, lConnection);
	}
	return customer;
    }

}
