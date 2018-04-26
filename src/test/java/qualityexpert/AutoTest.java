/**
 * Тестовое задание
 * Выполнено 27.04.2018
 * Роман Кондратенко
 * Quality Expert
 * Инженер по автоматизации тестирования
 */
package qualityexpert;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class AutoTest {
    public WebDriver driver;
    private WebElement element;
    private String expectedTitle;
    private String actual;
    private String nameDriver;
    private String fullDriverName;
    private String computerType;
    private String minPrice;
    private String maxPrice;
    private String brendName1;
    private String brendName2;

    /**
     * Конструктор с параметрами
     * @param fullDriverName --> полное имя драйвера
     * @param nameDriver     -->  имя драйвера
     * @param computerType   --> тип компьютера
     * @param minPrice       --> минимальная цена товара
     * @param maxPrice       --> максимальная цена товара
     * @param brendName1     --> наименование производителя товара
     * @param brendName2     --> наименование производителя товара
     */
    public AutoTest(String fullDriverName, String nameDriver, String computerType, String minPrice, String maxPrice, String brendName1, String brendName2) {
        this.nameDriver = nameDriver;
        this.fullDriverName = fullDriverName;
        this.computerType = computerType;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.brendName1 = brendName1;
        this.brendName2 = brendName2;

    }

    @Before
    public void setUp() {
        System.out.println("Run browser");
        System.setProperty(fullDriverName, "C:/Test/drivers/" + nameDriver);
        if (nameDriver.equals("chromedriver.exe")) {
            driver = new ChromeDriver();
        } else {
            driver = new FirefoxDriver();
        }
        driver.manage().window().maximize();
        driver.get("http://yandex.ru");
        expectedTitle = "Яндекс";
        actual = driver.getTitle();
        Assert.assertEquals(expectedTitle, actual);

    }


    @After
    public void afterEnd() {
      driver.close();
      }

    @Parameterized.Parameters
    public static Collection parameters() {
        return Arrays.asList(new Object[][]{{"webdriver.chrome.driver","chromedriver.exe", "Ноутбуки", "0", "30000", "152722", "152981"},
                {"webdriver.gecko.driver", "geckodriver.exe", "Планшеты", "20000", "25000", "152863", "459710"}});
    }

    @Test
    public void myTest() {
        goToMarket();
        goToComputers();
        chooseOfComputerType();
        goToFilters();
        choosePrice();
        chooseBrendName();
        clickShowResult();
        checkResult();

    }

    /**
     * Переход на страницу Яндекс-маркет
     */
    public void goToMarket() {
        element = driver.findElement(By.cssSelector("a[data-id = 'market']"));
        waitClickable(element, 10);
        element.click();
        expectedTitle = "Яндекс.Маркет — выбор и покупка товаров из проверенных интернет-магазинов";
        actual = driver.getTitle();
        Assert.assertEquals(expectedTitle, actual);
    }

    /**
     * Переход в раздел "Компьютеры"
     */
    public void goToComputers() {
        element = driver.findElement(By.xpath("//li/a[text()='Компьютеры']"));
        waitClickable(element, 10);
        element.click();
        expectedTitle = "Компьютерная техника — купить на Яндекс.Маркете";
        actual = driver.getTitle();
        Assert.assertEquals(expectedTitle, actual);
    }

    /**
     * Выбор типа компьютера
     */
    public void chooseOfComputerType() {
        element = driver.findElement(By.xpath("//div[@class='catalog-menu__list']/a[text()='" + computerType + "']"));
        waitClickable(element, 10);
        element.click();
    }

    /**
     * Переход в рассширенный поиск
     */
    public void goToFilters() {
        element = driver.findElement(By.xpath("//a[text()='Перейти ко всем фильтрам']"));
        waitClickable(element, 10);
        element.click();
    }

    /**
     * Устанавливаем диапазон цен
     */
    public void choosePrice() {
        element = driver.findElement(By.id("glf-pricefrom-var"));
        waitVisible(element, 10);
        element.sendKeys(minPrice);
        element = driver.findElement(By.id("glf-priceto-var"));
        element.sendKeys(maxPrice);
    }

    /**
     * Выбираем названия брендов
     */
    public void chooseBrendName() {

        WebElement element1 = driver.findElement(By.cssSelector("label[for='glf-7893318-" + brendName1));
        element1.click();
        WebElement element2 = driver.findElement(By.cssSelector("label[for='glf-7893318-" + brendName2));
        element2.click();
    }

    /**
     * Нажимаем на поиск результатов
     */
    public void clickShowResult() {
        element = element.findElement(By.xpath("//span[text()='Показать подходящие']/.."));
        waitVisible(element, 10);
        element.click();
    }

    /**
     * Свераем результаты поиска
     */
    public void checkResult() {
        String expectedTitle;
        String actualTitle;
        element = driver.findElement(By.xpath("//div[@class='n-snippet-list n-snippet-list_type_vertical metrika b-zone b-spy-init i-bem metrika_js_inited snippet-list_js_inited b-spy-init_js_inited b-zone_js_inited']/div[1]//div[@class='n-snippet-card2__title']/a"));
        expectedTitle = element.getText();
        System.out.println(expectedTitle);
        element = driver.findElement(By.id("header-search"));
        element.sendKeys(expectedTitle);
        element = driver.findElement(By.cssSelector("button.button2_js_inited"));
        waitClickable(element, 15);
        element.click();
        element = driver.findElement(By.cssSelector("div.n-title__text"));
        waitVisible(element, 10);
        actualTitle = element.getText().substring(0, expectedTitle.length());
        Assert.assertEquals(expectedTitle, actualTitle);

    }

    /**
     * Функция ожидания видимости элемента
     * @param element --> объект
     * @param timeout --> время ожидания
     * @return
     */
    public WebElement waitVisible(WebElement element, int timeout) {
        element = (new WebDriverWait(driver, timeout, 500))
                .until(ExpectedConditions.visibilityOf(element));
        return element;
    }
    /**
     * Функция ожидания кликабельности элемента
     * @param element --> объект
     * @param timeout --> время ожидания
     * @return
     */
    public WebElement waitClickable(WebElement element, int timeout) {
        element = (new WebDriverWait(driver, timeout, 500))
                .until(ExpectedConditions.elementToBeClickable(element));
        return element;
    }

}
