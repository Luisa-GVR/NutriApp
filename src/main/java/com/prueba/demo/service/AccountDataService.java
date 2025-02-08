package com.prueba.demo.service;

import com.prueba.demo.model.AccountData;
import com.prueba.demo.model.Goal;
import com.prueba.demo.repository.AccountAllergyFoodRepository;
import com.prueba.demo.repository.AccountDataRepository;
import com.prueba.demo.repository.AccountDislikesFoodRepository;
import com.prueba.demo.repository.AccountLikesFoodRepository;
import com.prueba.demo.service.dto.FoodPreferencesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountDataService {

    private final AccountDataRepository accountDataRepository;
    private final AccountAllergyFoodRepository accountAllergyFoodRepository;
    private final AccountLikesFoodRepository accountLikedFoodRepository;
    private final AccountDislikesFoodRepository accountDislikedFoodRepository;

    @Autowired
    public AccountDataService(AccountDataRepository accountDataRepository,
                              AccountAllergyFoodRepository accountAllergyFoodRepository,
                              AccountLikesFoodRepository accountLikedFoodRepository,
                              AccountDislikesFoodRepository accountDislikedFoodRepository) {
        this.accountDataRepository = accountDataRepository;
        this.accountAllergyFoodRepository = accountAllergyFoodRepository;
        this.accountLikedFoodRepository = accountLikedFoodRepository;
        this.accountDislikedFoodRepository = accountDislikedFoodRepository;
    }
    // Calcular las calorías recomendadas
    public double calculateCalories(Long accountId) {

        Optional<AccountData> accountDataOpt = accountDataRepository.findByAccountId(accountId);

        if (accountDataOpt.isEmpty()) {
            throw new IllegalArgumentException("");
        }

        AccountData accountData = accountDataOpt.get();

        // Obtener valores de la cuenta
        boolean isMale = accountData.getGender(); // true = hombre, false = mujer
        int age = accountData.getAge();
        double weight = accountData.getWeight();
        double height = accountData.getHeight();
        Goal goal = accountData.getGoal() != null ? accountData.getGoal() : Goal.mantenimiento; // Valor por defecto

        // Cálculo del TMB según Harris-Benedict
        double tmb;


        // Ajustar según el objetivo, actividad de 5 dias por lo que se toma como moderado
        switch (goal) {
            case deficit:
                if (isMale) {
                    tmb = (((10*weight)+(6.25*height)-(5*age)+5)*1.55)*.8;
                } else {
                    tmb = (((10*weight)+(6.25*height)-(5*age)-161)*1.55)*(.8);
                }
                return  tmb;
            case volumen:
                if (isMale) {
                    tmb = (((10*weight)+(6.25*height)-(5*age)+5)*1.55)*1.2;
                } else {
                    tmb = (((10*weight)+(6.25*height)-(5*age)-161)*1.55)*1.2;
                }
                return tmb;
            default: // mantenimiento
                if (isMale) {
                    tmb = (((10*weight)+(6.25*height)-(5*age)+5)*1.55);
                } else {
                    tmb = (((10*weight)+(6.25*height)-(5*age)-161)*1.55);
                }
                return tmb;
        }
    }

    public FoodPreferencesDTO getFoodPreferences(Long accountId) {
        Optional<AccountData> accountDataOpt = accountDataRepository.findByAccountId(accountId);

        if (accountDataOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró AccountData para la cuenta con ID: " + accountId);
        }

        Long accountDataId = accountDataOpt.get().getId();

        // Obtener alergias
        List<String> allergies = accountAllergyFoodRepository.findFoodNamesByAccountDataId(accountDataId);

        // Obtener comidas que NO le gustan
        List<String> dislikedFoods = accountDislikedFoodRepository.findFoodNamesByAccountDataId(accountDataId);

        // Obtener comidas que SÍ le gustan
        List<String> likedFoods = accountLikedFoodRepository.findFoodNamesByAccountDataId(accountDataId);

        return new FoodPreferencesDTO(allergies, dislikedFoods, likedFoods);
    }

}
