/**
 * 
 */
package com.flipkart.util;

import java.util.*;
import javax.ws.rs.core.*;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

/**
 * @author JEDI04
 *
 */
public class ResponseHelpers {
	private static Map<String, Object> mapify(boolean flag, String msg, Object payload) {
		Map<String, Object> mapper = new HashMap<>();
		mapper.put("flag", flag);
		mapper.put("message", msg);
		mapper.put("payload", payload);
		return mapper;
	}
	
	public static Response success(Object payload, String msg) {
		JsonElement res = new Gson().toJsonTree(mapify(true, msg, payload));
		return Response.status(200).entity(res.toString()).build();
	}
	
	public static Response badRequest(Object payload, String msg) {
		JsonElement res = new Gson().toJsonTree(mapify(false, msg, payload));
		return Response.status(400).entity(res.toString()).build();
	}
	
	public static Response somethingWentWrong(Object payload) {
		JsonElement res = new Gson().toJsonTree(mapify(false, "Some Internal Error Occured", payload));
		return Response.status(500).entity(res.toString()).build();
	}
	
	public static Response successPost(Object payload, String msg) {
		JsonElement res = new Gson().toJsonTree(mapify(true, msg, payload));
		return Response.status(201).entity(res.toString()).build();
	}
	
}
