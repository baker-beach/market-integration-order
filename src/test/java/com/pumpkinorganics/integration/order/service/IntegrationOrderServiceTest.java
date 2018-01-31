package com.pumpkinorganics.integration.order.service;

import java.util.Calendar;
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
		GregorianCalendar start = new GregorianCalendar();
		//start.add(Calendar.MONTH, -1);
		start.set(Calendar.DAY_OF_MONTH, 1);
		start.set(Calendar.HOUR_OF_DAY, 0);
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);
		start.set(Calendar.MILLISECOND, 0);
		GregorianCalendar stop = new GregorianCalendar(); 
		stop.set(Calendar.YEAR, start.get(Calendar.YEAR));
		stop.set(Calendar.MONTH, start.get(Calendar.MONTH));
		stop.set(Calendar.DAY_OF_MONTH, start.getActualMaximum(Calendar.DAY_OF_MONTH));
		stop.set(Calendar.HOUR_OF_DAY, 23);
		stop.set(Calendar.MINUTE, 59);
		stop.set(Calendar.SECOND, 59);
		stop.set(Calendar.MILLISECOND, 999);
		integrationOrderService.findByOrderPeriod(start.getTime(), stop.getTime());
	}

}
