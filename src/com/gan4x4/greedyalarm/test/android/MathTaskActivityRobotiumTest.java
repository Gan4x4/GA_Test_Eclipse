
package com.gan4x4.greedyalarm.test.android;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.apache.http.HttpStatus;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Matchers.anyFloat;
import static org.mockito.Mockito.*;
import android.app.Activity;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.UiThreadTest;
import android.test.ViewAsserts;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.gan4x4.greedyalarm.GaWebApi;
import com.gan4x4.greedyalarm.GaWebViewActivity;
import com.gan4x4.greedyalarm.GreedyAlarmService;
import com.gan4x4.greedyalarm.MainActivity;
import com.gan4x4.greedyalarm.MathTaskActivity;
import com.gan4x4.greedyalarm.NewAlarmActivity;
import com.gan4x4.greedyalarm.descriptions.DescriptionForAlarm;
import com.gan4x4.greedyalarm.mock.GaCalendar;
import com.gan4x4.greedyalarm.objects.Alarm;
import com.gan4x4.greedyalarm.objects.GaObject;
import com.gan4x4.greedyalarm.objects.HttpRequestResponse;
import com.gan4x4.greedyalarm.objects.TaskMath;
import com.gan4x4.greedyalarm.test.GaActivityTest;
import com.gan4x4.greedyalarm.test.TestHelper;
import com.google.android.gms.games.internal.GamesContract.GameSearchSuggestionsColumns;
import com.robotium.solo.Condition;
import com.robotium.solo.Solo;

public class MathTaskActivityRobotiumTest extends
		GaActivityTest<MathTaskActivity> {

	private Button btCancel;
	private Button btOk;
	
	public MathTaskActivityRobotiumTest() {
		this("MathTaskActivityTest");
	}
	
	public MathTaskActivityRobotiumTest(String name) {
		super(name, MathTaskActivity.class);
		setName(name);
	}
	
	protected void setUp() throws Exception {
		super.setUp();		
		btCancel = (Button) mActivity.findViewById(com.gan4x4.greedyalarm.R.id.button_cancel);
		btOk = (Button) mActivity.findViewById(com.gan4x4.greedyalarm.R.id.button_ok);
		max_delay = 10000;
		assertTrue(solo.waitForActivity(MathTaskActivity.class));
		// May be not all test require this init in future
		//setTimeAndNetwork();
	}

// ========================================== Adapters ================================================================================		

	
// Mockito ============================================================================================================================
	
// Robotium ===========================================================================================================================
	
	
	public void testSimple() throws InterruptedException{
		assertTrue(solo.waitForActivity(MathTaskActivity.class));
		Thread.sleep(60000);
		solo.clickOnView(btCancel);
		//assertTrue(solo.waitForActivity(MainActivity.class));
		mActivity.ga_service.doExit();
		assertTrue(waitForActivityIsFinished(mActivity));
		assertTrue(waitForServiceStop());
		
	}
	
	}
