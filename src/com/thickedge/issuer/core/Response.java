package com.thickedge.issuer.core;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.thickedge.issuer.constant.ResponseCode;

@XmlRootElement(name = "Response")
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Response {

	private String responseCode;
	private String responseMsg;
	private Double balance;
	private String cardHolderName;


	public Response() {
		responseCode = ResponseCode.APPROVED;
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


}
