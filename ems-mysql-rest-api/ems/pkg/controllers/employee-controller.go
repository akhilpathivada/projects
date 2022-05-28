package controllers

import (
	"ems/pkg/model"
	"ems/pkg/util"
	"encoding/json"
	"fmt"
	"net/http"
	"strconv"

	"github.com/gorilla/mux"
)

// Creating a new Employee record ...
func CreateEmployee(w http.ResponseWriter, r *http.Request) {
	newEmployee := &model.Employee{}
	util.ParseBody(r, newEmployee)
	b := newEmployee.CreateEmployee()
	result, _ := json.Marshal(b)
	w.Header().Set("Content-Type", "pkglication/json")
	w.WriteHeader(http.StatusCreated)
	w.Write(result)
}

// Getting all Employee records ...
func GetAllEmployees(w http.ResponseWriter, r *http.Request) {
	employees := model.GetEmployees()
	res, _ := json.Marshal(employees)
	w.Header().Set("Content-Type", "pkglication/json")
	w.WriteHeader(http.StatusOK)
	w.Write(res)
}

// Getting an Employee record based on Employee Id ...
func GetEmployeeByID(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	employeeID := vars["employeeId"]
	ID, err := strconv.ParseInt(employeeID, 0, 0)

	if err != nil {
		fmt.Println("Error while parsing")
	}
	employeeDetails, _ := model.GetEmployee(ID)
	result, _ := json.Marshal(employeeDetails)
	w.Header().Set("Content-Type", "pkglication/json")
	w.WriteHeader(http.StatusOK)
	w.Write(result)
}

// Deleting an Employee record based on Employee Id ...
func DeleteEmployeeByID(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	employeeID := vars["employeeId"]
	ID, err := strconv.ParseInt(employeeID, 0, 0)

	if err != nil {
		fmt.Println("Error while parsing")
	}
	employee := model.DeleteEmployee(ID)
	result, _ := json.Marshal(employee)
	w.Header().Set("Content-Type", "pkglication/json")
	w.WriteHeader(http.StatusNoContent)
	w.Write(result)
}

// Updating an Employee record based on Employee Id ...
func UpdateEmployeeByID(w http.ResponseWriter, r *http.Request) {
	var updateEmployee = &model.Employee{}
	util.ParseBody(r, updateEmployee)
	vars := mux.Vars(r)
	employeeID := vars["employeeId"]
	ID, err := strconv.ParseInt(employeeID, 0, 0)

	if err != nil {
		fmt.Println("Error while parsing")
	}
	employeeDetails, db := model.GetEmployee(ID)

	if updateEmployee.FirstName != "" {
		employeeDetails.FirstName = updateEmployee.FirstName
	}

	if updateEmployee.LastName != "" {
		employeeDetails.LastName = updateEmployee.LastName
	}
	db.Save(employeeDetails)
	res, _ := json.Marshal(employeeDetails)
	w.Header().Set("Content-Type", "pkglication/json")
	w.WriteHeader(http.StatusOK)
	w.Write(res)
}
