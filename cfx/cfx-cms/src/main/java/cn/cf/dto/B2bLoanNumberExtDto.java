package cn.cf.dto;

public class B2bLoanNumberExtDto extends B2bLoanNumberDto {

    private String loanStartTimeBegin;

    private String loanStartTimeEnd;

    private String loanEndTimeBegin;

    private String loanEndTimeEnd;

    private String loans;

    private Double orderAmount;

    private Double actualAmount;

    private Double platformRate = 0.00;// 平台利率

    private Double repayingSerCharge = 0.00;// 未还本金服务费

    private Double repayingInterest = 0.00;// 未还银行利息

    private Double amountPayable = 0.00;// 客户应还金额

    private String accountPk;
    
    private String isOverdueName;
    
    private String block;

    public String getAccountPk() {
        return accountPk;
    }

    public void setAccountPk(String accountPk) {
        this.accountPk = accountPk;
    }

    public Double getPlatformRate() {
        return platformRate;
    }

    public void setPlatformRate(Double platformRate) {
        this.platformRate = platformRate;
    }

    public Double getRepayingSerCharge() {
        return repayingSerCharge;
    }

    public void setRepayingSerCharge(Double repayingSerCharge) {
        this.repayingSerCharge = repayingSerCharge;
    }

    public Double getRepayingInterest() {
        return repayingInterest;
    }

    public void setRepayingInterest(Double repayingInterest) {
        this.repayingInterest = repayingInterest;
    }

    public Double getAmountPayable() {
        return amountPayable;
    }

    public void setAmountPayable(Double amountPayable) {
        this.amountPayable = amountPayable;
    }

    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Double getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(Double actualAmount) {
        this.actualAmount = actualAmount;
    }

    public String getLoans() {
        return loans;
    }

    public void setLoans(String loans) {
        this.loans = loans;
    }

    public String getLoanStartTimeBegin() {
        return loanStartTimeBegin;
    }

    public void setLoanStartTimeBegin(String loanStartTimeBegin) {
        this.loanStartTimeBegin = loanStartTimeBegin;
    }

    public String getLoanStartTimeEnd() {
        return loanStartTimeEnd;
    }

    public void setLoanStartTimeEnd(String loanStartTimeEnd) {
        this.loanStartTimeEnd = loanStartTimeEnd;
    }

    public String getLoanEndTimeBegin() {
        return loanEndTimeBegin;
    }

    public void setLoanEndTimeBegin(String loanEndTimeBegin) {
        this.loanEndTimeBegin = loanEndTimeBegin;
    }

    public String getLoanEndTimeEnd() {
        return loanEndTimeEnd;
    }

    public void setLoanEndTimeEnd(String loanEndTimeEnd) {
        this.loanEndTimeEnd = loanEndTimeEnd;
    }

	public String getIsOverdueName() {
		return isOverdueName;
	}

	public void setIsOverdueName(String isOverdueName) {
		this.isOverdueName = isOverdueName;
	}

	public String getBlock() {
		return block;
	}

	public void setBlock(String block) {
		this.block = block;
	}
    
    

}
