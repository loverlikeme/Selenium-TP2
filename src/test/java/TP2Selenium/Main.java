package TP2Selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import TP2Selenium.models.Account;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {
    WebDriver driver;
    JavascriptExecutor jse;

    @Before
    public void prepareDriver(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(12));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(12));
        driver.manage().timeouts().scriptTimeout(Duration.ofMinutes(4));

        jse = (JavascriptExecutor) driver;
    }

    @Test
    public void buyMacTestCase() throws InterruptedException {
        // ********************* Scenario à testé **************
        // 1 - Créer un compte, déconnexion et connexion
        // 2 - Chercher un Mac
        // 3 - Ajouter au chariot puis valider la commande
        // -------------------------------------------

        driver.get("https://www.tunisianet.com.tn/");

        String pattern = "dd/MM/yyyy";
        SimpleDateFormat dateFormatter = new SimpleDateFormat(pattern);
        String codeForEmailAndPwd = RandomStringUtils.random(10, true, true);
        String codeForNames = RandomStringUtils.random(10, true, false);
        Account userAccount = new Account(
                codeForNames + "firstname",
                codeForNames + "lastname",
                codeForEmailAndPwd.substring(5) + "test@test.com",
                codeForEmailAndPwd,
                new Date(new Date().getTime() - TimeUnit.DAYS.toMillis(1) * 365 * 30)
        );

        // Cliquez sur l'icone de l'utilisateur
        Thread.sleep(2000); 
        WebElement userDropdown = driver.findElement(By.cssSelector("#_desktop_user_info > div > div > svg"));
        userDropdown.click();

        // Cliquez sur "Connexion"
        Thread.sleep(2000);
        WebElement connexion = driver.findElement(By.cssSelector(".user-down > li > a > span"));
        connexion.click();

        // Cliquze sur créer un compte
        Thread.sleep(2000);
        WebElement creerCompte = driver.findElement(By.className("no-account"));
        Assert.assertEquals("Pas de compte ? Créez-en un", creerCompte.findElement(By.cssSelector("*")).getText());
        creerCompte.click();

        // Choisir homme comme gendre
        Thread.sleep(2000);
        List<WebElement> sexeGendre = driver.findElements(By.className("custom-radio"));
        sexeGendre.get(0).click();

        Thread.sleep(2000);
        List<WebElement> registerForm = driver.findElements(By.cssSelector("input.form-control"));
        // la barre de recherche est d'index (0) donc on brule cette etape
        registerForm.get(1).sendKeys(userAccount.firstName);
        registerForm.get(2).sendKeys(userAccount.lastName);
        registerForm.get(3).sendKeys(userAccount.email);
        registerForm.get(4).sendKeys(userAccount.password);
        registerForm.get(5).sendKeys(dateFormatter.format(userAccount.birthday));

        //En Bas
        jse.executeScript("window.scrollBy(0,250)", ""); 

        // Cliquze sur SignUp
        Thread.sleep(2000);
        WebElement signUp = driver.findElement(By.className("form-control-submit"));
        signUp.click();

        // Cliquez sur l'icon de l'utilisateur
        Thread.sleep(2000);
        userDropdown = driver.findElement(By.cssSelector("#_desktop_user_info > div > div > svg"));
        userDropdown.click();

        // Cliquez sur "deconnexion"
        Thread.sleep(2000);
        WebElement deconnexion = driver.findElement(By.className("logout"));
        deconnexion.click();

        Thread.sleep(2000);
        userDropdown = driver.findElement(By.cssSelector("#_desktop_user_info > div > div > svg"));
        userDropdown.click();

        Thread.sleep(2000);
        connexion = driver.findElement(By.cssSelector(".user-down > li > a > span"));
        connexion.click();

        // Donnez un e-mail
        Thread.sleep(2000);
        WebElement emailInput = driver.findElement(By.cssSelector(".form-group > div > input"));
        emailInput.sendKeys(userAccount.email);

        // Donnez un pwd
        Thread.sleep(2000);
        WebElement pwdInput = driver.findElement(By.cssSelector(".form-group > div > div > input"));
        pwdInput.sendKeys(userAccount.password);

        // Cliquez sur "valider"
        Thread.sleep(2000);
        WebElement validerSubmit = driver.findElement(By.id("submit-login"));
        validerSubmit.click();

        // Rechercher le Mac
        Thread.sleep(2000);
        WebElement searchQuery = driver.findElement(By.className("search_query"));
        searchQuery.sendKeys("PC portable MacBook M1 13.3");

        Thread.sleep(2000);
        WebElement search = driver.findElement(By.cssSelector("#sp-btn-search > button"));
        search.click();

        // Cliquez sur le premier champ
        Thread.sleep(2000);
        List<WebElement> productTitle = driver.findElements(By.className("product-title"));
        productTitle.get(0).click();

        // Ajouter au chariot
        Thread.sleep(2000);
        WebElement addToCart = driver.findElement(By.className("add-to-cart"));
        addToCart.click();

        // Valider Commande
        Thread.sleep(2000);
        WebElement validerCommande = driver.findElement(By.cssSelector("a.btn-block"));
        validerCommande.click();
    }

    @After
    public void quitDriver() throws InterruptedException {
        Thread.sleep(10000);
        driver.quit();
    }
}