package com.fpg.ec.wsServer.action.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpg.ec.utility.StringUtil;
import com.fpg.ec.wsServer.action.DpService;

@RestController
@RequestMapping(value = "/DpService", produces = "application/json; charset=utf-8")
public class DpController {
    @Autowired
    private DpService dpService;

	public final StringUtil strUtil = new StringUtil();
	public final ObjectMapper mapper = new ObjectMapper();

	/**
	 * 部門資料新增
	 */
	@RequestMapping(value = "/dpCreate", method = RequestMethod.POST)
	public String dpCreate(@RequestBody String json) throws Exception {
		return dpService.dpCreate(json);
	}
	
	/**
	 * 部門資料更新
	 */
	@RequestMapping(value = "/dpUpdate", method = RequestMethod.POST)
	public String dpUpdate(@RequestBody String json) throws Exception {
		return dpService.dpUpdate(json);
	}

	/**
	 * 部門資料刪除
	 */
	@RequestMapping(value = "/dpRemove", method = RequestMethod.POST)
	public String dpRemove(@RequestBody String json) throws Exception {
		return dpService.dpRemove(json);
	}

}
