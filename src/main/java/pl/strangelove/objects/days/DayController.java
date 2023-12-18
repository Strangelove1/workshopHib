package pl.strangelove.objects.days;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.strangelove.objects.dayNames.DayNameRepository;
import pl.strangelove.objects.ingredients.Ingredient;
import pl.strangelove.objects.meals.Meal;
import pl.strangelove.objects.meals.MealRepository;
import pl.strangelove.objects.weeks.WeekRepository;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/days")
public class DayController {

    private final DayRepository dayRepository;
    private final MealRepository mealRepository;
    private final DayNameRepository dayNameRepository;
    private final WeekRepository weekRepository;

    public DayController(DayRepository dayRepository,WeekRepository weekRepository, MealRepository mealRepository, DayNameRepository dayNameRepository) {
        this.dayRepository = dayRepository;
        this.mealRepository = mealRepository;
        this.dayNameRepository = dayNameRepository;
        this.weekRepository = weekRepository;
    }

    @GetMapping("/list")
    public String getAllDays(Model model) {
        List<Day> dayList = dayRepository.findAll();
        model.addAttribute("days", dayList);
        return "day/list";
    }

    @GetMapping("/create")
    public String createDay(Model model) {
        Day day = new Day();

        // Retrieve the list of available meals
        List<Meal> allMeals = mealRepository.findAll();

        // Set the initial meals for the day (you can adjust this based on your requirements)
        day.setMeals(allMeals);

        // Calculate the sum of kcal and carbohydrates from the chosen ingredients
        int totalKcal = 0;
        int totalCarbohydrates = 0;
        for (Meal meal : allMeals) {
            for (Ingredient ingredient : meal.getIngredientsList()) {
                totalKcal += ingredient.getKcal();
                totalCarbohydrates += ingredient.getCarbohydrates();
            }
        }

        // Set the calculated values to the day
        day.setKcal(totalKcal);
        day.setCarbohydrates(totalCarbohydrates);

        model.addAttribute("day", day);
        model.addAttribute("meals", allMeals);
        model.addAttribute("dayNames", dayNameRepository.findAll());
        model.addAttribute("weeks", weekRepository.findAll());

        return "day/addDay";
    }


    @PostMapping("/create")
    public String saveDay(@ModelAttribute @Validated Day day, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "day/addDay";
        }
        LocalDate localDate = LocalDate.now();
        day.setCreated(localDate);
        dayRepository.save(day);
        return "redirect:/days/list";
    }

    @GetMapping("/updateDay/{id}")
    public String editDay(Model model, @PathVariable Long id) {
        Day day = dayRepository.findById(id).orElse(null);

        // Calculate totals and set keto-friendly status
        calculateAndSetDayTotals(day);
        checkAndSetKetoFriendlyStatus(day);

        model.addAttribute("day", day);
        model.addAttribute("meals", mealRepository.findAll());
        model.addAttribute("dayNames", dayNameRepository.findAll());
        model.addAttribute("weeks", weekRepository.findAll());
        return "day/updateDay";
    }

    @PostMapping("/update/{id}")
    public String updateDay(@PathVariable Long id, @ModelAttribute Day day) {
        Day dayToUpdate = dayRepository.findById(id).orElse(null);
        LocalDate localDate = LocalDate.now();

        if (dayToUpdate != null) {
            calculateAndSetDayTotals(day);
            checkAndSetKetoFriendlyStatus(day);

            dayToUpdate.setDayNames(day.getDayNames());
            dayToUpdate.setId(day.getId());
            dayToUpdate.setKcal(day.getKcal());
            dayToUpdate.setMeals(day.getMeals());
            dayToUpdate.setCarbohydrates(day.getCarbohydrates());
            dayToUpdate.setCreated(localDate);
            dayToUpdate.setWeek(day.getWeek());
            dayToUpdate.setUser(day.getUser());

            dayRepository.save(dayToUpdate);
        }

        return "redirect:/days/list";
    }

    @GetMapping("/deleteDay/{id}")
    public String deleteDay(@PathVariable Long id) {
        dayRepository.deleteById(id);
        return "redirect:/days/list";
    }

    private void calculateAndSetDayTotals(Day day) {
        int totalKcal = 0;
        int totalCarbohydrates = 0;

        for (Meal meal : day.getMeals()) {
            totalKcal += meal.getKcal();
            totalCarbohydrates += meal.getCarbohydrates();
        }

        day.setKcal(totalKcal);
        day.setCarbohydrates(totalCarbohydrates);
    }

    private void checkAndSetKetoFriendlyStatus(Day day) {
        // Check if the sum of carbohydrates is below 40 to set keto-friendly status
        int totalCarbohydrates = day.getCarbohydrates();
        day.setKetoFriendly(totalCarbohydrates < 40);
    }

    @GetMapping("/days/calculateTotals")
    public TotalsResponse calculateTotals(@RequestParam("mealIds") List<Long> mealIds) {
        int totalKcal = 0;
        int totalCarbohydrates = 0;

        // Calculate totals based on the selected meal IDs
        for (Long mealId : mealIds) {
            Meal meal = mealRepository.findById(mealId).orElse(null);
            totalKcal += meal.getKcal();
            totalCarbohydrates += meal.getCarbohydrates();
        }

        // Create a response object to hold the totals
        TotalsResponse response = new TotalsResponse();
        response.setTotalKcal(totalKcal);
        response.setTotalCarbohydrates(totalCarbohydrates);

        return response;
    }

}
