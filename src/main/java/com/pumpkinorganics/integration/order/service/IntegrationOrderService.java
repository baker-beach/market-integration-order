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
		OrderList orderList;
		try {
			orderList = orderDaos.get("PUMPKIN_DE").findByFilters(filters, null, null, null);
			return orderList;
		} catch (OrderDaoException e) {
			return null;
		}
	}
}
