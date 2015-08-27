package com.thickedge.issuer.service;

import com.thickedge.issuer.constant.AppConstant;
import com.thickedge.issuer.constant.ResponseCode;
import com.thickedge.issuer.core.Card;
import com.thickedge.issuer.core.Customer;
import com.thickedge.issuer.core.Request;
import com.thickedge.issuer.core.Response;
import com.thickedge.issuer.dao.CardDao;

public class TransactionService {

private static TransactionService transactionService = null;

private CardDao cardDao = new CardDao();
	private TransactionService() {
	}

	public static TransactionService getInstance(){
		if(transactionService == null){
			transactionService = new TransactionService();
		}
		return transactionService;
	}


	public Response getBalance(Request request) {
		Response response = new Response();
		try {
			// Get the response from issuer server and prepare the response.
			Card card = cardDao.getCarddetail(request.getCardNumber());
			if (!isCardGoodForTxn(card, response)) {
				return response;
			}

			response.setBalance(card.getBalance());
			response.setResponseCode(ResponseCode.APPROVED); // success
		} catch (Exception e) {
			response.setResponseCode(ResponseCode.PROCESSOR_NO_RESPONSE);
			e.printStackTrace();
		}
		return response;
	}


	public Response getCustomerDetail(Request request) {
		Response response = new Response();
		try {
			// Get the response from issuer server and prepare the response.
			Customer cust = cardDao.getCustomer(request.getCardNumber());
			if(cust==null){
			    //customer not found for given card.
			    response.setResponseCode(ResponseCode.CARD_NOT_FOUND);
			    return response;
			}
			response.setCardHolderName(cust.getSalutation()+" "+cust.getFirstName()+" "+cust.getLastName());
			response.setResponseCode(ResponseCode.APPROVED); // success
		} catch (Exception e) {
			response.setResponseCode(ResponseCode.PROCESSOR_NO_RESPONSE);
			e.printStackTrace();
		}
		return response;
	}

	public Response doSale(Request request){
		Response response = new Response();
		//Do the sale operation and populate the Response object
		try {
			// Get the response from issuer server and prepare the response.
			Card card = cardDao.getCarddetail(request.getCardNumber());
			if(!isCardGoodForTxn(card, response)){
				return response;
			}
			double balance = card.getBalance() - request.getTxnAmount();
			if (balance > 0) {
				//card have sufficient balance for sale.
				cardDao.updateCardBalance(request.getCardNumber(), balance);
				response.setBalance(balance);
				response.setResponseCode(ResponseCode.APPROVED); // success
			} else {
				response.setResponseCode(ResponseCode.INSUFFICIENT_BALANCE);//insufficient balance
			}

		} catch (Exception e) {
			response.setResponseCode(ResponseCode.PROCESSOR_NO_RESPONSE);
			e.printStackTrace();
		}
		return response;
	}


	public Response doReload(Request request){
		Response response = new Response();
		//Do the sale operation and populate the Response object
		try {
			// Get the response from issuer server and prepare the response.
			Card card = cardDao.getCarddetail(request.getCardNumber());
			if(!isCardGoodForTxn(card, response)){
				return response;
			}
			double balance = card.getBalance() + request.getTxnAmount();

			cardDao.updateCardBalance(request.getCardNumber(), balance);
			response.setBalance(balance);
			response.setResponseCode(ResponseCode.APPROVED); // success

		} catch (Exception e) {
			response.setResponseCode(ResponseCode.PROCESSOR_NO_RESPONSE);
			e.printStackTrace();
		}
		return response;
	}


	public Response activateCard(Request request){
		Response response = new Response();
		//Do the sale operation and populate the Response object
		try {
			// Get the response from issuer server and prepare the response.
			Card card = cardDao.getCarddetail(request.getCardNumber());
			if(card==null){
				response.setResponseCode(ResponseCode.CARD_NOT_FOUND); //card not found
				return response;
			}else{
				if(card.getStatus().equals(AppConstant.CardStatus.OPEN.name())){
					//already active card
					response.setResponseCode(ResponseCode.ALREADY_ACTIVE_CARD); //already active card
					return response;
				}else{
					cardDao.updateCardStatus(request.getCardNumber(), AppConstant.CardStatus.OPEN.name());
					response.setBalance(card.getBalance()==null?0.0:card.getBalance());
					response.setResponseCode(ResponseCode.APPROVED); // success
				}
			}

		} catch (Exception e) {
			response.setResponseCode(ResponseCode.PROCESSOR_NO_RESPONSE);
			e.printStackTrace();
		}
		return response;
	}

	public Response deactivateCard(Request request){
		Response response = new Response();
		//Do the sale operation and populate the Response object
		try {
			// Get the response from issuer server and prepare the response.
			Card card = cardDao.getCarddetail(request.getCardNumber());
			if(card==null){
				response.setResponseCode(ResponseCode.CARD_NOT_FOUND); //card not found
				return response;
			}else{
				if(card.getStatus().equals(AppConstant.CardStatus.CLOSED.name())){
					//already active card
					response.setResponseCode(ResponseCode.INACTIVE_CARD); //already active card
					return response;
				}else{
					cardDao.updateCardStatus(request.getCardNumber(), AppConstant.CardStatus.CLOSED.name());
					response.setBalance(card.getBalance()==null?0.0:card.getBalance());
					response.setResponseCode(ResponseCode.APPROVED); // success
				}
			}

		} catch (Exception e) {
			response.setResponseCode(ResponseCode.PROCESSOR_NO_RESPONSE);
			e.printStackTrace();
		}
		return response;
	}

	private boolean isCardGoodForTxn(Card card, Response response){
		boolean flag = true;
		if (card == null) {
			// card does not exist
			response.setResponseCode(ResponseCode.CARD_NOT_FOUND);// card not found)
			flag = false;
		} else {
			// check for inactive card
			if (!card.getStatus().equals(AppConstant.CardStatus.OPEN.name())) {
				response.setResponseCode(ResponseCode.INACTIVE_CARD);// inactivate card
				flag = false;
			}
		}
		return flag;
	}
}
