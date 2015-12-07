package com.thickedge.issuer.service;

import com.thickedge.issuer.constant.AppConstant;
import com.thickedge.issuer.constant.ResponseCode;
import com.thickedge.issuer.core.Card;
import com.thickedge.issuer.core.CardHolder;
import com.thickedge.issuer.core.Request;
import com.thickedge.issuer.core.Response;
import com.thickedge.issuer.dao.CardDao;

public class TransactionService {

	private static TransactionService transactionService = null;

	private CardDao cardDao = new CardDao();

	private TransactionService() {
	}

	public static TransactionService getInstance() {
		if (transactionService == null) {
			transactionService = new TransactionService();
		}
		return transactionService;
	}

	private Response populateResponse(Response response, Card card){
		response.setCard(card);
		return response;
	}
	
	public Response registerUser(Request request) {
		Response response = new Response();
		try {
			String membershipId = cardDao.registerUser(request);
			//Apply business logic on the card to assign any bonus points etc.
			
			Card card = cardDao.getCarddetail(request.getCardNumber());
			card.setMembershipId(membershipId);
			
			response = populateResponse(response, card);
		} catch (Exception e) {
			response.setResponseCode(ResponseCode.INTERNAL_FAILURE);
			e.printStackTrace();
		}
		return response;
		
	}
	public Response getCardDetail(Request request) {
		Response response = new Response();
		try {
			// Get the response from issuer server and prepare the response.
			Card card = cardDao.getCarddetail(request.getCardNumber());
			if (!isCardGoodForTxn(card, response)) {
				return response;
			}
			response = populateResponse(response, card);

		} catch (Exception e) {
			response.setResponseCode(ResponseCode.PROCESSOR_NO_RESPONSE);
			e.printStackTrace();
		}
		return response;
	}

	public Response getUserProfile(Request request) {
		Response response = new Response();
		try {
			// Get the response from issuer server and prepare the response.
			CardHolder cardHolder = cardDao.getCardHolderProfile(request.getCardNumber());
			if (cardHolder == null) {
				// customer not found for given card.
				response.setResponseCode(ResponseCode.CARD_NOT_FOUND);
				return response;
			}
			response.setCardHolder(cardHolder);
			response.setResponseCode(ResponseCode.APPROVED); // success
		} catch (Exception e) {
			response.setResponseCode(ResponseCode.PROCESSOR_NO_RESPONSE);
			e.printStackTrace();
		}
		return response;
	}

	public Response burnPoints(Request request) {
		Response response = new Response();
		// Do the sale operation and populate the Response object
		try {
			// Get the response from issuer server and prepare the response.
			Card card = cardDao.getCarddetail(request.getCardNumber());
			if (!isCardGoodForTxn(card, response)) {
				return response;
			}
			int pointBalance = card.getPoints() - request.getPoints();
			if (pointBalance > 0) {
				// card have sufficient balance for sale.
				/*
				 * might need to inert some logic to extend the points expiry
				 * date.
				 */
				cardDao.updateCardPoints(request.getCardNumber(), pointBalance);
				response.setPointsAvailable(pointBalance);
				response.setPointsExpireOn(card.getPointExpireOn());
				response.setResponseCode(ResponseCode.APPROVED); // success
			} else {
				response.setResponseCode(ResponseCode.INSUFFICIENT_BALANCE);// insufficient
																			// balance
			}

		} catch (Exception e) {
			response.setResponseCode(ResponseCode.PROCESSOR_NO_RESPONSE);
			e.printStackTrace();
		}
		return response;
	}

	public Response earnPoints(Request request) {
		Response response = new Response();
		// Do the sale operation and populate the Response object
		try {
			// Get the response from issuer server and prepare the response.
			Card card = cardDao.getCarddetail(request.getCardNumber());
			response.setPointsAvailable(card.getPoints());
			if (!isCardGoodForTxn(card, response)) {
				return response;
			}
			int pointBalance = card.getPoints() + request.getPoints();

			cardDao.updateCardPoints(request.getCardNumber(), pointBalance);
			response.setPointsAvailable(pointBalance);
			response.setPointsExpireOn(card.getPointExpireOn());
			response.setResponseCode(ResponseCode.APPROVED); // success

		} catch (Exception e) {
			response.setResponseCode(ResponseCode.PROCESSOR_NO_RESPONSE);
			e.printStackTrace();
		}
		return response;
	}
	
	public Response addPoints(Request request) {
		Response response = new Response();
		// Do the sale operation and populate the Response object
		try {
			// Get the response from issuer server and prepare the response.
			Card card = cardDao.getCarddetail(request.getCardNumber());
			if (!isCardGoodForTxn(card, response)) {
				return response;
			}
			int pointBalance = card.getPoints() + request.getPoints();

			cardDao.updateCardPoints(request.getCardNumber(), pointBalance);
			response.setPointsAvailable(pointBalance);
			response.setPointsExpireOn(card.getPointExpireOn());
			response.setResponseCode(ResponseCode.APPROVED); // success

		} catch (Exception e) {
			response.setResponseCode(ResponseCode.PROCESSOR_NO_RESPONSE);
			e.printStackTrace();
		}
		return response;
	}
	
	
	public Response reissueCard(Request request) {
		Response response = new Response();
		// Do the sale operation and populate the Response object
		try {
			Card card = cardDao.reissueCard(request);
			response = populateResponse(response, card);
		} catch (Exception e) {
			response.setResponseCode(ResponseCode.PROCESSOR_NO_RESPONSE);
			e.printStackTrace();
		}
		return response;
	}
	
	public Response reverseTxn(Request request) {
		Response response = new Response();
		// Do the sale operation and populate the Response object
		try {
			// Get the response from issuer server and prepare the response.
			Card card = cardDao.getCarddetail(request.getCardNumber());
			if (!isCardGoodForTxn(card, response)) {
				return response;
			}
			int pointBalance = card.getPoints() + request.getPoints();

			cardDao.updateCardPoints(request.getCardNumber(), pointBalance);
			response.setPointsAvailable(pointBalance);
			response.setResponseCode(ResponseCode.APPROVED); // success

		} catch (Exception e) {
			response.setResponseCode(ResponseCode.PROCESSOR_NO_RESPONSE);
			e.printStackTrace();
		}
		return response;
	}

	
	public Response cancelTxn(Request request) {
		Response response = new Response();
		// Do the sale operation and populate the Response object
		try {
			// Get the response from issuer server and prepare the response.
			Card card = cardDao.getCarddetail(request.getCardNumber());
			if (!isCardGoodForTxn(card, response)) {
				return response;
			}
			int pointBalance = card.getPoints() + request.getPoints();

			cardDao.updateCardPoints(request.getCardNumber(), pointBalance);
			response.setPointsAvailable(pointBalance);
			response.setResponseCode(ResponseCode.APPROVED); // success

		} catch (Exception e) {
			response.setResponseCode(ResponseCode.PROCESSOR_NO_RESPONSE);
			e.printStackTrace();
		}
		return response;
	}
	
	public Response getTxnHistory(Request request) {
		Response response = new Response();
		// Do the sale operation and populate the Response object
		try {
			// Get the response from issuer server and prepare the response.
			Card card = cardDao.getCarddetail(request.getCardNumber());
			if (!isCardGoodForTxn(card, response)) {
				return response;
			}
			int pointBalance = card.getPoints() + request.getPoints();

			cardDao.updateCardPoints(request.getCardNumber(), pointBalance);
			response.setPointsAvailable(pointBalance);
			response.setResponseCode(ResponseCode.APPROVED); // success

		} catch (Exception e) {
			response.setResponseCode(ResponseCode.PROCESSOR_NO_RESPONSE);
			e.printStackTrace();
		}
		return response;
	}



	public Response deactivateCard(Request request) {
		Response response = new Response();
		// Do the sale operation and populate the Response object
		try {
			// Get the response from issuer server and prepare the response.
			Card card = cardDao.getCarddetail(request.getCardNumber());
			if (card == null) {
				response.setResponseCode(ResponseCode.CARD_NOT_FOUND); // card
																		// not
																		// found
				return response;
			} else {
				if (card.getStatus().equals(AppConstant.CardStatus.CLOSED.name())) {
					// already active card
					response.setResponseCode(ResponseCode.INACTIVE_CARD); // already
																			// active
																			// card
					return response;
				} else {
					cardDao.updateCardStatus(request.getCardNumber(), AppConstant.CardStatus.CLOSED.name());
					response.setBalance(card.getBalance() == null ? 0.0 : card.getBalance());
					response.setResponseCode(ResponseCode.APPROVED); // success
				}
			}

		} catch (Exception e) {
			response.setResponseCode(ResponseCode.PROCESSOR_NO_RESPONSE);
			e.printStackTrace();
		}
		return response;
	}

	private boolean isCardGoodForTxn(Card card, Response response) {
		boolean flag = true;
		if (card == null) {
			// card does not exist
			response.setResponseCode(ResponseCode.CARD_NOT_FOUND);// card not
																	// found)
			flag = false;
		} else {
			// check for inactive card
			if (!card.getStatus().equals(AppConstant.CardStatus.OPEN.name())) {
				response.setResponseCode(ResponseCode.INACTIVE_CARD);// inactivate
																		// card
				flag = false;
			}
		}
		return flag;
	}

	// TODO - might be used in case of GIFT Card
	public Response activateCard(Request request) {
		Response response = new Response();
		// Do the sale operation and populate the Response object
		try {
			// Get the response from issuer server and prepare the response.
			Card card = cardDao.getCarddetail(request.getCardNumber());
			if (card == null) {
				response.setResponseCode(ResponseCode.CARD_NOT_FOUND); // card
																		// not
																		// found
				return response;
			} else {
				if (card.getStatus().equals(AppConstant.CardStatus.OPEN.name())) {
					// already active card
					response.setResponseCode(ResponseCode.ALREADY_ACTIVE_CARD); // already
																				// active
																				// card
					return response;
				} else {
					cardDao.updateCardStatus(request.getCardNumber(), AppConstant.CardStatus.OPEN.name());
					response.setBalance(card.getBalance() == null ? 0.0 : card.getBalance());
					response.setResponseCode(ResponseCode.APPROVED); // success
				}
			}

		} catch (Exception e) {
			response.setResponseCode(ResponseCode.PROCESSOR_NO_RESPONSE);
			e.printStackTrace();
		}
		return response;
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
}
