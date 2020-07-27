package com.fpg.ec.wsServer.action;

import java.util.Hashtable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fpg.ec.group.bo.GroupBO;
import com.fpg.ec.group.model.Dp;
import com.fpg.ec.group.model.condition.DpCondition;
import com.fpg.ec.utility.StringUtil;

@Service
public class DpService {
	@Autowired
	GroupBO groupUtilBO;

	public final StringUtil strUtil = new StringUtil();
	public final ObjectMapper mapper = new ObjectMapper();

	/**
	 * 部門資料新增
	 */
	public String dpCreate(@RequestBody String json) throws Exception {
		// 回傳資料
		ObjectNode returnObj = mapper.createObjectNode();

		// 錯誤訊息
		StringBuffer errMsg = new StringBuffer();
		
		try{
			// 字串轉回JSON
			JsonNode obj = mapper.readTree(json);

			// 檢核資料
			this.validateDpCreate(obj, errMsg, true);
			if (errMsg.toString().length() > 0) {
				this.returnError(returnObj, errMsg.toString());
			} else {
				// 轉換成物件
				Hashtable<String, String> iHashtable = mapper.convertValue(obj, Hashtable.class);
				Dp iDp = new Dp(iHashtable);
				groupUtilBO.saveADp(iDp);
				this.returnSuccess(returnObj, "部門資料新增成功");
			}
			
		}catch(Exception e){
			this.returnError(returnObj, e.toString());
		}
		
		return returnObj.toString();
	}
	
	/**
	 * 部門資料更新
	 */
	public String dpUpdate(@RequestBody String json) throws Exception {
		// 回傳資料
		ObjectNode returnObj = mapper.createObjectNode();

		// 錯誤訊息
		StringBuffer errMsg = new StringBuffer();
		
		try{
			// 字串轉回JSON
			JsonNode obj = mapper.readTree(json);

			// 檢核資料
			this.validateDpCreate(obj, errMsg, false);
			if (errMsg.toString().length() > 0) {
				this.returnError(returnObj, errMsg.toString());
			} else {
				// 轉換成物件
				Hashtable<String, String> iHashtable = mapper.convertValue(obj, Hashtable.class);
				Dp iDp = new Dp(iHashtable);
				
				//依據部門代號取得原有資料
				DpCondition iDpCondition = new DpCondition();
				iDpCondition.setDpid(iDp.getDpid());
				Dp dbDp = groupUtilBO.findDpList(null, null, iDpCondition).getDp(0);
				
				//將PK放入做資料更新
				iDp.setXuid(dbDp.getXuid());
				
				groupUtilBO.saveADp(iDp);
				this.returnSuccess(returnObj, "部門資料更新成功");
			}
			
		}catch(Exception e){
			this.returnError(returnObj, e.toString());
		}

		return returnObj.toString();
	}

	/**
	 * 部門資料刪除
	 */
	public String dpRemove(@RequestBody String json) throws Exception {
		// 回傳資料
		ObjectNode returnObj = mapper.createObjectNode();

		// 錯誤訊息
		StringBuffer errMsg = new StringBuffer();
		
		try{
			// 字串轉回JSON
			JsonNode obj = mapper.readTree(json);

			// 檢核資料
			if (obj.get("dpid") == null || obj.get("dpid").asText().length() == 0) {
				errMsg.append("請輸入部門代號;");
				this.returnError(returnObj, errMsg.toString());
			}else{
				DpCondition iDpCondition = new DpCondition();
				iDpCondition.setDpid(obj.get("dpid").asText());
				if(groupUtilBO.findDpListCountHasRec(iDpCondition)){
					Dp iDp = new Dp();
					iDp.setDpid(obj.get("dpid").asText());
					groupUtilBO.removeDpBySelective(iDp);
					this.returnSuccess(returnObj, "部門資料刪除成功");
				}else{
					errMsg.append("部門資料不存在;");
					this.returnError(returnObj, errMsg.toString());
				}
			}
		}catch(Exception e){
			this.returnError(returnObj, e.toString());
		}
		
		return returnObj.toString();
	}

	// 部門建檔檢核
	private void validateDpCreate(JsonNode obj, StringBuffer errMsg, boolean isCreate) {
		try {
			if (obj.get("dpid") == null || obj.get("dpid").asText().length() == 0) {
				errMsg.append("請輸入部門代號;");
			} else if(isCreate == true){
				DpCondition iDpCondition = new DpCondition();
				iDpCondition.setDpid(obj.get("dpid").asText());
				if (groupUtilBO.findDpListCountHasRec(iDpCondition) == true) {
					errMsg.append("部門代號己被使用;");
				}
			} else if(isCreate == false){
				DpCondition iDpCondition = new DpCondition();
				iDpCondition.setDpid(obj.get("dpid").asText());
				if (groupUtilBO.findDpListCountHasRec(iDpCondition) == false) {
					errMsg.append("部門資料不存在;");
				}
			}

			if (obj.get("dpnm") == null || obj.get("dpnm").asText().length() == 0) {
				errMsg.append("請輸入部門名稱;");
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg.append(e.getMessage());
		}
	}

	// 返回錯誤
	public void returnError(ObjectNode returnObj, String msg) {
		returnObj.put("code", "01");
		returnObj.put("message", msg);
	}

	// 返回成功
	public void returnSuccess(ObjectNode returnObj, String msg) {
		returnObj.put("code", "00");
		returnObj.put("message", msg);
	}
}
