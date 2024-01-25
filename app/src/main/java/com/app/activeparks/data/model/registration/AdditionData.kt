package com.app.activeparks.data.model.registration

data class AdditionData(
	var weight: Double = 80.0,
	var height: Int = 175,
	var sex: String = "male",
	var fName: String = "",
	var lName: String = "",
	var birthday: String = "",
	var isVpo: Int = 0,
	var isVeteran: Int = 0,
	var facebookToken: String = "",
	var googleToken: String = "",
	var appleToken: String = "",
	var bankIdToken: String = ""
)

