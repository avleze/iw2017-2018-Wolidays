
package es.uca.wolidays;

import org.junit.Rule;
import org.junit.rules.TestName;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.junit.SauceOnDemandTestWatcher;

public class TestBase implements SauceOnDemandSessionIdProvider{

	public static final String USERNAME = "avleze";
	public static final String ACCESS_KEY = "5acfd4e5-3c09-400a-b8be-44e843fcd417";
	public static final String URL = "https://" + USERNAME + ":" + ACCESS_KEY + "@ondemand.saucelabs.com:443/wd/hub";
	
	public SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication(USERNAME, ACCESS_KEY);
	@Rule
	public SauceOnDemandTestWatcher resultReportingTestWatcher = new SauceOnDemandTestWatcher(this, authentication);
	@Rule
	public TestName name = new TestName() {
			public String getMethodName() {
				return String.format("%s", super.getMethodName());
			}
		};

	protected String sessionId;

	@Override
	public String getSessionId() {
		return sessionId;
	}

}