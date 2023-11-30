package ru.rambler.ptvkz1000.companyResourceManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

@Controller
@SpringBootApplication
@EnableJpaRepositories
public class CompanyResourceManagementApplication {

	@Autowired
	private EmployeeRepository employeeRepository;

	public static void main(String[] args) {
		SpringApplication.run(CompanyResourceManagementApplication.class, args);
	}

	@GetMapping("/")
	public String index(Model model){
		String employeeCount = String.valueOf(employeeRepository.count());
		model.addAttribute("employeeCountMessage",
				"В нашей компании уже "
				+ employeeCount + " сотрудников!");
		return "index";
	}

	@GetMapping("employees")
	public String employeesGet(Model model) {
		model.addAttribute("employeeList",
				employeeRepository.findAll());
		return "employees";
	}

	@GetMapping("employees/{id}")
	public String employeeProfile(@PathVariable long id, Model model) {
		AtomicReference<String> view = new AtomicReference<>("profile");
		employeeRepository.findById(id).ifPresentOrElse(employee -> model.addAttribute("employee", employee),
				() -> {
					view.set("error");
					model.addAttribute("message", "Профиль не найден.");
		});

		return view.get();
	}

	@PostMapping("employees/delete")
	public String deleteEmployee(@RequestParam(name="id") Long id) {
		employeeRepository.deleteById(id);
		return "redirect:/employees";
	}

	@GetMapping("newEmployee")
	public String newEmployeeGet(Model model) {
		model.addAttribute("employee", new Employee());
		return "newEmployee";
	}

	@PostMapping("newEmployee")
	public String employeesPost(@ModelAttribute Employee employee) {
		employee.setRegistrationDate(Date.from(Instant.now()));
		employeeRepository.save(employee);
		return "redirect:/employees";
	}
}