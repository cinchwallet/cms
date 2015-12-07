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

			// change card status to OPEN
			index = 1;
			lPreparedStatement = lConnection.prepareStatement(UPDTAE_CARD_STATUS);
			lPreparedStatement.setString(index++, AppConstant.CardStatus.OPEN.name());
			lPreparedStatement.setTimestamp(index++, new Timestamp(new Date().getTime()));
			lPreparedStatement.setString(index++, request.getCardNumber());
			lPreparedStatement.executeUpdate();

			// assign card to new user
			assignCardToUser(membershipId, request.getCardNumber(), lConnection);
			
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
				card.setPoints(lResultSet.getInt("points"));
				card.setPointExpireOn(lResultSet.getDate("points_expire_on"));
				card.setStatus(lResultSet.getString("status"));
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
	 * Read the card and card holder data. It returns the card holder detail
	 * along with card detail like balance, status and points expiry
	 */
	public CardHolder getCardHolderProfile(String cardNumber) {
		Connection lConnection = null;
		PreparedStatement lPreparedStatement = null;
		ResultSet lResultSet = null;
		CardHolder customer = null;
		try {
			lConnection = DBConnection.getTxnConnection();
			lPreparedStatement = lConnection.prepareStatement(SELECT_USER_PROFILE);
			lPreparedStatement.setString(1, cardNumber);
			lResultSet = lPreparedStatement.executeQuery();
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
				customer.setEmail(lResultSet.getString("EMAIL_ADDRESS"));
				customer.setIsActive(lResultSet.getBoolean("IS_ACTIVE"));
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
	 * Modifies the card status. Valid status can be NEW, OPEN, CLOSED. Service
	 * used while registration and deactivation process.
	 */
	public void updateCardStatus(String cardNumber, String status) {
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
		} catch (Exception _Exception) {
			_Exception.printStackTrace();
		} finally {
			DBConnection.closeAll(lResultSet, lPreparedStatement, lConnection);
		}
	}

	/*
	 * Reissure a new card against the old card
	 */
	public Card reissueCard(Request request) throws Exception{
		Connection lConnection = null;
		PreparedStatement lPreparedStatement = null;
		ResultSet lResultSet = null;
		Card card = null;
		try {
			lConnection = DBConnection.getTxnConnection();
			lConnection.setAutoCommit(false);
			// get the customer info for given old card
			CardHolder cardHolder = getCardHolderProfile(request.getCardNumber());
			// change the new card status to OPEN
			updateCardStatus(request.getNewCardNumber(), AppConstant.CardStatus.OPEN.name());
			//de-associate old card with user
			unAssignCardToUser(cardHolder.getMembershipId(), request.getCardNumber(), lConnection);
			// assign new card to user
			assignCardToUser(cardHolder.getMembershipId(), request.getNewCardNumber(), lConnection);
			// TODO - transfer all points to the new card
			
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

}
