package routes

import (
	"ems/pkg/controllers"

	"github.com/gorilla/mux"
)

func RegisterEmployeeRoutes(router *mux.Router) {
	router.HandleFunc("/employees", controllers.CreateEmployee).Methods("POST")
	router.HandleFunc("/employees", controllers.GetAllEmployees).Methods("GET")
	router.HandleFunc("/employees/{employeeId}", controllers.GetEmployeeByID).Methods("GET")
	router.HandleFunc("/employees/{employeeId}", controllers.UpdateEmployeeByID).Methods("PUT")
	router.HandleFunc("/employees/{employeeId}", controllers.DeleteEmployeeByID).Methods("DELETE")
}
