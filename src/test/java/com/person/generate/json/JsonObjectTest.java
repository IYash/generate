package com.person.generate.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangchangling on 2018/10/31.
 */
public class JsonObjectTest {

    private static ObjectMapper jsonObjectMapper = new ObjectMapper();
    @Test
    public void testSingle() throws Exception{
        People p = new People("a",1);
        ByteArrayOutputStream e = new ByteArrayOutputStream(1024);
        jsonObjectMapper.writeValue(e,p);
        System.out.println(e.toString());
    }
    @Test
    public void testMulti() throws Exception{
        People p1 = new People("a",1);
        People p2=new People("b",2);
        List<People> lps = new ArrayList<>();
        lps.add(p1);lps.add(p2);
        ByteArrayOutputStream e = new ByteArrayOutputStream(1024);
        jsonObjectMapper.writeValue(e,lps);
        System.out.println(e.toString());
    }
    class People{
        private String name;
        private int age;

        public People(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
