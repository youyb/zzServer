package com.zz.app.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zz.app.db.QueryRecord;
import com.zz.app.db.SaveRecord;
import com.zz.app.db.SaveUserInfo;
import com.zz.app.util.MinBoundaryRect;

@Controller
@RequestMapping({ "/report" })
public class ProcessAppRequest {
	@RequestMapping(value = "/image", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Map<String, Object> processFileUpload(@RequestBody String json) {
		System.out.println(json);
		Map<String, Object> retMap = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> jsonMap = mapper.readValue(json, Map.class);
			if (jsonMap != null && jsonMap.size() > 0) {

				String phoneNum = jsonMap.get("phone_num").toString();
				String record_type = jsonMap.get("record_type").toString();
				String record_category = jsonMap.get("record_category").toString();
				String record_time = jsonMap.get("record_time").toString();
				String record_desc = jsonMap.get("record_desc").toString();
				String longitude = jsonMap.get("longitude").toString();
				String latitude = jsonMap.get("latitude").toString();
				String location = jsonMap.get("location").toString();
				String fileList = jsonMap.get("fileList").toString();

				System.out.println(phoneNum);
				System.out.println(record_type);
				System.out.println(record_category);
				System.out.println(record_time);
				System.out.println(record_desc);
				System.out.println(longitude);
				System.out.println(latitude);
				System.out.println(location);
				System.out.println(fileList);

				// process multi-image upload in json body
				JSONObject jsonObj = new JSONObject(json);
				System.out.println(jsonObj.get("fileList").toString());
				JSONArray jsonArray = jsonObj.getJSONArray("fileList");

				SaveRecord sr = new SaveRecord();
				// sr.queryRecordCategory();
				sr.queryRecord();
				sr.queryTask();
				sr.insertNewRecordAndAssignTask(phoneNum, record_type, record_category, record_time, record_desc,
						longitude, latitude, location, jsonArray);
				sr.queryRecord();
				sr.queryTask();

				// send ok to IOS client
				retMap = sendSucessRsp();
			}

		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retMap;
	}

	@RequestMapping(value = "/nearby", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Map<String, Object> getNearbyRecordByRadius(@RequestBody String json) {
		System.out.println(json);
		Map<String, Object> retMap = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> jsonMap = mapper.readValue(json, Map.class);
			if (jsonMap != null && jsonMap.size() > 0) {
				String longitude = jsonMap.get("longitude").toString();
				String latitude = jsonMap.get("latitude").toString();
				String radius = jsonMap.get("radius").toString();

				System.out.println(latitude + ", " + longitude);
				System.out.println(radius);

				MinBoundaryRect mbr = new MinBoundaryRect(Double.parseDouble(latitude), Double.parseDouble(longitude),
						Double.parseDouble(radius));
				System.out.println(mbr.minLatitude + ", " + mbr.maxLatitude);
				System.out.println(mbr.minLongitude + ", " + mbr.maxLongitude);
				QueryRecord qr = new QueryRecord();
				retMap = qr.queryRecordByMBR(mbr.minLatitude, mbr.maxLatitude, mbr.minLongitude, mbr.maxLongitude);

			}

		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retMap;
	}

	@RequestMapping(value = "/token", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Map<String, Object> processUserLogin(@RequestBody String json) {
		System.out.println(json);
		Map<String, Object> retMap = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> jsonMap = mapper.readValue(json, Map.class);
			if (jsonMap != null && jsonMap.size() > 0) {
				String phone_num = jsonMap.get("phone_num").toString();
				String phone_type = jsonMap.get("phone_type").toString();
				String token = jsonMap.get("token").toString();

				System.out.println(phone_num);
				System.out.println(phone_type);
				System.out.println(token);

				SaveUserInfo sur = new SaveUserInfo();
				sur.queryUserInfo();
				sur.saveAll(phone_num, phone_type, token);
				sur.queryUserInfo();
			}

		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retMap;
	}

	public Map<String, Object> sendSucessRsp() {
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		// Map<String, Object> successMap = new LinkedHashMap<String, Object>();
		// successMap.put("status", "saved");
		resultMap.put("code", "200");
		// resultMap.put("success", successMap);
		return resultMap;
	}
}
