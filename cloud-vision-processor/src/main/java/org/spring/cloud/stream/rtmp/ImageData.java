/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package org.spring.cloud.stream.rtmp;

import org.apache.avro.specific.SpecificData;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class ImageData extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 2644400074790166237L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"ImageData\",\"namespace\":\"org.spring.cloud.stream.rtmp\",\"fields\":[{\"name\":\"extension\",\"type\":[\"string\",\"null\"]},{\"name\":\"timestamp\",\"type\":[\"long\",\"null\"]},{\"name\":\"data\",\"type\":\"bytes\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<ImageData> ENCODER =
      new BinaryMessageEncoder<ImageData>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<ImageData> DECODER =
      new BinaryMessageDecoder<ImageData>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   */
  public static BinaryMessageDecoder<ImageData> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   */
  public static BinaryMessageDecoder<ImageData> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<ImageData>(MODEL$, SCHEMA$, resolver);
  }

  /** Serializes this ImageData to a ByteBuffer. */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /** Deserializes a ImageData from a ByteBuffer. */
  public static ImageData fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  @Deprecated public java.lang.CharSequence extension;
  @Deprecated public java.lang.Long timestamp;
  @Deprecated public java.nio.ByteBuffer data;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public ImageData() {}

  /**
   * All-args constructor.
   * @param extension The new value for extension
   * @param timestamp The new value for timestamp
   * @param data The new value for data
   */
  public ImageData(java.lang.CharSequence extension, java.lang.Long timestamp, java.nio.ByteBuffer data) {
    this.extension = extension;
    this.timestamp = timestamp;
    this.data = data;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return extension;
    case 1: return timestamp;
    case 2: return data;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: extension = (java.lang.CharSequence)value$; break;
    case 1: timestamp = (java.lang.Long)value$; break;
    case 2: data = (java.nio.ByteBuffer)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'extension' field.
   * @return The value of the 'extension' field.
   */
  public java.lang.CharSequence getExtension() {
    return extension;
  }

  /**
   * Sets the value of the 'extension' field.
   * @param value the value to set.
   */
  public void setExtension(java.lang.CharSequence value) {
    this.extension = value;
  }

  /**
   * Gets the value of the 'timestamp' field.
   * @return The value of the 'timestamp' field.
   */
  public java.lang.Long getTimestamp() {
    return timestamp;
  }

  /**
   * Sets the value of the 'timestamp' field.
   * @param value the value to set.
   */
  public void setTimestamp(java.lang.Long value) {
    this.timestamp = value;
  }

  /**
   * Gets the value of the 'data' field.
   * @return The value of the 'data' field.
   */
  public java.nio.ByteBuffer getData() {
    return data;
  }

  /**
   * Sets the value of the 'data' field.
   * @param value the value to set.
   */
  public void setData(java.nio.ByteBuffer value) {
    this.data = value;
  }

  /**
   * Creates a new ImageData RecordBuilder.
   * @return A new ImageData RecordBuilder
   */
  public static org.spring.cloud.stream.rtmp.ImageData.Builder newBuilder() {
    return new org.spring.cloud.stream.rtmp.ImageData.Builder();
  }

  /**
   * Creates a new ImageData RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new ImageData RecordBuilder
   */
  public static org.spring.cloud.stream.rtmp.ImageData.Builder newBuilder(org.spring.cloud.stream.rtmp.ImageData.Builder other) {
    return new org.spring.cloud.stream.rtmp.ImageData.Builder(other);
  }

  /**
   * Creates a new ImageData RecordBuilder by copying an existing ImageData instance.
   * @param other The existing instance to copy.
   * @return A new ImageData RecordBuilder
   */
  public static org.spring.cloud.stream.rtmp.ImageData.Builder newBuilder(org.spring.cloud.stream.rtmp.ImageData other) {
    return new org.spring.cloud.stream.rtmp.ImageData.Builder(other);
  }

  /**
   * RecordBuilder for ImageData instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<ImageData>
    implements org.apache.avro.data.RecordBuilder<ImageData> {

    private java.lang.CharSequence extension;
    private java.lang.Long timestamp;
    private java.nio.ByteBuffer data;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(org.spring.cloud.stream.rtmp.ImageData.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.extension)) {
        this.extension = data().deepCopy(fields()[0].schema(), other.extension);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.timestamp)) {
        this.timestamp = data().deepCopy(fields()[1].schema(), other.timestamp);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.data)) {
        this.data = data().deepCopy(fields()[2].schema(), other.data);
        fieldSetFlags()[2] = true;
      }
    }

    /**
     * Creates a Builder by copying an existing ImageData instance
     * @param other The existing instance to copy.
     */
    private Builder(org.spring.cloud.stream.rtmp.ImageData other) {
            super(SCHEMA$);
      if (isValidValue(fields()[0], other.extension)) {
        this.extension = data().deepCopy(fields()[0].schema(), other.extension);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.timestamp)) {
        this.timestamp = data().deepCopy(fields()[1].schema(), other.timestamp);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.data)) {
        this.data = data().deepCopy(fields()[2].schema(), other.data);
        fieldSetFlags()[2] = true;
      }
    }

    /**
      * Gets the value of the 'extension' field.
      * @return The value.
      */
    public java.lang.CharSequence getExtension() {
      return extension;
    }

    /**
      * Sets the value of the 'extension' field.
      * @param value The value of 'extension'.
      * @return This builder.
      */
    public org.spring.cloud.stream.rtmp.ImageData.Builder setExtension(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.extension = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'extension' field has been set.
      * @return True if the 'extension' field has been set, false otherwise.
      */
    public boolean hasExtension() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'extension' field.
      * @return This builder.
      */
    public org.spring.cloud.stream.rtmp.ImageData.Builder clearExtension() {
      extension = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'timestamp' field.
      * @return The value.
      */
    public java.lang.Long getTimestamp() {
      return timestamp;
    }

    /**
      * Sets the value of the 'timestamp' field.
      * @param value The value of 'timestamp'.
      * @return This builder.
      */
    public org.spring.cloud.stream.rtmp.ImageData.Builder setTimestamp(java.lang.Long value) {
      validate(fields()[1], value);
      this.timestamp = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'timestamp' field has been set.
      * @return True if the 'timestamp' field has been set, false otherwise.
      */
    public boolean hasTimestamp() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'timestamp' field.
      * @return This builder.
      */
    public org.spring.cloud.stream.rtmp.ImageData.Builder clearTimestamp() {
      timestamp = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'data' field.
      * @return The value.
      */
    public java.nio.ByteBuffer getData() {
      return data;
    }

    /**
      * Sets the value of the 'data' field.
      * @param value The value of 'data'.
      * @return This builder.
      */
    public org.spring.cloud.stream.rtmp.ImageData.Builder setData(java.nio.ByteBuffer value) {
      validate(fields()[2], value);
      this.data = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'data' field has been set.
      * @return True if the 'data' field has been set, false otherwise.
      */
    public boolean hasData() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'data' field.
      * @return This builder.
      */
    public org.spring.cloud.stream.rtmp.ImageData.Builder clearData() {
      data = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ImageData build() {
      try {
        ImageData record = new ImageData();
        record.extension = fieldSetFlags()[0] ? this.extension : (java.lang.CharSequence) defaultValue(fields()[0]);
        record.timestamp = fieldSetFlags()[1] ? this.timestamp : (java.lang.Long) defaultValue(fields()[1]);
        record.data = fieldSetFlags()[2] ? this.data : (java.nio.ByteBuffer) defaultValue(fields()[2]);
        return record;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<ImageData>
    WRITER$ = (org.apache.avro.io.DatumWriter<ImageData>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<ImageData>
    READER$ = (org.apache.avro.io.DatumReader<ImageData>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}