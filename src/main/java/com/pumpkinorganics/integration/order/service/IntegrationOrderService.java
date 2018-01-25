package com.pumpkinorganics.integration.order.service;

import java.util.Date;

import com.bakerbeach.market.core.service.order.service.XOrderServiceImpl;
import com.bakerbeach.market.order.api.model.OrderList;

public class IntegrationOrderService extends XOrderServiceImpl{
	
	public OrderList findByOrderPeriod(Date startDate, Date endDate) {
		
		return null;
	}

}
