package com.fpg.ec.wsServer.action.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Hashtable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fpg.ec.group.bo.GroupBO;
import com.fpg.ec.wsServer.action.DpService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
//@ContextConfiguration("applicationContext.xml")
@ContextConfiguration(locations = { "classpath*:/WEB-INF/applicationContext.xml" })
public class DpServiceTest {
	public final ObjectMapper mapper = new ObjectMapper();
	
	@InjectMocks
    private DpService dpService;
	
	@Mock
	private GroupBO groupBO;
	
	@Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
	
	@Test
	public void dpCreateTest() throws Exception{
		ObjectNode obj = mapper.createObjectNode();
		obj.put("dpid", "4");
		obj.put("dpnm", "ABCDEFG");
		
//		when(dpService.dpCreate(obj.toString())).thenCallRealMethod();
		
		String strResult = dpService.dpCreate(obj.toString());
		System.out.println("strResult:"+strResult);
		Hashtable<String, String> iHashtable = mapper.convertValue(strResult, Hashtable.class);
		
		assertEquals("00", iHashtable.get("code"));
	}
	
}
