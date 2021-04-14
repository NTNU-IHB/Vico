/**
 * Autogenerated by Thrift Compiler (0.13.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.opensimulationplatform.proxyfmu.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.13.0)", date = "2021-04-14")
public class IntegerAttribute implements org.apache.thrift.TBase<IntegerAttribute, IntegerAttribute._Fields>, java.io.Serializable, Cloneable, Comparable<IntegerAttribute> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("IntegerAttribute");

  private static final org.apache.thrift.protocol.TField START_FIELD_DESC = new org.apache.thrift.protocol.TField("start", org.apache.thrift.protocol.TType.I32, (short)1);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new IntegerAttributeStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new IntegerAttributeTupleSchemeFactory();

  private int start; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    START((short)1, "start");

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
        case 1: // START
          return START;
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
  private static final int __START_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  private static final _Fields optionals[] = {_Fields.START};
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.START, new org.apache.thrift.meta_data.FieldMetaData("start", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(IntegerAttribute.class, metaDataMap);
  }

  public IntegerAttribute() {
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public IntegerAttribute(IntegerAttribute other) {
    __isset_bitfield = other.__isset_bitfield;
    this.start = other.start;
  }

  public IntegerAttribute deepCopy() {
    return new IntegerAttribute(this);
  }

  @Override
  public void clear() {
    setStartIsSet(false);
    this.start = 0;
  }

  public int getStart() {
    return this.start;
  }

  public IntegerAttribute setStart(int start) {
    this.start = start;
    setStartIsSet(true);
    return this;
  }

  public void unsetStart() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __START_ISSET_ID);
  }

  /** Returns true if field start is set (has been assigned a value) and false otherwise */
  public boolean isSetStart() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __START_ISSET_ID);
  }

  public void setStartIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __START_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case START:
      if (value == null) {
        unsetStart();
      } else {
        setStart((java.lang.Integer)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case START:
      return getStart();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case START:
      return isSetStart();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof IntegerAttribute)
      return this.equals((IntegerAttribute)that);
    return false;
  }

  public boolean equals(IntegerAttribute that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_start = true && this.isSetStart();
    boolean that_present_start = true && that.isSetStart();
    if (this_present_start || that_present_start) {
      if (!(this_present_start && that_present_start))
        return false;
      if (this.start != that.start)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetStart()) ? 131071 : 524287);
    if (isSetStart())
      hashCode = hashCode * 8191 + start;

    return hashCode;
  }

  @Override
  public int compareTo(IntegerAttribute other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetStart()).compareTo(other.isSetStart());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetStart()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.start, other.start);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("IntegerAttribute(");
    boolean first = true;

    if (isSetStart()) {
      sb.append("start:");
      sb.append(this.start);
      first = false;
    }
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

  private static class IntegerAttributeStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public IntegerAttributeStandardScheme getScheme() {
      return new IntegerAttributeStandardScheme();
    }
  }

  private static class IntegerAttributeStandardScheme extends org.apache.thrift.scheme.StandardScheme<IntegerAttribute> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, IntegerAttribute struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // START
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.start = iprot.readI32();
              struct.setStartIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, IntegerAttribute struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.isSetStart()) {
        oprot.writeFieldBegin(START_FIELD_DESC);
        oprot.writeI32(struct.start);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class IntegerAttributeTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public IntegerAttributeTupleScheme getScheme() {
      return new IntegerAttributeTupleScheme();
    }
  }

  private static class IntegerAttributeTupleScheme extends org.apache.thrift.scheme.TupleScheme<IntegerAttribute> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, IntegerAttribute struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetStart()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.isSetStart()) {
        oprot.writeI32(struct.start);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, IntegerAttribute struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        struct.start = iprot.readI32();
        struct.setStartIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}
