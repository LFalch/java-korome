package com.lfalch.korome;


public class ComplexInteger extends Number {
	private static final long serialVersionUID = 1L;
	private int a, b;
	
	public ComplexInteger(int real, int imaginary){
		a = real;
		b = imaginary;
	}
	
	public ComplexInteger add(ComplexInteger z){
		return new ComplexInteger(a + z.a, b + z.b);
	}
	
	public ComplexInteger sub(ComplexInteger z){
		return new ComplexInteger(a - z.a, b - z.b);
	}
	
	public ComplexInteger mul(ComplexInteger z){
		return new ComplexInteger(a * z.a - b * z.b, a * z.b + b * z.a);// (a + b i) * (x + y i) = a x + a y i + b i x + b y (-1);
	}
	
	public ComplexInteger div(ComplexInteger z){
		return this.mul(z.inverse());
	}
	
	public ComplexInteger inverse(){
		return new ComplexInteger(a / intValue(), -b / intValue());
	}
	
	@Override
	public int intValue() {
		return (int) Math.hypot(a, b);
	}
	
	@Override
	public long longValue() {
		return (long) Math.hypot(a, b);
	}
	
	@Override
	public float floatValue() {
		return (float) Math.hypot(a, b);
	}
	
	@Override
	public double doubleValue() {
		return Math.hypot(a, b);
	}
	
}
