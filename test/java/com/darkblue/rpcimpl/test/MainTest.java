package com.darkblue.rpcimpl.test;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.junit.Test;
import org.springframework.util.SerializationUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainTest {

    @Test
    public void testMain(){

        Kryo kryo = new Kryo();
        // ...
        Output output = new Output(10240);



        kryo.writeClassAndObject(output,new RuntimeException("66666"));
        byte[] buffer = output.toBytes();

        //output.flush();
        output.close();


        // ...
        /*Input input = new Input(buffer);
        Object o = kryo.readObjectOrNull(input);
        System.out.println(o.getClass());
        input.close();*/



    }

    @Test
    public void testKryo2(){

        Kryo kryo = new Kryo();

        Output output = new Output(1024);
        kryo.writeObject(output,new Message(2L,"da"));

        Input input = new Input(output.toBytes());
        Message message = kryo.readObject(input, Message.class);
        System.out.println(message);


    }

    @Test
    public void testSpringSerializationUtils(){
        byte[] serialize = SerializationUtils.serialize(new RuntimeException("6666"));

        Object deserialize = SerializationUtils.deserialize(serialize);

        System.out.println(deserialize.getClass());

        RuntimeException runtimeException = (RuntimeException) deserialize;
        runtimeException.printStackTrace();


    }
}
