package com.kcf.entity;

/**
 * Created with IntelliJ IDEA.
 * User: 老牛 -- TK
 * Date: 14-4-1
 * Time: 下午3:32
 */
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String gender;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
