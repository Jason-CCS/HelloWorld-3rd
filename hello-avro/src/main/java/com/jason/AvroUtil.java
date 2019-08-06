package com.jason;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jc6t on 2015/8/10.
 */
public final class AvroUtil {

    /**
     * give file path and get avro schema.
     *
     * @param filePath
     * @return
     */
    public static Schema getSchema(String filePath) {
        if (filePath == null) {
            return null;
        }
        Schema schema = null;
        try {
            schema = new Schema.Parser().parse(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return schema;
    }

    /**
     * @param schema
     * @param map
     * @return
     */
    public static GenericRecord genGenericRecord(Schema schema, Map<String, Object> map) {
        if (schema == null || map == null) {
            return null;
        }
        GenericRecord result = new GenericData.Record(schema);
        result.put("id", map.get("id"));
        result.put("time", System.currentTimeMillis());
        result.put("dbTable", map.get("dbTable"));
        result.put("table", map.get("table"));
        result.put("action", map.get("action"));
        result.put("rows", map.get("rows"));
        result.put("cf", map.get("cf"));
        result.put("fields", map.get("fields"));
        result.put("valueslist", map.get("valueslist"));

        return result;
    }

    /**
     * Avro Serialization
     * the avro way to serialize GenericRecord.
     * Input GenericRecord which represented by Avro schema.
     *
     * @param schema
     * @param genericRecord
     * @return
     */
    public static byte[] serialize(Schema schema, GenericRecord genericRecord) {
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
            throw new RuntimeException("Avro serialize failure", e);
        }
        return message;
    }

    /**
     * Avro Deserialization
     *
     * @param schema
     * @param byteMsg
     * @return
     */
    public static GenericRecord deserialize(Schema schema, byte[] byteMsg) {
        // Maybe I can say GenericRecord is data container holding a specified data schema.
        GenericRecord message = new GenericData.Record(schema);
        InputStream inputStream = new ByteArrayInputStream(byteMsg);
        DatumReader<GenericRecord> reader = new GenericDatumReader<GenericRecord>(schema);

        BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(inputStream, null);
        try {
            message = reader.read(message, decoder);
        } catch (IOException e) {
        }
        return message;
    }

    /**
     * This function is for the purpose to transform kv to GenericRecord directly.
     * But not finished yet.
     *
     * @param schema
     * @param kv
     * @return
     */
    public static GenericRecord genGR(Schema schema, Map<String, Object> kv) {
        if (schema == null || kv == null) {
            return null;
        }

        GenericRecord result = new GenericData.Record(schema);
        List<Schema.Field> fieldList = schema.getFields();
        for (int i = 0; i < fieldList.size(); i++) {
            String fName = fieldList.get(i).name();
            if (kv.containsKey(fName)) {
//                result.put(fName,);
            }
        }

        return null;
    }

    public static Map<String, Object> getOneSampleData(long id) {
        Map<String, Object> result = new HashMap<String, Object>();

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

        result.put("id", id);
        result.put("time", System.currentTimeMillis());
        result.put("dbTable", dbTable);
        result.put("table", hbaseTable);
        result.put("action", action);
        result.put("rows", iccs);
        result.put("cf", columnFamily);
        result.put("fields", fields);
        result.put("valueslist", values);

        return result;
    }
}
