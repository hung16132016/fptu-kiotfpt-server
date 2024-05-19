package com.kiotfpt.response;

import com.kiotfpt.model.Voucher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoucherResponse {

	private int id;
	private int value;
	private StatusResponse status;

	public VoucherResponse(Voucher voucher) {
		super();
		this.id = voucher.getId();
		this.value = voucher.getValue();
		this.status = new StatusResponse(voucher.getStatus());
	}

}
