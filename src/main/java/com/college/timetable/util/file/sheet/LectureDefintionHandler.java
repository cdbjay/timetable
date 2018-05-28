package com.college.timetable.util.file.sheet;

import com.college.timetable.entity.LectureCapabilities;
import com.college.timetable.entity.LectureCapabilityCompositeKey;
import com.college.timetable.entity.LectureInfo;
import com.college.timetable.repository.LectureCapabilityRepository;
import com.college.timetable.repository.LectureRepository;
import com.college.timetable.repository.SubjectRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("LECTURE_DEFINITION")
public class LectureDefintionHandler implements ISheetHandler {

    private LectureRepository lectureRepository;
    private LectureCapabilityRepository lectureCapabilityRepository;
    private SubjectRepository subjectRepository;

    @Autowired
    public LectureDefintionHandler(LectureRepository lectureRepository, LectureCapabilityRepository lectureCapabilityRepository, SubjectRepository subjectRepository) {
        this.lectureRepository = lectureRepository;
        this.lectureCapabilityRepository = lectureCapabilityRepository;
        this.subjectRepository = subjectRepository;
    }

    @Override
    public void processSheet(XSSFSheet sheet) throws Exception {
        Row firstRow = sheet.getRow(sheet.getFirstRowNum());
        Map<Integer, String> indexHeaderMap = new HashMap<>();
        for (int index = 0; index < firstRow.getLastCellNum(); index++) {
            indexHeaderMap.put(index, firstRow.getCell(index).getStringCellValue());
        }
        for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            int firstCellNum = row.getFirstCellNum();
            LectureInfo lectureInfo = new LectureInfo();
            switch (firstCellNum) {
                case 0:
                    lectureInfo.setFirstName(row.getCell(0).getStringCellValue());
                case 1:
                    lectureInfo.setLastName(row.getCell(1).getStringCellValue());
                case 2:
                    lectureInfo.setEmployeeId(row.getCell(2) != null ? (int) row.getCell(2).getNumericCellValue() : null);
                case 3:
                    lectureInfo.setCapacity((int) row.getCell(3).getNumericCellValue());
                case 4:
                    lectureInfo.setActive(Boolean.TRUE);

            }
            lectureRepository.save(lectureInfo);
            processLectureCapability(row, lectureInfo, row.getCell(4).getStringCellValue(), indexHeaderMap);

        }
    }

    private void processLectureCapability(Row row, LectureInfo lectureInfo, String gradeLevel, Map<Integer, String> indexHeaderMap) {
        List<LectureCapabilities> lectureCapabilitiesList = new ArrayList<>();
        for (int j = 5; j <= row.getLastCellNum(); j++) {
            String cellValue = row.getCell(j) != null ? row.getCell(j).getStringCellValue() : null;
            if (cellValue != null) {
                Integer lectureId = lectureInfo.getLectureId();

                LectureCapabilities lectureCapabilities = new LectureCapabilities();
                String subjectName = indexHeaderMap.get(j);

                LectureCapabilityCompositeKey lectureCapabilityCompositeKey = new LectureCapabilityCompositeKey();
                lectureCapabilityCompositeKey.setLectureId(lectureId);
                lectureCapabilityCompositeKey.setSubjectId(subjectRepository.findBySubjectName(subjectName).getSubjectId());
                lectureCapabilityCompositeKey.setGradeLevel(gradeLevel);

                lectureCapabilities.setCompositeKey(lectureCapabilityCompositeKey);

                lectureCapabilitiesList.add(lectureCapabilities);
            }
        }
        if (lectureCapabilitiesList.size() > 0) {
            lectureCapabilityRepository.saveAll(lectureCapabilitiesList);
        }
    }
}
