package ems.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Department")
public class Department {
    @Id
    @JsonProperty
    @Column(name = "depId")
    private Integer depId;

    @NotEmpty
    @JsonProperty
    @Column(name = "name")
    private String name;

    public Department(){

    }

    public Department(int depId, String name){
        this.depId = depId;
        this.name = name;
    }

    public Integer getDepId() {
        return this.depId;
    }

    public Department setDepId(Integer depId) {
        this.depId = depId;
        return this;
    }

    public String getName() {
        return name;
    }

    public Department setName(String name) {
        this.name = name;
        return this;
    }
}
