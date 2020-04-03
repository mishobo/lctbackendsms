/**
 * 
 */
package com.compulynx.compas.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.compulynx.compas.dal.RegionDal;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.Area;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Location;
import com.compulynx.compas.models.Zone;

/**
 * @author Anita
 *
 */
public class RegionDalImpl implements RegionDal{
	private DataSource dataSource;

	public RegionDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	@Override
	public CompasResponse UpdateZone(Zone zone) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();

			if (zone.zoneId == 0) {
				if (GetZoneByCode(zone.zoneCode))
				{
					return new CompasResponse(201, "Zone Code Already Exists");
				}
				if (GetZoneByName(zone.zoneName))
				{
					return new CompasResponse(201, "Zone Name Already Exists");
				}
				preparedStatement = connection
						.prepareStatement(Queryconstants.insertZone);
				preparedStatement.setString(1, zone.zoneCode);
				preparedStatement.setString(2, zone.zoneName);
				preparedStatement.setBoolean(3, zone.active);
				preparedStatement.setInt(4, zone.createdBy);
				preparedStatement.setTimestamp(5, new java.sql.Timestamp(
						new java.util.Date().getTime()));
			} else {
				if (CheckZoneNameByCode(zone.zoneName,zone.zoneCode))
				{
					return new CompasResponse(201, "Zone Name Already Exists");
				}
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateZone);
				preparedStatement.setString(1, zone.zoneName);
				preparedStatement.setBoolean(2, zone.active);
				preparedStatement.setInt(3, zone.createdBy);
				preparedStatement.setTimestamp(4, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(5, zone.zoneId);

			}
			return (preparedStatement.executeUpdate() > 0) ? new CompasResponse(
					200, "Records Updated") : new CompasResponse(201,
					"Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0)
			{
				return new CompasResponse(201, "Zone Name Already Exists");
			}
			else
			{
				return new CompasResponse(404, "Exception Occured");	
			}
		}catch (Exception ex) {
			ex.printStackTrace();
			return new CompasResponse(404, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	@Override
	public List<Zone> GetZones() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count=1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getZones);
			resultSet = preparedStatement.executeQuery();
			List<Zone> zones = new ArrayList<Zone>();
			while (resultSet.next()) {
				zones.add(new Zone(resultSet.getInt("ID"),resultSet
						.getString("zone_Code"), resultSet
						.getString("zone_Name"),resultSet.getBoolean("Active"),count));
				count++;
			}
			return zones;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	public boolean GetZoneByCode(String zoneCode) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getZoneByCode);
			preparedStatement.setString(1, zoneCode);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	public boolean GetZoneByName(String zoneName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getZoneByName);
			preparedStatement.setString(1, zoneName);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	public boolean CheckZoneNameByCode(String zoneCode,String zoneName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getZoneNameByCode);
			preparedStatement.setString(1, zoneName);
			preparedStatement.setString(2, zoneCode);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	@Override
	public CompasResponse UpdateArea(Area area) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();

			if (area.areaId== 0) {
				if (CheckAreaByCode(area.areaCode,area.zoneId))
				{
					return new CompasResponse(201, "Area Code Already Exists");
				}
				if (CheckAreaByName(area.areaName,area.zoneId))
				{
					return new CompasResponse(201, "Area Name Already Exists");
				}
				preparedStatement = connection
						.prepareStatement(Queryconstants.insertArea);
				preparedStatement.setString(1, area.areaCode);
				preparedStatement.setString(2, area.areaName);
				preparedStatement.setInt(3, area.zoneId);
				preparedStatement.setBoolean(4, area.active);
				preparedStatement.setInt(5, area.createdBy);
				preparedStatement.setTimestamp(6, new java.sql.Timestamp(
						new java.util.Date().getTime()));
			} else {
				if (CheckAreaNameByCode(area.areaName,area.areaCode,area.zoneId))
				{
					return new CompasResponse(201, "Area Name Already Exists");
				}
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateArea);
				preparedStatement.setString(1, area.areaName);
				preparedStatement.setInt(2, area.zoneId);
				preparedStatement.setBoolean(3, area.active);
				preparedStatement.setInt(4, area.createdBy);
				preparedStatement.setTimestamp(5, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(6, area.areaId);
				
			}
			return (preparedStatement.executeUpdate() > 0) ? new CompasResponse(
					200, "Records Updated") : new CompasResponse(201,
					"Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0)
			{
				return new CompasResponse(201, "Area Name Already Exists");
			}
			else
			{
				return new CompasResponse(404, "Exception Occured");	
			}
		}catch (Exception ex) {
			ex.printStackTrace();
			return new CompasResponse(404, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	@Override
	public List<Area> GetAreas() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count=1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getAreas);
			resultSet = preparedStatement.executeQuery();
			List<Area> areas = new ArrayList<Area>();
			while (resultSet.next()) {
				areas.add(new Area(resultSet.getInt("ID"),resultSet
						.getString("area_Code"), resultSet
						.getString("area_Name"),resultSet.getInt("zone_ID"),resultSet
						.getString("zone_Name"),resultSet.getBoolean("Active"),count));
				count++;
			}
			return areas;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	public boolean CheckAreaByCode(String areaCode,int zoneId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getAreaByCode);
			preparedStatement.setString(1, areaCode);
			preparedStatement.setInt(2, zoneId);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	public boolean CheckAreaByName(String areaName,int zoneId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getAreaByName);
			preparedStatement.setString(1, areaName);
			preparedStatement.setInt(2, zoneId);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	public boolean CheckAreaNameByCode(String areaName,String areaCode,int zoneId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getAreaNameByCode);
			preparedStatement.setString(1, areaName);
			preparedStatement.setString(2, areaCode);
			preparedStatement.setInt(3, zoneId);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	@Override
	public CompasResponse UpdateLocation(Location location) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			if (location.locationId == 0) {

				for (int i = 0; i < location.locationDetails.size(); i++) {
					if (checkLocationCode(location.locationDetails.get(i).locationCode,location.areaId)) {
						return new CompasResponse(201,
								"Location code already exists");
					}
					if (checkLocationName(location.locationDetails.get(i).locationName,location.areaId)) {
						return new CompasResponse(201,
								"Location name already exists");
					}
					
					preparedStatement = connection
							.prepareStatement(Queryconstants.insertLocation);
					preparedStatement.setString(1,
							location.locationDetails.get(i).locationCode);
					preparedStatement.setString(2,
							location.locationDetails.get(i).locationName);
					preparedStatement.setInt(3, location.areaId);
					preparedStatement.setBoolean(4, location.active);
					preparedStatement.setInt(5, location.createdBy);
					preparedStatement.setTimestamp(6, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					if (preparedStatement.executeUpdate() <= 0) {
						throw new Exception("Failed to Insert Location");
					}
				}
				return new CompasResponse(200, "Records Updated");

			} else {
				if (checkLocationNameByCode(location.locationName, location.locationCode,location.areaId)) {
					return new CompasResponse(201,
							"Location name already exists");
				}
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateLocation);
				preparedStatement.setString(1, location.locationName);
				preparedStatement.setInt(2, location.areaId);
				preparedStatement.setBoolean(3, location.active);
				preparedStatement.setInt(4, location.createdBy);
				preparedStatement.setTimestamp(5, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(6, location.locationId);
				return (preparedStatement.executeUpdate() > 0) ? new CompasResponse(
						200, "Records Updated") : new CompasResponse(201,
								"Nothing To Update");
			}

		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			return new CompasResponse(202, "Exception Occured");

		} catch (Exception ex) {
			ex.printStackTrace();
			return new CompasResponse(202, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}

	}

	@Override
	public List<Location> GetLocations() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getLocations);
			resultSet = preparedStatement.executeQuery();
			List<Location> locations = new ArrayList<Location>();
			while (resultSet.next()) {
				locations.add(new Location(resultSet.getInt("ID"), resultSet
						.getString("location_Code"),
						resultSet.getString("location_Name"), resultSet
						.getInt("area_id"), resultSet
						.getString("area_name"), resultSet
						.getInt("zone_id"), resultSet
						.getString("zone_name"), resultSet
						.getBoolean("active"), 200, count,resultSet
						.getString("status")));
				count++;
			}
			return locations;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	public List<Area> GetActiveArea() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getActiveArea);
		
			resultSet = preparedStatement.executeQuery();
			List<Area> wards = new ArrayList<Area>();
			while (resultSet.next()) {
				wards.add(new Area(resultSet.getInt("id"),
						resultSet.getString("area_Name")));
				count++;
			}
			return wards;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	public List<Zone> GetActiveRegion() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count=1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getActiveZones);
			resultSet = preparedStatement.executeQuery();
			List<Zone> zones = new ArrayList<Zone>();
			while (resultSet.next()) {
				zones.add(new Zone(resultSet.getInt("ID"),resultSet
						.getString("zone_Code"), resultSet
						.getString("zone_Name"),resultSet.getBoolean("Active"),count));
				count++;
			}
			return zones;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	public boolean checkLocationName(String locationName,int areaId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkLocationName);
			preparedStatement.setString(1, locationName);
			preparedStatement.setInt(2, areaId);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	public boolean checkLocationCode(String locationCode,int areaId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkLocationCode);
			preparedStatement.setString(1, locationCode);
			preparedStatement.setInt(2, areaId);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	public boolean checkLocationNameByCode(String marketName, String Code,int areaId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkLocationNameByCode);
			preparedStatement.setString(1, marketName);
			preparedStatement.setString(2, Code);
			preparedStatement.setInt(3, areaId);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
}
