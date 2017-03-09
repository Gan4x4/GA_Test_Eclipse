package com.gan4x4.greedyalarm.test.PureJava;


public class Singletone2 {
private static Singletone2 _instance = null;

	
public static Singletone2 getInstance() {
		if (_instance == null){
			_instance = new Singletone2();
		}
		return _instance;
	}

	public String getName(){
		return "Second class";
	}
	
}
