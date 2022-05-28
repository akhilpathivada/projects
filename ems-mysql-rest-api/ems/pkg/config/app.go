package config

import (
	"fmt"

	"github.com/jinzhu/gorm"
	_ "github.com/jinzhu/gorm/dialects/mysql"
)

var db *gorm.DB

func ConnectToDB() {
	// Please define your user name and password for my sql.
	d, err := gorm.Open("mysql", "root:codec@rgukt.in@(localhost)/EMS?charset=utf8&parseTime=True&loc=Local")
	if err != nil {
		fmt.Println(err)
		panic(err)
	}
	db = d
}

func GetDB() *gorm.DB {
	return db
}
