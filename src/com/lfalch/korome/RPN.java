package com.lfalch.korome;

import java.util.HashMap;
import java.util.Stack;


public class RPN {
	
	public static double calculate(String calculation){
		return calculate(calculation, null);
	}
	
	public static double calculate(String calculation, HashMap<String, Double> vars){
		Stack<Double> stack = new Stack<Double>();
		
		String[] operations = calculation.trim().split(" ");
		
		if(vars == null)
			vars = new HashMap<String, Double>(0);
		
		for(String operation: operations) {
			if(isDouble(operation))
				stack.push(Double.parseDouble(operation));
			else if(vars.containsKey(operation))
				stack.push(vars.get(operation));
			else
				stack = calc(operation.toCharArray()[0], stack);
		}
		
		return stack.pop();
	}
	
	private static boolean isDouble(String s) {
	    try { 
	        Double.parseDouble(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    return true;
	}
	
	private static Stack<Double> calc(char op, Stack<Double> stack){
		switch(op){
		case '+':
			stack.push(Maths.sum(stack.pop(), stack.pop()));
			break;
		case '-':
			stack.push(Maths.sum(-stack.pop(), stack.pop()));
			break;
		case '/':
			stack.push(Maths.product(1/stack.pop(), stack.pop()));
			break;
		case '*':
			stack.push(Maths.product(stack.pop(), stack.pop()));
			break;
		case '^':
			double temp = stack.pop();
			stack.push(Math.pow(stack.pop(), temp));
			break;
		case '_':
			//I know it's a silly square root symbol
			stack.push(Math.sqrt(stack.pop()));
			break;
		case 'c':
		case 'C':
			stack.push(Math.cos(stack.pop()));
			break;
		case 's':
		case 'S':
			stack.push(Math.cos(stack.pop()));
			break;
		default:
			System.err.println("Operation \'" + op + "\' not recognised. Skipped.");
		case '\"': //These are "comments"
			break;
		}
		return stack;
	}
}
