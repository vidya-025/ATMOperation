package com.codegnan.cards;

import java.util.ArrayList;
import java.util.Collections;

import com.codegnan.customException.InsufficientBalanceException;
import com.codegnan.customException.InsufficientMachineBalanceException;
import com.codegnan.customException.InvalidAmountException;
import com.codegnan.customException.NotAOperatorException;
import com.codegnan.interfaces.IATMService;

public class AxisDebitCard implements IATMService{
	String name;
	long debitCardNumber;
	double accountBalance;
	int pinNumber;
	ArrayList<String> statemenrt;
	final String type = "user";
	int chances;
	private ArrayList<String> statement;

	public AxisDebitCard(long debitCardNumber,String name,double accountBalance,int pinNumber) {
		chances=3;
		this.name=name;
		this.accountBalance=accountBalance;
		this.pinNumber=pinNumber;
		statement=new ArrayList<>();
	}
	
	@Override
	public String getUserType() throws NotAOperatorException {
		// TODO Auto-generated method stub
		return type;
	}

	@Override
	public double withdrawAmount(double withAmount) throws InvalidAmountException, InsufficientBalanceException,
			InsufficientMachineBalanceException, InsufficientMachineBalanceException {
		if(withAmount<=0) {
			throw new InvalidAmountException("you can enter Zero(0) amount to withdraw. please enter valid Amount");
			
		}else if(withAmount%100==0) {
			throw new InvalidAmountException("Please withdraw Multiples of 100");
			
		}else if(withAmount<500){
			throw new InsufficientBalanceException("please withdraw more than 500 rupees");
			
		}else if(withAmount>accountBalance) {
			throw new InsufficientBalanceException("you don't have sufficient  balance to withdraw.. please check your account balance");
			
		}else {
			accountBalance=accountBalance-withAmount;
			statement.add("Debited : "+ withAmount);
			return withAmount;
		}

	}

	@Override
	public void depositAmount(double deptAmount) throws InvalidAmountException {
		if(deptAmount<=0||deptAmount%100!=0||deptAmount<500) {
			throw new InvalidAmountException("please deposit multiples of 100 and deposit more than 500");
			
		}else {
			accountBalance=accountBalance+deptAmount;
			statement.add("Creadited : "+ deptAmount);
		}
		
	}

	@Override
	public double checkAccountBalance() {
		// TODO Auto-generated method stub
		return accountBalance;
	}

	@Override
	public void changePinNumber(int pinNumber) {
		this.pinNumber = pinNumber;
		
	}

	@Override
	public int getPinNumber() {
		// TODO Auto-generated method stub
		return pinNumber;
	}

	@Override
	public String getUserName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public void decreaseChances() {
		// TODO Auto-generated method stub
		--chances;
	}

	@Override
	public int getChances() {
		// TODO Auto-generated method stub
		return chances;
	}

	@Override
	public void resetPinChances() {
		// TODO Auto-generated method stub
		chances = 3;
	}

	@Override
	public void generateMiniStatement() {
		// TODO Auto-generated method stub
		int count=5;
		if(statement.size()==0) {
			System.out.println("There are NO transactions Happend");
			return;
		}
		System.out.println("===================== List 5 Transactions===============");
		Collections.reverse(statement);
		for(String trans:statement) {
			if(count == 0) {
				break;
			}
			System.out.println(trans);
			count--;
		}Collections.reverse(statement);
	}
}
