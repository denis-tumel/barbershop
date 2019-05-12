package com.exemple.hair.model;

import java.util.List;

import com.exemple.hair.database.DB;
import org.sql2o.*;

public class Stylist {
  private int id;
  private String userName;
  private String firstName;
  private String lastName;
  private String specialty;

  public Stylist(String userName, String firstName, String lastName, String specialty) {
    this.userName = userName;
    this.firstName = firstName;
    this.lastName = lastName;
    this.specialty = specialty;
  }

  public int getId() {
    return this.id;
  }

  public String getUserName() {
    return this.userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getFirstName() {
    return this.firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return this.lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getSpecialty() {
    return this.specialty;
  }

  public void setSpecialty(String specialty) {
    this.specialty = specialty;
  }

  public String getFullName() {
    return this.firstName + " " + this.lastName;
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO stylists (username, firstname, lastname, specialty) VALUES (:username, :firstname, :lastname, :specialty);";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("username", this.userName)
        .addParameter("firstname", this.firstName)
        .addParameter("lastname", this.lastName)
        .addParameter("specialty", this.specialty)
        .executeUpdate()
        .getKey();
    }
  }

  public void update() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE stylists SET username = :username, firstname = :firstname, lastname = :lastname, specialty = :specialty WHERE id = :id;";
      con.createQuery(sql)
        .addParameter("username", this.userName)
        .addParameter("firstname", this.firstName)
        .addParameter("lastname", this.lastName)
        .addParameter("specialty", this.specialty)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM stylists WHERE id = :id;";
      con.createQuery(sql)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  public static Stylist find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM stylists WHERE id = :id;";
      Stylist stylist = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Stylist.class);
      return stylist;
    }
  }

  public static Stylist findByUserName(String userName) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM stylists WHERE username = :username;";
      Stylist stylist = con.createQuery(sql)
        .addParameter("username", userName)
        .executeAndFetchFirst(Stylist.class);
      return stylist;
    }
  }

  public static List<Stylist> search(String search) {
    String sql = "SELECT * FROM stylists WHERE username ~* :search OR firstname ~* :search OR lastname ~* :search OR specialty ~* :search;";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql)
        .addParameter("search", ".*" + search + ".*")
        .executeAndFetch(Stylist.class);
    }
  }

  public static List<Stylist> all() {
    String sql = "SELECT * FROM stylists;";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Stylist.class);
    }
  }

  @Override
    public boolean equals(Object other) {
      if (!(other instanceof Stylist)) {
        return false;
      } else {
        Stylist otherStylist = (Stylist) other;
        return this.getUserName().equals(otherStylist.getUserName()) &&
          this.getId() == otherStylist.getId();
      }
    }

}
