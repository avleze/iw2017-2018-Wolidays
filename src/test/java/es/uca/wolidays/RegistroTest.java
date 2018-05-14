package es.uca.wolidays;

import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.saucelabs.common.SauceOnDemandSessionIdProvider;

public class RegistroTest extends TestBase implements SauceOnDemandSessionIdProvider {


	private static final String HOST_URL = "http://ec2-18-236-104-144.us-west-2.compute.amazonaws.com:";
	private static final String PORT = "8080";
	private static final String XPATH_NAV_BTN_PERFIL = "//*[@id=\"nav_btn_perfil\"]";
	private static final String XPATH_NAV_BTN_INICIOSESION = "//*[@id=\"nav_btn_iniciosesion\"]";
	private static final String XPATH_BOTON_INICIOSESION = "//*[@id=\"form_btn_login\"]";
	private static final String XPATH_BOTON_REGISTRARSE_FORMULARIO = "//*[@id=\"form_btn_registrate\"]";
	private static final String XPATH_TEXTBOX_CONFIRMACIONCONTRASENA = "//*[@id=\"form_confirmpassword\"]";
	private static final String XPATH_TEXTBOX_CONTRASENA = "//*[@id=\"form_password\"]";
	private static final String XPATH_TEXTBOX_USERNAME = "//*[@id=\"form_username\"]";
	private static final String XPATH_TEXTBOX_CORREO = "//*[@id=\"form_correo\"]";
	private static final String XPATH_TEXTBOX_NOMBRE = "//*[@id=\"form_nombre\"]";
	private static final String XPATH_TEXTBOX_APELLIDOS = "//*[@id=\"form_apellidos\"]";

	private static final String XPATH_NAV_BTN_REGISTRARSE = "//*[@id=\"nav_btn_registrarse\"]";

	private WebDriver driver;
	private WebDriverWait wait;

	@Before
	public void setUp() throws MalformedURLException {
		ChromeOptions caps = new ChromeOptions();
		caps.setCapability("platform", "Windows 10");
		caps.setCapability("version", "latest");
		this.driver = new RemoteWebDriver(new URL(URL), caps);
		this.sessionId = (((RemoteWebDriver) driver).getSessionId()).toString();
		this.wait = new WebDriverWait(driver, 100);
	}

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
		elementoFormulario.sendKeys("antonio.velezestevez@alum.uca.es");
		elementoFormulario = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(XPATH_TEXTBOX_USERNAME)));
		elementoFormulario.sendKeys("pruebausername");
		elementoFormulario = wait
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath(XPATH_TEXTBOX_CONTRASENA)));
		elementoFormulario.sendKeys("pruebacontraseña");
		elementoFormulario = wait
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath(XPATH_TEXTBOX_CONFIRMACIONCONTRASENA)));
		elementoFormulario.sendKeys("pruebacontraseña");

		elementoFormulario = wait
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath(XPATH_BOTON_REGISTRARSE_FORMULARIO)));
		elementoFormulario.click();

		assertTrue(wait.until(ExpectedConditions.urlContains("login")));
	}

	@Test
	public void iniciarSesion() {
		driver.get(getHostUrl());
		WebElement botonRegistrarse = wait
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath(XPATH_NAV_BTN_INICIOSESION)));
		botonRegistrarse.click();

		WebElement elementoFormulario = null;

		elementoFormulario = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(XPATH_TEXTBOX_USERNAME)));
		elementoFormulario.sendKeys("pruebausername");
		elementoFormulario = wait
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath(XPATH_TEXTBOX_CONTRASENA)));
		elementoFormulario.sendKeys("pruebacontraseña");

		elementoFormulario = wait
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath(XPATH_BOTON_INICIOSESION)));
		elementoFormulario.click();

		WebElement navBtnPerfil = wait
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath(XPATH_NAV_BTN_PERFIL)));
		assertTrue(navBtnPerfil.isDisplayed());
	}

	@After
	public void tearDown() {
		driver.quit();
	}

	private String getHostUrl() {
		return HOST_URL + PORT;
	}
}
