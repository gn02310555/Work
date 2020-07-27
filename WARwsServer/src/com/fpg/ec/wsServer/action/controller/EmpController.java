package com.fpg.ec.wsServer.action.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpg.ec.group.bo.GroupUtilBO;
import com.fpg.ec.utility.StringUtil;
import com.fpg.ec.wsServer.action.EmpService;

@RestController
@RequestMapping(value = "/EmpService", produces = "application/json; charset=utf-8")
public class EmpController {
	@Autowired
    private EmpService empService;
	
	@Autowired
	GroupUtilBO groupUtilBO;

	public final StringUtil strUtil = new StringUtil();
	public final ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * 員工資料查詢
	 */
	@RequestMapping(value = "/empSearch", method = RequestMethod.POST)
	public String empSearch(@RequestBody String json) throws Exception {
		return empService.empSearch(json);
	}
	

	/**
	 * 員工資料新增
	 */
	@RequestMapping(value = "/empCreate", method = RequestMethod.POST)
	public String dpCreate(@RequestBody String json) throws Exception {
		return empService.dpCreate(json);
	}
	
	/**
	 * 員工資料更新
	 */
	@RequestMapping(value = "/empUpdate", method = RequestMethod.POST)
	public String empUpdate(@RequestBody String json) throws Exception {
		return empService.empUpdate(json);
	}

	/**
	 * 員工資料刪除
	 */
	@RequestMapping(value = "/empRemove", method = RequestMethod.POST)
	public String empRemove(@RequestBody String json) throws Exception {
		return empService.empRemove(json);
	}

}
