package sndml.datamart;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;

import sndml.datamart.Loader;
import sndml.datamart.YamlFile;
import sndml.servicenow.*;

@RunWith(Parameterized.class)
public class RowCountExceededTest {

	final TestingProfile profile;
	final TestFolder folder = new TestFolder("Exceptions");
	final YamlFile yamlFile = folder.getYaml("row_count_exceeded");	
	final Logger logger = TestManager.getLogger(this.getClass());
			
	@Parameters(name = "{index}:{0}")
	public static TestingProfile[] profiles() {
		return TestManager.getDatamartProfiles();
	}

	@AfterClass
	public static void clear() throws Exception {
		TestManager.clearAll();
	}

	@After
	public void closeProfile() {
		profile.close();
	}
	
	public RowCountExceededTest(TestingProfile profile) {
		TestManager.setProfile(this.getClass(), profile);
		this.profile = profile;
	}
	
	@Test
	public void test() throws Exception {
		TestManager.bannerStart(this.getClass(), "test", yamlFile);
		Loader loader = yamlFile.getLoader(profile);
		boolean caught = false;
		try {
			loader.loadTables();			
		}
		catch (TooManyRowsException e) {
			logger.warn(Log.TEST, e.getMessage());
			caught = true;
		}
		logger.info(Log.TEST, "Caught=" + caught);		
		logger.info(Log.TEST, "End: " + yamlFile.getAbsolutePath());
		assertTrue(caught);
	}

}
