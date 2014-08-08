package com.lfalch.korome;

/**
 * @author LFalch
 *
 */
public class Body {
	protected Vector position;
	
	protected double theta, radius;
	
	public Body(double angle, double x, double y, double radius) {
		theta = angle;
		position = new Vector(x, y);
		this.radius = radius;
	}
	
	
	public void setPosition(Vector pos){
		position = pos;
	}
	
	public Vector getPosition(){
		return position;
	}
	
	public static boolean collide(Body a, Body b){
		return a.getDistance(b) < a.radius + b.radius;
	}
	
	public static double collision(Body a, Body b){
		return a.getDistance(b) - (a.radius + b.radius);
	}
	
	public double getDistance(Body other){
		return this.getPosition().getDistance(other.getPosition());
	}
	
	public double getDirection(Body other) {
		return this.getPosition().getDirectionTowards(other.getPosition());
	}
}
