package com.thickedge.issuer.core;

import java.sql.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.thickedge.issuer.constant.ResponseCode;

@XmlRootElement(name = "Response")
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Response {

	//general fields
	private String responseCode;
	private String responseMsg;
	private Double balance;
	private Integer pointsAvailable;
	private Date pointsExpireOn;
	private String accountStatus;
	
	private Card card;
	//user registration
	private String membershipId;
	private String cardHolderName;

	//getprofile
	private CardHolder cardHolder;

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public CardHolder getCardHolder() {
		return cardHolder;
	}

	public void setCardHolder(CardHolder cardHolder) {
		this.cardHolder = cardHolder;
	}

	public Response() {
		responseCode = ResponseCode.APPROVED;
	}
	
	public String getMembershipId() {
		return membershipId;
	}
	public void setMembershipId(String membershipId) {
		this.membershipId = membershipId;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseMsg() {
		return responseMsg;
	}
	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public String getCardHolderName() {
            return cardHolderName;
        }

	public void setCardHolderName(String cardHolderName) {
            this.cardHolderName = cardHolderName;
        }

	public Integer getPointsAvailable() {
		return pointsAvailable;
	}

	public void setPointsAvailable(Integer pointsAvailable) {
		this.pointsAvailable = pointsAvailable;
	}

	public Date getPointsExpireOn() {
		return pointsExpireOn;
	}

	public void setPointsExpireOn(Date pointsExpireOn) {
		this.pointsExpireOn = pointsExpireOn;
	}


}
