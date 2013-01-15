package edu.ncsu.dbms.custom.objects;

/**
 * Custom object to help get vendor purchases
 *
 */
public class VendorMerchandiseOrder {

	private int orderId;

	private int storeId;

	private int merchandiseId;

	private int quantity;

	public VendorMerchandiseOrder(int orderId, int storeId, int mercandiseId,
			int quantity) {

		this.orderId = orderId;
		this.storeId = storeId;
		this.merchandiseId = mercandiseId;
		this.quantity = quantity;

	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public int getMerchandiseId() {
		return merchandiseId;
	}

	public void setMerchandiseId(int merchandiseId) {
		this.merchandiseId = merchandiseId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public String toString(){
		
		return "Order Id ="+orderId+", Merchandise Id="+merchandiseId+"\n";
		
	}
	

}
