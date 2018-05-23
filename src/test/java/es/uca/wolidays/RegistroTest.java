
package es.uca.wolidays;

import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.LinkedList;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.junit.ConcurrentParameterized;
import com.saucelabs.junit.SauceOnDemandTestWatcher;



@RunWith(ConcurrentParameterized.class)
@ContextConfiguration(classes = {TestConfig.class})
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class RegistroTest  implements SauceOnDemandSessionIdProvider {

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

	@Override
	public String getSessionId() {
		return sessionId;
	}

	
	
	@ClassRule
	public static final SpringClassRule springClassRule = new SpringClassRule();
	
	@Rule
	public final SpringMethodRule springMethodRule = new SpringMethodRule();
	
    protected String browser;
    protected String os;
    protected String version;
    protected String deviceName;
    protected String deviceOrientation;
    protected String sessionId;
    protected WebDriver driver;
    protected String username;
    protected String correo;

 

    public RegistroTest(String os, String version, String browser, String deviceName, String deviceOrientation, String username, String correo) {
        this.os = os;
        this.version = version;
        this.browser = browser;
        this.deviceName = deviceName;
        this.deviceOrientation = deviceOrientation;
        this.username = username;
        this.correo = correo;
    }
    @ConcurrentParameterized.Parameters
    public static LinkedList<String[]> browsersStrings() {
        LinkedList<String[]> browsers = new LinkedList<>();

        browsers.add(new String[]{"Windows 10", "14.14393", "MicrosoftEdge", null, null, "username1", "correo1@correo.com"});
        browsers.add(new String[]{"Windows 10", "49.0", "firefox", null, null, "username2", "correo2@correo.com"});
        browsers.add(new String[]{"Windows 7", "11.0", "internet explorer", null, null, "username3", "correo3@correo.com"});
        browsers.add(new String[]{"OS X 10.11", "10.0", "safari", null, null, "username4", "correo4@correo.com"});
        browsers.add(new String[]{"OS X 10.10", "54.0", "chrome", null, null, "username5", "correo5@correo.com"});
        return browsers;
    }


	@Autowired
	public Environment env;
	
	private TestContextManager testContextManager;
	
	private static final String HOST_URL = "http://ec2-18-236-104-144.us-west-2.compute.amazonaws.com:";
	private static final String XPATH_NAV_BTN_PERFIL = "//*[@id=\"nav_btn_perfil\"]/select";
	private static final String XPATH_NAV_BTN_INICIOSESION = "//*[@id=\"nav_btn_iniciosesion\"]";
	private static final String XPATH_BOTON_INICIOSESION = "//*[@id=\"form_btn_login\"]";
	private static final String XPATH_BOTON_REGISTRARSE_FORMULARIO = "//*[@id=\"form_btn_registrate\"]";
	private static final String XPATH_TEXTBOX_CONFIRMACIONCONTRASENA = "//*[@id=\"form_confirmpassword\"]";
	private static final String XPATH_TEXTBOX_CONTRASENA = "//*[@id=\"form_password\"]";
	private static final String XPATH_TEXTBOX_USERNAME = "//*[@id=\"form_username\"]";
	private static final String XPATH_TEXTBOX_CORREO = "//*[@id=\"form_correo\"]";
	private static final String XPATH_TEXTBOX_CUENTABANCARIA = "//*[@id=\"form_cuentabancaria\"]";
	private static final String XPATH_TEXTBOX_NOMBRE = "//*[@id=\"form_nombre\"]";
	private static final String XPATH_TEXTBOX_APELLIDOS = "//*[@id=\"form_apellidos\"]";
	private static final String XPATH_NAV_BTN_REGISTRARSE = "//*[@id=\"nav_btn_registrarse\"]";

	private WebDriverWait wait;

	
	@Before
	public void setUp() throws Exception {
		this.testContextManager = new TestContextManager(getClass());
		this.testContextManager.prepareTestInstance(this);
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability(CapabilityType.BROWSER_NAME, browser);
        capabilities.setCapability(CapabilityType.VERSION, version);
        capabilities.setCapability("deviceName", deviceName);
        capabilities.setCapability("device-orientation", deviceOrientation);
        capabilities.setCapability(CapabilityType.PLATFORM_NAME, os);

        String methodName = name.getMethodName();
        capabilities.setCapability("name", methodName);
        
		this.driver = new RemoteWebDriver(new URL(URL), capabilities);
		this.sessionId = (((RemoteWebDriver) driver).getSessionId()).toString();
		this.wait = new WebDriverWait(driver, 100);
	}

	@Ignore
	@Test
	public void registrarse() {
		driver.get(getHostUrl());
		WebElement botonRegistrarse = wait
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath(XPATH_NAV_BTN_REGISTRARSE)));
		botonRegistrarse.click();

		WebElement elementoFormulario = null;

		elementoFormulario = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(XPATH_TEXTBOX_NOMBRE)));
		elementoFormulario.sendKeys("ANTONIO");
		elementoFormulario = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(XPATH_TEXTBOX_APELLIDOS)));
		elementoFormulario.sendKeys("VÉLEZ ESTÉVEZ");
		elementoFormulario = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(XPATH_TEXTBOX_CORREO)));
		elementoFormulario.sendKeys(this.correo);
		elementoFormulario = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(XPATH_TEXTBOX_CUENTABANCARIA)));
		elementoFormulario.sendKeys("12345678901234567890");
		elementoFormulario = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(XPATH_TEXTBOX_USERNAME)));
		elementoFormulario.sendKeys(this.username);
		elementoFormulario = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(XPATH_TEXTBOX_CONTRASENA)));
		elementoFormulario.sendKeys("pruebacontraseña");
		elementoFormulario = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(XPATH_TEXTBOX_CONFIRMACIONCONTRASENA)));
		elementoFormulario.sendKeys("pruebacontraseña");

		elementoFormulario = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(XPATH_BOTON_REGISTRARSE_FORMULARIO)));
		elementoFormulario.click();

		assertTrue(wait.until(ExpectedConditions.urlContains("login")));
	}

	@Test
	public void iniciarSesion() {
		this.registrarse();
		driver.get(getHostUrl());
		WebElement botonRegistrarse = wait
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath(XPATH_NAV_BTN_INICIOSESION)));
		botonRegistrarse.click();

		WebElement elementoFormulario = null;

		elementoFormulario = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(XPATH_TEXTBOX_USERNAME)));
		elementoFormulario.sendKeys(this.username);
		elementoFormulario = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(XPATH_TEXTBOX_CONTRASENA)));
		elementoFormulario.sendKeys("pruebacontraseña");

		elementoFormulario = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(XPATH_BOTON_INICIOSESION)));
		elementoFormulario.click();

		WebElement navBtnPerfil = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(XPATH_NAV_BTN_PERFIL)));
		Boolean presente = false;
		for(WebElement seleccionable : navBtnPerfil.findElements(By.tagName("option")))
			if(seleccionable.getText().contains(this.username))
				presente = true;
		
		assertTrue(presente);
	}

	@After
	public void tearDown() {
		driver.quit();
	}

	private String getHostUrl() {
		return HOST_URL + env.getProperty("local.server.port");
	}
}
