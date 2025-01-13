package itau.cotacao.seguro.domain.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class InsurancePolicy {

    private UUID productId;
    private UUID offerId;
    private String category;
    private BigDecimal totalMonthlyPremiumAmount;
    private BigDecimal totalCoverageAmount;
    private Map<String, BigDecimal> coverages;
    private List<String> assistances;
    private Customer customer;

    public InsurancePolicy() {
    }

    public InsurancePolicy(Customer customer, List<String> assistances, Map<String, BigDecimal> coverages, BigDecimal totalCoverageAmount, BigDecimal totalMonthlyPremiumAmount, String category, UUID offerId, UUID productId) {
        this.customer = customer;
        this.assistances = assistances;
        this.coverages = coverages;
        this.totalCoverageAmount = totalCoverageAmount;
        this.totalMonthlyPremiumAmount = totalMonthlyPremiumAmount;
        this.category = category;
        this.offerId = offerId;
        this.productId = productId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<String> getAssistances() {
        return assistances;
    }

    public void setAssistances(List<String> assistances) {
        this.assistances = assistances;
    }

    public Map<String, BigDecimal> getCoverages() {
        return coverages;
    }

    public void setCoverages(Map<String, BigDecimal> coverages) {
        this.coverages = coverages;
    }

    public BigDecimal getTotalCoverageAmount() {
        return totalCoverageAmount;
    }

    public void setTotalCoverageAmount(BigDecimal totalCoverageAmount) {
        this.totalCoverageAmount = totalCoverageAmount;
    }

    public BigDecimal getTotalMonthlyPremiumAmount() {
        return totalMonthlyPremiumAmount;
    }

    public void setTotalMonthlyPremiumAmount(BigDecimal totalMonthlyPremiumAmount) {
        this.totalMonthlyPremiumAmount = totalMonthlyPremiumAmount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public UUID getOfferId() {
        return offerId;
    }

    public void setOfferId(UUID offerId) {
        this.offerId = offerId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }
}
