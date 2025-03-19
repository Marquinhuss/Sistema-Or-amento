package com.sistema.orcamento.Model.Enum;
import java.math.BigDecimal;

public enum Supplier {
    C(12.5), L(10), SK(17.5), SERVICE(0);

    private Double shippingValue;

    private Supplier(double d) {
        this.shippingValue = d;
    }

    public Double getValorFrete() {
        return shippingValue;
    }

    @Override
    public String toString() {
        return this.name();
    }

    public  BigDecimal getshippingValue(Integer margin) {
        if(this.getValorFrete() <= 0){
            return BigDecimal.valueOf(0);
        }
        BigDecimal shippingValue = BigDecimal.valueOf(this.getValorFrete());
        BigDecimal marginIncreaseValue = BigDecimal.valueOf(margin).divide(BigDecimal.valueOf(100)).multiply(shippingValue);

        shippingValue = shippingValue.add(marginIncreaseValue);

        return shippingValue;
    }


    public static BigDecimal getshippingValue(Supplier supplier, Integer margin) {
        BigDecimal shippingValue = BigDecimal.valueOf(0);

        if (supplier.equals(Supplier.C)) {
            shippingValue = BigDecimal.valueOf(12.5).setScale(2);
            BigDecimal marginIncreaseValue = BigDecimal.valueOf(margin).divide(BigDecimal.valueOf(100)).multiply(shippingValue);

            shippingValue = shippingValue.add(marginIncreaseValue);
            return shippingValue;
        } else if (supplier.equals(Supplier.L)) {
            shippingValue = BigDecimal.valueOf(10).setScale(2);
            BigDecimal marginIncreaseValue = BigDecimal.valueOf(margin).divide(BigDecimal.valueOf(100)).multiply(shippingValue);
            shippingValue = shippingValue.add(marginIncreaseValue);
            return shippingValue;
        } else {
            shippingValue = BigDecimal.valueOf(17.5).setScale(2);
            BigDecimal marginIncreaseValue = BigDecimal.valueOf(margin).divide(BigDecimal.valueOf(100)).multiply(shippingValue);
            shippingValue = shippingValue.add(marginIncreaseValue);
            return shippingValue;
        }

    }
}
