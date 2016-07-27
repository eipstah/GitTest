package com.ericsson.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
@Path("/Service")
public class GroupService {
	static final Logger LOGGER = Logger.getLogger(GroupService.class);
	@Context
	private ServletContext context;
	ArrayList<String> usernames, usernamedetails, returnList;
	String username, returnJson;

	@SuppressWarnings("unchecked")
	@GET
	@Consumes("application/xml")
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/getGroups")
	public String getGroupInfoFromGetReq(@Context HttpServletRequest request,
			@Context HttpHeaders response) throws IOException {
		Map<String, Map<String, ArrayList<String>>> indexMap = new HashMap<String, Map<String, ArrayList<String>>>();
		Map<String, ArrayList<String>> ericollEridocCombinedMap = new HashMap<String, ArrayList<String>>();
		ArrayList<String> mergedGroupsList = new ArrayList<String>();
		indexMap = (Map<String, Map<String, ArrayList<String>>>) context
				.getAttribute("indexMap");
		LOGGER.info("indexMap size:::" + indexMap.size());
		returnList = new ArrayList<String>();
		String username = request.getParameter("User");
		String indexName = "";
		if (username.contains("\\")) {
			indexName = username.split("\\\\")[1];
		}
		
		 else
		 { indexName = username; }
		 
		String indexKey = String.valueOf(indexName.charAt(1)).toUpperCase();// m.group(1).toUpperCase();
		if (indexKey != null) {
			ericollEridocCombinedMap = indexMap.get(indexKey);
			ArrayList<String> ericollGrpList = new ArrayList<String>();
			if (ericollEridocCombinedMap.containsKey(username.toUpperCase())) {
				ericollGrpList = ericollEridocCombinedMap.get(username
						.toUpperCase());
				if (!ericollGrpList.contains("ERICSSON_ALL"))
					ericollGrpList.add("ERICSSON_ALL");
			} else
				ericollGrpList.add("ERICSSON_ALL");
			mergedGroupsList.addAll(ericollGrpList);
			if (username.contains("\\")) {
				ArrayList<String> eridocGrpList = ericollEridocCombinedMap
						.get(username.toUpperCase().split("\\\\")[1]);
				if (eridocGrpList != null) {
					if (mergedGroupsList.contains("ERICSSON_ALL")
							&& eridocGrpList.contains("ERICSSON_ALL")) {
						mergedGroupsList.remove("ERICSSON_ALL");
					}
					mergedGroupsList.addAll(eridocGrpList);
				}
			}
		}
		returnJson = new Gson().toJson(mergedGroupsList);
		returnList.add(returnJson);
		//System.runFinalization();
		return returnList.toString();
	}
}
