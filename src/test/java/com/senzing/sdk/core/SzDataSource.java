package com.senzing.sdk.core;

import java.util.Objects;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonObject;

/**
 * Provides a simple data source implementation.
 */
public class SzDataSource {
  /**
   * The data source code.
   */
  private String dataSourceCode;

  /**
   * The data source ID.
   */
  private Integer dataSourceId;

  /**
   * Default constructor.
   */
  public SzDataSource() {
    this.dataSourceCode = null;
    this.dataSourceId   = null;
  }

  /**
   * Constructs with the specified data source code and a <tt>null</tt>
   * data source ID.
   *
   * @param dataSourceCode The data source code for the data source.
   */
  public SzDataSource(String dataSourceCode) {
    this(dataSourceCode, null);
  }

  /**
   * Constructs with the specified data source code and data source ID.
   *
   * @param dataSourceCode The data source code for the data source.
   * @param dataSourceId The data source ID for the data source, or
   *                     <tt>null</tt> if the data source ID is not
   *                     specified.
   */
  public SzDataSource(String dataSourceCode, Integer dataSourceId) {
    this.dataSourceCode = dataSourceCode.toUpperCase().trim();
    this.dataSourceId   = dataSourceId;
  }

  /**
   * Gets the data source code for the data source.
   *
   * @return The data source code for the data source.
   */
  public String getDataSourceCode() {
    return this.dataSourceCode;
  }

  /**
   * Sets the data source code for the data source.
   *
   * @param code The data source code for the data source.
   */
  public void setDataSourceCode(String code) {
    this.dataSourceCode = code;
    if (this.dataSourceCode != null) {
      this.dataSourceCode = this.dataSourceCode.toUpperCase().trim();
    }
  }

  /**
   * Return the data source ID associated with the data source.
   *
   * @return The data source ID associated with the data source.
   */
  public Integer getDataSourceId() {
    return this.dataSourceId;
  }

  /**
   * Sets the data source ID associated with the data source.
   *
   * @param dataSourceId The data source ID associated with the data source.
   */
  public void setDataSourceId(Integer dataSourceId) {
    this.dataSourceId = dataSourceId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SzDataSource dataSource = (SzDataSource) o;
    return Objects.equals(getDataSourceCode(), dataSource.getDataSourceCode())
        && Objects.equals(this.getDataSourceId(), dataSource.getDataSourceId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getDataSourceCode(), this.getDataSourceId());
  }

  @Override
  public String toString() {
    JsonObjectBuilder job = Json.createObjectBuilder();
    job.add("dataSourceCode", this.getDataSourceCode());
    Integer sourceId = this.getDataSourceId();
    if (sourceId != null) {
      job.add("dataSourceId", sourceId);
    }
    JsonObject jsonObj = job.build();
    return jsonObj.toString();
  }
}
