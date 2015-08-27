package com.thickedge.issuer;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.thickedge.issuer.core.Request;
import com.thickedge.issuer.core.Response;

public class IssuerCommunicator {

	private String balanceInquiryPath = "getbalance";
	private String activationPath = "activate";
	private String salePath = "sale";
	private String reloadPath = "load";
	/*
	 * this is acquirer side component and will be communicating with the issuer to exchange the message.
	 */

    private static IssuerCommunicator client;

    private final WebResource webResource;

    /*
     * private constructor to avoid construction of object.
     */
    private IssuerCommunicator(String url) {
      Client httpClient = Client.create();
      this.webResource = httpClient.resource(url);
    }

    /*
     * Creates singleton instance of the Client class.
     */
    public static synchronized IssuerCommunicator getInstance(String url) {
      if (client == null) {
        client = new IssuerCommunicator(url);
      }
      return client;
    }


	public Response invokeBalanceInquiry(Request request) {
		Response response = webResource.path(balanceInquiryPath)
				.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.entity(request, MediaType.APPLICATION_JSON)
				.post(Response.class);
		System.out.println("Response " + response);
		return response;

		// Exception handling needs to be done.
	}

	public Response doSale(Request request){
		Response response = webResource.path(salePath)
				.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(Response.class, request);
		System.out.println("Response " + response);
		return response;

		// Exception handling needs to be done.
	}


	public Response activateCard(Request request){
		Response response = webResource.path(activationPath)
				.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(Response.class, request);
		System.out.println("Response " + response);
		return response;

		// Exception handling needs to be done.
	}

	public Response doReload(Request request){

		Response response = webResource.path(reloadPath)
				.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(Response.class, request);
		System.out.println("Response " + response);
		return response;

		// Exception handling needs to be done.
	}

}
