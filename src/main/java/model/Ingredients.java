package model;

import lombok.Data;
import model.Ingredient;

import java.util.List;
@Data
public class Ingredients {
    private List<Ingredient> data;
    private String success;
}
