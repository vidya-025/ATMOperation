package com.codegnan.interfaces;

import com.codegnan.customException.InsufficientBalanceException;
import com.codegnan.customException.InsufficientMachineBalanceException;
import com.codegnan.customException.InvalidAmountException;
import com.codegnan.customException.NotAOperatorException;


public interface IATMService {
	//to get the user type. weather the user is operater or normal user

	public abstract String getUserType() throws NotAOperatorException;
// to withdrawAmount
//1.will throw invalidAmountException if the Amount is not a valid demomination
//2.will throw insufficientBalance if the customer has insufficient amount in his/her account.
//3. will throw insufficient MachineBalance exception if the machine has insufficient cash.

public abstract double withdrawAmount(double withAmount) 
		throws InvalidAmountException,InsufficientBalanceException,InsufficientMachineBalanceException, InsufficientMachineBalanceException;
//to deposit amount
public abstract void depositAmount(double deptAmount) throws InvalidAmountException;
//to check balance
public abstract double checkAccountBalance();
//to change PIN number
public abstract void changePinNumber(int pinNumber);
//get PIN Number
public abstract int getPinNumber();
//to get the userName
public abstract String getUserName();
//to decrese the number of chances while enter the wrong pin number.
public abstract void decreaseChances();
//to get the chances of pin number
public abstract int getChances();
//to reset the pin number chances by bank operator
public abstract void resetPinChances();
//to generate MINI Statement
public abstract void generateMiniStatement();
}

