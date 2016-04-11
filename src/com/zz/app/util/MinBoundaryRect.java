package com.zz.app.util;

public class MinBoundaryRect {
	public double Ea = 6378137; // equator radius
	public double Eb = 6356725; // polar radius
	public double maxLatitude;
	public double minLatitude;
	public double maxLongitude;
	public double minLongitude;

	public MinBoundaryRect(double centralLatitude, double centralLongitude, double distance) {
		maxLatitude = getLatitude(centralLatitude, distance, 0);
		minLatitude = getLatitude(centralLatitude, distance, 180);
		maxLongitude = getLongitude(centralLatitude, centralLongitude, distance, 90);
		minLongitude = getLongitude(centralLatitude, centralLongitude, distance, 270);
	}

	private double getLatitude(double centralLatitude, double distance, double angle) {
		// double dx = distance * 1000 * Math.sin(angle * Math.PI / 180.0);
		double dy = distance * 1000 * Math.cos(angle * Math.PI / 180.0);
		double ec = Eb + (Ea - Eb) * (90.0 - centralLatitude) / 90.0;
		// double ed = ec * Math.cos(centralLatitude * Math.PI / 180);
		// double newLon = (dx / ed + centralLongitude * Math.PI / 180.0) *
		// 180.0 / Math.PI;
		double newLat = (dy / ec + centralLatitude * Math.PI / 180.0) * 180.0 / Math.PI;
		return newLat;
	}

	private double getLongitude(double centralLatitude, double centralLongitude, double distance, double angle) {
		double dx = distance * 1000 * Math.sin(angle * Math.PI / 180.0);
		// double dy = distance * 1000 * Math.cos(angle * Math.PI / 180.0);
		double ec = Eb + (Ea - Eb) * (90.0 - centralLatitude) / 90.0;
		double ed = ec * Math.cos(centralLatitude * Math.PI / 180);
		double newLon = (dx / ed + centralLongitude * Math.PI / 180.0) * 180.0 / Math.PI;
		// double newLat = (dy / ec + centralLatitude * Math.PI / 180.0) * 180.0
		// / Math.PI;
		return newLon;
	}

	public static void main(String[] args) {
		double latOrig = 40.0808970000, lonOrig = 116.3310870000;
		MinBoundaryRect mbr = new MinBoundaryRect(latOrig, lonOrig, 8);
		System.out.println(latOrig + ", " + lonOrig);
		System.out.println(mbr.minLatitude + ", " + mbr.minLongitude);
		System.out.println(mbr.maxLatitude + ", " + mbr.maxLongitude);
	}
}
