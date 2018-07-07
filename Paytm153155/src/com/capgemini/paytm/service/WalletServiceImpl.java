package com.capgemini.paytm.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.capgemini.paytm.beans.Customer;
import com.capgemini.paytm.beans.Wallet;
import com.capgemini.paytm.exception.InsufficientBalanceException;
import com.capgemini.paytm.exception.InvalidInputException;
import com.capgemini.paytm.repo.WalletRepo;
import com.capgemini.paytm.repo.WalletRepoImpl;

public class WalletServiceImpl implements WalletService {


public WalletRepo repo;
	
	public WalletServiceImpl(){
		repo= new WalletRepoImpl();
	}
	public WalletServiceImpl(Map<String, Customer> data){
		repo= new WalletRepoImpl(data);
	}
	public WalletServiceImpl(WalletRepo repo) {
		super();
		this.repo = repo;
	}
	WalletRepoImpl obj=new WalletRepoImpl();
	public Customer createAccount(String name, String mobileNo, BigDecimal amount) {		
		
		Customer cust=new Customer(name,mobileNo,new Wallet((amount)));
		validate(cust);
		boolean result=repo.save(cust);
		if(result==true)
			return cust;
		else
			return null;
				//create object of customer, and call dao save layer
		}

	
	public Customer showBalance(String mobileNo) {
		
		Customer customer=repo.findOne(mobileNo);		
		if(customer!=null)
			return customer;
		else
			throw new InvalidInputException("Invalid mobile no ");
	}

	public Customer fundTransfer(String sourceMobileNo, String targetMobileNo, BigDecimal amount) {	
		
		Customer scust=new Customer();
		Customer tcust=new Customer();
		Wallet sw=new Wallet();
		Wallet tw=new Wallet();
		scust=repo.findOne(sourceMobileNo);
		tcust=repo.findOne(targetMobileNo);
		if(scust!=null && tcust!=null)
		{			
			BigDecimal balance=scust.getWallet().getBalance();			
				if(balance.compareTo(amount)>0)
				{
					BigDecimal diff=balance.subtract(amount);
					sw.setBalance(diff);
					scust.setWallet(sw);				
					BigDecimal sum=tcust.getWallet().getBalance().add(amount);			
					tw.setBalance(sum);
					tcust.setWallet(tw);
					
					obj.getData().put(targetMobileNo, tcust);
					obj.getData().put(sourceMobileNo, scust);
				}
				else
				{
					System.err.println("Sorry amount can not be withdraw. Insufficient balance!!");
				}		
		}
		else
		{
			throw new InvalidInputException("Account does not exist!!");
		}		
		return tcust;
	}

	public Customer depositAmount(String mobileNo, BigDecimal amount) {
		
		Customer cust=new Customer();
		Wallet wallet=new Wallet();
		cust=repo.findOne(mobileNo);
		if(cust!=null)
		{
			BigDecimal amtAdd=cust.getWallet().getBalance().add(amount);
			wallet.setBalance(amtAdd);
			cust.setWallet(wallet);
			obj.getData().put(mobileNo, cust);
		}
		else
		{
			throw new InvalidInputException("Account does not exist!!");
		}	
		return cust;
	}

	public Customer withdrawAmount(String mobileNo, BigDecimal amount) {
		Customer cust=new Customer();
		Wallet wallet=new Wallet();
		cust=repo.findOne(mobileNo);
		if(cust!=null)
		{
			BigDecimal balance=cust.getWallet().getBalance();
			BigDecimal amtSub;
			if(balance.compareTo(amount) > 0)
			{
				amtSub=balance.subtract(amount);
				wallet.setBalance(amtSub);
				cust.setWallet(wallet);
				obj.getData().put(mobileNo, cust);
			}
			else
			{
				System.err.println("Sorry amount can not be withdrawn. Insufficient balance!!");
			}
		}
		else
		{
			throw new InvalidInputException("Account does not exist!!");
		}	
		return cust;
	}
	
	public void validate(Customer customer) {
		Scanner sc=new Scanner(System.in);		
		while(true)
		{
			String phoneno=customer.getMobileNo();			
			Pattern pattern=Pattern.compile("(0/91)?[7-9][0-9]{9}");
			Matcher matcher=pattern.matcher(phoneno);
			if(matcher.matches())
				break;
			else
			{				
				System.err.println("Incorrect Mobile number!!");
				System.out.println("please enter a valid contact number.");
				customer.setMobileNo(sc.next());
			}	
		}
		while(true)
		{
			if(validateName(customer.getName()))
				break;
			else
			{				
				System.err.println("Incorrect Name!!");
				System.out.println("Please start name with capital letter");
				customer.setName(sc.next());
			}			
		}
		
	}
	private boolean validateName(String name) {
		
		String pattern="[A-Z][a-zA-Z]*";
		return name.matches(pattern)?true:false;
	}
}
