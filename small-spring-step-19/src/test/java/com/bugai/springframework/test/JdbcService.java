package com.bugai.springframework.test;

import com.bugai.springframework.jdbc.suport.JdbcTemplate;

import java.sql.SQLException;

public interface JdbcService {

  void saveDataWithTranslation() throws SQLException;


  void saveData(JdbcTemplate jdbcTemplate) ;
}
