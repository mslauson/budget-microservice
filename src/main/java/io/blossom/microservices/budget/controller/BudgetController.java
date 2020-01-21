package io.blossom.microservices.budget.controller;

import io.blossom.microservices.budget.domain.models.BudgetRequestModel;
import io.blossom.microservices.budget.domain.models.BudgetResponseModel;
import io.blossom.microservices.budget.service.IBudgetService;
import io.blossom.util.budgetcommonutil.constants.GlobalConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class BudgetController {

    private final IBudgetService budgetService;

    @Autowired
    public BudgetController(IBudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping("/budget")
    public ResponseEntity<BudgetResponseModel> saveBudgetV1(@RequestBody @Valid BudgetRequestModel request) {
        log.info("saveBudgetV1: request=[{}]", request);
        return ResponseEntity.ok(budgetService.saveBudget(request));
    }

    @GetMapping("/budget/{id}")
    public ResponseEntity<BudgetResponseModel> getBudgetByIdV1(@PathVariable String id) {
        log.info("getBudgetByIdV1: id=[{}]", id);
        return ResponseEntity.ok(budgetService.getBudgetById(id));
    }

    @GetMapping("/budgets/username/{username}")
    public ResponseEntity<BudgetResponseModel> getAllBudgetsByUsernameV1(@PathVariable @Size(min = 4, max = 30) String username) {
        log.info("getAllBudgetsByUsernameV1: username=[{}]", username);
        return ResponseEntity.ok(budgetService.getAllBudgetsByUsername(username.toUpperCase()));
    }

    @GetMapping("/budgets/year/{year}")
    public ResponseEntity<BudgetResponseModel> getAllBudgetsByYearV1(@PathVariable @Min(1970) @Max(9999) Integer year) {
        log.info("getAllBudgetsByYearV1: year=[{}]", year);
        return ResponseEntity.ok(budgetService.getAllBudgetsByYear(year));
    }

    @GetMapping("/budgets/year/{year}/month/{month}")
    public ResponseEntity<BudgetResponseModel> getAllBudgetsByYearAndMonthV1(
            @PathVariable @Min(1970) @Max(9999) Integer year,
            @PathVariable @Min(1) @Max(12) Integer month) {

        log.info("getAllBudgetsByYearAndMonthV1: year=[{}] month=[{}]", year, month);
        return ResponseEntity.ok(budgetService.getAllBudgetsByYearAndMonth(year, month));
    }

    @GetMapping("/budgets/category/{category}")
    public ResponseEntity<BudgetResponseModel> getAllBudgetsByCategoryV1(@PathVariable @Size(max = 50) String category) {
        log.info("getAllBudgetsByCategoryV1: category=[{}]", category);
        return ResponseEntity.ok(budgetService.getAllBudgetsByCategory(category.toUpperCase()));
    }

    @GetMapping("/budgets/type/{type}")
    public ResponseEntity<BudgetResponseModel> getAllBudgetsByTypeV1(@PathVariable @Size(max = 50) String type) {
        log.info("getAllBudgetsByTypeV1: type=[{}]", type);
        return ResponseEntity.ok(budgetService.getAllBudgetsByType(type.toUpperCase()));
    }

    @DeleteMapping("/budget/{id}")
    public ResponseEntity<Map<String, String>> deleteBudgetByIdV1(@PathVariable String id) {
        log.info("deleteBudgetByIdV1: id=[{}]", id);
        boolean deleted = budgetService.deleteBudgetById(id);
        Map<String, String> response = new HashMap<>();
        if (deleted) {
            response.put("status", GlobalConstants.SUCCESS_MSG.getConstant());
        } else {
            response.put("status", "Failed");
        }
        return ResponseEntity.ok(response);
    }

}
