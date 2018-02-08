package com.pumpkinorganics.integration.order.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bakerbeach.market.order.api.model.Order;
import com.bakerbeach.market.order.api.model.OrderList;
import com.bakerbeach.market.order.api.service.OrderServiceException;
import com.bakerbeach.market.payment.model.PaymentTransaction;
import com.bakerbeach.market.payment.service.TransactionDao;
import com.bakerbeach.market.payment.service.TransactionDaoException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles(profiles = { "env.office" })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring/resources.xml", "classpath*:spring/service.xml",
		"classpath*:spring/db.xml" })
public class IntegrationOrderServiceTest {

	@Autowired
	private IntegrationOrderService integrationOrderService;
	
	@Autowired
	private TransactionDao transactionDao;

	@Test
	public void listOrders() throws OrderServiceException {
		SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("dd.MM.yyyy");
			
		GregorianCalendar start = new GregorianCalendar();
		start.add(Calendar.MONTH, -1);
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
		OrderList orderList = integrationOrderService.findByOrderPeriod(start.getTime(), stop.getTime());
		StringBuilder sb = new StringBuilder();
		for (Order order : orderList.getOrders()) {
			sb.append(order.getTotal(true).getGross().toString().replace(".", ",")).append(";");
			sb.append("H").append(";");
			sb.append("1000000").append(";");
			sb.append(order.getInvoices().get(0).getId()).append(";");
			sb.append(simpleDateFormat.format(order.getCreatedAt())).append(";");
			if (order.getBillingAddress().getCountryCode().equals("DE")) {
				sb.append("430000").append(";").append(order.getCustomerId()).append("/").append(order.getId())
						.append(";\n");
			} else {
				sb.append("431001").append(";").append(order.getCustomerId()).append("/").append(order.getId()).append(";")
						.append(order.getBillingAddress().getCountryCode()).append(";\n");
			}
		}
		
		orderList = integrationOrderService.findByInvoicePeriod(start.getTime(), stop.getTime(),"AMAZON");

		for (Order order : orderList.getOrders()) {
			sb.append(order.getTotal(true).getGross().toString().replace(".", ",")).append(";");
			sb.append("S").append(";");
			sb.append("1000000").append(";");
			sb.append(order.getInvoices().get(0).getId()).append(";");
			sb.append(simpleDateFormat.format(order.getInvoices().get(0).getInvoiceDate())).append(";");
			sb.append("137302").append(";\n");
		}
		
		orderList = integrationOrderService.findByInvoicePeriod(start.getTime(), stop.getTime(),"CONCARDIS_DIRECT_DEBIT");

		for (Order order : orderList.getOrders()) {
			sb.append(order.getTotal(true).getGross().toString().replace(".", ",")).append(";");
			sb.append("S").append(";");
			sb.append("1000000").append(";");
			sb.append(order.getInvoices().get(0).getId()).append(";");
			sb.append(simpleDateFormat.format(order.getInvoices().get(0).getInvoiceDate())).append(";");
			sb.append("137303").append(";\n");
		}
		
		orderList = integrationOrderService.findByInvoicePeriod(start.getTime(), stop.getTime(),"PAYPAL_ONE_TIME");

		for (Order order : orderList.getOrders()) {
			sb.append(order.getTotal(true).getGross().toString().replace(".", ",")).append(";");
			sb.append("S").append(";");
			sb.append("1000000").append(";");
			sb.append(order.getInvoices().get(0).getId()).append(";");
			sb.append(simpleDateFormat.format(order.getInvoices().get(0).getInvoiceDate())).append(";");
			sb.append("170001").append(";\n");
			
			try {
				PaymentTransaction paymentTransaction = transactionDao.findByOrderId(order.getId());
				String data = (String)paymentTransaction.getLog().get(3).get("response");
				
				Map value = (new ObjectMapper()).readValue(data,Map.class);
			
				sb.append(((String)((Map)value.get("transaction_fee")).get("value")).replace(".", ",")).append(";");
				sb.append("S").append(";");
				sb.append("1000000").append(";");
				sb.append(order.getInvoices().get(0).getId()).append(";");
				sb.append(simpleDateFormat.format(order.getInvoices().get(0).getInvoiceDate())).append(";");
				sb.append("685501").append(";\n");
			} catch (TransactionDaoException | IOException e) {}
		}
		start.add(Calendar.DAY_OF_MONTH, -1);
		stop.add(Calendar.DAY_OF_MONTH, -1);
		orderList = integrationOrderService.findByInvoicePeriod(start.getTime(), stop.getTime(),"CONCARDIS_CREDITCARD");
		
		for (Order order : orderList.getOrders()) {
			sb.append(order.getTotal(true).getGross().toString().replace(".", ",")).append(";");
			sb.append("S").append(";");
			sb.append("1000000").append(";");
			sb.append(order.getInvoices().get(0).getId()).append(";");
			
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(order.getInvoices().get(0).getInvoiceDate());
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			sb.append(simpleDateFormat.format(calendar.getTime())).append(";");
			sb.append("137301").append(";\n");
		}
		
	       PrintWriter pWriter = null;
	        try {
	            pWriter = new PrintWriter(new BufferedWriter(new FileWriter("C:/work/test.csv")));
	            pWriter.println(sb.toString());
	        } catch (IOException ioe) {
	            ioe.printStackTrace();
	        } finally {
	            if (pWriter != null){
	                pWriter.flush();
	                pWriter.close();
	            }
	        } 
	}

}
