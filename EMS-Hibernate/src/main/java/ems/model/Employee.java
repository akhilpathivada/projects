package ems.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Employee")
public class Employee {
    @Id
    @JsonProperty
    @Column(name = "empId")
    private Integer empId;

    @NotEmpty
    @JsonProperty
    @Column(name = "name")
    private String name;

    @NotNull
    @JsonProperty
    @Column(name = "depId")
    private Integer depId;

    public Employee(){

    }

    public Employee(int empId, int depId, String name){
        this.empId = empId;
        this.depId = depId;
        this.name = name;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public Integer getEmpId(){
        return this.empId;
    }

    public String getName() {
        return this.name;
    }

    public Employee setName(String name) {
        this.name = name;
        return this;
    }

    public Employee setDepId(Integer depId){
        this.depId = depId;
        return this;
    }

    public Integer getDepId(){
        return this.depId;
    }
}
