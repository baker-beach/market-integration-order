package com.pumpkinorganics.integration.order.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.bakerbeach.market.core.service.order.dao.OrderDaoException;
import com.bakerbeach.market.core.service.order.service.OrderServiceImpl;
import com.bakerbeach.market.order.api.model.OrderList;

public class IntegrationOrderService extends OrderServiceImpl {

	public OrderList findByOrderPeriod(Date startDate, Date endDate) {
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put("createdAt >=", startDate);
		filters.put("createdAt <=", endDate);
		String[] status = {"sent","logistic"};
		filters.put("status in",status);
		filters.put("total.gross !=","0.00");
		OrderList orderList;
		try {
			orderList = orderDaos.get("PUMPKIN_DE").findByFilters(filters, null, null, null, false);
			return orderList;
		} catch (OrderDaoException e) {
			return null;
		}
	}
	
	public OrderList findByInvoicePeriod(Date startDate, Date endDate,String paymentCode) {
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put("invoices.invoiceDate >=", startDate);
		filters.put("invoices.invoiceDate <=", endDate);
		String[] status = {"sent","logistic"};
		filters.put("status in",status);
		filters.put("total.gross !=","0.00");
		filters.put("paymentCode =",paymentCode);
		OrderList orderList;
		try {
			orderList = orderDaos.get("PUMPKIN_DE").findByFilters(filters, null, null, null, false);
			return orderList;
		} catch (OrderDaoException e) {
			return null;
		}
	}
}
