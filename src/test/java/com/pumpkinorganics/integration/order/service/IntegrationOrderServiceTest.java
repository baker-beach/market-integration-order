package com.pumpkinorganics.integration.order.service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bakerbeach.market.order.api.service.OrderServiceException;

@ActiveProfiles(profiles = { "env.office" })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring/resources.xml", "classpath*:spring/service.xml", "classpath*:spring/db.xml" })
public class IntegrationOrderServiceTest {
	
	@Autowired
	private IntegrationOrderService integrationOrderService;
	
	@Test
	public void listOrders() throws OrderServiceException {
		GregorianCalendar sart = new GregorianCalendar();
		sart.add(Calendar.MONTH, -1);
		GregorianCalendar stopDate = new GregorianCalendar(); 
		integrationOrderService.findByOrderPeriod(null, null);
	}

}
