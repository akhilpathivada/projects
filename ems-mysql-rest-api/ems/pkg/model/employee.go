package model

import (
	"ems/pkg/config"

	"github.com/jinzhu/gorm"
)

var db *gorm.DB

type Employee struct {
	EmpID     int `gorm:"AUTO_INCREMENT; column:emp_id; primary_key "`
	FirstName string
	LastName  string
}

// Connecting to MySQL Database ...
func init() {
	config.ConnectToDB()
	db = config.GetDB()
	db.AutoMigrate(&Employee{})
}

// Selecting all Employee records ...
func GetEmployees() []Employee {
	var employees []Employee
	db.Find(&employees)
	return employees
}

// Inserting a new Employee record ...
func (e *Employee) CreateEmployee() *Employee {
	db.NewRecord(e)
	db.Create(&e)
	return e
}

// Selecting a Employee record based on Employee Id
func GetEmployee(EmpID int64) (*Employee, *gorm.DB) {
	var getEmployee Employee
	db := db.Where("EMP_ID = ?", EmpID).Find(&getEmployee)
	return &getEmployee, db
}

// Deleting a Employee Record based on Employee Id ...
func DeleteEmployee(EmpID int64) Employee {
	var employee Employee
	db.Where("EMP_ID = ?", EmpID).Delete(employee)
	return employee
}
