package com.capgemini.paytm.repo;

import com.capgemini.paytm.beans.Customer;

public interface WalletRepo {

	public boolean save(Customer customer);	
	public Customer findOne(String mobileNo);
}
