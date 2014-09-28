package com.lfalch.korome;

/**
 * @author LFalch
 *
 */
public class CircularBody {
	protected Vector position;
	
	protected double theta, radius;
	
	public CircularBody(double angle, double x, double y, double radius) {
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
	
	public static boolean collide(CircularBody a, CircularBody b){
		return a.getDistance(b) < a.radius + b.radius;
	}
	
	public static double collision(CircularBody a, CircularBody b){
		return a.getDistance(b) - (a.radius + b.radius);
	}
	
	public double getDistance(CircularBody other){
		return this.getPosition().getDistance(other.getPosition());
	}
	
	public double getDirection(CircularBody other) {
		return this.getPosition().getDirectionTowards(other.getPosition());
	}
}
