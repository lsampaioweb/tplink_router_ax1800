package tplink.router.ax1800.page;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.directory.AttributeInUseException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import tplink.router.ax1800.model.RoutingRule;

public class StaticRouting extends BasePage {

  private static final String URL = "#routingAdv";

  private static final String XPATH_BUTTON_ADD = "//a[@tbar-name='add']";
  private static final String XPATH_BUTTON_SAVE = "//div[@id='static-routing-grid-save-button']//a";
  private static final String XPATH_BUTTON_EDIT = "//tr[contains (@id, 'static-routing')][%d]//a[1]";
  private static final String XPATH_BUTTON_CANCEL = "//div[@id='static-routing-grid-cancel-button']//a";
  private static final String XPATH_BUTTON_DELETE = "//tr[contains (@id, 'static-routing')][%d]//a[2]";

  private static final String XPATH_PAGING_BTN_NUM = "//div[@id='static-routing-grid_paging']//a[contains (@class, 'paging-btn-num')]";

  private static final String XPATH_STATIC_ROUTING = "//td[contains (@id, 'static-routing') and @name='%s']//div[@class='content']";

  private static final String XPATH_TARGET_ELEMENTS = String.format(XPATH_STATIC_ROUTING, "target");
  private static final String XPATH_NETMASK_ELEMENTS = String.format(XPATH_STATIC_ROUTING, "netmask");
  private static final String XPATH_GATEWAY_ELEMENTS = String.format(XPATH_STATIC_ROUTING, "gateway");
  private static final String XPATH_INTERFACE_ELEMENTS = String.format(XPATH_STATIC_ROUTING, "interface");
  private static final String XPATH_DESCRIPTION_ELEMENTS = String.format(XPATH_STATIC_ROUTING, "name");

  private static final String XPATH_ELEMENT_FORM = "//div[@data-bind='{newRoutingModel.%s}']";
  private static final String XPATH_INPUT = "//input";
  private static final String XPATH_MESSAGE_ERROR = "//div[@class='content error-tips-content']";

  private static final String XPATH_ELEMENT_TARGET = String.format(XPATH_ELEMENT_FORM, "target");
  private static final String XPATH_ELEMENT_NETMASK = String.format(XPATH_ELEMENT_FORM, "netmask");
  private static final String XPATH_ELEMENT_GATEWAY = String.format(XPATH_ELEMENT_FORM, "gateway");

  private static final String XPATH_ELEMENT_INTERFACE = String.format(XPATH_ELEMENT_FORM, "interface");
  private static final String XPATH_ELEMENT_INTERFACE_COMBOBOX = "//li[@class='combobox-list']//span[text()='%s']";
  private static final String XPATH_ELEMENT_INTERFACE_LINK = "//a";
  private static final String INTERFACE_WAN = "WAN";
  private static final String INTERFACE_LAN_WLAN = "LAN/WLAN";

  private static final String XPATH_ELEMENT_DESCRIPTION = String.format(XPATH_ELEMENT_FORM, "name");

  public StaticRouting() {
    this(URL);
  }

  public StaticRouting(String url) {
    super(url);
  }

  public void addStaticRoutings(WebDriver driver, WebDriverWait wait, List<RoutingRule> routingRules)
      throws URISyntaxException {
    goTo(driver, wait);

    waitUntilPresenceOfElement(wait, By.xpath(XPATH_BUTTON_ADD));

    for (RoutingRule routingRule : routingRules) {
      List<RoutingRule> existingRoutingRules = getExistingRoutingRules(wait);

      if ((existingRoutingRules.isEmpty()) || (!routingRuleIsInTheExistingList(routingRule, existingRoutingRules))) {
        addRoutingRule(driver, wait, routingRule);
      } else {
        editRoutingRule(driver, wait, routingRule, existingRoutingRules);
      }
    }

    // Verify if there are rules that should be deleted.
    List<RoutingRule> existingRoutingRules = getExistingRoutingRules(wait);
    List<RoutingRule> rulesTodelete = getExistingRulesThatShouldBeDeleted(existingRoutingRules, routingRules);

    deleteRoutingRules(driver, wait, rulesTodelete);
  }

  private List<RoutingRule> getExistingRoutingRules(WebDriverWait wait) {
    List<RoutingRule> routingRules = new ArrayList<>();

    // If the page has more than 6 static routings, the page will have pagination
    // buttons.
    List<WebElement> pagingElements = getPagingElements(wait);

    if (pagingElements.isEmpty()) {
      extractRoutingRulesFromHtml(wait, routingRules);
    } else {
      // It is necessary to iterate on all the pages.
      for (int i = 0; i < pagingElements.size(); i++) {
        elementClick(pagingElements.get(i));

        waitUntilPageIsFullyLoaded(wait);

        extractRoutingRulesFromHtml(wait, routingRules);
      }
    }

    return routingRules;
  }

  private boolean routingRuleIsInTheExistingList(RoutingRule routingRule, List<RoutingRule> existingRoutingRules) {
    for (RoutingRule currentRoutingRule : existingRoutingRules) {
      if (currentRoutingRule.equals(routingRule)) {
        return true;
      }
    }
    return false;
  }

  private void extractRoutingRulesFromHtml(WebDriverWait wait, List<RoutingRule> routingRules) {
    List<WebElement> targetElements = getTargetElements(wait);
    getLogger().info(getI18n().getString("target_elements"), targetElements.size());

    if (!targetElements.isEmpty()) {
      List<WebElement> netmaskElements = getNetmaskElements(wait);
      List<WebElement> gatewayElements = getGatewayElements(wait);
      List<WebElement> interfaceElements = getInterfaceElements(wait);
      List<WebElement> descriptionElements = getDescriptionElements(wait);

      for (int i = 0; i < targetElements.size(); i++) {
        WebElement targetElement = targetElements.get(i);

        if (targetElement.isDisplayed()) {
          String target = targetElement.getText();
          String netmask = netmaskElements.get(i).getText();
          String gateway = gatewayElements.get(i).getText();
          String interfaceName = interfaceElements.get(i).getText();
          String description = descriptionElements.get(i).getText();

          routingRules.add(new RoutingRule(target, netmask, gateway, interfaceName, description));
        }
      }
    }
  }

  private void addRoutingRule(WebDriver driver, WebDriverWait wait, RoutingRule routingRule) throws URISyntaxException {
    try {
      elementClick(getAddButtonElement(wait));

      setTargetValue(wait, routingRule);

      setNetmaskValue(wait, routingRule);

      setGatewayValue(wait, routingRule);

      setInterfaceNameValue(wait, routingRule);

      setDescriptionValue(wait, routingRule);

      elementClick(getSaveButtonElement(wait));

      goTo(driver, wait);

    } catch (Exception e) {
      getLogger().error(e.getLocalizedMessage());

      elementClick(getCancelButtonElement(wait));
    }
  }

  private void editRoutingRule(WebDriver driver, WebDriverWait wait, RoutingRule routingRule,
      List<RoutingRule> existingRoutingRules) throws URISyntaxException {
    try {
      for (int i = 0; i < existingRoutingRules.size(); i++) {
        RoutingRule currentRule = existingRoutingRules.get(i);

        if (routingRule.equals(currentRule)) {
          boolean netmaskChanged = false;
          boolean gatewayChanged = false;
          boolean interfaceNameChanged = false;
          boolean descriptionChanged = false;

          if (!routingRule.getNetmask().equals(currentRule.getNetmask())) {
            netmaskChanged = true;
          }

          if (!routingRule.getGateway().equals(currentRule.getGateway())) {
            gatewayChanged = true;
          }

          if (!routingRule.getInterfaceName().equals(currentRule.getInterfaceName())) {
            interfaceNameChanged = true;
          }

          if (!routingRule.getDescription().equals(currentRule.getDescription())) {
            descriptionChanged = true;
          }

          if (anyValueChanged(netmaskChanged, gatewayChanged, interfaceNameChanged, descriptionChanged)) {
            navigateToTheRightPage(wait, i + 1);

            elementClick(getEditButtonElementAtIndex(wait, i + 1));

            if (netmaskChanged) {
              setNetmaskValue(wait, routingRule);
            }
            if (gatewayChanged) {
              setGatewayValue(wait, routingRule);
            }
            if (interfaceNameChanged) {
              setInterfaceNameValue(wait, routingRule);
            }
            if (descriptionChanged) {
              setDescriptionValue(wait, routingRule);
            }

            elementClick(getSaveButtonElement(wait));

            goTo(driver, wait);
          }
          break;

        }
      }
    } catch (Exception e) {
      getLogger().error(e.getLocalizedMessage());

      elementClick(getCancelButtonElement(wait));
    }
  }

  private boolean anyValueChanged(boolean... values) {
    for (boolean currentValue : values) {
      if (currentValue) {
        return true;
      }
    }
    return false;
  }

  private void navigateToTheRightPage(WebDriverWait wait, int position) {
    List<WebElement> pagingElements = getPagingElements(wait);
    if (!pagingElements.isEmpty()) {
      int elementsPerPage = 6;

      int pageToGo = Math.floorDiv(position, elementsPerPage);
      if (position % elementsPerPage == 0) {
        pageToGo--;
      }

      elementClick(pagingElements.get(pageToGo));

      waitUntilPageIsFullyLoaded(wait);
    }
  }

  private List<RoutingRule> getExistingRulesThatShouldBeDeleted(List<RoutingRule> existingRoutingRules,
      List<RoutingRule> routingRules) {
    List<RoutingRule> differences = new ArrayList<>(existingRoutingRules);

    differences.removeAll(routingRules);

    return differences;
  }

  private void deleteRoutingRules(WebDriver driver, WebDriverWait wait, List<RoutingRule> rulesTodelete)
      throws URISyntaxException {
    for (RoutingRule routingRule : rulesTodelete) {
      List<WebElement> targetElements = getTargetElements(wait);

      for (int i = 0; i < targetElements.size(); i++) {
        WebElement currentElement = targetElements.get(i);

        if (routingRule.getTarget().equals(currentElement.getAttribute("textContent"))) {

          navigateToTheRightPage(wait, i + 1);

          elementClick(getDeleteButtonElementAtIndex(wait, i + 1));

          wait.until(ExpectedConditions.stalenessOf(currentElement));

          goTo(driver, wait);

          getLogger().info(getI18n().getString("deleted_rule"), routingRule);

          break;
        }
      }
    }
  }

  private void setTargetValue(WebDriverWait wait, RoutingRule routingRule) throws AttributeInUseException {
    setInputValue(getTargetInputElement(wait), routingRule.getTarget());

    throwExceptionIfAnyErrorMessageWasFound(getTargetErrorMessageElement(wait), routingRule.getTarget());
  }

  private void setNetmaskValue(WebDriverWait wait, RoutingRule routingRule) throws AttributeInUseException {
    setInputValue(getNetmaskInputElement(wait), routingRule.getNetmask());

    throwExceptionIfAnyErrorMessageWasFound(getNetmaskErrorMessageElement(wait), routingRule.getNetmask());
  }

  private void setGatewayValue(WebDriverWait wait, RoutingRule routingRule) throws AttributeInUseException {
    setInputValue(getGatewayInputElement(wait), routingRule.getGateway());

    throwExceptionIfAnyErrorMessageWasFound(getGatewayErrorMessageElement(wait), routingRule.getGateway());
  }

  private void setInterfaceNameValue(WebDriverWait wait, RoutingRule routingRule) {
    elementClick(getInterfaceNameComboboxElement(wait));

    elementClick(getInterfaceNameSpanElement(wait, routingRule));
  }

  private void setDescriptionValue(WebDriverWait wait, RoutingRule routingRule) throws AttributeInUseException {
    setInputValue(getDescriptionInputElement(wait), routingRule.getDescription());

    throwExceptionIfAnyErrorMessageWasFound(getDescriptionErrorMessageElement(wait), routingRule.getDescription());
  }

  private void throwExceptionIfAnyErrorMessageWasFound(WebElement errorMessageElement, String attributeValue)
      throws AttributeInUseException {
    if (!errorMessageElement.getText().isBlank()) {
      String errorMessage = String.format(getI18n().getString("error_message_found"), attributeValue,
          errorMessageElement.getText());

      throw new AttributeInUseException(errorMessage);
    }
  }

  private List<WebElement> getPagingElements(WebDriverWait wait) {
    return findElementsByPath(wait, XPATH_PAGING_BTN_NUM);
  }

  private List<WebElement> getTargetElements(WebDriverWait wait) {
    return findElementsByPath(wait, XPATH_TARGET_ELEMENTS);
  }

  private List<WebElement> getNetmaskElements(WebDriverWait wait) {
    return findElementsByPath(wait, XPATH_NETMASK_ELEMENTS);
  }

  private List<WebElement> getGatewayElements(WebDriverWait wait) {
    return findElementsByPath(wait, XPATH_GATEWAY_ELEMENTS);
  }

  private List<WebElement> getInterfaceElements(WebDriverWait wait) {
    return findElementsByPath(wait, XPATH_INTERFACE_ELEMENTS);
  }

  private List<WebElement> getDescriptionElements(WebDriverWait wait) {
    return findElementsByPath(wait, XPATH_DESCRIPTION_ELEMENTS);
  }

  private WebElement getTargetInputElement(WebDriverWait wait) {
    return findElementByPath(wait, XPATH_ELEMENT_TARGET + XPATH_INPUT);
  }

  private WebElement getTargetErrorMessageElement(WebDriverWait wait) {
    return findElementByPath(wait, XPATH_ELEMENT_TARGET + XPATH_MESSAGE_ERROR);
  }

  private WebElement getNetmaskInputElement(WebDriverWait wait) {
    return findElementByPath(wait, XPATH_ELEMENT_NETMASK + XPATH_INPUT);
  }

  private WebElement getNetmaskErrorMessageElement(WebDriverWait wait) {
    return findElementByPath(wait, XPATH_ELEMENT_NETMASK + XPATH_MESSAGE_ERROR);
  }

  private WebElement getGatewayInputElement(WebDriverWait wait) {
    return findElementByPath(wait, XPATH_ELEMENT_GATEWAY + XPATH_INPUT);
  }

  private WebElement getGatewayErrorMessageElement(WebDriverWait wait) {
    return findElementByPath(wait, XPATH_ELEMENT_GATEWAY + XPATH_MESSAGE_ERROR);
  }

  private WebElement getInterfaceNameComboboxElement(WebDriverWait wait) {
    return findElementByPath(wait, XPATH_ELEMENT_INTERFACE + XPATH_ELEMENT_INTERFACE_LINK);
  }

  private WebElement getInterfaceNameSpanElement(WebDriverWait wait, RoutingRule routingRule) {
    String interfaceName = (INTERFACE_WAN.equals(routingRule.getInterfaceName()) ? INTERFACE_WAN : INTERFACE_LAN_WLAN);

    return findElementByPath(wait, String.format(XPATH_ELEMENT_INTERFACE_COMBOBOX, interfaceName));
  }

  private WebElement getDescriptionInputElement(WebDriverWait wait) {
    return findElementByPath(wait, XPATH_ELEMENT_DESCRIPTION + XPATH_INPUT);
  }

  private WebElement getDescriptionErrorMessageElement(WebDriverWait wait) {
    return findElementByPath(wait, XPATH_ELEMENT_DESCRIPTION + XPATH_MESSAGE_ERROR);
  }

  private WebElement getAddButtonElement(WebDriverWait wait) {
    return findElementByPath(wait, XPATH_BUTTON_ADD);
  }

  private WebElement getSaveButtonElement(WebDriverWait wait) {
    return findElementByPath(wait, XPATH_BUTTON_SAVE);
  }

  private WebElement getEditButtonElementAtIndex(WebDriverWait wait, int index) {
    return findElementByPath(wait, String.format(XPATH_BUTTON_EDIT, index));
  }

  private WebElement getCancelButtonElement(WebDriverWait wait) {
    return findElementByPath(wait, XPATH_BUTTON_CANCEL);
  }

  private WebElement getDeleteButtonElementAtIndex(WebDriverWait wait, int index) {
    return findElementByPath(wait, String.format(XPATH_BUTTON_DELETE, index));
  }

}
