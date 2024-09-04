package api.test;

import org.apache.logging.log4j.LogManager;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.UserEndPoints;
import api.payloads.User;
import io.restassured.response.Response;

public class UserTest {
	
	Faker faker;
	User userPayload;
	public org.apache.logging.log4j.Logger logger;
	

	@BeforeClass
	public void setupData() {
		
		faker = new Faker();
		userPayload = new User();
		logger=LogManager.getLogger(this.getClass());
		logger.debug("debugging......");
		
		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setUsername(faker.name().username());
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setPassword(faker.internet().password());
		userPayload.setPhone(faker.phoneNumber().cellPhone());
		
		
	}
	
	@Test(priority =1)
	public void testPostUser() {
		logger.info("********Creating User******");
		Response response= UserEndPoints.createUser(userPayload);
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(), 200);
		logger.info("*****user is created****");
	}
	
	@Test(priority=2)
	public void testGetUserByName() {
		logger.info("******Reading user info*****");
		Response response =UserEndPoints.readUser(this.userPayload.getUsername());
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
		
		logger.info("******user info is displayed*****");
		
	}
	
	@Test(priority =3)
	public void testUpdateUserByName() {
		
		logger.info("*****Update user*****");
		
		//update data using payload
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
	
		
		
		Response response = UserEndPoints.updateUser(this.userPayload.getUsername(), userPayload);
		response.then().log().body();
		Assert.assertEquals(response.getStatusCode(), 200);
		
		//Checking data after update
		Response responseAfterUpdate =UserEndPoints.readUser(this.userPayload.getUsername());
		response.then().log().all();
		Assert.assertEquals(responseAfterUpdate.getStatusCode(), 200);
		
		logger.info("*****Update user is displayed*****");
		
	
	}
	
	@Test(priority =4)
	public void testDeleteUserByName()
	{
		logger.info("*****delete user*****");
		Response response = UserEndPoints.deleteUser(this.userPayload.getUsername());
		Assert.assertEquals(response.getStatusCode(), 200);
		
		logger.info("*****delete user is displayed*****");
	}
	
}
