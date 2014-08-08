package com.lfalch.korome;

public class Vector {
	public double x,y;
	
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public static Vector createUnitVector(double direction){
		return new Vector(Math.cos(direction), Math.sin(direction));
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public double getDistance(Vector other){
		return Math.hypot(x - other.x, y - other.y);
	}
	
	public double getDistanceSq(Vector other){
		return (x - other.x) * (x - other.x) +  (y - other.y) * (y - other.y);
	}
	
	public double length(){
		return Math.hypot(x, y);
	}
	
	public double lengthSq(){
		return x * x + y * y;
	}
	
	public double getDirection(){
		return Math.atan2(y, x);
	}
	
	public Vector getNormalisedVector(){
		return scale(this, 1/length());
	}
	
	public double getDirectionTowards(Vector other){
		return Math.atan2(other.y-y, other.x-x);
	}
	
	public double getPositionOnAxisRad(double axisInRad){
		return this.length() * Math.cos(this.getDirection() - axisInRad);
	}
	
	public double getPositionOnAxisRad(Vector axis){
		return this.length() * Math.cos(this.getDirection() - axis.getDirection());
	}
	
	public static Vector add(Vector a, Vector b){
		return new Vector(a.x+b.x, a.y+b.y);
	}
	
	public static Vector sub(Vector a, Vector b){
		return new Vector(a.x-b.x, a.y-b.y);
	}
	
	public static double dot(Vector a, Vector b){
		return a.x * b.x + a.y * b.y;
	}
	
	public static Vector scale(Vector v, double scalar){
		return new Vector(v.x * scalar, v.y * scalar);
	}
	
	public void add(Vector other){
		this.x += other.x;
		this.y += other.y;
	}
	
	public void subtract(Vector other){
		this.x -= other.x;
		this.y -= other.y;
	}
	
	public void scale(double d) {
		this.x *= d;
		this.y *= d;
	}
}
