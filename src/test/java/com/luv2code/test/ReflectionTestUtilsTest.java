package com.luv2code.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import com.luv2code.component.MvcTestingExampleApplication;
import com.luv2code.component.models.CollegeStudent;
import com.luv2code.component.models.StudentGrades;

@SpringBootTest(classes = MvcTestingExampleApplication.class)
class ReflectionTestUtilsTest {
	
	@Autowired
	ApplicationContext context;
	
	@Autowired
	CollegeStudent studentOne;
	
	@Autowired
	StudentGrades studentGrades;
	
	@BeforeEach
	public void studentBeforeEach()
	{
		studentOne.setFirstname("Artur");
		studentOne.setLastname("Kings");
		studentOne.setEmailAddress("artur.kigns@citalin.com");
		studentOne.setStudentGrades(studentGrades);
		
		ReflectionTestUtils.setField(studentOne, "id",1);
		ReflectionTestUtils.setField(studentOne, "studentGrades",
				new StudentGrades(new ArrayList<>(Arrays.asList(
						100.0, 85.0, 76.5, 91.75
						)))
				
				);
	}

	@Test
	@DisplayName("Get data from private field")
	void getPriviateFieldData() {
		assertEquals(1,ReflectionTestUtils.getField(studentOne,"id"));
	}
	
	@Test
	@DisplayName("Invoking a private method")
	public void invokePrivateMethod()
	{
		assertEquals("Artur 1",
				ReflectionTestUtils.invokeMethod(studentOne, "getFirstNameAndId"),
				"Invoking a private method error"
				);
	}

}
