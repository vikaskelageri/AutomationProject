package AI.Attri.StepDefinitions.General;

import java.awt.AWTException;
import java.io.IOException;
import java.util.Map;

import AI.Attri.TestContext;
//import com.arisglobal.LSHE2.PageFactory.LSRIMS.General.Common;
import Ai.Attri.PageFactory.Product.General.Home;
import Ai.Attri.PageFactory.Product.General.Login;
import Ai.Attri.PageFactory.Product.General.Logout;
import Ai.Attri.PageFactory.Product.General.Navigate;
//import com.arisglobal.LSHE2.PageObjects.LSRIMS.General.CommonPageObjects;
import Ai.Attri.PageObjects.Product.General.LoginPageObjects;
import Ai.Attri.PageObjects.Product.General.LogoutPageObjects;
import Ai.Attri.Utils.DBUtil;
import Ai.Attri.Utils.XlsxVerification;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

public class LoginSteps {
    Login login;
    LoginPageObjects loginPageObjects;
    LogoutPageObjects logoutPageObjects;
    TestContext testContext;
    DBUtil dbUtil;
    Logout logout;
    Home home;
    Navigate navigate;

//    Common common;
//    CommonPageObjects commonPageObjects;
    public static String ENV = null;

    public LoginSteps(TestContext context) {
        testContext = context;
        login = testContext.getPageObjectManager().getLogin();
        dbUtil = new DBUtil();

        home = testContext.getPageObjectManager().getHome();

//        common = testContext.getPageObjectManager().getCommon();
    }

    public String getScenario(int count) {
        return testContext.getScenarioContext().getScenario(count);
    }

    public String getLoginScenario(String loginScenarioName) {
        String scenario = testContext.scenarioContext.getScenarioConstant("ScenarioName") + "_" + loginScenarioName;
        return scenario;
    }

    @Given("Set the Environment {string}")
    public void Set_Environment(String ENV) throws Exception {
        testContext.scenarioContext.putScenarioConstant("Environmet", ENV);

    }

    @Given("Update TestData")
    public void Set_TestData() throws Exception {
        String filepath = testContext.scenarioContext.getScenarioConstant("IExcel") + testContext.scenarioContext.getScenarioConstant("MasterTestDataFile");
//		String filepath = testContext.scenarioContext.getScenarioConstant("IExcel")+"Login_Logout.xlsx";
        XlsxVerification.writeToExcel(filepath, "TestCaseInfo", "Login_Logout1", "PURPOSE", "Updated from Script at 4:10");
        XlsxVerification.writeToExcel(filepath, "TestCaseInfo", 2, 2, "Updated from Script at 4:10");
        String cellData = XlsxVerification.readCellDataAsString(filepath, "TestCaseInfo", 2, 2);
    }

    @Given("Set testcase count to {string}")
    public void Set_TestcaseCount(String TC_Count) throws Exception {
        testContext.scenarioContext.putScenarioConstant("TC_Count", TC_Count);
    }

    @Given("launch {string}")
    public void launch(String URL) throws IOException, AWTException, InterruptedException {
        login.launchURL(URL);
    }

    @Given("Login with user")
    public void login_with_the_user() throws Exception {
//        String userName = testContext.scenarioContext.getTestDataFromIMap("LSRIMSLogin", getLoginScenario(user),
//                "User Name");
//        String password = testContext.scenarioContext.getTestDataFromIMap("LSRIMSLogin", getLoginScenario(user),
//                "Password");
//        String url = testContext.scenarioContext.getTestDataFromIMap("LSRIMSLogin", getLoginScenario(user), "URL");
        login.launchURL("https://attri.ai/");
//        login.doLogin(userName, password);
        // login.validateHome();
//		navigate.navigateToHeaderPage("Platform");
//		navigate.navigateToMenu("Users & Roles", "Users");

//        for (Map<String, String> data : dt.asMaps(String.class, String.class)) {
//
//            if (data.get("SetRole").equalsIgnoreCase("Yes")) {
//                String roleUser = testContext.scenarioContext.getTestDataFromIMap("LSRIMSLogin",
//                        getLoginScenario(data.get("USER")), "User Name");
//                String roles = testContext.scenarioContext.getTestDataFromIMap("LSRIMSLogin",
//                        getLoginScenario(data.get("USER")), "Roles");
//            }
//        }
        //navigate.navigateToHeaderPage("LSRIMS");

        // navigate.navigateToMenu("Home", "Default Dashboard");
        //// login.validateHome();

    }


    @Given("something")
    public void some() throws Exception {
        login.some();
    }
        @Given("Login with role user {string}")
    public void login_with_role_user(String user) throws Exception {

        System.out.print(testContext.scenarioContext.getScenarioConstant("Environmet"));
        String userName = testContext.scenarioContext.getTestDataFromIMap("LSRIMSLogin", getLoginScenario(user),
                "User Name");
        String password = testContext.scenarioContext.getTestDataFromIMap("LSRIMSLogin", getLoginScenario(user),
                "Password");
        String url = testContext.scenarioContext.getTestDataFromIMap("LSRIMSLogin", getLoginScenario(user), "URL");
        login.launchURL(url);


        // login.validateHome();
    }


    @Given("Login with role user env {string}")
    public void login_with_role_user_ENV(String user) throws Exception {

        System.out.print(testContext.scenarioContext.getScenarioConstant("Environmet"));
//        String userName = testContext.scenarioContext.getScenarioConstant("WS_USER" + "_" + user);
//        String password = testContext.scenarioContext.getScenarioConstant("WS_PASSWORD" + "_" + user);
        String url = testContext.scenarioContext.getScenarioConstant("URL");
        login.launchURL(url);
//        login.doLogin(userName, password);
    }

    @Given("Login with user the env {string} {string}")
    public void login_with_the_user_env(String user1, String user2, DataTable dt) throws Exception {
        String userName1 = testContext.scenarioContext.getScenarioConstant("WS_USER" + "_" + user1);
        String userName2 = testContext.scenarioContext.getScenarioConstant("WS_USER" + "_" + user2);
        String password = testContext.scenarioContext.getScenarioConstant("WS_PASSWORD" + "_" + user1);
        String url = testContext.scenarioContext.getScenarioConstant("URL" + "_" + user1);
        login.launchURL(url);

        // login.validateHome();

        // userAndRoles.setUserRoles(userName1,password,userName2,roles);

        for (Map<String, String> data : dt.asMaps(String.class, String.class)) {

            if (data.get("SetRole").equalsIgnoreCase("Yes")) {
//				navigate.navigateToHeaderPage("Platform");
//				navigate.navigateToMenu("Users & Roles", "Users");
                // String roleUser =
                // testContext.scenarioContext.getTestDataFromIMap("LSRIMSLogin",
                // getLoginScenario(data.get("USER")), "User Name");
                String rolesdt = data.get("Roles");


//				navigate.navigateToHeaderPage("LSRIMS");

                // navigate.navigateToMenu("Home", "Default Dashboard");
                //// login.validateHome();
            }
        }

    }

    @Given("Update user the env {string} {string}")
    public void update_the_user_env(String user1, String user2, DataTable dt) throws Exception {
        String userName1 = testContext.scenarioContext.getScenarioConstant("WS_USER" + "_" + user1);
        String userName2 = testContext.scenarioContext.getScenarioConstant("WS_USER" + "_" + user2);
        String password = testContext.scenarioContext.getScenarioConstant("WS_PASSWORD" + "_" + user1);
        for (Map<String, String> data : dt.asMaps(String.class, String.class)) {

            if (data.get("SetRole").equalsIgnoreCase("Yes")) {
//				navigate.navigateToHeaderPage("Platform");
//				navigate.navigateToMenu("Users & Roles", "Users");
                // String roleUser =
                // testContext.scenarioContext.getTestDataFromIMap("LSRIMSLogin",
                // getLoginScenario(data.get("USER")), "User Name");
                String rolesdt = data.get("Roles");

//				navigate.navigateToHeaderPage("LSRIMS");

                // navigate.navigateToMenu("Home", "Default Dashboard");
                //// login.validateHome();
            }
        }

    }


    @When("Login to the Application with User {string}")
    public void login_the_application_with_user(String str, DataTable dt) throws IOException, AWTException, InterruptedException {
        String Uname = "";
        for (Map<String, String> data : dt.asMaps(String.class, String.class)) {
            switch (data.get("Login_Details")) {

                case "URL":
                    Thread.sleep(2000);
                    String url = data.get("Value");
                    login.launchURL(url);


                    break;

                case "username":
                    Thread.sleep(2000);
                    Uname = data.get("Value");

                    break;
                case "password":
                    Thread.sleep(2000);
                    String Paswd = data.get("Value");
//                    login.doLogin(Uname, Paswd);
                    break;

            }
        }
    }


    }