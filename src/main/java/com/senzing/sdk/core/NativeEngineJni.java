/**********************************************************************************
 © Copyright Senzing, Inc. 2017-2024
 The source code for this program is not published or otherwise divested
 of its trade secrets, irrespective of what has been deposited with the U.S.
 Copyright Office.
**********************************************************************************/

package com.senzing.sdk.core;

/**
 * Implements the {@link NativeEngine} interface to call the native implementations
 * of each function.
 */
class NativeEngineJni implements NativeEngine {
  static {
    System.loadLibrary("G2");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public native int init(String moduleName, String iniParams, boolean verboseLogging);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int initWithConfigID(String moduleName, String iniParams, long initConfigID, boolean verboseLogging);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int reinit(long initConfigID);


  /**
   * {@inheritDoc}
   */
  @Override
  public native int destroy();

  /**
   * {@inheritDoc}
   */
  @Override
  public native int primeEngine();
  
  /**
   * {@inheritDoc}
   */
  @Override
  public native String stats();

  /**
   * {@inheritDoc}
   */
  @Override
  public native String getLastException();

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getLastExceptionCode();

  /**
   * {@inheritDoc}
   */
  @Override
  public native void clearLastException();

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getActiveConfigID(Result<Long> configID);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int addRecord(String dataSourceCode, String recordID, String jsonData);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int addRecordWithInfo(String        dataSourceCode,
                                      String        recordID,
                                      String        jsonData,
                                      long          flags,
                                      StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int deleteRecord(String dataSourceCode,
                                 String recordID);
  
  /**
   * {@inheritDoc}
   */
  @Override
  public native int deleteRecordWithInfo(String       dataSourceCode,
                                         String       recordID,
                                         long         flags,
                                         StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int reevaluateRecord(String dataSourceCode,
                                     String recordID,
                                     long   flags);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int reevaluateEntity(long entityID, long flags);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int reevaluateRecordWithInfo(String       dataSourceCode,
                                             String       recordID,
                                             long         flags,
                                             StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int reevaluateEntityWithInfo(long         entityID,
                                             long         flags,
                                             StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int searchByAttributes(String jsonData, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int searchByAttributes(String       jsonData,
                                       long         flags,
                                       StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int searchByAttributes(String       jsonData,
                                       String       searchProfile,
                                       long         flags,
                                       StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getEntityByEntityID(long entityID, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getEntityByEntityID(long          entityID,
                                        long          flags,
                                        StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getEntityByRecordID(String        dataSourceCode,
                                        String        recordID,
                                        StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getEntityByRecordID(String        dataSourceCode,
                                        String        recordID,
                                        long          flags,
                                        StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int findInterestingEntitiesByEntityID(long          entityID,
                                                      long          flags,
                                                      StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int findInterestingEntitiesByRecordID(String        dataSourceCode,
                                                      String        recordID,
                                                      long          flags,
                                                      StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int findPathByEntityID(long         entityID1,
                                       long         entityID2,
                                       int          maxDegree,
                                       StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int findPathByEntityID(long         entityID1,
                                       long         entityID2,
                                       int          maxDegree,
                                       long         flags,
                                       StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int findPathByRecordID(String       dataSourceCode1,
                                       String       recordID1,
                                       String       dataSourceCode2,
                                       String       recordID2,
                                       int          maxDegree,
                                       StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int findPathByRecordID(String       dataSourceCode1,
                                       String       recordID1,
                                       String       dataSourceCode2,
                                       String       recordID2,
                                       int          maxDegree,
                                       long         flags,
                                       StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int findPathByEntityIDWithAvoids(long         entityID1,
                                                 long         entityID2,
                                                 int          maxDegree,
                                                 String       avoidedEntities,
                                                 StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int findPathByEntityIDWithAvoids(long         entityID1,
                                                 long         entityID2,
                                                 int          maxDegree,
                                                 String       avoidedEntities,
                                                 long         flags,
                                                 StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int findPathByRecordIDWithAvoids(String         dataSourceCode1,
                                                 String         recordID1,
                                                 String         dataSourceCode2,
                                                 String         recordID2,
                                                 int            maxDegree,
                                                 String         avoidedEntities,
                                                 StringBuffer   response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int findPathByRecordIDWithAvoids(String         dataSourceCode1,
                                                 String         recordID1,
                                                 String         dataSourceCode2,
                                                 String         recordID2,
                                                 int            maxDegree,
                                                 String         avoidedEntities,
                                                 long           flags,
                                                 StringBuffer   response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int findPathByEntityIDIncludingSource(
      long          entityID1,
      long          entityID2,
      int           maxDegree,
      String        avoidedEntities,
      String        requiredDsrcs,
      StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int findPathByEntityIDIncludingSource(
      long          entityID1,
      long          entityID2,
      int           maxDegree,
      String        avoidedEntities,
      String        requiredDsrcs,
      long          flags,
      StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int findPathByRecordIDIncludingSource(
      String        dataSourceCode1,
      String        recordID1,
      String        dataSourceCode2,
      String        recordID2,
      int           maxDegree,
      String        avoidedEntities,
      String        requiredDsrcs,
      StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int findPathByRecordIDIncludingSource(
      String        dataSourceCode1,
      String        recordID1,
      String        dataSourceCode2,
      String        recordID2,
      int           maxDegree,
      String        avoidedEntities,
      String        requiredDsrcs,
      long          flags,
      StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int findNetworkByEntityID(String        entityList,
                                          int           maxDegree,
                                          int           buildOutDegree,
                                          int           maxEntities,
                                          StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int findNetworkByEntityID(String        entityList,
                                          int           maxDegree,
                                          int           buildOutDegree,
                                          int           maxEntities,
                                          long          flags,
                                          StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int findNetworkByRecordID(String        recordList,
                                          int           maxDegree,
                                          int           buildOutDegree,
                                          int           maxEntities,
                                          StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int findNetworkByRecordID(String        recordList,
                                          int           maxDegree,
                                          int           buildOutDegree,
                                          int           maxEntities,
                                          long          flags,
                                          StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int whyRecordInEntity(String        dataSourceCode,
                                        String        recordID,
                                        StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int whyRecordInEntity(String        dataSourceCode,
                                        String        recordID,
                                        long          flags,
                                        StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int whyRecords(String       dataSourceCode1,
                               String       recordID1,
                               String       dataSourceCode2,
                               String       recordID2,
                               StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int whyRecords(String       dataSourceCode1,
                               String       recordID1,
                               String       dataSourceCode2,
                               String       recordID2,
                               long         flags,
                               StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int whyEntities(long          entityID1,
                                long          entityID2,
                                StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int whyEntities(long          entityID1,
                                long          entityID2,
                                long          flags,
                                StringBuffer  response);
  
  /**
   * {@inheritDoc}
   */
  @Override
  public native int howEntityByEntityID(long          entityID,
                                        StringBuffer  response);
  
  /**
   * {@inheritDoc}
   */
  @Override
  public native int howEntityByEntityID(long          entityID,
                                        long          flags,
                                        StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getVirtualEntityByRecordID(String        recordList,
                                               StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getVirtualEntityByRecordID(String        recordList,
                                               long          flags,
                                               StringBuffer  response);
  
  /**
   * {@inheritDoc}
   */
  @Override
  public native int getRecord(String        dataSourceCode,
                              String        recordID,
                              StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getRecord(String        dataSourceCode,
                              String        recordID,
                              long          flags,
                              StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int exportJSONEntityReport(long         flags,
                                           Result<Long> exportHandle);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int exportCSVEntityReport(String        csvColumnList,
                                          long          flags,
                                          Result<Long>  exportHandle);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int fetchNext(long exportHandle, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int closeExport(long exportHandle);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int processRedoRecord(String redoRecord);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int processRedoRecordWithInfo(String redoRecord,
                                              StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getRedoRecord(StringBuffer record);

  /**
   * {@inheritDoc}
   */
  @Override
  public native long countRedoRecords();

}

