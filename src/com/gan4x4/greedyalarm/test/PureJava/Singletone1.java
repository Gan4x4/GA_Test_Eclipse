package com.gan4x4.greedyalarm.test.PureJava;


public class Singletone1 {
private static Singletone1 _instance = null;
	
public static Singletone1 getInstance() {
		if (_instance == null){
			_instance = new Singletone1();
		}
		return _instance;
	}
	public String getName(){
		return "First class";
	}
	
}
