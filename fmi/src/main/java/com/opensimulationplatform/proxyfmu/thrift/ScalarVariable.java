/**
 * Autogenerated by Thrift Compiler (0.13.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.opensimulationplatform.proxyfmu.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.13.0)", date = "2021-04-14")
public class ScalarVariable implements org.apache.thrift.TBase<ScalarVariable, ScalarVariable._Fields>, java.io.Serializable, Cloneable, Comparable<ScalarVariable> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("ScalarVariable");

  private static final org.apache.thrift.protocol.TField NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("name", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField VALUE_REFERENCE_FIELD_DESC = new org.apache.thrift.protocol.TField("value_reference", org.apache.thrift.protocol.TType.I64, (short)2);
  private static final org.apache.thrift.protocol.TField INITIAL_FIELD_DESC = new org.apache.thrift.protocol.TField("initial", org.apache.thrift.protocol.TType.STRING, (short)4);
  private static final org.apache.thrift.protocol.TField CAUSALITY_FIELD_DESC = new org.apache.thrift.protocol.TField("causality", org.apache.thrift.protocol.TType.STRING, (short)5);
  private static final org.apache.thrift.protocol.TField VARIABILITY_FIELD_DESC = new org.apache.thrift.protocol.TField("variability", org.apache.thrift.protocol.TType.STRING, (short)6);
  private static final org.apache.thrift.protocol.TField ATTRIBUTE_FIELD_DESC = new org.apache.thrift.protocol.TField("attribute", org.apache.thrift.protocol.TType.STRUCT, (short)7);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new ScalarVariableStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new ScalarVariableTupleSchemeFactory();

  private @org.apache.thrift.annotation.Nullable java.lang.String name; // required
  private long value_reference; // required
  private @org.apache.thrift.annotation.Nullable java.lang.String initial; // optional
  private @org.apache.thrift.annotation.Nullable java.lang.String causality; // optional
  private @org.apache.thrift.annotation.Nullable java.lang.String variability; // optional
  private @org.apache.thrift.annotation.Nullable ScalarVariableAttribute attribute; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    NAME((short)1, "name"),
    VALUE_REFERENCE((short)2, "value_reference"),
    INITIAL((short)4, "initial"),
    CAUSALITY((short)5, "causality"),
    VARIABILITY((short)6, "variability"),
    ATTRIBUTE((short)7, "attribute");

    private static final java.util.Map<java.lang.String, _Fields> byName = new java.util.HashMap<java.lang.String, _Fields>();

    static {
      for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    @org.apache.thrift.annotation.Nullable
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // NAME
          return NAME;
        case 2: // VALUE_REFERENCE
          return VALUE_REFERENCE;
        case 4: // INITIAL
          return INITIAL;
        case 5: // CAUSALITY
          return CAUSALITY;
        case 6: // VARIABILITY
          return VARIABILITY;
        case 7: // ATTRIBUTE
          return ATTRIBUTE;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new java.lang.IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    @org.apache.thrift.annotation.Nullable
    public static _Fields findByName(java.lang.String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final java.lang.String _fieldName;

    _Fields(short thriftId, java.lang.String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public java.lang.String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __VALUE_REFERENCE_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  private static final _Fields optionals[] = {_Fields.INITIAL,_Fields.CAUSALITY,_Fields.VARIABILITY};
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.NAME, new org.apache.thrift.meta_data.FieldMetaData("name", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.VALUE_REFERENCE, new org.apache.thrift.meta_data.FieldMetaData("value_reference", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64        , "ValueReference")));
    tmpMap.put(_Fields.INITIAL, new org.apache.thrift.meta_data.FieldMetaData("initial", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.CAUSALITY, new org.apache.thrift.meta_data.FieldMetaData("causality", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.VARIABILITY, new org.apache.thrift.meta_data.FieldMetaData("variability", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.ATTRIBUTE, new org.apache.thrift.meta_data.FieldMetaData("attribute", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, ScalarVariableAttribute.class)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(ScalarVariable.class, metaDataMap);
  }

  public ScalarVariable() {
  }

  public ScalarVariable(
    java.lang.String name,
    long value_reference,
    ScalarVariableAttribute attribute)
  {
    this();
    this.name = name;
    this.value_reference = value_reference;
    setValueReferenceIsSet(true);
    this.attribute = attribute;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public ScalarVariable(ScalarVariable other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetName()) {
      this.name = other.name;
    }
    this.value_reference = other.value_reference;
    if (other.isSetInitial()) {
      this.initial = other.initial;
    }
    if (other.isSetCausality()) {
      this.causality = other.causality;
    }
    if (other.isSetVariability()) {
      this.variability = other.variability;
    }
    if (other.isSetAttribute()) {
      this.attribute = new ScalarVariableAttribute(other.attribute);
    }
  }

  public ScalarVariable deepCopy() {
    return new ScalarVariable(this);
  }

  @Override
  public void clear() {
    this.name = null;
    setValueReferenceIsSet(false);
    this.value_reference = 0;
    this.initial = null;
    this.causality = null;
    this.variability = null;
    this.attribute = null;
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.String getName() {
    return this.name;
  }

  public ScalarVariable setName(@org.apache.thrift.annotation.Nullable java.lang.String name) {
    this.name = name;
    return this;
  }

  public void unsetName() {
    this.name = null;
  }

  /** Returns true if field name is set (has been assigned a value) and false otherwise */
  public boolean isSetName() {
    return this.name != null;
  }

  public void setNameIsSet(boolean value) {
    if (!value) {
      this.name = null;
    }
  }

  public long getValueReference() {
    return this.value_reference;
  }

  public ScalarVariable setValueReference(long value_reference) {
    this.value_reference = value_reference;
    setValueReferenceIsSet(true);
    return this;
  }

  public void unsetValueReference() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __VALUE_REFERENCE_ISSET_ID);
  }

  /** Returns true if field value_reference is set (has been assigned a value) and false otherwise */
  public boolean isSetValueReference() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __VALUE_REFERENCE_ISSET_ID);
  }

  public void setValueReferenceIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __VALUE_REFERENCE_ISSET_ID, value);
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.String getInitial() {
    return this.initial;
  }

  public ScalarVariable setInitial(@org.apache.thrift.annotation.Nullable java.lang.String initial) {
    this.initial = initial;
    return this;
  }

  public void unsetInitial() {
    this.initial = null;
  }

  /** Returns true if field initial is set (has been assigned a value) and false otherwise */
  public boolean isSetInitial() {
    return this.initial != null;
  }

  public void setInitialIsSet(boolean value) {
    if (!value) {
      this.initial = null;
    }
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.String getCausality() {
    return this.causality;
  }

  public ScalarVariable setCausality(@org.apache.thrift.annotation.Nullable java.lang.String causality) {
    this.causality = causality;
    return this;
  }

  public void unsetCausality() {
    this.causality = null;
  }

  /** Returns true if field causality is set (has been assigned a value) and false otherwise */
  public boolean isSetCausality() {
    return this.causality != null;
  }

  public void setCausalityIsSet(boolean value) {
    if (!value) {
      this.causality = null;
    }
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.String getVariability() {
    return this.variability;
  }

  public ScalarVariable setVariability(@org.apache.thrift.annotation.Nullable java.lang.String variability) {
    this.variability = variability;
    return this;
  }

  public void unsetVariability() {
    this.variability = null;
  }

  /** Returns true if field variability is set (has been assigned a value) and false otherwise */
  public boolean isSetVariability() {
    return this.variability != null;
  }

  public void setVariabilityIsSet(boolean value) {
    if (!value) {
      this.variability = null;
    }
  }

  @org.apache.thrift.annotation.Nullable
  public ScalarVariableAttribute getAttribute() {
    return this.attribute;
  }

  public ScalarVariable setAttribute(@org.apache.thrift.annotation.Nullable ScalarVariableAttribute attribute) {
    this.attribute = attribute;
    return this;
  }

  public void unsetAttribute() {
    this.attribute = null;
  }

  /** Returns true if field attribute is set (has been assigned a value) and false otherwise */
  public boolean isSetAttribute() {
    return this.attribute != null;
  }

  public void setAttributeIsSet(boolean value) {
    if (!value) {
      this.attribute = null;
    }
  }

  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case NAME:
      if (value == null) {
        unsetName();
      } else {
        setName((java.lang.String)value);
      }
      break;

    case VALUE_REFERENCE:
      if (value == null) {
        unsetValueReference();
      } else {
        setValueReference((java.lang.Long)value);
      }
      break;

    case INITIAL:
      if (value == null) {
        unsetInitial();
      } else {
        setInitial((java.lang.String)value);
      }
      break;

    case CAUSALITY:
      if (value == null) {
        unsetCausality();
      } else {
        setCausality((java.lang.String)value);
      }
      break;

    case VARIABILITY:
      if (value == null) {
        unsetVariability();
      } else {
        setVariability((java.lang.String)value);
      }
      break;

    case ATTRIBUTE:
      if (value == null) {
        unsetAttribute();
      } else {
        setAttribute((ScalarVariableAttribute)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case NAME:
      return getName();

    case VALUE_REFERENCE:
      return getValueReference();

    case INITIAL:
      return getInitial();

    case CAUSALITY:
      return getCausality();

    case VARIABILITY:
      return getVariability();

    case ATTRIBUTE:
      return getAttribute();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case NAME:
      return isSetName();
    case VALUE_REFERENCE:
      return isSetValueReference();
    case INITIAL:
      return isSetInitial();
    case CAUSALITY:
      return isSetCausality();
    case VARIABILITY:
      return isSetVariability();
    case ATTRIBUTE:
      return isSetAttribute();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof ScalarVariable)
      return this.equals((ScalarVariable)that);
    return false;
  }

  public boolean equals(ScalarVariable that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_name = true && this.isSetName();
    boolean that_present_name = true && that.isSetName();
    if (this_present_name || that_present_name) {
      if (!(this_present_name && that_present_name))
        return false;
      if (!this.name.equals(that.name))
        return false;
    }

    boolean this_present_value_reference = true;
    boolean that_present_value_reference = true;
    if (this_present_value_reference || that_present_value_reference) {
      if (!(this_present_value_reference && that_present_value_reference))
        return false;
      if (this.value_reference != that.value_reference)
        return false;
    }

    boolean this_present_initial = true && this.isSetInitial();
    boolean that_present_initial = true && that.isSetInitial();
    if (this_present_initial || that_present_initial) {
      if (!(this_present_initial && that_present_initial))
        return false;
      if (!this.initial.equals(that.initial))
        return false;
    }

    boolean this_present_causality = true && this.isSetCausality();
    boolean that_present_causality = true && that.isSetCausality();
    if (this_present_causality || that_present_causality) {
      if (!(this_present_causality && that_present_causality))
        return false;
      if (!this.causality.equals(that.causality))
        return false;
    }

    boolean this_present_variability = true && this.isSetVariability();
    boolean that_present_variability = true && that.isSetVariability();
    if (this_present_variability || that_present_variability) {
      if (!(this_present_variability && that_present_variability))
        return false;
      if (!this.variability.equals(that.variability))
        return false;
    }

    boolean this_present_attribute = true && this.isSetAttribute();
    boolean that_present_attribute = true && that.isSetAttribute();
    if (this_present_attribute || that_present_attribute) {
      if (!(this_present_attribute && that_present_attribute))
        return false;
      if (!this.attribute.equals(that.attribute))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetName()) ? 131071 : 524287);
    if (isSetName())
      hashCode = hashCode * 8191 + name.hashCode();

    hashCode = hashCode * 8191 + org.apache.thrift.TBaseHelper.hashCode(value_reference);

    hashCode = hashCode * 8191 + ((isSetInitial()) ? 131071 : 524287);
    if (isSetInitial())
      hashCode = hashCode * 8191 + initial.hashCode();

    hashCode = hashCode * 8191 + ((isSetCausality()) ? 131071 : 524287);
    if (isSetCausality())
      hashCode = hashCode * 8191 + causality.hashCode();

    hashCode = hashCode * 8191 + ((isSetVariability()) ? 131071 : 524287);
    if (isSetVariability())
      hashCode = hashCode * 8191 + variability.hashCode();

    hashCode = hashCode * 8191 + ((isSetAttribute()) ? 131071 : 524287);
    if (isSetAttribute())
      hashCode = hashCode * 8191 + attribute.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(ScalarVariable other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetName()).compareTo(other.isSetName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.name, other.name);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetValueReference()).compareTo(other.isSetValueReference());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetValueReference()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.value_reference, other.value_reference);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetInitial()).compareTo(other.isSetInitial());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetInitial()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.initial, other.initial);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetCausality()).compareTo(other.isSetCausality());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCausality()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.causality, other.causality);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetVariability()).compareTo(other.isSetVariability());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetVariability()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.variability, other.variability);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetAttribute()).compareTo(other.isSetAttribute());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetAttribute()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.attribute, other.attribute);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  @org.apache.thrift.annotation.Nullable
  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    scheme(iprot).read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    scheme(oprot).write(oprot, this);
  }

  @Override
  public java.lang.String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder("ScalarVariable(");
    boolean first = true;

    sb.append("name:");
    if (this.name == null) {
      sb.append("null");
    } else {
      sb.append(this.name);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("value_reference:");
    sb.append(this.value_reference);
    first = false;
    if (isSetInitial()) {
      if (!first) sb.append(", ");
      sb.append("initial:");
      if (this.initial == null) {
        sb.append("null");
      } else {
        sb.append(this.initial);
      }
      first = false;
    }
    if (isSetCausality()) {
      if (!first) sb.append(", ");
      sb.append("causality:");
      if (this.causality == null) {
        sb.append("null");
      } else {
        sb.append(this.causality);
      }
      first = false;
    }
    if (isSetVariability()) {
      if (!first) sb.append(", ");
      sb.append("variability:");
      if (this.variability == null) {
        sb.append("null");
      } else {
        sb.append(this.variability);
      }
      first = false;
    }
    if (!first) sb.append(", ");
    sb.append("attribute:");
    if (this.attribute == null) {
      sb.append("null");
    } else {
      sb.append(this.attribute);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class ScalarVariableStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public ScalarVariableStandardScheme getScheme() {
      return new ScalarVariableStandardScheme();
    }
  }

  private static class ScalarVariableStandardScheme extends org.apache.thrift.scheme.StandardScheme<ScalarVariable> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, ScalarVariable struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.name = iprot.readString();
              struct.setNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // VALUE_REFERENCE
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.value_reference = iprot.readI64();
              struct.setValueReferenceIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // INITIAL
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.initial = iprot.readString();
              struct.setInitialIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // CAUSALITY
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.causality = iprot.readString();
              struct.setCausalityIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 6: // VARIABILITY
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.variability = iprot.readString();
              struct.setVariabilityIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 7: // ATTRIBUTE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.attribute = new ScalarVariableAttribute();
              struct.attribute.read(iprot);
              struct.setAttributeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, ScalarVariable struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.name != null) {
        oprot.writeFieldBegin(NAME_FIELD_DESC);
        oprot.writeString(struct.name);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(VALUE_REFERENCE_FIELD_DESC);
      oprot.writeI64(struct.value_reference);
      oprot.writeFieldEnd();
      if (struct.initial != null) {
        if (struct.isSetInitial()) {
          oprot.writeFieldBegin(INITIAL_FIELD_DESC);
          oprot.writeString(struct.initial);
          oprot.writeFieldEnd();
        }
      }
      if (struct.causality != null) {
        if (struct.isSetCausality()) {
          oprot.writeFieldBegin(CAUSALITY_FIELD_DESC);
          oprot.writeString(struct.causality);
          oprot.writeFieldEnd();
        }
      }
      if (struct.variability != null) {
        if (struct.isSetVariability()) {
          oprot.writeFieldBegin(VARIABILITY_FIELD_DESC);
          oprot.writeString(struct.variability);
          oprot.writeFieldEnd();
        }
      }
      if (struct.attribute != null) {
        oprot.writeFieldBegin(ATTRIBUTE_FIELD_DESC);
        struct.attribute.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class ScalarVariableTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public ScalarVariableTupleScheme getScheme() {
      return new ScalarVariableTupleScheme();
    }
  }

  private static class ScalarVariableTupleScheme extends org.apache.thrift.scheme.TupleScheme<ScalarVariable> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, ScalarVariable struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetName()) {
        optionals.set(0);
      }
      if (struct.isSetValueReference()) {
        optionals.set(1);
      }
      if (struct.isSetInitial()) {
        optionals.set(2);
      }
      if (struct.isSetCausality()) {
        optionals.set(3);
      }
      if (struct.isSetVariability()) {
        optionals.set(4);
      }
      if (struct.isSetAttribute()) {
        optionals.set(5);
      }
      oprot.writeBitSet(optionals, 6);
      if (struct.isSetName()) {
        oprot.writeString(struct.name);
      }
      if (struct.isSetValueReference()) {
        oprot.writeI64(struct.value_reference);
      }
      if (struct.isSetInitial()) {
        oprot.writeString(struct.initial);
      }
      if (struct.isSetCausality()) {
        oprot.writeString(struct.causality);
      }
      if (struct.isSetVariability()) {
        oprot.writeString(struct.variability);
      }
      if (struct.isSetAttribute()) {
        struct.attribute.write(oprot);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, ScalarVariable struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(6);
      if (incoming.get(0)) {
        struct.name = iprot.readString();
        struct.setNameIsSet(true);
      }
      if (incoming.get(1)) {
        struct.value_reference = iprot.readI64();
        struct.setValueReferenceIsSet(true);
      }
      if (incoming.get(2)) {
        struct.initial = iprot.readString();
        struct.setInitialIsSet(true);
      }
      if (incoming.get(3)) {
        struct.causality = iprot.readString();
        struct.setCausalityIsSet(true);
      }
      if (incoming.get(4)) {
        struct.variability = iprot.readString();
        struct.setVariabilityIsSet(true);
      }
      if (incoming.get(5)) {
        struct.attribute = new ScalarVariableAttribute();
        struct.attribute.read(iprot);
        struct.setAttributeIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

