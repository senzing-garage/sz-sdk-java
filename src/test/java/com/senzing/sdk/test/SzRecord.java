package com.senzing.sdk.test;

import java.util.Map;

import com.senzing.util.JsonUtilities;
import com.senzing.sdk.SzRecordKey;

import java.util.List;
import java.util.LinkedList;
import java.util.LinkedHashMap;
import javax.json.JsonObject;

import static com.senzing.util.JsonUtilities.jsonEscape;

public class SzRecord {
    public interface SzRecordData {
        default String getPluralName() {
            return null;
        } 
    }
    public record SzDataSourceCode(String dataSourceCode) implements SzRecordData {
        public static SzDataSourceCode of(String dataSourceCode) {
            return new SzDataSourceCode(dataSourceCode);
        }
        public String toString() {
            return "\"DATA_SOURCE\":" + jsonEscape(this.dataSourceCode());
        }
    }

    public record SzRecordId(String recordId) implements SzRecordData {
        public static SzRecordId of(String recordId) {
            return new SzRecordId(recordId);
        }
        public String toString() {
            return "\"RECORD_ID\":" + jsonEscape(this.recordId());
        }
    }

    public record SzRecordType(String recordType) implements SzRecordData {
        public static SzRecordType of(String recordType) {
            return new SzRecordType(recordType);
        }
        public String toString() {
            return "\"RECORD_TYPE\":" + jsonEscape(this.recordType());
        }
    }

    public interface SzName extends SzRecordData {
        String nameType();

        @Override default String getPluralName() {
            return "NAMES";
        }
    }

    public interface SzAddress extends SzRecordData {
        String addressType();
        
        @Override default String getPluralName() {
            return "ADDRESSES";
        }
    }

    public record SzFullName(String fullName, String nameType)
        implements SzName
    {   
        public static SzFullName of(String fullName) {
            return new SzFullName(fullName, null);
        }
        public static SzFullName of(String fullName, String nameType) {
            return new SzFullName(fullName, nameType);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("\"NAME_FULL\":" + jsonEscape(this.fullName()));
            if (this.nameType() != null) {
                sb.append(",\"NAME_TYPE\":" + jsonEscape(this.nameType()));
            }
            return sb.toString();
        }    
    }

    public record SzNameByParts(
        String firstName, String lastName, String nameType)
        implements SzName 
    {
        public static SzNameByParts of(String firstName, String lastName) {
            return new SzNameByParts(firstName, lastName, null);
        }
        public static SzNameByParts of(
            String firstName, String lastName, String nameType)
        {
            return new SzNameByParts(firstName, lastName, nameType);
        }
        public String toString() {
            StringBuilder sb = new StringBuilder();
            String prefix = "";
            if (this.firstName() != null) {
                sb.append("\"NAME_FIRST\":" + jsonEscape(this.firstName()));
                prefix = ",";
            }
            if (this.lastName() != null) {
                sb.append(prefix);
                sb.append("\"NAME_LAST\":" + jsonEscape(this.lastName()));
                prefix = ",";
            }
            if (this.nameType() != null) {
                sb.append(prefix);
                sb.append("\"NAME_TYPE\":" + jsonEscape(this.nameType()));
            }
            return sb.toString();
        }    
    }

    public record SzFullAddress(String fullAddress, String addressType)
        implements SzAddress
    {
        public static SzFullAddress of(String fullAddress) {
            return new SzFullAddress(fullAddress, null);
        }
        public static SzFullAddress of(String fullAddress, String addressType) {
            return new SzFullAddress(fullAddress, addressType);
        }
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("\"ADDR_FULL\":" + jsonEscape(this.fullAddress()));
            if (this.addressType() != null) {
                sb.append(",\"ADDR_TYPE\":" + jsonEscape(this.addressType()));
            }
            return sb.toString();
        }    
    }

    public record SzAddressByParts(
        String street, String city, String state, String postalCode, String addressType)
        implements SzAddress
    {
        public static SzAddressByParts of(String street, String city, String state, String postalCode) {
            return new SzAddressByParts(street, city, state, postalCode, null);
        }
        public static SzAddressByParts of(String street, String city, String state, String postalCode, String addressType) {
            return new SzAddressByParts(street, city, state, postalCode, addressType);
        }
        public String toString() {
            StringBuilder sb = new StringBuilder();
            String prefix = "";
            if (this.street() != null) {
                sb.append("\"ADDR_LINE1\":" + jsonEscape(this.street()));
                prefix = ",";
            }
            if (this.city() != null) {
                sb.append(prefix);
                sb.append("\"ADDR_CITY\":" + jsonEscape(this.city()));
                prefix = ",";
            }
            if (this.state() != null) {
                sb.append(prefix);
                sb.append("\"ADDR_STATE\":" + jsonEscape(this.state()));
            }
            if (this.postalCode() != null) {
                sb.append(prefix);
                sb.append("\"ADDR_POSTAL_CODE\":" + jsonEscape(this.postalCode()));
            }
            if (this.addressType() != null) {
                sb.append(prefix);
                sb.append("\"ADDR_TYPE\":" + jsonEscape(this.addressType()));
            }
            return sb.toString();
        }            

    }

    public record SzPhoneNumber(String phone, String phoneType) 
        implements SzRecordData
    {
        public String getPluralName() {
            return "PHONE_NUMBERS";
        }

        public static SzPhoneNumber of(String phone) {
            return new SzPhoneNumber(phone, null);
        }
        public static SzPhoneNumber of(String phone, String phoneType) {
            return new SzPhoneNumber(phone, phoneType);
        }
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("\"PHONE_MUMBER\":" + jsonEscape(this.phone()));
            if (this.phoneType() != null) {
                sb.append(",\"PHONE_TYPE\":" + jsonEscape(this.phoneType()));
            }
            return sb.toString();
        }    
    }

    public record SzEmailAddress(String email, String emailType) 
        implements SzRecordData
    {
        public String getPluralName() {
            return "EMAILS";
        }
        public static SzEmailAddress of(String email) {
            return new SzEmailAddress(email, null);
        }
        public static SzEmailAddress of(String email, String emailType) {
            return new SzEmailAddress(email, emailType);
        }
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("\"EMAIL_ADDRESS\":" + jsonEscape(this.email()));
            if (this.emailType() != null) {
                sb.append(",\"EMAIL_TYPE\":" + jsonEscape(this.emailType()));
            }
            return sb.toString();
        }    
    }

    public record SzDateOfBirth(String date) implements SzRecordData {
        public static SzDateOfBirth of(String date) {
            return new SzDateOfBirth(date);
        }
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("\"DATE_OF_BIRTH\":" + jsonEscape(this.date()));
            return sb.toString();
        }
    }
    
    public record SzSocialSecurity(String ssn) implements SzRecordData {
        public static SzSocialSecurity of(String ssn) {
            return new SzSocialSecurity(ssn);
        }
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("\"SSN_NUMBER\":" + jsonEscape(this.ssn()));
            return sb.toString();
        }    
    }

    public record SzDriversLicense(String licenseNumber, String state)
        implements SzRecordData
    {
        public static SzDriversLicense of(String licenseNumber) {
            return new SzDriversLicense(licenseNumber, null);
        }
        public static SzDriversLicense of(String licenseNumber, String state) {
            return new SzDriversLicense(licenseNumber, state);
        }
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("\"DRIVERS_LICENSE_NUMBER\":" + jsonEscape(this.licenseNumber()));
            if (this.state() != null) {
                sb.append(",\"DRIVERS_LICENSE_STATE\":" + jsonEscape(this.state()));
            }
            return sb.toString();
        }    
    }

    public record SzPassport(String passportNumber, String country)
        implements SzRecordData
    {
        public static SzPassport of(String passportNumber) {
            return new SzPassport(passportNumber, null);
        }
        public static SzPassport of(String passportNumber, String country) {
            return new SzPassport(passportNumber, country);
        }
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("\"PASSPORT_NUMBER\":" + jsonEscape(this.passportNumber()));
            if (this.country() != null) {
                sb.append(",\"PASSPORT_COUNTRY\":" + jsonEscape(this.country()));
            }
            return sb.toString();
        }    
    }

    private SzRecordKey recordKey = null;
    private Map<Object, List<SzRecordData>> dataMap;

    public SzRecord(SzRecordKey recordKey, SzRecord other) {
        this.recordKey = recordKey;
        this.dataMap = new LinkedHashMap<>();
        if (this.recordKey != null) {
            this.dataMap.put(SzDataSourceCode.class, 
                List.of(SzDataSourceCode.of(recordKey.dataSourceCode())));
            this.dataMap.put(SzRecordId.class,
                List.of(SzRecordId.of(recordKey.recordId())));
        }
        if (other != null) {
            other.dataMap.forEach((key, valueList) -> {
                if (key.equals(SzDataSourceCode.class)) return;
                if (key.equals(SzRecordId.class)) return;
                this.dataMap.put(key, valueList);
            });
        }
    }

    public SzRecord(SzRecordKey recordKey, SzRecordData... recordData) {
        this(recordData);
        this.recordKey = recordKey;
        this.dataMap.remove(SzDataSourceCode.class);
        this.dataMap.remove(SzRecordId.class);
        if (recordKey != null) {
            Map<Object, List<SzRecordData>> map = new LinkedHashMap<>();
            map.put(SzDataSourceCode.class, 
                List.of(SzDataSourceCode.of(recordKey.dataSourceCode())));
            map.put(SzRecordId.class,
                List.of(SzRecordId.of(recordKey.recordId())));
            map.putAll(this.dataMap);
            this.dataMap = map;
        }
    }

    public SzRecord(SzRecordData... recordData) {
        this.dataMap = new LinkedHashMap<>();

        String dataSourceCode   = null;
        String recordId         = null;

        if (recordData != null) {
            for (SzRecordData data : recordData) {
                if (data == null) continue;
                if (data instanceof SzDataSourceCode) {
                    dataSourceCode = ((SzDataSourceCode) data).dataSourceCode();
                }
                if (data instanceof SzRecordId) {
                    recordId = ((SzRecordId) data).recordId();
                }
                Class<? extends SzRecordData> classKey = data.getClass();
                String pluralName = data.getPluralName();
                Object key = (pluralName == null) ? classKey : pluralName;
                List<SzRecordData> list = this.dataMap.get(key);
                if (list == null) {
                    list = new LinkedList<>();
                    this.dataMap.put(key, list);
                }
                if (list.size() > 0 && pluralName == null) {
                    throw new IllegalArgumentException(
                        "Multiple values for " + classKey.getSimpleName() 
                        + " are not allowed.  specified=[ " + data 
                        + "], existing=[ " + list.get(0) + " ]");
                }
                list.add(data);
            }
        }
        if (dataSourceCode != null && recordId != null) {
            this.recordKey = SzRecordKey.of(dataSourceCode, recordId);
        }
    }

    public SzRecordKey getRecordKey() {
        return this.recordKey;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        String[] prefix = { "" };
        this.dataMap.forEach((key, valueList) -> {
            sb.append(prefix[0]);
            if (valueList.size() == 1) {
                sb.append(valueList.get(0));
            } else {
                String pluralName = valueList.get(0).getPluralName();
                sb.append(jsonEscape(pluralName)).append(":[");
                String subPrefix = "";
                for (SzRecordData data : valueList) {
                    sb.append(subPrefix);
                    sb.append("{").append(data).append("}");
                    subPrefix = ",";
                }
                sb.append("]");
            }
            prefix[0] = ",";
        });
        sb.append("}");

        return sb.toString();
    }

    public static void main(String[] args) {
        SzRecord record = new SzRecord(
            SzDataSourceCode.of("CUSTOMERS"), 
            SzRecordId.of("ABC-123"),
            SzFullName.of("Joe Schmoe"), 
            SzFullAddress.of("101 Main Street; Las Vegas, NV 89101", "WORK"),
            SzAddressByParts.of("200 Homestead Road", "Las Vegas", "NV", "89101", "HOME"),
            SzPhoneNumber.of("702-555-1212", "MOBILE"),
            SzPhoneNumber.of("702-555-1313", "WORK"),
            SzEmailAddress.of("joeschmoe@somewhere.com", "WORK"),
            SzEmailAddress.of("joeschmoe@nowhere.com", "PERSONAL"));
        
        String recordJson = record.toString();
        try {
            JsonObject jsonObject = JsonUtilities.parseJsonObject(recordJson);
            recordJson = JsonUtilities.toJsonText(jsonObject, true);
            System.out.println(recordJson);
        } catch (Exception e) {
            System.err.println(e);
            System.err.println();
            System.err.println(recordJson);
        }
    }
}
