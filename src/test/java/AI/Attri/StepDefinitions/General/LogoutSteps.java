package AI.Attri.StepDefinitions.General;

import AI.Attri.TestContext;

import Ai.Attri.PageFactory.Product.General.Logout;
import Ai.Attri.PageObjects.Product.General.LogoutPageObjects;

public class LogoutSteps {
	Logout logout;
	LogoutPageObjects logoutPageObjects;
	TestContext testContext;

	
	public String getScenario(int count) {
        return testContext.getScenarioContext().getScenario(count);
    }

	
	
	
	
}