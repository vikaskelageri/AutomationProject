package AI.Attri.Runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
//@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        monochrome=true
        ,features = "classpath:com/arisglobal/LSHE2/Features"
        ,glue={"classpath:AI/Attri/StepDefinitions"}
        ,tags= "@testv2")
public class TestRunner {

}
//plugin = {"pretty", "rerun:target/failedrerun.txt"},