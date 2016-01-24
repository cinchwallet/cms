package com.thickedge.issuer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import com.thickedge.issuer.constant.AppConstant;
import com.thickedge.issuer.core.Card;
import com.thickedge.issuer.core.CardHolder;
import com.thickedge.issuer.core.Loyalty;
import com.thickedge.issuer.core.Request;
import com.thickedge.issuer.util.DBConnection;
import com.thickedge.issuer.util.Util;

public class CardDao extends DBStatements {

	/*
	 * Register a new user and assign a card to him.
	 */
	public String registerUser(Request request) throws Exception{
		Connection lConnection = null;
		PreparedStatement lPreparedStatement = null;
		ResultSet lResultSet = null;
		String membershipId = null;
		try {
			int index = 1;
			lConnection = DBConnection.getTxnConnection();
			lConnection.setAutoCommit(false);
			// save customer detail
			lPreparedStatement = lConnection.prepareStatement(CREATE_NEW_USER);
			membershipId = Util.getMembershipId();
			lPreparedStatement.setString(index++, membershipId);// TODO - logic
																// to generate
																// customer Id
			lPreparedStatement.setString(index++, null);
			lPreparedStatement.setString(index++, request.getCardHolder().getFirstName());
			lPreparedStatement.setString(index++, null);
			lPreparedStatement.setString(index++, request.getCardHolder().getLastName());
			lPreparedStatement.setString(index++, request.getCardHolder().getAddress());
			lPreparedStatement.setString(index++, null);
			lPreparedStatement.setString(index++, request.getCardHolder().getCity());
			lPreparedStatement.setString(index++, request.getCardHolder().getState());
			lPreparedStatement.setString(index++, request.getCardHolder().getCountry());
			lPreparedStatement.setString(index++, request.getCardHolder().getPhoneNumber());
			lPreparedStatement.setString(index++, request.getCardHolder().getPhoneNumber());
			lPreparedStatement.setString(index++, request.getCardHolder().getPhoneNumber());
			lPreparedStatement.setString(index++, request.getCardHolder().getEmail());
			lPreparedStatement.setBoolean(index++, true);
			lPreparedStatement.setTimestamp(index++, new Timestamp(new Date().getTime()));
			lPreparedStatement.setTimestamp(index++, new Timestamp(new Date().getTime()));

			lPreparedStatement.executeUpdate();

			if(request.getCardNumber()!=null && request.getCardNumber().length()>0){
				// change card status to OPEN, card existance should be checked as well.
				index = 1;
				lPreparedStatement = lConnection.prepareStatement(UPDTAE_CARD_STATUS);
				lPreparedStatement.setString(index++, AppConstant.CardStatus.OPEN.name());
				lPreparedStatement.setTimestamp(index++, new Timestamp(new Date().getTime()));
				lPreparedStatement.setString(index++, request.getCardNumber());
				int result = lPreparedStatement.executeUpdate();
				if(result>0){
					// assign card to new user
					assignCardToUser(membershipId, request.getCardNumber(), lConnection);
				}else{
					// card do not existing
				}
			}

			//registration is done without card, hence mobile is the KEY
			assignCustomerLoyalty(membershipId, request.getPoints(), "2512", lConnection);

			
			lConnection.commit();
		} catch (SQLException _sqlException) {
			_sqlException.printStackTrace();
			throw _sqlException;
		} catch (Exception _Exception) {
			_Exception.printStackTrace();
			throw _Exception;
		} finally {
			DBConnection.closeAll(lResultSet, lPreparedStatement, lConnection);
		}
		return membershipId;
	}

	private int assignCustomerLoyalty(String membershipId, int points, String pointsExpireOn, Connection lConnection) throws SQLException{
		int index = 1;
		PreparedStatement lPreparedStatement = lConnection.prepareStatement(ASSIGN_CUSTOMER_LOYALTY);
		lPreparedStatement.setString(index++, membershipId);
		lPreparedStatement.setInt(index++, points);
		lPreparedStatement.setString(index++, pointsExpireOn);
		lPreparedStatement.setTimestamp(index++, new Timestamp(new Date().getTime()));
		lPreparedStatement.setTimestamp(index++, new Timestamp(new Date().getTime()));
		return lPreparedStatement.executeUpdate();
	}

	
	private int assignCardToUser(String membershipId, String cardNumber, Connection lConnection) throws SQLException{
		int index = 1;
		PreparedStatement lPreparedStatement = lConnection.prepareStatement(ASSOCIATE_USER_CARD);
		lPreparedStatement.setString(index++, membershipId);
		lPreparedStatement.setString(index++, cardNumber);
		lPreparedStatement.setBoolean(index++, true);
		lPreparedStatement.setTimestamp(index++, new Timestamp(new Date().getTime()));
		lPreparedStatement.setTimestamp(index++, new Timestamp(new Date().getTime()));
		return lPreparedStatement.executeUpdate();
	}
	
	private int unAssignCardToUser(String membershipId, String cardNumber, Connection lConnection) throws SQLException{
		int index = 1;
		PreparedStatement lPreparedStatement = lConnection.prepareStatement(DE_ASSOCIATE_USER_CARD);
		lPreparedStatement.setTimestamp(index++, new Timestamp(new Date().getTime()));
		lPreparedStatement.setString(index++, membershipId);
		lPreparedStatement.setString(index++, cardNumber);
		return lPreparedStatement.executeUpdate();
	}

	/*
	 * Read the card status and balance available on the card. This service do
	 * not returns any card holder data.
	 */
	public Card getCarddetail(String cardNumber) throws Exception{
		Connection lConnection = null;
		PreparedStatement lPreparedStatement = null;
		ResultSet lResultSet = null;
		Card card = null;
		try {
			lConnection = DBConnection.getTxnConnection();
			lPreparedStatement = lConnection.prepareStatement(SELECT_CARD);
			lPreparedStatement.setString(1, cardNumber);
			lResultSet = lPreparedStatement.executeQuery();
			if (lResultSet != null && lResultSet.next()) {
				card = new Card();
				card.setPoints(lResultSet.getInt("POINTS"));
				card.setPointExpireOn(lResultSet.getString("POINTS_EXPIRE_ON"));
				card.setStatus(lResultSet.getString("STATUS"));
				card.setCardNumber(lResultSet.getString("CARD_PRODUCT"));
			}
		} catch (SQLException _sqlException) {
			_sqlException.printStackTrace();
			throw _sqlException;
		} catch (Exception _Exception) {
			_Exception.printStackTrace();
			throw _Exception;
		} finally {
			DBConnection.closeAll(lResultSet, lPreparedStatement, lConnection);
		}
		return card;
	}

	/*
	 * Read the card holder data for given card number. It returns the card holder detail
	 * along with loyalty point balance.
	 */
	public CardHolder getCardHolderProfileByCard(String cardNumber) {
		Connection lConnection = null;
		PreparedStatement lPreparedStatement = null;
		ResultSet lResultSet = null;
		CardHolder customer = null;
		try {
			lConnection = DBConnection.getTxnConnection();
			lPreparedStatement = lConnection.prepareStatement(SELECT_USER_PROFILE_BY_CARD);
			lPreparedStatement.setString(1, cardNumber);
			lResultSet = lPreparedStatement.executeQuery();
			customer = populateCustomerDetail(lResultSet);
			
		} catch (SQLException _sqlException) {
			_sqlException.printStackTrace();
		} catch (Exception _Exception) {
			_Exception.printStackTrace();
		} finally {
			DBConnection.closeAll(lResultSet, lPreparedStatement, lConnection);
		}
		return customer;
	}
	
	/*
	 * Read the card holder data for given phone number. It returns the card holder detail
	 * along with loyalty point balance.
	 */
	public CardHolder getCardHolderProfileByPhone(String phoneNumber) {
		Connection lConnection = null;
		PreparedStatement lPreparedStatement = null;
		ResultSet lResultSet = null;
		CardHolder customer = null;
		try {
			lConnection = DBConnection.getTxnConnection();
			lPreparedStatement = lConnection.prepareStatement(SELECT_USER_PROFILE_BY_PHONE);
			lPreparedStatement.setString(1, phoneNumber);
			lResultSet = lPreparedStatement.executeQuery();
			customer = populateCustomerDetail(lResultSet);
			
		} catch (SQLException _sqlException) {
			_sqlException.printStackTrace();
		} catch (Exception _Exception) {
			_Exception.printStackTrace();
		} finally {
			DBConnection.closeAll(lResultSet, lPreparedStatement, lConnection);
		}
		return customer;
	}

	/*
	 * Read the card holder data for given membershipId. It returns the card holder detail
	 * along with loyalty point balance.
	 */
	public CardHolder getCardHolderProfileById(String membershipId) {
		Connection lConnection = null;
		PreparedStatement lPreparedStatement = null;
		ResultSet lResultSet = null;
		CardHolder customer = null;
		try {
			lConnection = DBConnection.getTxnConnection();
			lPreparedStatement = lConnection.prepareStatement(SELECT_USER_PROFILE_BY_ID);
			lPreparedStatement.setString(1, membershipId);
			lResultSet = lPreparedStatement.executeQuery();
			customer = populateCustomerDetail(lResultSet);
			
		} catch (SQLException _sqlException) {
			_sqlException.printStackTrace();
		} catch (Exception _Exception) {
			_Exception.printStackTrace();
		} finally {
			DBConnection.closeAll(lResultSet, lPreparedStatement, lConnection);
		}
		return customer;
	}

	public void updateCardHolderProfile(CardHolder cardHolder) {
		Connection lConnection = null;
		PreparedStatement lPreparedStatement = null;
		ResultSet lResultSet = null;
		CardHolder customer = null;
		try {
			lConnection = DBConnection.getTxnConnection();
			StringBuffer stmt = new StringBuffer("UPDATE CUSTOMER ");
			stmt.append("set UPDATED_TS= now()");
			if(cardHolder.getFirstName()!=null)
				stmt.append(",FIRST_NAME='").append(cardHolder.getFirstName()).append("'");
			if(cardHolder.getLastName()!=null)
				stmt.append(",LAST_NAME='").append(cardHolder.getLastName()).append("'");
			if(cardHolder.getAddress()!=null)
				stmt.append(",ADDRESS_LINE1='").append(cardHolder.getFirstName()).append("'");
			if(cardHolder.getCity()!=null)
				stmt.append(",CITY='").append(cardHolder.getCity()).append("'");
			if(cardHolder.getState()!=null)
				stmt.append(",STATE='").append(cardHolder.getState()).append("'");
			if(cardHolder.getCountry()!=null)
				stmt.append(",COUNTRY='").append(cardHolder.getCountry()).append("'");
			if(cardHolder.getPhoneNumber()!=null)
				stmt.append(",PHONE_WORK='").append(cardHolder.getPhoneNumber()).append("'");
			if(cardHolder.getPhoneNumber()!=null)
				stmt.append(",PHONE_HOME='").append(cardHolder.getPhoneNumber()).append("'");
			if(cardHolder.getPhoneNumber()!=null)
				stmt.append(",PHONE_MOBILE='").append(cardHolder.getPhoneNumber()).append("'");
			if(cardHolder.getEmail()!=null)
				stmt.append(",EMAIL='").append(cardHolder.getEmail()).append("'");
			
			stmt.append("WHERE CUSTOMER_ID = '").append(cardHolder.getMembershipId()).append("'");
			lPreparedStatement = lConnection.prepareStatement(stmt.toString());
			lPreparedStatement.executeUpdate();
		} catch (SQLException _sqlException) {
			_sqlException.printStackTrace();
		} catch (Exception _Exception) {
			_Exception.printStackTrace();
		} finally {
			DBConnection.closeAll(lResultSet, lPreparedStatement, lConnection);
		}
	}
	
	private CardHolder populateCustomerDetail(ResultSet lResultSet) throws SQLException {
		CardHolder customer = null;
		if (lResultSet != null && lResultSet.next()) {
			customer = new CardHolder();

			customer.setMembershipId(lResultSet.getString("CUSTOMER_ID"));
			// customer.set(lResultSet.getString("TITLE"));
			customer.setFirstName(lResultSet.getString("FIRST_NAME"));
			customer.setLastName(lResultSet.getString("LAST_NAME"));
			customer.setAddress(lResultSet.getString("ADDRESS_LINE1"));
			customer.setCity(lResultSet.getString("CITY"));
			customer.setState(lResultSet.getString("STATE"));
			customer.setCountry(lResultSet.getString("COUNTRY"));
			customer.setPhoneNumber(lResultSet.getString("PHONE_WORK"));
			// customer.set(lResultSet.getString("PHONE_HOME"));
			// customer.set(lResultSet.getString("PHONE_MOBILE"));
			customer.setEmail(lResultSet.getString("EMAIL"));
			customer.setIsActive(lResultSet.getBoolean("IS_ACTIVE"));
			customer.setPoints(lResultSet.getInt("POINTS"));
		}
		return customer;
	}

	/*
	 * It simply updates the points on the card. Can be used for earn point,
	 * burn point and add point API.
	 */
	public void updateCardPoints(String cardNumber, int points) throws Exception{
		Connection lConnection = null;
		PreparedStatement lPreparedStatement = null;
		ResultSet lResultSet = null;
		try {
			int index=1;
			lConnection = DBConnection.getTxnConnection();
			lPreparedStatement = lConnection.prepareStatement(UPDTAE_CARD_POINT_BALANCE);
			lPreparedStatement.setInt(index++, points);
			lPreparedStatement.setTimestamp(index++, new Timestamp(new Date().getTime()));
			lPreparedStatement.setString(index++, cardNumber);
			int count = lPreparedStatement.executeUpdate();
		} catch (SQLException _sqlException) {
			_sqlException.printStackTrace();
			throw _sqlException;
		} catch (Exception _Exception) {
			_Exception.printStackTrace();
			throw _Exception;
		} finally {
			DBConnection.closeAll(lResultSet, lPreparedStatement, lConnection);
		}
	}

	/*
	 * It simply updates the loyalty points. Can be used for earn point,
	 * burn point and add point API.
	 */
	public void updateLoyaltyPoints(String membershipId, int points) throws Exception{
		Connection lConnection = null;
		PreparedStatement lPreparedStatement = null;
		ResultSet lResultSet = null;
		try {
			int index=1;
			lConnection = DBConnection.getTxnConnection();
			lPreparedStatement = lConnection.prepareStatement(UPDTAE_LOYALTY_POINT);
			lPreparedStatement.setInt(index++, points);
			lPreparedStatement.setTimestamp(index++, new Timestamp(new Date().getTime()));
			lPreparedStatement.setString(index++, membershipId);
			lPreparedStatement.executeUpdate();
		} catch (SQLException _sqlException) {
			_sqlException.printStackTrace();
			throw _sqlException;
		} catch (Exception _Exception) {
			_Exception.printStackTrace();
			throw _Exception;
		} finally {
			DBConnection.closeAll(lResultSet, lPreparedStatement, lConnection);
		}
	}

	/*
	 * Modifies the card status. Valid status can be NEW, OPEN, CLOSED. Service
	 * used while registration and deactivation process.
	 */
	public void updateCardStatus(String cardNumber, String status) throws Exception{
		Connection lConnection = null;
		PreparedStatement lPreparedStatement = null;
		ResultSet lResultSet = null;
		try {
			int index=1;
			lConnection = DBConnection.getTxnConnection();
			lPreparedStatement = lConnection.prepareStatement(UPDTAE_CARD_STATUS);
			lPreparedStatement.setString(index++, status);
			lPreparedStatement.setTimestamp(index++, new Timestamp(new Date().getTime()));
			lPreparedStatement.setString(index++, cardNumber);
			int count = lPreparedStatement.executeUpdate();
		} catch (SQLException _sqlException) {
			_sqlException.printStackTrace();
			throw _sqlException;
		} catch (Exception _Exception) {
			_Exception.printStackTrace();
			throw _Exception;
		} finally {
			DBConnection.closeAll(lResultSet, lPreparedStatement, lConnection);
		}
	}

	/*
	 * Reissure a new card against the old card
	 */
	public Card reissueCard(Request request, String membershipId) throws Exception{
		Connection lConnection = null;
		PreparedStatement lPreparedStatement = null;
		ResultSet lResultSet = null;
		Card card = null;
		try {
			lConnection = DBConnection.getTxnConnection();
			lConnection.setAutoCommit(false);
			// change the new card status to OPEN
			updateCardStatus(request.getNewCardNumber(), AppConstant.CardStatus.OPEN.name());
			//de-associate old card with user
			unAssignCardToUser(membershipId, request.getCardNumber(), lConnection);
			// assign new card to user
			assignCardToUser(membershipId, request.getNewCardNumber(), lConnection);
			// deactivate old card
			updateCardStatus(request.getCardNumber(), AppConstant.CardStatus.CLOSED.name());
			
			lConnection.commit();
		} catch (SQLException _sqlException) {
			_sqlException.printStackTrace();
			throw _sqlException;
		} catch (Exception _Exception) {
			_Exception.printStackTrace();
			throw _Exception;
		} finally {
			DBConnection.closeAll(lResultSet, lPreparedStatement, lConnection);
		}
		return card;
	}

	
	/*
	 * Read the membershipId and points for a given card. 
	 */
	public Loyalty getLoyaltyDetail(String cardNumber) throws Exception{
		Connection lConnection = null;
		PreparedStatement lPreparedStatement = null;
		ResultSet lResultSet = null;
		Loyalty loyalty = null;
		try {
			lConnection = DBConnection.getTxnConnection();
			lPreparedStatement = lConnection.prepareStatement(SELECT_LOYALTY_DETAIL);
			lPreparedStatement.setString(1, cardNumber);
			lResultSet = lPreparedStatement.executeQuery();
			if (lResultSet != null && lResultSet.next()) {
				loyalty = new Loyalty();
				loyalty.setMembershipId(lResultSet.getString("MEMBERSHIP_ID"));
				loyalty.setLoyaltyPoints(lResultSet.getInt("POINTS"));
			}
		} catch (SQLException _sqlException) {
			_sqlException.printStackTrace();
			throw _sqlException;
		} catch (Exception _Exception) {
			_Exception.printStackTrace();
			throw _Exception;
		} finally {
			DBConnection.closeAll(lResultSet, lPreparedStatement, lConnection);
		}
		return loyalty;
	}

	/*
	 * Read the membershipId and points for a given card. 
	 */
	public Loyalty getLoyaltyDetailForPhone(String phoneNumber) throws Exception{
		Connection lConnection = null;
		PreparedStatement lPreparedStatement = null;
		ResultSet lResultSet = null;
		Loyalty loyalty = null;
		try {
			lConnection = DBConnection.getTxnConnection();
			lPreparedStatement = lConnection.prepareStatement(SELECT_LOYALTY_DETAIL_FOR_PHONE);
			lPreparedStatement.setString(1, phoneNumber);
			lResultSet = lPreparedStatement.executeQuery();
			if (lResultSet != null && lResultSet.next()) {
				loyalty = new Loyalty();
				loyalty.setMembershipId(lResultSet.getString("MEMBERSHIP_ID"));
				loyalty.setLoyaltyPoints(lResultSet.getInt("POINTS"));
			}
		} catch (SQLException _sqlException) {
			_sqlException.printStackTrace();
			throw _sqlException;
		} catch (Exception _Exception) {
			_Exception.printStackTrace();
			throw _Exception;
		} finally {
			DBConnection.closeAll(lResultSet, lPreparedStatement, lConnection);
		}
		return loyalty;
	}


}
