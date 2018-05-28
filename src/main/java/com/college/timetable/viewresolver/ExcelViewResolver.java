package com.college.timetable.viewresolver;

import com.college.timetable.view.ExcelView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

public class ExcelViewResolver implements ViewResolver {

    @Override
    public View resolveViewName(String s, Locale locale) throws Exception {
        return new ExcelView();
    }
}
