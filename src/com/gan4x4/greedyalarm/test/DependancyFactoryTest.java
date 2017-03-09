package com.gan4x4.greedyalarm.test;

//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;

import android.test.AndroidTestCase;
import android.test.InstrumentationTestCase;

import com.gan4x4.greedyalarm.mock.DependancyFactory;
import com.gan4x4.greedyalarm.test.PureJava.Singletone1;
import com.gan4x4.greedyalarm.test.PureJava.Singletone2;

import static org.mockito.Mockito.*;


import junit.framework.TestCase;

public class DependancyFactoryTest extends InstrumentationTestCase {

	protected void setUp() throws Exception {
		super.setUp();
		Class[] simpleClasses  = {Singletone1.class,Singletone2.class};
		DependancyFactory.classList = simpleClasses;
		System.setProperty("dexmaker.dexcache",getInstrumentation().getTargetContext().getCacheDir().getPath());
		//MockitoAnnotations.initMocks(this);
		
	}
	
	public final void testGetInstanceNormal() throws ClassNotFoundException {
		Object result = DependancyFactory.getInstance(Singletone1.class);
		Singletone1 s1 = (Singletone1) result;
		assertEquals("First class",s1.getName());
	}
	
	public final void testGetInstanceReturnNotSameObjects() throws ClassNotFoundException {
		Singletone1 s1 = (Singletone1) DependancyFactory.getInstance(Singletone1.class);
		Singletone2 s2 = (Singletone2) DependancyFactory.getInstance(Singletone2.class);
		assertFalse(s1.getName() == s2.getName());
	}
	
	

	public final void testGetInstanceForUnregistratedObject() {
		
	
	}
	
	public final void testSetFake() throws ClassNotFoundException {
		testGetInstanceNormal();
		//Singletone1Mock fakeS1 = new Singletone1Mock();
		Singletone1 fakeS1 = mock(Singletone1.class);
		DependancyFactory.setFake(fakeS1);
		when(fakeS1.getName()).thenReturn("This is fake");
		Singletone1 s1 = (Singletone1) DependancyFactory.getInstance(Singletone1.class);
		assertEquals("This is fake",s1.getName());
	}


	public final void testRemoveFake() throws ClassNotFoundException {
		testSetFake();
		DependancyFactory.removeFake(Singletone1.class);
		Singletone1 s1 = (Singletone1) DependancyFactory.getInstance(Singletone1.class);
		assertEquals("First class",s1.getName());
	}

}
