package com.exemple.hair.model;

import java.util.List;

import com.exemple.hair.database.DB;
import org.sql2o.*;

public class Client {
  private int id;
  private String userName;
  private String firstName;
  private String lastName;
  private Integer stylistId;
  private Integer s;

  public Client(String userName, String firstName, String lastName) {
    this.userName = userName;
    this.firstName = firstName;
    this.lastName = lastName;
    this.stylistId = null;
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

  public String getFullName() {
    return this.firstName + " " + this.lastName;
  }

  public Integer getStylistId() {
    return this.stylistId;
  }

  public void setStylistId(Integer stylistId) {
    this.stylistId = stylistId;
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO clients (username, firstname, lastname, stylistId) VALUES (:username, :firstname, :lastname, :stylistId);";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("username", this.userName)
        .addParameter("firstname", this.firstName)
        .addParameter("lastname", this.lastName)
        .addParameter("stylistId", this.stylistId)
        .executeUpdate()
        .getKey();
    }
  }

  public void update() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE clients SET username = :username, firstname = :firstname, lastname = :lastname, stylistId = :stylistId WHERE id = :id;";
      con.createQuery(sql)
        .addParameter("username", this.userName)
        .addParameter("firstname", this.firstName)
        .addParameter("lastname", this.lastName)
        .addParameter("stylistId", this.stylistId)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM clients WHERE id = :id;";
      con.createQuery(sql)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  public static Client find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM clients WHERE id = :id;";
      Client client = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Client.class);
      return client;
    }
  }

  public static Client findByUserName(String userName) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM clients WHERE username = :username;";
      Client client = con.createQuery(sql)
        .addParameter("username", userName)
        .executeAndFetchFirst(Client.class);
      return client;
    }
  }

  public static List<Client> withStylistId(int stylistId) {
    String sql = "SELECT * FROM clients WHERE stylistId = :stylistId;";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql)
        .addParameter("stylistId", stylistId)
        .executeAndFetch(Client.class);
    }
  }

  public static List<Client> search(String search) {
    String sql = "SELECT * FROM clients WHERE userName ~* :search OR firstname ~* :search OR lastname ~* :search;";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql)
        .addParameter("search", ".*" + search + ".*")
        .executeAndFetch(Client.class);
    }
  }

  public static List<Client> all() {
    String sql = "SELECT * FROM clients;";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Client.class);
    }
  }

  @Override
    public boolean equals(Object other) {
      if (!(other instanceof Client)) {
        return false;
      } else {
        Client otherClient = (Client) other;
        return this.getUserName().equals(otherClient.getUserName()) &&
          this.getStylistId() == otherClient.getStylistId() &&
          this.getId() == otherClient.getId();
      }
    }

}
