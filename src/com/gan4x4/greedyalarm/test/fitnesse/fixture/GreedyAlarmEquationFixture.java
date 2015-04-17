package com.gan4x4.greedyalarm.test.fitnesse.fixture;
import com.gan4x4.greedyalarm.math.Equation;
import com.gan4x4.greedyalarm.math.Sign;

public class GreedyAlarmEquationFixture {
	private int n1_,n2_;
	private Sign s1_;
	
	
	public GreedyAlarmEquationFixture(){
		
	}
	public void setNumber1(int n1){
		n1_=n1;
	}
	
	public void setNumber2(int n2){
		n2_=n2;
	}
	
	public void setSign(Sign s){
		s1_ =s;
	}
	
	public String answer(){
		Equation e = new Equation();
		e.add(n1_, null);
		e.add(n2_, s1_);
		Integer r =  e.calculate();
		return r.toString(); 
	}
	

}
