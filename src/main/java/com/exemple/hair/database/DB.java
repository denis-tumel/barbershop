package com.exemple.hair.database;

import org.sql2o.*;

public class DB {
  public static Sql2o sql2o = new Sql2o("jdbc:postgresql://localhost:5432/postgres?currentSchema=hair_salon", "postgres", "root");
}
