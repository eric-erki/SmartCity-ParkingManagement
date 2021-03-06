package logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.parse4j.ParseGeoPoint;
import org.parse4j.ParseObject;

import data.management.DatabaseManager;
import data.management.DatabaseManagerImpl;
import data.members.StickersColor;
import util.Distance;

/**
 * 
 * @author Toma
 * @since 19.06.2017
 * 
 * The purpose of this class is to implement the logic behind the graph presentation
 */

public class Graph {
	private DatabaseManager manager;
	
	//in default constructor. create the normal dbm
	public Graph(){
		manager = new DatabaseManagerImpl();
	}
	
	//for testing, use a mocking dbm
	public Graph(DatabaseManager dbm){
		manager = dbm;
	}
	
	// This method will collect data about price vs. distance
	public Map<Double, Double> CreatePriceDistanceData(ParseGeoPoint destenation){
		manager.initialize();
		List<ParseObject> allParkingSlot = manager.getAllObjects("ParkingSlot", 600);
		Map<Double, Double> distanceVsPrice = new HashMap<Double,Double>();
		for (ParseObject p : allParkingSlot) {
			StickersColor rank = StickersColor.values()[p.getInt("rank")];
			double distance = Distance.AirDistance(p.getParseGeoPoint("location"), destenation);
			if (!distanceVsPrice.containsKey(distance))
				distanceVsPrice.put(distance, (new BasicBilling()).calculateCost(rank, distance));		
		}
		return distanceVsPrice;
	}
	
	// This method will collect data about price vs. rating
		public Map<Double, Double> CreatePriceRanttingData(ParseGeoPoint destenation){
			manager.initialize();
			List<ParseObject> allParkingSlot = manager.getAllObjects("ParkingSlot", 600);
			Map<Double, Double> ratingVsPrice = new HashMap<Double,Double>();
			Map<Double, Integer> ratingTimes = new HashMap<Double,Integer>();
			for (ParseObject p : allParkingSlot) {
				double rating = p.getDouble("rating");
				int numberOfVoters = p.getInt("numOfVoters");
				double score = numberOfVoters == 0 ? 0 : rating/numberOfVoters;
				StickersColor rank = StickersColor.values()[p.getInt("rank")];
				double distance = Distance.AirDistance(p.getParseGeoPoint("location"), destenation);
				if (ratingVsPrice.containsKey(score)){
					double combinePrice = ratingVsPrice.get(score)+(new BasicBilling()).calculateCost(rank, distance);
					ratingVsPrice.put(score, combinePrice);	
					ratingTimes.put(score, ratingTimes.get(score)+1);
				}
				else {
					ratingVsPrice.put(score, (new BasicBilling()).calculateCost(rank, distance));	
					ratingTimes.put(score, 1);
				}
			}
			for (Double rate : ratingTimes.keySet()) {
				ratingVsPrice.put(rate, ratingVsPrice.get(rate)/ratingTimes.get(rate)); 
			}
			return ratingVsPrice;
		}
}
