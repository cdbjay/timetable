package com.project.timetable;

import com.college.timetable.TimetableApplication;
import com.college.timetable.repository.ClassCapacityRepository;
import com.college.timetable.repository.GradeInfoRepository;
import com.college.timetable.service.ReportGenerationService;
import com.college.timetable.service.TimeTableTemplateService;
import com.college.timetable.util.ReportGenerationUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TimetableApplication.class)
public class TimetableApplicationTests {
	@Autowired
	ReportGenerationService reportGenerationService;

	@Autowired
	TimeTableTemplateService timeTableTemplateService;

	@Autowired
	GradeInfoRepository gradeInfoRepository;

/*	@Test
	public void saveGradeInfoList(){
		reportGenerationUtil.getGradeInfoRepository().saveAll(
				Arrays.asList(
						new GradeInfo("UG",15),
						new GradeInfo("PG", 18)
				)
		);
	}*/

	/*@Test
	public void saveSubjectInfoList(){
		reportGenerationUtil.getSubjectRepository().saveAll(
				Arrays.asList(
						new SubjectInfo("CHEMISTRY"),
						new SubjectInfo("BIOLOGY")
				)
		);
	}*/

	/*@Test
	public void saveClassInfoList(){
		reportGenerationUtil.getClassInfoRepository().saveAll(
				Arrays.asList(
						//String courseName, String majorSubject, String section, Integer year, @NotNull Integer gradeId
						new ClassInfo("B.SC","MATHS","A",1, "UG"),
						new ClassInfo("M.SC","MATHS","A",1, "PG")
				)
		);
	}

	@Test
	public void saveLectureInfoList(){
		reportGenerationUtil.getLectureRepository().saveAll(
			Arrays.asList(
					//@NotNull String firstName, @NotNull String lastName, Integer employeeId, Integer capacity, Boolean isActive, Date beginEffectiveDate, Date endEffectiveDate
				new LectureInfo("Jay", "Thangeswaran",101, 40, Boolean.TRUE, Calendar.getInstance().getTime(), null)
			)
		);
	}

	@Test
	public void saveLectureCapabilityList(){
		reportGenerationUtil.getLectureCapabilityRepository().saveAll(
				Arrays.asList(
						//@NotNull Integer lectureId, @NotNull Integer subjectId, @NotNull String gradeLevel
						new LectureCapabilities(
						new LectureCapabilityCompositeKey(
						reportGenerationUtil.getLectureRepository().findByFirstNameAndLastName("Jay","Thangeswaran").getLectureId(),
						reportGenerationUtil.getSubjectRepository().findBySubjectName("MATHS").getSubjectId(),
						"UG"))
				)
		);
	}*/

	@Test
	public void testTemplateGenerateUtil() throws Exception {
		/*ClassCapacityRepository classCapacityRepository = templateGenerationUtil.getClassCapacityRepository();
		classCapacityRepository.findAll().forEach(System.out::println);*/
		reportGenerationService.buildTimeTableReport();
		//timeTableTemplateService.generateTemplate();

		//gradeInfoRepository.findAllByOrderByGradeSeniorityWhereClassFoundDesc().forEach(System.out::println);
	}

}
