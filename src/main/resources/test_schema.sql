  drop table time_table_report;
  drop table time_table_template;
  drop table lecture_capabilities;
  drop table lecture_info;
  drop table class_capacity;
  drop table class_info;
  drop table subject_info;
  drop table grade_info;
  drop table HIBERNATE_SEQUENCE;

  CREATE TABLE GRADE_INFO (
        GRADE_LEVEL varchar(20) NOT NULL,
        GRADE_SENIORITY int(11) NOT NULL,
        PRIMARY KEY (GRADE_LEVEL)
      );

  CREATE TABLE SUBJECT_INFO (
    SUBJECT_ID int(11) AUTO_INCREMENT NOT NULL,
    SUBJECT_NAME varchar(100) NOT NULL,
    UNIQUE KEY (SUBJECT_NAME),
    PRIMARY KEY (SUBJECT_ID)
  );

   CREATE TABLE CLASS_INFO (
     CLASS_ID int(11) AUTO_INCREMENT NOT NULL,
     COURSE_NAME varchar(50) NOT NULL,
     MAJOR_SUBJECT VARCHAR(10) DEFAULT NULL,
     SECTION varchar(5) DEFAULT NULL,
     YEAR int(1) DEFAULT NULL,
     GRADE_LEVEL varchar(20) NOT NULL,
     PRIMARY KEY (CLASS_ID),
     FOREIGN KEY (GRADE_LEVEL) REFERENCES GRADE_INFO (GRADE_LEVEL)
   );

   CREATE TABLE CLASS_CAPACITY (
        CLASS_ID int(11) NOT NULL,
        SUBJECT_ID int(11) NOT NULL,
        IS_PRACTICAL bit(1) NOT NULL DEFAULT 0,
        CAPACITY int(11) NOT NULL,
        PRIMARY KEY (CLASS_ID, SUBJECT_ID,IS_PRACTICAL),
        FOREIGN KEY (CLASS_ID) REFERENCES CLASS_INFO (CLASS_ID),
        FOREIGN KEY (SUBJECT_ID) REFERENCES SUBJECT_INFO (SUBJECT_ID)
      );

   CREATE TABLE LECTURE_INFO (
       LECTURE_ID int(11) AUTO_INCREMENT NOT NULL,
       FIRST_NAME varchar(255) NOT NULL,
       LAST_NAME varchar(255) NOT NULL,
       EMPLOYEE_ID int(11) DEFAULT NULL,
       CAPACITY int(11) NOT NULL,
       IS_ACTIVE bit(1) NOT NULL DEFAULT 1,
       BEGIN_EFFECTIVE_DATE date DEFAULT NULL,
       END_EFFECTIVE_DATE date DEFAULT NULL,
       PRIMARY KEY (LECTURE_ID)
   );

   CREATE TABLE LECTURE_CAPABILITIES (
     LECTURE_ID int(11) NOT NULL,
     SUBJECT_ID int(11) NOT NULL,
     GRADE_LEVEL VARCHAR(20) NOT NULL,
     PRIMARY KEY (LECTURE_ID,SUBJECT_ID,GRADE_LEVEL),
     FOREIGN KEY (LECTURE_ID) REFERENCES LECTURE_INFO (LECTURE_ID),
     FOREIGN KEY (SUBJECT_ID) REFERENCES SUBJECT_INFO (SUBJECT_ID),
     FOREIGN KEY (GRADE_LEVEL) REFERENCES GRADE_INFO (GRADE_LEVEL)
   );

    CREATE TABLE TIME_TABLE_TEMPLATE (
       CLASS_ID int(11) NOT NULL,
       DAY int(11) NOT NULL,
       HOUR int(11) NOT NULL,
       SUBJECT_ID int(11) NULL,
       IS_PRACTICAL bit(1) NULL DEFAULT 0,
       PRIMARY KEY (CLASS_ID,DAY,HOUR),
       FOREIGN KEY (CLASS_ID) REFERENCES CLASS_INFO (CLASS_ID),
       FOREIGN KEY (SUBJECT_ID) REFERENCES SUBJECT_INFO (SUBJECT_ID)
     );


      CREATE TABLE TIME_TABLE_REPORT (
             CLASS_ID int(11) NOT NULL,
             DAY int(11) NOT NULL,
             HOUR int(11) NOT NULL,
             SUBJECT_ID int(11) NULL,
             IS_PRACTICAL bit(1) NULL DEFAULT 0,
             LECTURE_ID int(11),
             OVERRIDE_DATE date NOT NULL,
             IS_GLOBAL bit(1) NOT NULL,
             PRIMARY KEY (CLASS_ID,DAY,HOUR,OVERRIDE_DATE, IS_GLOBAL),
             FOREIGN KEY (CLASS_ID) REFERENCES CLASS_INFO (CLASS_ID),
             FOREIGN KEY (LECTURE_ID) REFERENCES LECTURE_INFO (LECTURE_ID),
             FOREIGN KEY (SUBJECT_ID) REFERENCES SUBJECT_INFO (SUBJECT_ID)
           );

   CREATE TABLE HIBERNATE_SEQUENCE (
      NEXT_VAL bigint(20) DEFAULT NULL
    );

    INSERT INTO hibernate_sequence VALUES(1);