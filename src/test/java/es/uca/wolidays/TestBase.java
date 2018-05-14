package es.uca.wolidays;

import java.util.LinkedList;

import org.junit.Rule;
import org.junit.rules.TestName;
import org.openqa.selenium.WebDriver;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.junit.ConcurrentParameterized;
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

	    protected String browser;
	    protected String os;
	    protected String version;
	    protected String deviceName;
	    protected String deviceOrientation;
	    protected String sessionId;
	    protected WebDriver driver;

	    /**
	     * Constructs a new instance of the test.  The constructor requires three string parameters, which represent the operating
	     * system, version and browser to be used when launching a Sauce VM.  The order of the parameters should be the same
	     * as that of the elements within the {@link #browsersStrings()} method.
	     * @param os
	     * @param version
	     * @param browser
	     * @param deviceName
	     * @param deviceOrientation
	     */

	    public TestBase(String os, String version, String browser, String deviceName, String deviceOrientation) {
	        super();
	        this.os = os;
	        this.version = version;
	        this.browser = browser;
	        this.deviceName = deviceName;
	        this.deviceOrientation = deviceOrientation;
	    }

	    /**
	     * @return a LinkedList containing String arrays representing the browser combinations the test should be run against. The values
	     * in the String array are used as part of the invocation of the test constructor
	     */
	    @ConcurrentParameterized.Parameters
	    public static LinkedList<String[]> browsersStrings() {
	        LinkedList<String[]> browsers = new LinkedList<>();

	        browsers.add(new String[]{"Windows 10", "14.14393", "MicrosoftEdge", null, null});
	        browsers.add(new String[]{"Windows 10", "49.0", "firefox", null, null});
	        browsers.add(new String[]{"Windows 7", "11.0", "internet explorer", null, null});
	        browsers.add(new String[]{"OS X 10.11", "10.0", "safari", null, null});
	        browsers.add(new String[]{"OS X 10.10", "54.0", "chrome", null, null});
	        return browsers;
	    }
	

	@Override
	public String getSessionId() {
		return sessionId;
	}

}