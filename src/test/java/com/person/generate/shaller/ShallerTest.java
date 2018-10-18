package com.person.generate.shaller;

import com.person.shaller.CustomMessageMarshallerUtils;
import org.junit.Test;

import javax.xml.bind.annotation.*;

/**
 * Created by huangchangling on 2018/10/18.
 */
public class ShallerTest {

    @Test
    public void testJson(){
        People jp = new People("test",1);
        String result = CustomMessageMarshallerUtils.getMessage(jp,"json");
        System.out.println(result);
    }
    @Test
    public void testXml(){
        People jp = new People("test",1);
        Address address = new Address("local");
        jp.setAddress(address);
        String result = CustomMessageMarshallerUtils.getMessage(jp,"xml");
        System.out.println(result);
    }
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "xmlPeople")
    class People{
        @XmlAttribute(name="NameTest")
        private String name;
        @XmlElement(name = "Age")
        private int age;
        private Address address;

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

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }
    }
    class Address{
        private String locate;

        public Address(String locate) {
            this.locate = locate;
        }

        public String getLocate() {
            return locate;
        }

        public void setLocate(String locate) {
            this.locate = locate;
        }
    }
}
