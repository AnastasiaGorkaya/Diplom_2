package models;

import lombok.*;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Order {
    private ArrayList<String> ingredients;
}
