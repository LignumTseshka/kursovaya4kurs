package ru.rambler.ptvkz1000.companyResourceManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDateTime;

@Controller
@SpringBootApplication
@EnableJpaRepositories
public class CompanyResourceManagementApplication {

	@Autowired
	private EmployeeRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(CompanyResourceManagementApplication.class, args);
	}

	@GetMapping("/")
	public String index(Model model){
		String employeeCount = String.valueOf(repository.count());
		model.addAttribute("employeeCountMessage",
				"В нашей компании уже "
				+ employeeCount + " сотрудников!");
		return "index";
	}

	@GetMapping("employees")
	public String employeesGet(Model model) {
		model.addAttribute("employeeList",
				repository.findAll());
		return "employees";
	}

	@PostMapping("employees/delete")
	public String deleteEmployee(@RequestParam(name="id") Long id) {
		repository.deleteById(id);
		return "redirect:/employees";
	}

	@GetMapping("newEmployee")
	public String newEmployeeGet(Model model) {
		model.addAttribute("employee", new Employee());
		return "newEmployee";
	}

	@PostMapping("newEmployee")
	public String employeesPost(@ModelAttribute Employee employee) {
		repository.save(employee);
		return "redirect:/employees";
	}
}