package pl.allegrov2.allegrov2.validation.exceptions;

import pl.allegrov2.allegrov2.data.entities.Product;

import java.util.Map;

public class QuantityCriteriaNotMetException extends RuntimeException {
    private String detailedMessage;

    public QuantityCriteriaNotMetException(Map<Product, Integer> notAvailableItems) {
        super(notAvailableItems.toString());
        this.detailedMessage = createMessage(notAvailableItems);
    }

    public QuantityCriteriaNotMetException(Product product, int requestedQuantity) {
        super(product + "; requested quantity: " + requestedQuantity);
        this.detailedMessage = createMessage(Map.of(product, requestedQuantity));
    }

    public String getDetailedMessage() {
        return detailedMessage;
    }

    private String createMessage(Map<Product, Integer> notAvailableItems){
        StringBuilder builder = new StringBuilder();

        builder.append("Quantity criteria not met for following items: ");

        for(Map.Entry<Product, Integer> entry : notAvailableItems.entrySet()){

            Product product = entry.getKey();
            Integer requestedQuantity = entry.getValue();

            builder.append(String.format(
                    "%-20s (requested: %d, available: %d)\n",
                    product.shortName(),
                    requestedQuantity,
                    product.getStock()
            ));
        }
        return builder.toString();
    }
}
