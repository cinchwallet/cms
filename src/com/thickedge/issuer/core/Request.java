package com.thickedge.issuer.core;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@XmlRootElement(name = "Request")
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Request {

    private String cardNumber;
	private String merchantId;
	private Date txnDate;
	private String terminalId;
	private Double txnAmount;
	private int points;
	private String stan;
	private String txnType;
	private CardHolder cardHolder;
	//reissue card
	private String newCardNumber;
	

	public String getNewCardNumber() {
		return newCardNumber;
	}
	public void setNewCardNumber(String newCardNumber) {
		this.newCardNumber = newCardNumber;
	}
	public CardHolder getCardHolder() {
		return cardHolder;
	}
	public void setCardHolder(CardHolder cardHolder) {
		this.cardHolder = cardHolder;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public Date getTxnDate() {
		return txnDate;
	}
	public void setTxnDate(Date txnDate) {
		this.txnDate = txnDate;
	}
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public Double getTxnAmount() {
            return txnAmount;
        }
	public void setTxnAmount(Double txnAmount) {
            this.txnAmount = txnAmount;
        }
	public String getStan() {
            return stan;
        }
	public void setStan(String stan) {
            this.stan = stan;
        }
	public String getTxnType() {
            return txnType;
        }
	public void setTxnType(String txnType) {
            this.txnType = txnType;
        }
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}

}
