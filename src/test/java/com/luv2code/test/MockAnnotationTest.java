package com.luv2code.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;

import com.luv2code.component.MvcTestingExampleApplication;
import com.luv2code.component.dao.ApplicationDao;
import com.luv2code.component.models.CollegeStudent;
import com.luv2code.component.models.StudentGrades;
import com.luv2code.component.service.ApplicationService;

@SpringBootTest(classes = MvcTestingExampleApplication.class)
class MockAnnotationTest {
	
	@Autowired
	ApplicationContext context;
	
	@Autowired
	CollegeStudent studentOne;
	
	@Autowired
	StudentGrades studentGrades;
	
	//@Mock
	@MockBean
	private ApplicationDao applicationDao;
	
	//@InjectMocks
	@Autowired
	private ApplicationService applicationService;
	
	@BeforeEach
	public void beforeEach()
	{
		studentOne.setFirstname("Erick");
		studentOne.setLastname("Roby");
		studentOne.setEmailAddress("erick.roby@luv2code_school.com");
		studentOne.setStudentGrades(studentGrades);
		
	}

	@DisplayName("When & Verify")
	@Test
	void assertEqualsTestAddGrades() {
		
		// Set expectations
		when(applicationDao.addGradeResultsForSingleClass(
				studentGrades.getMathGradeResults()))
		.thenReturn(100.00);
		
		//Execute and Assert
		assertEquals(100, applicationService.addGradeResultsForSingleClass(
				studentOne.getStudentGrades().getMathGradeResults()));
		
		//Verify that the method ran
		verify(applicationDao).addGradeResultsForSingleClass(studentGrades
				.getMathGradeResults());
		//how many times ran
		verify(applicationDao, times(1)).addGradeResultsForSingleClass(studentGrades
				.getMathGradeResults());
		
	}
	
	@Test
	@DisplayName("Find GPA")
	public void assertEqualsTestFindGpa() 
	{
		when(applicationDao.findGradePointAverage(
				studentGrades.getMathGradeResults()))
		.thenReturn(88.31);
		
		assertEquals(88.31, applicationService.findGradePointAverage(
				studentGrades.getMathGradeResults()));
	}
	
	@Test
	@DisplayName("Check for Not null")
	public void testAssertNotNull()
	{
		when(applicationDao.checkNull(studentGrades.getMathGradeResults()))
		.thenReturn(true);
		
		assertNotNull(applicationService.checkNull(
				studentOne.getStudentGrades().getMathGradeResults()),
				"Object should not be null");
	}
	
	
	@Test
	@DisplayName("Throw Runtime Exception")
	public void throwRuntimeError()
	{
		CollegeStudent nullStudent = (CollegeStudent) context.getBean("collegeStudent");
		
		doThrow(new RuntimeException())
			.when(applicationDao).checkNull(nullStudent);
		
		assertThrows(RuntimeException.class, 
				() -> {
					applicationService.checkNull(nullStudent);
				}
				);
		
		verify(applicationDao, times(1)).checkNull(nullStudent);
	}
	
	@Test
	@DisplayName("Multiple Stubbing")
	public void stubbingConsecutiveCalls()
	{
		CollegeStudent nullStudent = (CollegeStudent) context.getBean("collegeStudent");
		when(applicationDao.checkNull(nullStudent))
			.thenThrow(new RuntimeException())
			.thenReturn("Do not throw exception second time");
		
		assertThrows(RuntimeException.class,
				() -> {
					applicationService.checkNull(nullStudent);					
		});
		
		assertEquals("Do not throw exception second time", 
				applicationService.checkNull(nullStudent));
		
		verify(applicationDao,times(2)).checkNull(nullStudent);
		
	}

}
