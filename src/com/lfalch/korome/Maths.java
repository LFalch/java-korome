package com.lfalch.korome;

public class Maths {
	public static double sum(double... values){
		double sum = 0;
		
		for(double value: values)
			sum += value;
		
		return sum;
	}
	
	public static double product(double... values){
		double sum = 1;
		
		for(double value: values)
			sum *= value;
		
		return sum;
	}
	
	public static double negative(double value){
		return  -value;
	}
	
	public static double inverse (double value){
		return 1/value;
	}
	
	public static double getVelocityAfterCollision(double m1, double v1, double m2, double v2){
		double finalV1 = (m1 - m2) * v1 + 2 * m2 * v2;
		finalV1 /= m1 + m2;
		
		return finalV1;
	}
}
