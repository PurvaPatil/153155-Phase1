package com.capgemini.paytm.ui;

import java.math.BigDecimal;
import java.util.Scanner;

import com.capgemini.paytm.beans.Customer;
import com.capgemini.paytm.beans.Wallet;
import com.capgemini.paytm.service.WalletService;
import com.capgemini.paytm.service.WalletServiceImpl;

public class Client {

	private WalletService walletService;
	public Client()
	{
		walletService=new WalletServiceImpl();
	}
	public void menu() {
		String ans="";	
		Scanner sc=new Scanner(System.in);
		System.out.println("****Paytm Application****");
		Customer customer=new Customer();
		BigDecimal amount;
		String mobileNo;
		do
		{
			System.out.println("1. Create Account ");
			System.out.println("2. Show Balance");
			System.out.println("3. Fund Transfer");
			System.out.println("4. Deposit amount");
			System.out.println("5. Withdraw amount");
			System.out.println("6. Exit");			
			System.out.println("\nPlease Select an option:");
			int choice=sc.nextInt();
			
			switch (choice) {
			case 1:
				System.out.println("Enter Name: ");
				String name=sc.next();
				System.out.println("Enter Mobile Number: ");
				mobileNo=sc.next();
				System.out.println("Enter Balance");
				amount=sc.nextBigDecimal();				
				customer=walletService.createAccount(name, mobileNo, amount);
				if(customer!=null)
				{
					System.out.println("Your account is created!");
					System.out.println(customer);
				}
				else
					System.out.println("Account already exist!");
				break;				
			case 2:				
				System.out.println("Enter the mobile number to view balance: ");
				mobileNo=sc.next();
				customer=walletService.showBalance(mobileNo);
				System.out.println("Your balance for mobile number: "+mobileNo+" is "+customer.getWallet());
				break;
			case 3:
				System.out.println("Enter Source Mobile Number: ");
				String sourceMobileNo=sc.next();
				System.out.println("Enter Target Mobile Number: ");
				String targetMobileNo=sc.next();
				System.out.println("Enter amount to transfer");
				amount=sc.nextBigDecimal();				
				customer=walletService.fundTransfer(sourceMobileNo, targetMobileNo, amount);
				System.out.println(customer);
				break;
			case 4:
				System.out.println("Enter Mobile Number: ");
				mobileNo=sc.next();
				System.out.println("Enter amount to deposit");
				amount=sc.nextBigDecimal();				
				customer=walletService.depositAmount(mobileNo, amount);
				System.out.println(customer);
				break;
			case 5:
				System.out.println("Enter Mobile Number: ");
				mobileNo=sc.next();
				System.out.println("Enter amount to withdraw");
				amount=sc.nextBigDecimal();				
				customer=walletService.withdrawAmount(mobileNo, amount);
				System.out.println(customer);
				break;
			case 6:
				System.exit(0);
				break;
			default:
				System.out.println("Invalid options");
				break;
			}			
			System.out.println("\nDo you want to continue: Y/N ");
			ans=sc.next();
		}while(ans.equalsIgnoreCase("y"));
		System.out.println("\nThank you for using this service!");	
	}
	public static void main(String[] args) {
		Client client=new Client();
		client.menu();

	}

}
