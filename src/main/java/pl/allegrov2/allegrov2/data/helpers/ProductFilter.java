package pl.allegrov2.allegrov2.data.helpers;

import lombok.*;

import java.util.Optional;

@ToString
@EqualsAndHashCode
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductFilter {

    private String searchString;
    private boolean onlyInStock;


    public Optional<String> getSearchString() {
        return Optional.ofNullable(searchString);
    }

    public Optional<Boolean> getOnlyInStock() {
        return Optional.ofNullable(onlyInStock);
    }
}
