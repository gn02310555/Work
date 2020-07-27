package com.fpg.ec.wsServer.action;

import java.util.Hashtable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fpg.ec.group.bo.GroupUtilBO;
import com.fpg.ec.group.model.Employee;
import com.fpg.ec.group.model.condition.DpCondition;
import com.fpg.ec.group.model.condition.EmployeeCondition;
import com.fpg.ec.group.model.list.EmployeeList;
import com.fpg.ec.utility.StringUtil;

@Service
public class EmpService {
	@Autowired
	GroupUtilBO groupUtilBO;

	public final StringUtil strUtil = new StringUtil();
	public final ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * 員工資料查詢
	 */
	public String empSearch(@RequestBody String json) throws Exception {
		// 回傳資料
		ObjectNode returnObj = mapper.createObjectNode();

		// 字串轉回JSON
		JsonNode obj = mapper.readTree(json);

		// 轉換成物件
		Hashtable<String, String> iHashtable = mapper.convertValue(obj, Hashtable.class);
		EmployeeCondition iEmployeeCondition = new EmployeeCondition(iHashtable);
		
		String strStartCnt = null;
		String strEndCnt = null;
		if(obj.get("startcnt") != null && obj.get("startcnt").asText().length() > 0){
			strStartCnt = obj.get("startcnt").asText();
		}
		if(obj.get("endcnt") != null && obj.get("endcnt").asText().length() > 0){
			strEndCnt = obj.get("endcnt").asText();
		}
		
		EmployeeList iEmployeeList = groupUtilBO.findEmployeeList(strStartCnt, strEndCnt, iEmployeeCondition);
		
		Employee iEmployee = null;
		ArrayNode employees = mapper.createArrayNode();
		for(int i = 0 ; i < iEmployeeList.size() ; i++){
			iEmployee = iEmployeeList.getEmployee(i);
			
			ObjectNode employee = mapper.createObjectNode();
			employee.put("empid", iEmployee.getEmpid()); 			
			employee.put("empnm", iEmployee.getEmpnm()); 				
			employee.put("dpid", iEmployee.getDpid()); 		
			employee.put("gender", iEmployee.getGender()); 	
			employee.put("tel", iEmployee.getTel()); 								
			employee.put("addr", iEmployee.getAddr()); 			
			employee.put("age", iEmployee.getAge()); 	
			employee.put("gftm", iEmployee.getGftm()); 	
			employee.put("txtm", iEmployee.getTxtm()); 	
			employees.add(employee);
		}
		
		returnObj.putArray("employees").addAll(employees);
    	this.returnSuccess(returnObj, "員工資料查詢成功");
		
		return returnObj.toString();
	}
	

	/**
	 * 員工資料新增
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
			this.validateEmpCreate(obj, errMsg, true);
			if (errMsg.toString().length() > 0) {
				this.returnError(returnObj, errMsg.toString());
			} else {
				// 轉換成物件
				Hashtable<String, String> iHashtable = mapper.convertValue(obj, Hashtable.class);
				Employee iEmployee = new Employee(iHashtable);
				
				groupUtilBO.saveAEmployee(iEmployee);
				this.returnSuccess(returnObj, "員工資料新增成功");
			}
			
		}catch(Exception e){
			this.returnError(returnObj, e.toString());
		}
		
		return returnObj.toString();
	}
	
	/**
	 * 員工資料更新
	 */
	public String empUpdate(@RequestBody String json) throws Exception {
		// 回傳資料
		ObjectNode returnObj = mapper.createObjectNode();

		// 錯誤訊息
		StringBuffer errMsg = new StringBuffer();
		
		try{
			// 字串轉回JSON
			JsonNode obj = mapper.readTree(json);

			// 檢核資料
			this.validateEmpCreate(obj, errMsg, false);
			if (errMsg.toString().length() > 0) {
				this.returnError(returnObj, errMsg.toString());
			} else {
				// 轉換成物件
				Hashtable<String, String> iHashtable = mapper.convertValue(obj, Hashtable.class);
				Employee iEmployee = new Employee(iHashtable);
				
				//依據員工編號取得原有資料
				EmployeeCondition iEmployeeCondition = new EmployeeCondition();
				iEmployeeCondition.setEmpid(iEmployee.getEmpid());
				Employee dbEmployee = groupUtilBO.findEmployeeList(null, null, iEmployeeCondition).getEmployee(0);
				
				//將PK放入做資料更新
				iEmployee.setXuid(dbEmployee.getXuid());
				
				groupUtilBO.saveAEmployee(iEmployee);
				this.returnSuccess(returnObj, "員工資料更新成功");
			}
			
		}catch(Exception e){
			this.returnError(returnObj, e.toString());
		}

		return returnObj.toString();
	}

	/**
	 * 員工資料刪除
	 */
	public String empRemove(@RequestBody String json) throws Exception {
		// 回傳資料
		ObjectNode returnObj = mapper.createObjectNode();

		// 錯誤訊息
		StringBuffer errMsg = new StringBuffer();
		
		try{
			// 字串轉回JSON
			JsonNode obj = mapper.readTree(json);

			// 檢核資料
			if (obj.get("empid") == null || obj.get("empid").asText().length() == 0) {
				errMsg.append("請輸入員工編號;");
				this.returnError(returnObj, errMsg.toString());
			}else{
				EmployeeCondition iEmployeeCondition = new EmployeeCondition();
				iEmployeeCondition.setEmpid(obj.get("empid").asText());
				if(groupUtilBO.findEmployeeListCountHasRec(iEmployeeCondition)){
					Employee iEmployee = new Employee();
					iEmployee.setEmpid(obj.get("empid").asText());
					groupUtilBO.removeEmployeeBySelective(iEmployee);
					this.returnSuccess(returnObj, "員工資料刪除成功");
				}else{
					errMsg.append("員工資料不存在;");
					this.returnError(returnObj, errMsg.toString());
				}
			}
		}catch(Exception e){
			this.returnError(returnObj, e.toString());
		}
		
		return returnObj.toString();
	}

	// 員工建檔檢核
	private void validateEmpCreate(JsonNode obj, StringBuffer errMsg, boolean isCreate) {
		try {
			if (obj.get("empid") == null || obj.get("empid").asText().length() == 0) {
				errMsg.append("請輸入員工編號;");
			} else if(isCreate == true){
				EmployeeCondition iEmployeeCondition = new EmployeeCondition();
				iEmployeeCondition.setEmpid(obj.get("empid").asText());
				if (groupUtilBO.findEmployeeListCountHasRec(iEmployeeCondition) == true) {
					errMsg.append("員工編號己被使用;");
				}
			} else if(isCreate == false){
				EmployeeCondition iEmployeeCondition = new EmployeeCondition();
				iEmployeeCondition.setEmpid(obj.get("empid").asText());
				if (groupUtilBO.findEmployeeListCountHasRec(iEmployeeCondition) == false) {
					errMsg.append("員工編號不存在;");
				}
			}

			if (obj.get("dpid") == null || obj.get("dpid").asText().length() == 0) {
				errMsg.append("請輸入部門代號;");
			}else{
				DpCondition iDpCondition = new DpCondition();
				iDpCondition.setDpid(obj.get("dpid").asText());
				if (groupUtilBO.findDpListCountHasRec(iDpCondition) == false) {
					errMsg.append("部門資料不存在;");
				}
			}
			
			if (obj.get("empnm") == null || obj.get("empnm").asText().length() == 0) {
				errMsg.append("請輸入員工姓名;");
			}
			if (obj.get("gender") == null || obj.get("gender").asText().length() == 0) {
				errMsg.append("請輸入性別;");
			}
			if (obj.get("tel") == null || obj.get("tel").asText().length() == 0) {
				errMsg.append("請輸入電話;");
			}
			if (obj.get("addr") == null || obj.get("addr").asText().length() == 0) {
				errMsg.append("請輸入地址;");
			}
			if (obj.get("age") == null || obj.get("age").asText().length() == 0) {
				errMsg.append("請輸入年齡;");
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
