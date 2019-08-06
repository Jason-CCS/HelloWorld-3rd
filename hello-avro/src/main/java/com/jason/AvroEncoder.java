package com.jason;

import kafka.serializer.Encoder;
import kafka.utils.VerifiableProperties;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by jc6t on 2015/8/10.
 */
public class AvroEncoder implements Encoder<GenericRecord> {
    public AvroEncoder(VerifiableProperties verifiableProperties) {

    }

    public byte[] toBytes(GenericRecord genericRecord) {
        byte[] result = null;

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out, null);
            DatumWriter<GenericRecord> writer = new GenericDatumWriter<GenericRecord>(genericRecord.getSchema());
            writer.write(genericRecord, encoder);
            encoder.flush();
            out.close();
            result = out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("AvroEncoder toBytes() failed", e);
        }
        return result;
    }
}
