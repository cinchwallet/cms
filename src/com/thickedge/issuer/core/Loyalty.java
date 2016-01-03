package com.thickedge.issuer.core;

public class Loyalty {

	private String membershipId;
	private int loyaltyPoints;
	private String pointsExpireOn;
	
	
	public String getPointsExpireOn() {
		return pointsExpireOn;
	}
	public void setPointsExpireOn(String pointsExpireOn) {
		this.pointsExpireOn = pointsExpireOn;
	}
	public String getMembershipId() {
		return membershipId;
	}
	public void setMembershipId(String membershipId) {
		this.membershipId = membershipId;
	}
	public int getLoyaltyPoints() {
		return loyaltyPoints;
	}
	public void setLoyaltyPoints(int loyaltyPoints) {
		this.loyaltyPoints = loyaltyPoints;
	}
	
	
}
