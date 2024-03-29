package com.bprojects.foodcatalogue.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bprojects.foodcatalogue.DTO.FoodCataloguePage;
import com.bprojects.foodcatalogue.DTO.FoodItemDTO;
import com.bprojects.foodcatalogue.DTO.Restaurant;
import com.bprojects.foodcatalogue.entity.FoodItem;
import com.bprojects.foodcatalogue.mapper.FoodItemMapper;
import com.bprojects.foodcatalogue.repo.FoodItemRepo;

@Service
public class FoodCatalogueService {
	
	@Autowired
	private FoodItemRepo foodItemRepo;
	
	@Autowired
	private RestTemplate restTemplate;
	
	 public FoodItemDTO addFoodItem(FoodItemDTO foodItemDTO) {
	        FoodItem foodItemSavedInDB = foodItemRepo.save(FoodItemMapper.INSTANCE.mapFoodItemDTOToFoodItem(foodItemDTO));
	        return FoodItemMapper.INSTANCE.mapFoodItemToFoodItemDto(foodItemSavedInDB);
	    }

	    public FoodCataloguePage fetchFoodCataloguePageDetails(Integer restaurantId) {
	        List<FoodItem> foodItemList =  fetchFoodItemList(restaurantId);
	        Restaurant restaurant = fetchRestaurantDetailsFromRestaurantMS(restaurantId);
	        return createFoodCataloguePage(foodItemList, restaurant);
	    }
	    
	    private FoodCataloguePage createFoodCataloguePage(List<FoodItem> foodItemList, Restaurant restaurant) {
	        FoodCataloguePage foodCataloguePage = new FoodCataloguePage();
	        foodCataloguePage.setFoodItemsList(foodItemList);
	        foodCataloguePage.setRestaurant(restaurant);
	        return foodCataloguePage;
	    }

	    private Restaurant fetchRestaurantDetailsFromRestaurantMS(Integer restaurantId) {
	       return restTemplate.getForObject("http://RESTAURANT-SERVICE/restaurant/fetchById/"+restaurantId, Restaurant.class);
	    }

	    private List<FoodItem> fetchFoodItemList(Integer restaurantId) {
	        return foodItemRepo.findByRestaurantId(restaurantId);
	    }

}
