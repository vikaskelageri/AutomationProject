package AI.Attri.StepDefinitions.General;

import AI.Attri.TestContext;
import Ai.Attri.PageFactory.Product.General.Navigate;
import Ai.Attri.PageObjects.Product.General.NavigatePageObjects;

import io.cucumber.java.en.Given;

import java.awt.*;
import java.io.IOException;

public class NavigationSteps {
	Navigate navigate;
	NavigatePageObjects navigatePageObjects;
	TestContext testContext;
	

	
	@Given("Navigate to subtab {string} in documents tab")
	public void navigate_to_codelist_subtab(String Tab) throws InterruptedException, IOException, AWTException {
		navigate.navigateToSubtabDocuments(Tab);
	}

	

	}





