package data.members;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.parse4j.ParseException;
import org.parse4j.ParseGeoPoint;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;

import data.management.DBManager;

/**
 * @author Toma
 * 		   Inbal Matityahu
 * @since 12.11.16 This class represent a parking slot
 */
public class ParkingSlot extends dbMember {

	// The slot's name (id). Should be a unique value.
	private String name;

	// The slot's status. Can be either free, taken or unavailable
	private ParkingSlotStatus status;

	// The slot's color. Can be any sticker color
	private StickersColor color;

	// The slot's location
	private MapLocation location;

	// The slot's default color
	private StickersColor defaultColor;

	// The slot's endTime. if null the color permanent, else temporary
	private Date endTime;
	
	private final String objectClass = "ParkingSlot";

	/* Constructors */

	// Create a new parking slot. Will result in a new slot in the DB.
	public ParkingSlot(final String name, final ParkingSlotStatus status, final StickersColor color, final StickersColor defaultColor,
			final MapLocation location, final Date endTime) throws ParseException {
		
		DBManager.initialize();
		
		validateArgument(status, color, defaultColor, location);
		Map<String, Object> fields = new HashMap<String, Object>(), keyValues = new HashMap<String, Object>();
		fields.put("status", status.ordinal());
		fields.put("color", color.ordinal());
		fields.put("defaultColor", defaultColor.ordinal());
		fields.put("location", new ParseGeoPoint(location.getLat(), location.getLon()));
		fields.put("endTime", endTime);
		
		keyValues.put("name", name);
		DBManager.insertObject(objectClass, keyValues, fields);
	}

	public ParkingSlot(final ParseObject obj) throws ParseException {
		//TODO
		DBManager.initialize();
		parseObject = obj;
		name = parseObject.getString("name");
		status = ParkingSlotStatus.values()[parseObject.getInt("status")];
		color = StickersColor.values()[parseObject.getInt("color")];
		final ParseGeoPoint geo = parseObject.getParseGeoPoint("location");
		location = new MapLocation(geo.getLatitude(), geo.getLongitude());
		defaultColor = StickersColor.values()[parseObject.getInt("defaultColor")];
		endTime = parseObject.getDate("endTime");
		objectId = parseObject.getObjectId();
		parseObject.save();
	}

	public ParkingSlot(final String name) throws ParseException {
		DBManager.initialize();

		Map<String, Object> keys = new HashMap<>();
		keys.put("name", name);
		Map<String,Object> returnV = DBManager.getObjectFieldsByKey(objectClass, keys);
		
		this.color=StickersColor.valueOf(returnV.get("color") + "");
		this.defaultColor= StickersColor.valueOf(returnV.get("defaultColor") + "");
		this.endTime= (Date)returnV.get("endTime");
		this.location=(MapLocation)returnV.get("location");
		this.name=name;
		this.status=ParkingSlotStatus.valueOf(returnV.get("status") + "");
	}

	public String ParkingNameByLocation(final MapLocation l) {
		DBManager.initialize();

		Map<String, Object> keys = new HashMap<>();
		keys.put("location", new ParseGeoPoint(l.getLat(), l.getLon()));
		return DBManager.getObjectFieldsByKey(objectClass, keys).get("name") + "";
	}

	/* Getters */

	public String getName() {
		Map<String, Object> key = new HashMap<String, Object>();
		key.put("name", name);
		return DBManager.getObjectFieldsByKey("ParkingSlot", key).get("name") + "";
	}

	public ParkingSlotStatus getStatus(String name) {
		Map<String, Object> key = new HashMap<String, Object>();
		key.put("name", name);
		return ParkingSlotStatus.valueOf(DBManager.getObjectFieldsByKey("ParkingSlot", key).get("status") + "");
	}

	public StickersColor getColor() {
		Map<String, Object> key = new HashMap<String, Object>();
		key.put("name", name);
		return StickersColor.valueOf(DBManager.getObjectFieldsByKey("ParkingSlot", key).get("color") + "");
	}

	public MapLocation getLocation() {
		Map<String, Object> key = new HashMap<String, Object>();
		key.put("name", name);
		Map<String,Object> returnV = DBManager.getObjectFieldsByKey(objectClass, key);
		return new MapLocation(((MapLocation)returnV.get("locatoin")).getLat(), ((MapLocation)returnV.get("locatoin")).getLon());
	}

	public StickersColor getDefaultColor() {
		Map<String, Object> key = new HashMap<String, Object>();
		key.put("name", name);
		return StickersColor.valueOf(DBManager.getObjectFieldsByKey("ParkingSlot", key).get("defaultColor") + "");
	}

	public Date getEndTime() {
		Map<String, Object> key = new HashMap<String, Object>();
		key.put("name", name);
		return ((Date) DBManager.getObjectFieldsByKey(objectClass, key).get("endTime"));
	
	}

	/* Setters */

	private void setName(final String name) throws ParseException {
		//TODO: validateArgument
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("name", name);
		fields.put("color", this.color);
		fields.put("defaultColor", this.defaultColor);
		fields.put("status", this.status);
		fields.put("endTime", this.endTime);
		fields.put("location", this.location);
		DBManager.update(objectClass, fields);
	}

	private void setStatus(final ParkingSlotStatus s) throws ParseException {
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("name", this.name);
		fields.put("color", this.color);
		fields.put("defaultColor", this.defaultColor);
		fields.put("status", s);
		fields.put("endTime", this.endTime);
		fields.put("location", this.location);
		DBManager.update(objectClass, fields);
	}

	public void setColor(final StickersColor c) throws ParseException {
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("name", this.name);
		fields.put("color", c);
		fields.put("defaultColor", this.defaultColor);
		fields.put("status", this.status);
		fields.put("endTime", this.endTime);
		fields.put("location", this.location);
		DBManager.update(objectClass, fields);
	}

	private void setLocation(final MapLocation l) throws ParseException {
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("name", this.name);
		fields.put("color", this.color);
		fields.put("defaultColor", this.defaultColor);
		fields.put("status", this.status);
		fields.put("endTime", this.endTime);
		fields.put("location", l);
		DBManager.update(objectClass, fields);
	}

	private void setDefaultColor(final StickersColor defaultColor) throws ParseException {
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("name", this.name);
		fields.put("color", this.color);
		fields.put("defaultColor", defaultColor);
		fields.put("status", this.status);
		fields.put("endTime", this.endTime);
		fields.put("location", this.location);
		DBManager.update(objectClass, fields);
	}

	public void setEndTime(final Date endTime) throws ParseException {
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("name", this.name);
		fields.put("color", this.color);
		fields.put("defaultColor", this.defaultColor);
		fields.put("status", this.status);
		fields.put("endTime", endTime);
		fields.put("location", this.location);
		DBManager.update(objectClass, fields);
	}

	/* Methods */

	private void validateArgument(final ParkingSlotStatus s, final StickersColor c, final StickersColor defaultColor, final MapLocation l)
			throws IllegalArgumentException {
		if (s == null)
			throw new IllegalArgumentException("status can not be empty!");
		if (c == null)
			throw new IllegalArgumentException("color can not be empty!");
		if (defaultColor == null)
			throw new IllegalArgumentException("defaultColor can not be empty!");
		if (l == null)
			throw new IllegalArgumentException("location can not be empty!");
	}

	private ParseObject findContaingParkingArea() {
		//TODO: are we still want to hold to each parkingArea a list of slots?
		//or maybe for each slot to hold an area?
		final ParseQuery<ParseObject> $ = ParseQuery.getQuery("ParkingArea");
		$.whereEqualTo("parkingSlots", parseObject);
		try {
			return $.find().get(0);
		} catch (final ParseException ¢) {
			System.out.println("Could not find the containing area");
			¢.printStackTrace();
			return null;
		}
	}

	public String findContainingParkingArea() {
		return (String) findContaingParkingArea().get("name");
	}

	public void changeStatus(final ParkingSlotStatus newStatus) {
		try {
			setStatus(newStatus);
		} catch (final ParseException ¢) {
			System.out.println("could not set the slot's status");
			¢.printStackTrace();
		}
	};

	public void removeParkingSlotFromDB() throws ParseException {
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("status", status.ordinal());
		fields.put("color", color.ordinal());
		fields.put("defaultColor", defaultColor.ordinal());
		fields.put("location", new ParseGeoPoint(location.getLat(), location.getLon()));
		fields.put("endTime", endTime);
		fields.put("name", name);
		DBManager.deleteObject(objectClass, fields);
	}

	public void removeParkingSlotFromAreaAndDB() throws ParseException {
		new ParkingArea(findContainingParkingArea()).removeParkingSlot(this);
	}

	/***
	 * for now only delete from the DB the current parking
	 */
	@Override
	public void deleteParseObject() throws ParseException {
		//TODO: check if necessary
		final ParseQuery<ParseObject> query = ParseQuery.getQuery("PMUser");
		query.whereEqualTo("currentParking", getParseObject());
		try {
			final List<ParseObject> users = query.find();
			if (users != null) {
				users.get(0).remove("currentParking");
				users.get(0).save();
			}
		} catch (final Exception e) {
			return;
		}
		parseObject.delete();
	}

}
