package com.jason;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.avro.Protocol;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;

public class HelloAvro {
    public static Protocol protocol = null;
    public static Schema schema = null;

    private static void test(){

    }

    public HelloAvro(String schemaFile, String schemaName) {
        initAvroSchema(schemaFile, schemaName);
    }

    public static void main(String[] args) {
        initAvroSchema("schema.avsc", "message");
        GenericRecord message = getGenericRecordOne();
//		GenericRecord message = getGenericRecordTwo();

        System.out.println("message: " + message);
        System.out.println("rows: " + message.get("rows"));
        System.out.println("values: " + message.get("valueslist"));

        // Serialization and Deserialization
        byte[] post = serialization(message);
        GenericRecord deserializationMessage = deserialization(post);

        System.out.println("DeserializationMessage: " + deserializationMessage);
        System.out.println("DeserializationMessage.rows: " + deserializationMessage.get("rows"));
        System.out.println("DeserializationMessage.values: " + deserializationMessage.get("valueslist"));
    }

    public static GenericRecord getGenericRecordOne() {
        GenericRecord message = new GenericData.Record(schema);
        if (schema == null)
            return null;
        long id = 1234L;
        String dbTable = "aaa";
        String hbaseTable = "IM_ItemPrice";
        String action = "U";
        List<String> iccs = new ArrayList<String>();
        iccs.add("9SIA16U1T30157");
        List<String> columnFamily = new ArrayList<String>();
        columnFamily.add("ol");
        columnFamily.add("of");
        List<String> fields = new ArrayList<String>();
        fields.add("MAPPrice");
        fields.add("OEMMark");
        List<List<String>> values = new ArrayList<List<String>>();
        List<String> value = new ArrayList<String>();
        value.add("0");
        value.add("True");
        values.add(value);

        message.put("id", id);
        message.put("time", System.currentTimeMillis());
        message.put("datatype", dbTable);
        message.put("table", hbaseTable);
        message.put("action", action);
        message.put("rows", iccs);
        message.put("cf", columnFamily);
        message.put("fields", fields);
        message.put("valueslist", values);

        return message;
    }

    public static GenericRecord getGenericRecordTwo() {
        GenericRecord message = new GenericData.Record(schema);
        if (schema == null)
            return null;
        long id = 1234L;
        String dbTable = "iteminventory";
        String hbaseTable = "IM_ItemPrice";
        String action = "U";
        List<String> iccs = new ArrayList<String>();
        iccs.add("9SIA0SG17D1142|CAN|1003");
        iccs.add("9SIA0SG17D1232|CAN|1003");

        message.put("id", id);
        message.put("time", System.currentTimeMillis());
        message.put("datatype", dbTable);
        message.put("table", hbaseTable);
        message.put("action", action);
        message.put("rows", iccs);

        return message;
    }

    public static void initAvroSchema(String schemaFile, String schemaName) {
        if (schemaFile == null || schemaName == null) {
            return;
        }
        URL url = HelloAvro.class.getClassLoader().getResource(schemaFile);
        if (url == null) {
            return;
        }
        try {
            protocol = Protocol.parse(new File(url.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        schema = protocol.getType(schemaName);
        if (schema == null) {
            return;
        }
    }

    public static Schema getSchema() {
        return schema;
    }

    /**
     * Avro Serialization
     * the avro way to serialize GenericRecord.
     * Input GenericRecord which has Avro schema.
     * @param genericRecord
     * @return
     */
    public static byte[] serialization(GenericRecord genericRecord) {
        byte[] message = null;
        DatumWriter<GenericRecord> avroEventWriter = new GenericDatumWriter<GenericRecord>(schema);
        EncoderFactory avroEncoderFactory = EncoderFactory.get();
        if (avroEventWriter == null)
            return null;
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            BinaryEncoder binaryEncoder = avroEncoderFactory.binaryEncoder(stream, null);
            avroEventWriter.write(genericRecord, binaryEncoder);
            binaryEncoder.flush();
            message = stream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Avro serialization failure", e);
        }
        return message;
    }

    /**
     * Avro Deserialization
     *
     * @param post
     * @return
     */
    public static GenericRecord deserialization(byte[] post) {
        // Maybe I can say GenericRecord is data container holding a specified data schema.
        GenericRecord message = new GenericData.Record(schema);
        InputStream inputStream = new ByteArrayInputStream(post);
        DatumReader<GenericRecord> reader = new GenericDatumReader<GenericRecord>(schema);

        BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(inputStream, null);
        try {
            message = reader.read(message, decoder);
        } catch (IOException e) {
        }
        return message;
    }

}
