package com.college.timetable;

import com.college.timetable.entity.*;
import com.college.timetable.util.ReportGenerationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;
import com.college.timetable.AppConstants.ESubjectNames;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@SpringBootApplication
public class TimetableApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimetableApplication.class, args);
	}

}

class MyStartClass implements CommandLineRunner{
	ReportGenerationUtil reportGenerationUtil;
	@Autowired
	public MyStartClass(ReportGenerationUtil reportGenerationUtil) {
		this.reportGenerationUtil = reportGenerationUtil;
	}

	@Override
	public void run(String... args) throws Exception {
		//reportGenerationUtil.getTimeTableByMinGrade();
		//reportGenerationUtil.testCompositeKey();
	}
}

