package com.codegnan.operation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import com.codegnan.cards.AxisDebitCard;
import com.codegnan.cards.HDFCDebitCard;
import com.codegnan.cards.OperatorCard;
import com.codegnan.cards.SBIDebitCard;
import com.codegnan.customException.IncorrectPinLimitReachedException;
import com.codegnan.customException.InsufficientBalanceException;
import com.codegnan.customException.InsufficientMachineBalanceException;
import com.codegnan.customException.InvalidAmountException;
import com.codegnan.customException.InvalidCardException;
import com.codegnan.customException.InvalidPinException;
import com.codegnan.customException.NotAOperatorException; 
import com.codegnan.interfaces.IATMService;

public class ATMOperations {
	
	private static double ATM_MACHINE_BALANCE = 0;
	
	// initial ATM machine Balance
	public static double ATM_BALANCE =1000000.0;
	
	//loop to keep the track of all activities performed on the machine
	public static ArrayList<String>ACTIVITY=new ArrayList<>();
	
	//Database to map card numbers to card objects
	public static HashMap<Long,IATMService>dataBase=new HashMap<>();
	
	//flag to indicate ATM machine is on or off
	public static boolean MACHINE_ON =true;
	
	//reference the current card in use 
	public static IATMService card;
	
	
	//validate the inserted card by checki g againist the database  
	public static IATMService validateCard(long cardNumber)throws InvalidCardException{
		if(dataBase.containsKey(cardNumber)) {
			return dataBase.get(cardNumber);
											 }
		else {
			ACTIVITY.add("Accessed by : "+ cardNumber+ " is not compatiable");
			throw new InvalidCardException("This is Not a valid Card");
		      }
	}
	
	
	//display activities perform on the ATM machine
	public static void checkATMMachineActivites() {
		System.out.println("=======================Activites performed==========================");
		for(String activity:ACTIVITY) {
			System.out.println("================================================================");
			System.out.println(activity);
			System.out.println("=================================================================");
		}
	}
	
	
	// resent the number of pin attempts for a user
	public static void resetUserAttempts(IATMService operatorCard) {
		IATMService card = null;
		long number;
		Scanner scanner=new Scanner(System.in);
		System.out.println("Enter your card Number : ");
		number =scanner.nextLong();
		try {
			card=validateCard(number);
			card.resetPinChances();
			ACTIVITY.add("Accessed by : "+ operatorCard.getUserName()+":to reset the number of chances for user");
			
		} catch(InvalidCardException ive) {
			System.out.println(ive.getMessage());
		}
	}
	
	
	public static IATMService validCredentials(long cardNumber,int pinNumber) 
			throws InvalidCardException,IncorrectPinLimitReachedException,InvalidPinException {
		if (dataBase.containsKey(cardNumber)) {
			card=dataBase.get(cardNumber);
			
		}
	else {
			throw new InvalidCardException("This is not a valid card");
			
		}try {
			if(card.getUserType().equals("operator")) {
				//operators have a different Pin validation process
				if(card.getPinNumber()!=pinNumber) {
					throw new InvalidPinException("Dear operator,Please Enter correct pin number");
				}
			}else {
				return card;
			}
		} catch (NotAOperatorException noe) {
			noe.printStackTrace();
		}
		// validate pin and mandle incorrect attempts
		if(card.getChances()<=0) {
			throw new IncorrectPinLimitReachedException("you have reached wrong limit of pin number,which is 3 attempts.");
		}
		if(card.getPinNumber()!=pinNumber) {
			card.decreaseChances();//decrease the number of chances remaining
			throw new InvalidPinException("you have entered a wrong pin number");
			
		}else {
			return card;}
		}
		
		//validate the amounnt for withdraw to ensure sufficient machine balance
		public static void validateAmount(double amount) throws InsufficientMachineBalanceException{
			if(amount > ATM_MACHINE_BALANCE) {
				throw new InsufficientMachineBalanceException("Insufficinet cash in the machine");
			}
		}
		
		public static void validateDepositAmount(double amount) 
			throws InsufficientMachineBalanceException, InvalidAmountException {
				//ensure deposit multiples of 100.
				if (amount % 100!=0) {
					throw new InvalidAmountException("please deposit multiple of 100");	
				}
			//check if deposit will exceed the machine capacity
				if(amount + ATM_MACHINE_BALANCE >1200000.0d) {
					ACTIVITY.add("unable to deposit cash in ATM Machine....");
					throw new InsufficientMachineBalanceException("you can't deposit cash as limit of the ATM Machine reached");
				}
			
		}
	
	public static void operatorMode(IATMService card) {
		Scanner scanner = new Scanner(System.in);
		double amount;
		boolean flag = true;
		while(flag) {
			System.out.println("Operator MODE: Operator Name : "+ card.getUserName());
			System.out.println("****************************************************");
			System.out.println("||              0.Switch off the machine               ||");
			System.out.println("||              1.to check ATM machine Balance         ||");
			System.out.println("||              2.Deposit cash in ATM machine          ||");
			System.out.println("||              3.Reset user PIN attempts              ||");
			System.out.println("||              4.To check activities perfomed in the machine               ||");
			System.out.println("||              5.Exit operator mode                   ||");
			System.out.println("please enter your choice : ");
			int option = scanner.nextInt();
			switch(option) {
			case 0:
			MACHINE_ON = false;
			ACTIVITY.add("Accessed by : "+card.getUserName()+" Activity performed : switching off the ATM Machine ");
			flag = false;
			break;
			case 1:
				ACTIVITY.add("Accessed by : "+card.getUserName()+" Activity performed : checking ATM Machine Balance");
				System.out.println("The Balance of ATM Machine is : "+ATM_MACHINE_BALANCE +" : is avalable");
				break;
			case 2:
				System.out.println("Enter the amount to deposit : ");
				amount = scanner.nextDouble();
				try {
					validateDepositAmount(amount);//validate Deposit Amount
					ATM_MACHINE_BALANCE+=amount;
					ACTIVITY.add("Accessed by : "+card.getUserName()+" Activity performed : Deposit cash in the ATM Machine ");
					System.out.println("=======================================================");
					System.out.println("====================CASH Added int the ATM Machine===================");
					System.out.println("=======================================================");
					
				}catch(InvalidAmountException e ) {
					System.out.println(e.getMessage());
				}catch(InsufficientMachineBalanceException e) {
					System.out.println(e.getMessage());
				}
				break;
				
			case 3:
				resetUserAttempts(card);//reset user's pin attempts
				System.out.println("=======================================================");
				System.out.println("====================user attempts are reset===================");
				System.out.println("=======================================================");
				ACTIVITY.add("Accessed by : "+card.getUserName()+" Activity performed : Resetting user Pin Attempts of user : ");
				break;
			case 4:
				checkATMMachineActivites();//Display ATM Activites
				break;
			case 5:
				flag =false;// exit operator mode
				break;
				default:
					System.out.println("You have entered a wrong option");
			}
		}
		
	}
	
public static void main(String[] args) throws NotAOperatorException {
	// Initialize the database with some sample card data
	dataBase.put(2222222222l, new AxisDebitCard(2222222222l, "yasudas", 50000.0, 2222));
	dataBase.put(3333333333l, new HDFCDebitCard(3333333333l, "sathsena", 30000.0, 3333));
	dataBase.put(4444444444l, new SBIDebitCard(4444444444l, "yuva", 70000.0, 4444));
	dataBase.put(1111111111l, new OperatorCard(1111111111l, 1111, "riya"));
	Scanner scanner = new Scanner(System.in);
	long cardNumber = 0;
	double depositAmount = 0.0;
	double withdrawAmount = 0.0;
	int pin = 0;
	// Main loop for ATM operations
	while (MACHINE_ON) {
		System.out.println("Please Enter the Debit Card Number:");
		cardNumber = scanner.nextLong();
		try {
			System.out.println("Please Enter PIN Number:");
			pin = scanner.nextInt();
			card = validCredentials(cardNumber, pin); // Validate card and PIN
			if (card == null) {
				System.out.println("Card validation failed.");
				continue;
			}
			ACTIVITY.add("Accessed By: " + card.getUserName() + " Status: Access Approved");
			if (card.getUserType().equals("operator")) {
				operatorMode(card); // Enter operator mode
				continue;
			}
			while (true) {
				System.out.println("USER MODE: " + card.getUserName());
				System.out.println("===================================================");
				System.out.println("||            	1. Withdraw Amount         	||");
				System.out.println("||            	2. Deposit Amount          	||");
				System.out.println("||            	3. Check Balance          	||");
				System.out.println("||            	4. Change PIN             	||");
				System.out.println("||            	5. generate MINI Statement         	||");
				System.out.println("===================================================");
				System.out.println("Enter Your Choice:");
				int option = scanner.nextInt();
				try {
					switch (option) {
					case 1:
						System.out.println("Please Enter The Amount to Withdraw: ");
						withdrawAmount = scanner.nextDouble();
						validateAmount(withdrawAmount); // Validate withdrawal amount
						card.withdrawAmount(withdrawAmount); // Withdraw amount
						ATM_MACHINE_BALANCE -= withdrawAmount; // Update ATM balance
						ACTIVITY.add("Accessed By " + card.getUserName() + " Activity: Amount Withdrawn "
								+ withdrawAmount + " From Machine");
						break;
					case 2:
						System.out.println("Please Enter The Amount to Deposit: ");
						depositAmount = scanner.nextDouble();
						validateDepositAmount(depositAmount); // Validate deposit amount
						ATM_MACHINE_BALANCE += depositAmount; // Update ATM balance
						card.depositAmount(depositAmount); // Deposit amount
						ACTIVITY.add("Accessed By " + card.getUserName() + " Activity: Amount Deposited "
								+ depositAmount + " in the Machine");
						break;
					case 3:
						System.out.println("Your Account Balance is: " + card.checkAccountBalance()); // Check
																										// balance
						ACTIVITY.add("Accessed By " + card.getUserName() + " Activity: Checking The Balance");
						break;
					case 4:
						System.out.println("Enter A New PIN:");
						pin = scanner.nextInt();
						card.changePinNumber(pin); // Change PIN
						ACTIVITY.add("Accessed By " + card.getUserName() + " Activity: Changed PIN Number");
						break;
					case 5:
						ACTIVITY.add("Accessed By " + card.getUserName() + " Activity: Generating MINI Statement");
						card.generateMiniStatement(); // Generate mini statement
						break;
					default:
						System.out.println("You Have Entered A Wrong Option");
						break;
					}
					System.out.println("Do You Want To Continue? (Y/N):");
					String nextOption = scanner.next();
					if (nextOption.equalsIgnoreCase("N")) {
						break; // Exit user mode
					}
				} catch (InvalidAmountException | InsufficientBalanceException
						| InsufficientMachineBalanceException e) {
					System.out.println(e.getMessage());
				}
			}
		} catch (InvalidPinException | InvalidCardException | IncorrectPinLimitReachedException e) {
			ACTIVITY.add("Accessed By: " + card.getUserName() + " Activity:  Status: Access Denied");
			System.out.println(e.getMessage());
		}
	}
	// Display a message when the ATM machine is turned off
	System.out.println("===============================================================");
	System.out.println("============== Thanks For Using ICCI ATM Machine ==============");
	System.out.println("===============================================================");

}		
	
}

