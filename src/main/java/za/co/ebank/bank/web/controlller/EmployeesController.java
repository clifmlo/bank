/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.co.ebank.bank.web.controlller;


import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author cliff
 */
@CrossOrigin()
@RestController
@RequestMapping({ "/employees" })
public class EmployeesController {


	@GetMapping("/all")
	public String firstPage() {
		return "These are employees";
	}

	
        @GetMapping(produces = "application/json")
	@RequestMapping({ "/validateLogin" })
	public String validateLogin() {
		return "User successfully authenticated";
	}
}
