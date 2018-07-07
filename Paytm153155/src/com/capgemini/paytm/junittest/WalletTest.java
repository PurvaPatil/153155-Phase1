package com.capgemini.paytm.junittest;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.capgemini.paytm.beans.Customer;
import com.capgemini.paytm.beans.Wallet;
import com.capgemini.paytm.exception.InvalidInputException;
import com.capgemini.paytm.service.WalletService;
import com.capgemini.paytm.service.WalletServiceImpl;

import static org.junit.Assert.*;

public class WalletTest {

	WalletService service;
	Customer cust1, cust2, cust3;

	@Before
	public void initData() {
		Map<String, Customer> data = new HashMap<String, Customer>();
		cust1 = new Customer("Kalyani", "9900112212", new Wallet(new BigDecimal(9000)));
		cust2 = new Customer("Abhilasha", "9963242422", new Wallet(new BigDecimal(6000)));
		cust3 = new Customer("Sejal", "9922950519", new Wallet(new BigDecimal(7000)));

		data.put("9900112212", cust1);
		data.put("9963242422", cust2);
		data.put("9922950519", cust3);
		service = new WalletServiceImpl(data);

	}

	//for testing null value
	@Test(expected = NullPointerException.class)
	public void testCreateAccount() {

		service.createAccount(null, null, null);

	}

	@Test
	public void testCreateAccount1() {

		Customer c = new Customer();
		Customer cust = new Customer();
		cust = service.createAccount("Purva", "9900112213", new BigDecimal(7000));
		c.setMobileNo("9900112213");
		c.setName("Purva");
		c.setWallet(new Wallet(new BigDecimal(7000)));
		Customer actual = c;
		Customer expected = cust;
		assertEquals(expected, actual);
	}

	
	@Test
	public void testCreateAccount2() {
		Customer cust = new Customer();
		cust = service.createAccount("Purva", "9900112213", new BigDecimal(7000));
		assertEquals("Purva", cust.getName());
	}

	@Test
	public void testCreateAccount3() {

		Customer cust = new Customer();
		cust = service.createAccount("Purva", "9900112213", new BigDecimal(7000));
		assertEquals("9900112213", cust.getMobileNo());
	}

	//for testing invalid account
	@Test(expected = InvalidInputException.class)
	public void testShowBalance() {
		Customer cust = new Customer();
		cust = service.showBalance("9579405744");

	}

	//for testing showBalance method 
	@Test
	public void testShowBalance2() {

		Customer cust = new Customer();
		cust = service.showBalance("9922950519");
		assertEquals(cust, cust3);

	}

	@Test
	public void testShowBalance3() {

		Customer cust = new Customer();
		cust = service.showBalance("9900112212");
		BigDecimal actual = cust.getWallet().getBalance();
		BigDecimal expected = new BigDecimal(9000);
		assertEquals(expected, actual);

	}

	@Test(expected = InvalidInputException.class)
	public void testFundTransfer() {
		service.fundTransfer(null, null, new BigDecimal(7000));
	}

	@Test
	public void testFundTransfer2() {
		cust1 = service.fundTransfer("9900112212", "9963242422", new BigDecimal(2000));
		BigDecimal actual = cust1.getWallet().getBalance();
		BigDecimal expected = new BigDecimal(8000);
		assertEquals(expected, actual);
	}

	@Test(expected = InvalidInputException.class)
	public void testDeposit() {
		service.depositAmount("900000000", new BigDecimal(2000));
	}

	//for testing wrong number
	@Test(expected = InvalidInputException.class)
	public void testDeposit3() {
		service.depositAmount("88888", new BigDecimal(2000));
	}
	@Test
	public void testDeposit2() {
		cust1 = service.depositAmount("9963242422", new BigDecimal(2000));
		BigDecimal actual = cust1.getWallet().getBalance();
		BigDecimal expected = new BigDecimal(8000);
		assertEquals(expected, actual);
	}

	@Test(expected = InvalidInputException.class)
	public void testWithdraw() {
		service.withdrawAmount("900000000", new BigDecimal(2000));
	}

	//for testing wrong number
	@Test(expected = InvalidInputException.class)
	public void testWithdraw3() {
		service.withdrawAmount("888888", new BigDecimal(2000));
	}
	
	
	@Test
	public void testWithdraw2() {
		cust1 = service.withdrawAmount("9963242422", new BigDecimal(2000));
		BigDecimal actual = cust1.getWallet().getBalance();
		BigDecimal expected = new BigDecimal(4000);
		assertEquals(expected, actual);
	}

	//for validating name and mobile number
	@Test
	public void TestValidate() {
		Customer customer=new Customer("Vaishnavi","9090909090",new Wallet(new BigDecimal(100)));
		service.validate(customer);		
	}

	@After
	public void testAfter() {
		service = null;
	}
}
