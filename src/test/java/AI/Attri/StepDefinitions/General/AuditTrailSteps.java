package AI.Attri.StepDefinitions.General;

import java.awt.AWTException;
import java.io.IOException;

import AI.Attri.TestContext;
import Ai.Attri.PageFactory.Product.General.AuditTrial;
import Ai.Attri.PageObjects.Product.General.AuditTrialPageObjects;

import io.cucumber.java.en.Then;

public class AuditTrailSteps {
	
	AuditTrial auditTrail;
	AuditTrialPageObjects auditTrailPageObjects;
	TestContext testContext;

	
	public AuditTrailSteps(TestContext context) {
		testContext = context;
		auditTrail = testContext.getPageObjectManager().getAuditTrial();
	}

	@Then("save the module with {string}")
	public void save_the_module_with(String reason) throws IOException, AWTException, InterruptedException {
	    // Write code here that turns the phrase above into concrete actions
		
	   // auditTrail.save(reason);
	}
	

}
