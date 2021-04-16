package com.gadgetbadget.user.model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;

import com.gadgetbadget.user.util.JsonResponseBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * This class represents consumers and is a type of users. Performs 
 * database operations related to consumers, thus Extends the DBHandler.
 * 
 * @author Ishara_Dissanayake
 */
public class Consumer extends User{
	//Insert Consumer
	public JsonObject insertConsumer(String username, String password, String role_id, String first_name, String last_name, String gender, String primary_email, String primary_phone) {
		int status = 0;

		try {
			Connection conn = getConnection();
			if (conn == null) {
				return new JsonResponseBuilder().getJsonErrorResponse("Operation has been terminated due to a database connectivity issue.");
			}

			CallableStatement callableStmt = conn.prepareCall("{call sp_insert_consumer(?, ?, ?, ?, ?, ?, ?, ?, ?)}");

			callableStmt.registerOutParameter(9, Types.INTEGER);

			callableStmt.setString(1, username);
			callableStmt.setString(2, password);
			callableStmt.setString(3, role_id);
			callableStmt.setString(4, first_name);
			callableStmt.setString(5, last_name);
			callableStmt.setString(6, gender);
			callableStmt.setString(7, primary_email);
			callableStmt.setString(8, primary_phone);

			callableStmt.execute();

			status = (int) callableStmt.getInt(9);		

			if(status > 0) {
				return new JsonResponseBuilder().getJsonSuccessResponse("Consumer Inserted successfully.");
			} else {
				return new JsonResponseBuilder().getJsonFailedResponse("Unable to Insert Consumer.");
			}
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
			return new JsonResponseBuilder().getJsonExceptionResponse("Error occurred while inserting Consumer. Exception Details:" + ex.getMessage());
		}
	}

	//Read Consumer
	public JsonObject readConsumers() {
		JsonObject result = null;
		JsonArray resultArray = new JsonArray();
		try
		{
			Connection conn = getConnection();
			if (conn == null) {
				return new JsonResponseBuilder().getJsonErrorResponse("Operation has been terminated due to a database connectivity issue.");
			}

			String query = "SELECT u.user_id, u.role_id, u.first_name, u.last_name, u.gender, u.primary_email, u.primary_phone FROM `user` u, `consumer` c WHERE u.user_id=c.consumer_id;";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			if(!rs.isBeforeFirst()) {
				return new JsonResponseBuilder().getJsonSuccessResponse("Request Processed. No Consumers found.");
			}

			while (rs.next())
			{
				JsonObject recordObject = new JsonObject();
				recordObject.addProperty("user_id", rs.getString("user_id"));
				recordObject.addProperty("first_name", rs.getString("first_name"));
				recordObject.addProperty("last_name", rs.getString("last_name"));
				recordObject.addProperty("gender", rs.getString("gender"));
				recordObject.addProperty("primary_email", rs.getString("primary_email"));
				recordObject.addProperty("primary_phone", rs.getString("primary_phone"));
				resultArray.add(recordObject);
			}
			conn.close();

			result = new JsonObject();
			result.add("consumers", resultArray);

		}
		catch (Exception ex)
		{
			System.err.println(ex.getMessage());
			return new JsonResponseBuilder().getJsonExceptionResponse("Error occurred while reading consumers. Exception Details:" + ex.getMessage());
		}
		return result;
	}

	//Update Consumer
	public JsonObject updateConsumer(String user_id,String username, String password, String first_name, String last_name, String gender, String primary_email, String primary_phone)
	{
		int status = 0;

		try {
			Connection conn = getConnection();
			if (conn == null) {
				return new JsonResponseBuilder().getJsonErrorResponse("Operation has been terminated due to a database connectivity issue.");
			}

			CallableStatement callableStmt = conn.prepareCall("{call sp_update_consumer(?, ?, ?, ?, ?, ?, ?, ?, ?)}");

			//output parameter registering
			callableStmt.registerOutParameter(9, Types.INTEGER);

			//Input parameter binding
			callableStmt.setString(1, user_id);
			callableStmt.setString(2, username);
			callableStmt.setString(3, password);
			callableStmt.setString(4, first_name);
			callableStmt.setString(5, last_name);
			callableStmt.setString(6, gender);
			callableStmt.setString(7, primary_email);
			callableStmt.setString(8, primary_phone);

			callableStmt.execute();

			//test
			status = (int) callableStmt.getInt(9);		

			if(status > 0) {
				return new JsonResponseBuilder().getJsonSuccessResponse("Consumer " + user_id + " Updated successfully.");
			} else {
				return new JsonResponseBuilder().getJsonFailedResponse("Unable to Update Consumer " + user_id +".");
			}
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
			return new JsonResponseBuilder().getJsonExceptionResponse("Error occurred while updating Consumer " + user_id +". Exception Details:" + ex.getMessage());
		}
	}

	//Delete Consumer
	public JsonObject deleteConsumer(String user_id) {
		int status = 0;

		try {
			Connection conn = getConnection();
			if (conn == null) {
				return new JsonResponseBuilder().getJsonErrorResponse("Operation has been terminated due to a database connectivity issue.");
			}

			CallableStatement callableStmt = conn.prepareCall("{call sp_delete_consumer(?, ?)}");

			//output parameter registering
			callableStmt.registerOutParameter(2, Types.INTEGER);

			//Input parameter binding
			callableStmt.setString(1, user_id);

			callableStmt.execute();

			//test
			status = (int) callableStmt.getInt(2);	

			if(status > 0) {
				return new JsonResponseBuilder().getJsonSuccessResponse("Consumer " + user_id + " deleted successfully.");
			} else {
				return new JsonResponseBuilder().getJsonFailedResponse("Unable to Delete Consumer "+ user_id +".");
			}
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
			return new JsonResponseBuilder().getJsonExceptionResponse("Error occurred while deleting consumer. Exception Details:" + ex.getMessage());
		}
	}
}
