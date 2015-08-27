package com.thickedge.issuer.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.thickedge.issuer.core.Request;
import com.thickedge.issuer.core.Response;
import com.thickedge.issuer.processor.TransactionProcessor;



@Path("/issuer/api/")
public class IssuerController {


	@GET
	@Path("/getserverstatus")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getServerStatus() {
		Response response = new Response();
		response.setResponseCode("0000");
		response.setResponseMsg("Connection with server is OK");
		return response;

	}

	@POST
	@Path("/getcarddetail")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCardDetail(Request request) {
		return TransactionProcessor.getInstance().getCustomerDetail(request);

	}

	@POST
	@Path("/getbalance")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBalance(Request request) {
		return TransactionProcessor.getInstance().getCardBalance(request);

	}


	@POST
	@Path("/sale")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response doSale(Request request) {
		return TransactionProcessor.getInstance().doSale(request);

	}

	@POST
	@Path("/load")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response doReload(Request request) {
		return TransactionProcessor.getInstance().doReload(request);

	}


	@POST
	@Path("/activate")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response activateCard(Request request) {
		return TransactionProcessor.getInstance().activateCard(request);

	}

	@POST
	@Path("/deactivate")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deactivateCard(Request request) {
		return TransactionProcessor.getInstance().deactivateCard(request);

	}

}